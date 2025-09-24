package wangdaye.com.geometricweather.weather.services;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import java.util.List;

import wangdaye.com.geometricweather.BuildConfig;
import wangdaye.com.geometricweather.common.basic.models.weather.Weather;
import wangdaye.com.geometricweather.common.utils.LanguageUtils;
import wangdaye.com.geometricweather.common.basic.models.Location;
import wangdaye.com.geometricweather.common.basic.models.options.provider.WeatherSource;
import wangdaye.com.geometricweather.settings.SettingsManager;
import wangdaye.com.geometricweather.common.di.RetrofitModule;
import wangdaye.com.geometricweather.weather.apis.AccuWeatherApi;
import wangdaye.com.geometricweather.weather.apis.AtmoAuraIqaApi;
import wangdaye.com.geometricweather.weather.apis.CaiYunApi;
import wangdaye.com.geometricweather.weather.apis.MfWeatherApi;
import wangdaye.com.geometricweather.weather.apis.OpenMeteoApi;
import wangdaye.com.geometricweather.weather.apis.OwmApi;

import io.reactivex.disposables.CompositeDisposable;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

import dagger.hilt.android.qualifiers.ApplicationContext;

/**
 * Weather service.
 * */

public abstract class WeatherService {

    public static class WeatherResultWrapper {
        final Weather result;

        public WeatherResultWrapper(@Nullable Weather weather) {
            result = weather;
        }
    }

    public interface RequestWeatherCallback {
        void requestWeatherSuccess(@NonNull Location requestLocation);
        void requestWeatherFailed(@NonNull Location requestLocation);
    }

    public interface RequestLocationCallback {
        void requestLocationSuccess(String query, List<Location> locationList);
        void requestLocationFailed(String query);
    }

    public abstract void requestWeather(Context context, Location location,
                                        @NonNull RequestWeatherCallback callback);

    @WorkerThread
    @NonNull
    public abstract List<Location> requestLocation(Context context, String query);

    public abstract void requestLocation(Context context, Location location,
                                         @NonNull RequestLocationCallback callback);

    public abstract void cancel();

    public static WeatherService get(Context context, WeatherSource source) {
        switch (source) {
            case ACCU:
                return new AccuWeatherService(
                        new Retrofit.Builder()
                                .baseUrl(BuildConfig.ACCU_WEATHER_BASE_URL)
                                .client(new RetrofitModule().provideOkHttpClient(
                                        new wangdaye.com.geometricweather.common.retrofit.interceptors.GzipInterceptor(),
                                        new RetrofitModule().provideHttpLoggingInterceptor()))
                                .addConverterFactory(new RetrofitModule().provideGsonConverterFactory())
                                .addCallAdapterFactory(new RetrofitModule().provideRxJava2CallAdapterFactory())
                                .build()
                                .create(AccuWeatherApi.class),
                        new CompositeDisposable()
                );

            case OWM:
                return new OwmWeatherService(
                        new Retrofit.Builder()
                                .baseUrl(BuildConfig.OWM_BASE_URL)
                                .client(new RetrofitModule().provideOkHttpClient(
                                        new wangdaye.com.geometricweather.common.retrofit.interceptors.GzipInterceptor(),
                                        new RetrofitModule().provideHttpLoggingInterceptor()))
                                .addConverterFactory(new RetrofitModule().provideGsonConverterFactory())
                                .addCallAdapterFactory(new RetrofitModule().provideRxJava2CallAdapterFactory())
                                .build()
                                .create(OwmApi.class),
                        new CompositeDisposable()
                );

            case MF:
                return new MfWeatherService(
                        new Retrofit.Builder()
                                .baseUrl(BuildConfig.MF_WSFT_BASE_URL)
                                .client(new RetrofitModule().provideOkHttpClient(
                                        new wangdaye.com.geometricweather.common.retrofit.interceptors.GzipInterceptor(),
                                        new RetrofitModule().provideHttpLoggingInterceptor()))
                                .addConverterFactory(new RetrofitModule().provideGsonConverterFactory())
                                .addCallAdapterFactory(new RetrofitModule().provideRxJava2CallAdapterFactory())
                                .build()
                                .create(MfWeatherApi.class),
                        new Retrofit.Builder()
                                .baseUrl(BuildConfig.IQA_ATMO_AURA_URL)
                                .client(new RetrofitModule().provideOkHttpClient(
                                        new wangdaye.com.geometricweather.common.retrofit.interceptors.GzipInterceptor(),
                                        new RetrofitModule().provideHttpLoggingInterceptor()))
                                .addConverterFactory(new RetrofitModule().provideGsonConverterFactory())
                                .addCallAdapterFactory(new RetrofitModule().provideRxJava2CallAdapterFactory())
                                .build()
                                .create(AtmoAuraIqaApi.class),
                        new CompositeDisposable()
                );

            case CAIYUN:
                return new CaiYunWeatherService(
                        new Retrofit.Builder()
                                .baseUrl(BuildConfig.CAIYUN_WEATHER_BASE_URL)
                                .client(new RetrofitModule().provideOkHttpClient(
                                        new wangdaye.com.geometricweather.common.retrofit.interceptors.GzipInterceptor(),
                                        new RetrofitModule().provideHttpLoggingInterceptor()))
                                .addConverterFactory(new RetrofitModule().provideGsonConverterFactory())
                                .addCallAdapterFactory(new RetrofitModule().provideRxJava2CallAdapterFactory())
                                .build()
                                .create(CaiYunApi.class),
                        new CompositeDisposable()
                );
                
            case OPEN_METEO:
                return new OpenMeteoWeatherService(
                        new Retrofit.Builder()
                                .baseUrl("https://api.open-meteo.com/")
                                .client(new RetrofitModule().provideOkHttpClient(
                                        new wangdaye.com.geometricweather.common.retrofit.interceptors.GzipInterceptor(),
                                        new RetrofitModule().provideHttpLoggingInterceptor()))
                                .addConverterFactory(new RetrofitModule().provideGsonConverterFactory())
                                .addCallAdapterFactory(new RetrofitModule().provideRxJava2CallAdapterFactory())
                                .build()
                                .create(OpenMeteoApi.class),
                        new CompositeDisposable()
                );
        }
        return new AccuWeatherService(
                new Retrofit.Builder()
                        .baseUrl(BuildConfig.ACCU_WEATHER_BASE_URL)
                        .client(new RetrofitModule().provideOkHttpClient(
                                new wangdaye.com.geometricweather.common.retrofit.interceptors.GzipInterceptor(),
                                new RetrofitModule().provideHttpLoggingInterceptor()))
                        .addConverterFactory(new RetrofitModule().provideGsonConverterFactory())
                        .addCallAdapterFactory(new RetrofitModule().provideRxJava2CallAdapterFactory())
                        .build()
                        .create(AccuWeatherApi.class),
                new CompositeDisposable()
        );
    }

    protected static String formatLocationString(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }

        if (str.endsWith("地区")) {
            return str.substring(0, str.length() - 2);
        }
        if (str.endsWith("区")
                && !str.endsWith("新区")
                && !str.endsWith("矿区")
                && !str.endsWith("郊区")
                && !str.endsWith("风景区")
                && !str.endsWith("东区")
                && !str.endsWith("西区")) {
            return str.substring(0, str.length() - 1);
        }
        if (str.endsWith("县")
                && str.length() != 2
                && !str.endsWith("通化县")
                && !str.endsWith("本溪县")
                && !str.endsWith("辽阳县")
                && !str.endsWith("建平县")
                && !str.endsWith("承德县")
                && !str.endsWith("大同县")
                && !str.endsWith("五台县")
                && !str.endsWith("乌鲁木齐县")
                && !str.endsWith("伊宁县")
                && !str.endsWith("南昌县")
                && !str.endsWith("上饶县")
                && !str.endsWith("吉安县")
                && !str.endsWith("长沙县")
                && !str.endsWith("衡阳县")
                && !str.endsWith("邵阳县")
                && !str.endsWith("宜宾县")) {
            return str.substring(0, str.length() - 1);
        }

        if (str.endsWith("市")
                && !str.endsWith("新市")
                && !str.endsWith("沙市")
                && !str.endsWith("津市")
                && !str.endsWith("芒市")
                && !str.endsWith("西市")
                && !str.endsWith("峨眉山市")) {
            return str.substring(0, str.length() - 1);
        }
        if (str.endsWith("回族自治州")
                || str.endsWith("藏族自治州")
                || str.endsWith("彝族自治州")
                || str.endsWith("白族自治州")
                || str.endsWith("傣族自治州")
                || str.endsWith("蒙古自治州")) {
            return str.substring(0, str.length() - 5);
        }
        if (str.endsWith("朝鲜族自治州")
                || str.endsWith("哈萨克自治州")
                || str.endsWith("傈僳族自治州")
                || str.endsWith("蒙古族自治州")) {
            return str.substring(0, str.length() - 6);
        }
        if (str.endsWith("哈萨克族自治州")
                || str.endsWith("苗族侗族自治州")
                || str.endsWith("藏族羌族自治州")
                || str.endsWith("壮族苗族自治州")
                || str.endsWith("柯尔克孜自治州")) {
            return str.substring(0, str.length() - 7);
        }
        if (str.endsWith("布依族苗族自治州")
                || str.endsWith("土家族苗族自治州")
                || str.endsWith("蒙古族藏族自治州")
                || str.endsWith("柯尔克孜族自治州")
                || str.endsWith("傣族景颇族自治州")
                || str.endsWith("哈尼族彝族自治州")) {
            return str.substring(0, str.length() - 8);
        }
        if (str.endsWith("自治州")) {
            return str.substring(0, str.length() - 3);
        }

        if (str.endsWith("省")) {
            return str.substring(0, str.length() - 1);
        }
        if (str.endsWith("壮族自治区") || str.endsWith("回族自治区")) {
            return str.substring(0, str.length() - 5);
        }
        if (str.endsWith("维吾尔自治区")) {
            return str.substring(0, str.length() - 6);
        }
        if (str.endsWith("维吾尔族自治区")) {
            return str.substring(0, str.length() - 7);
        }
        if (str.endsWith("自治区")) {
            return str.substring(0, str.length() - 3);
        }
        return str;
    }

    protected static String convertChinese(String text) {
        try {
            return LanguageUtils.traditionalToSimplified(text);
        } catch (Exception e) {
            return text;
        }
    }
}
