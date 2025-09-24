package wangdaye.com.geometricweather.weather.services;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import wangdaye.com.geometricweather.common.basic.models.Location;
import wangdaye.com.geometricweather.common.rxjava.BaseObserver;
import wangdaye.com.geometricweather.common.rxjava.ObserverContainer;
import wangdaye.com.geometricweather.common.rxjava.SchedulerTransformer;
import wangdaye.com.geometricweather.weather.apis.OpenMeteoApi;
import wangdaye.com.geometricweather.weather.converters.OpenMeteoResultConverter;
import wangdaye.com.geometricweather.weather.json.open_meteo.OpenMeteoWeatherResult;

public class OpenMeteoWeatherService extends WeatherService {

    private static final String TAG = "OpenMeteoWeatherService";
    
    private final OpenMeteoApi mApi;
    private final CompositeDisposable mCompositeDisposable;

    @Inject
    public OpenMeteoWeatherService(OpenMeteoApi api, CompositeDisposable disposable) {
        mApi = api;
        mCompositeDisposable = disposable;
    }

    @Override
    public void requestWeather(Context context, Location location, @NonNull RequestWeatherCallback callback) {
        Log.d(TAG, "Requesting weather for lat: " + location.getLatitude() + ", lon: " + location.getLongitude());
        
        mApi.getWeatherForecast(
                location.getLatitude(),
                location.getLongitude(),
                "temperature_2m,relativehumidity_2m,apparent_temperature,weathercode,windspeed_10m,winddirection_10m,precipitation_probability,precipitation,uv_index,is_day",
                "weathercode,temperature_2m_max,temperature_2m_min,sunrise,sunset,precipitation_probability_max,windspeed_10m_max,winddirection_10m_dominant,uv_index_max,precipitation_sum",
                true, // current_weather
                "auto"
        ).compose(SchedulerTransformer.create())
         .subscribe(new ObserverContainer<>(mCompositeDisposable, new BaseObserver<OpenMeteoWeatherResult>() {
             @Override
             public void onSucceed(OpenMeteoWeatherResult result) {
                 Log.d(TAG, "Weather request succeeded");
                 if (result != null) {
                     // Check for API errors
                     if (result.getError() != null && result.getError()) {
                         Log.e(TAG, "OpenMeteo API error: " + result.getReason());
                         onFailed();
                         return;
                     }
                     
                     try {
                         Log.d(TAG, "Converting weather data");
                         Log.d(TAG, "Result details - Current: " + (result.getCurrent() != null) + 
                               ", Hourly count: " + (result.getHourly() != null ? result.getHourly().getTime().size() : "null") + 
                               ", Daily count: " + (result.getDaily() != null ? result.getDaily().getTime().size() : "null"));
                               
                         wangdaye.com.geometricweather.common.basic.models.weather.Weather weather = OpenMeteoResultConverter.convert(context, location, result);
                         if (weather != null) {
                             Log.d(TAG, "Weather conversion successful");
                             callback.requestWeatherSuccess(
                                     Location.copy(location, weather)
                             );
                         } else {
                             Log.e(TAG, "Weather conversion failed - converter returned null");
                             onFailed();
                         }
                     } catch (Exception e) {
                         Log.e(TAG, "Error converting weather data", e);
                         onFailed();
                     }
                 } else {
                     Log.w(TAG, "Result is null");
                     onFailed();
                 }
             }

             @Override
             public void onFailed() {
                 Log.e(TAG, "Weather request failed");
                 callback.requestWeatherFailed(location);
             }
             
             @Override
             public void onError(Throwable e) {
                 Log.e(TAG, "Weather request failed with exception", e);
                 super.onError(e);
             }
         }));
    }

    @Override
    @WorkerThread
    @NonNull
    public List<Location> requestLocation(Context context, String query) {
        return null;
    }

    @Override
    public void requestLocation(Context context, Location location, @NonNull RequestLocationCallback callback) {
        // OpenMeteo doesn't provide location search, so we'll just return an empty result
        callback.requestLocationFailed("Location search not supported by OpenMeteo");
    }

    @Override
    public void cancel() {
        mCompositeDisposable.clear();
    }
}