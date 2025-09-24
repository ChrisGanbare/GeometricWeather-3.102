package wangdaye.com.geometricweather.weather.apis;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import wangdaye.com.geometricweather.weather.json.open_meteo.OpenMeteoWeatherResult;

public interface OpenMeteoApi {

    @GET("v1/forecast")
    Observable<OpenMeteoWeatherResult> getWeatherForecast(
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("hourly") String hourly,
            @Query("daily") String daily,
            @Query("current_weather") boolean currentWeather,
            @Query("timezone") String timezone
    );
}