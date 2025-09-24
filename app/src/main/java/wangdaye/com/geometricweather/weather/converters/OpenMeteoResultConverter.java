package wangdaye.com.geometricweather.weather.converters;

import android.content.Context;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import wangdaye.com.geometricweather.common.basic.models.Location;
import wangdaye.com.geometricweather.common.basic.models.options.unit.TemperatureUnit;
import wangdaye.com.geometricweather.common.basic.models.weather.AirQuality;
import wangdaye.com.geometricweather.common.basic.models.weather.Astro;
import wangdaye.com.geometricweather.common.basic.models.weather.Base;
import wangdaye.com.geometricweather.common.basic.models.weather.Current;
import wangdaye.com.geometricweather.common.basic.models.weather.Daily;
import wangdaye.com.geometricweather.common.basic.models.weather.HalfDay;
import wangdaye.com.geometricweather.common.basic.models.weather.Hourly;
import wangdaye.com.geometricweather.common.basic.models.weather.Minutely;
import wangdaye.com.geometricweather.common.basic.models.weather.MoonPhase;
import wangdaye.com.geometricweather.common.basic.models.weather.Pollen;
import wangdaye.com.geometricweather.common.basic.models.weather.Precipitation;
import wangdaye.com.geometricweather.common.basic.models.weather.PrecipitationDuration;
import wangdaye.com.geometricweather.common.basic.models.weather.PrecipitationProbability;
import wangdaye.com.geometricweather.common.basic.models.weather.Temperature;
import wangdaye.com.geometricweather.common.basic.models.weather.UV;
import wangdaye.com.geometricweather.common.basic.models.weather.Weather;
import wangdaye.com.geometricweather.common.basic.models.weather.WeatherCode;
import wangdaye.com.geometricweather.common.basic.models.weather.Wind;
import wangdaye.com.geometricweather.common.basic.models.weather.WindDegree;
import wangdaye.com.geometricweather.weather.json.open_meteo.OpenMeteoWeatherResult;

public class OpenMeteoResultConverter {
    private static final String TAG = "OpenMeteoResultConverter";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault());
    private static final SimpleDateFormat DATE_FORMAT_DAY = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    static {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
        DATE_FORMAT_DAY.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static Weather convert(Context context, Location location, OpenMeteoWeatherResult result) {
        try {
            Log.d(TAG, "Starting weather conversion");
            
            // Check if result has all required data
            if (result.getCurrent() == null) {
                Log.e(TAG, "Missing current weather data in OpenMeteo result");
                return null;
            }
            
            if (result.getHourly() == null) {
                Log.e(TAG, "Missing hourly weather data in OpenMeteo result");
                return null;
            }
            
            if (result.getDaily() == null) {
                Log.e(TAG, "Missing daily weather data in OpenMeteo result");
                return null;
            }
            
            Log.d(TAG, "All required data present in result");
            Log.d(TAG, "Hourly data count: " + result.getHourly().getTime().size());
            Log.d(TAG, "Daily data count: " + result.getDaily().getTime().size());

            // Convert current weather
            Current current = new Current(
                    result.getCurrent().getWeatherText(),
                    WeatherCode.Companion.getInstance(String.valueOf(result.getCurrent().getWeatherCode())),
                    new Temperature(
                            (int) Math.round(result.getCurrent().getTemperature()),
                            (int) Math.round(result.getCurrent().getTemperature()),
                            (int) Math.round(result.getCurrent().getTemperature()),
                            (int) Math.round(result.getCurrent().getTemperature()),
                            (int) Math.round(result.getCurrent().getTemperature()),
                            null,
                            null
                    ),
                    new Precipitation(
                            (float) result.getCurrent().getPrecipitation(),
                            null,
                            null,
                            null,
                            null
                    ),
                    new PrecipitationProbability(
                            (float) result.getCurrent().getPrecipitation(),
                            null,
                            null,
                            null,
                            null
                    ),
                    new Wind(
                            String.valueOf((int) result.getCurrent().getWindDirection()) + "°",
                            new WindDegree((float) result.getCurrent().getWindDirection(), false),
                            (float) result.getCurrent().getWindSpeed(),
                            "" // wind level
                    ),
                    new UV(
                            (int) Math.round(result.getCurrent().getUvIndex()),
                            null,
                            null
                    ),
                    new AirQuality(
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                    ),
                    (float) result.getCurrent().getRelativeHumidity(),
                    null, // pressure
                    null, // visibility
                    null, // dewPoint
                    null, // cloudCover
                    null, // ceiling
                    null, // dailyForecast
                    null  // hourlyForecast
            );

            // Convert daily forecasts
            List<Daily> dailyList = new ArrayList<>();
            Log.d(TAG, "Processing " + result.getDaily().getTime().size() + " daily forecasts");
            for (int i = 0; i < result.getDaily().getTime().size(); i++) {
                // Skip if we don't have data for this day
                if (i >= result.getDaily().getWeatherCode().size() ||
                    i >= result.getDaily().getTemperature2mMax().size() ||
                    i >= result.getDaily().getTemperature2mMin().size() ||
                    i >= result.getDaily().getPrecipitationSum().size() ||
                    i >= result.getDaily().getPrecipitationProbabilityMax().size() ||
                    i >= result.getDaily().getWindDirection10mDominant().size() ||
                    i >= result.getDaily().getWindSpeed10mMax().size() ||
                    i >= result.getDaily().getUvIndexMax().size()) {
                    Log.w(TAG, "Skipping daily forecast at index " + i + " due to missing data");
                    continue;
                }
                
                // Parse date
                long dateMillis = 0;
                try {
                    Date date = DATE_FORMAT_DAY.parse(result.getDaily().getTime().get(i));
                    if (date != null) {
                        dateMillis = date.getTime() / 1000;
                        Log.d(TAG, "Parsed daily date: " + result.getDaily().getTime().get(i) + " -> " + dateMillis);
                    }
                } catch (ParseException e) {
                    Log.e(TAG, "Failed to parse daily time: " + result.getDaily().getTime().get(i), e);
                    continue;
                }

                HalfDay day = new HalfDay(
                        result.getDaily().getWeatherText().get(i),
                        "", // weather phase
                        WeatherCode.Companion.getInstance(String.valueOf(result.getDaily().getWeatherCode().get(i))),
                        new Temperature(
                                result.getDaily().getTemperature2mMax().get(i).intValue(),
                                result.getDaily().getTemperature2mMax().get(i).intValue(),
                                result.getDaily().getTemperature2mMax().get(i).intValue(),
                                result.getDaily().getTemperature2mMax().get(i).intValue(),
                                result.getDaily().getTemperature2mMax().get(i).intValue(),
                                null,
                                null
                        ),
                        new Precipitation(
                                result.getDaily().getPrecipitationSum().get(i).floatValue(),
                                null,
                                null,
                                null,
                                null
                        ),
                        new PrecipitationProbability(
                                result.getDaily().getPrecipitationProbabilityMax().get(i).floatValue(),
                                null,
                                null,
                                null,
                                null
                        ),
                        new PrecipitationDuration(
                                null,
                                null,
                                null,
                                null,
                                null
                        ),
                        new Wind(
                                String.valueOf(result.getDaily().getWindDirection10mDominant().get(i).intValue()) + "°",
                                new WindDegree(result.getDaily().getWindDirection10mDominant().get(i).floatValue(), false),
                                result.getDaily().getWindSpeed10mMax().get(i).floatValue(),
                                "" // wind level
                        ),
                        null // cloud cover
                );
                
                HalfDay night = new HalfDay(
                        result.getDaily().getWeatherText().get(i),
                        "", // weather phase
                        WeatherCode.Companion.getInstance(String.valueOf(result.getDaily().getWeatherCode().get(i))),
                        new Temperature(
                                result.getDaily().getTemperature2mMin().get(i).intValue(),
                                result.getDaily().getTemperature2mMin().get(i).intValue(),
                                result.getDaily().getTemperature2mMin().get(i).intValue(),
                                result.getDaily().getTemperature2mMin().get(i).intValue(),
                                result.getDaily().getTemperature2mMin().get(i).intValue(),
                                null,
                                null
                        ),
                        new Precipitation(
                                result.getDaily().getPrecipitationSum().get(i).floatValue(),
                                null,
                                null,
                                null,
                                null
                        ),
                        new PrecipitationProbability(
                                result.getDaily().getPrecipitationProbabilityMax().get(i).floatValue(),
                                null,
                                null,
                                null,
                                null
                        ),
                        new PrecipitationDuration(
                                null,
                                null,
                                null,
                                null,
                                null
                        ),
                        new Wind(
                                String.valueOf(result.getDaily().getWindDirection10mDominant().get(i).intValue()) + "°",
                                new WindDegree(result.getDaily().getWindDirection10mDominant().get(i).floatValue(), false),
                                result.getDaily().getWindSpeed10mMax().get(i).floatValue(),
                                "" // wind level
                        ),
                        null // cloud cover
                );
                
                Daily daily = new Daily(
                        new Date(dateMillis * 1000),
                        dateMillis,
                        day,
                        night,
                        new Astro(null, null), // sun
                        new Astro(null, null), // moon
                        new MoonPhase(null, null),
                        new AirQuality(null, null, null, null, null, null, null, null),
                        new Pollen(null, null, null, null, null, null, null, null, null, null, null, null),
                        new UV(result.getDaily().getUvIndexMax().get(i).intValue(), null, null),
                        0.0f // hours of sun
                );
                dailyList.add(daily);
            }

            // Convert hourly forecasts
            List<Hourly> hourlyList = new ArrayList<>();
            Log.d(TAG, "Processing " + result.getHourly().getTime().size() + " hourly forecasts");
            for (int i = 0; i < result.getHourly().getTime().size(); i++) {
                // Skip if we don't have data for this hour
                if (i >= result.getHourly().getWeatherCode().size() ||
                    i >= result.getHourly().getTemperature2m().size() ||
                    i >= result.getHourly().getPrecipitation().size() ||
                    i >= result.getHourly().getPrecipitationProbability().size() ||
                    i >= result.getHourly().getWindDirection10m().size() ||
                    i >= result.getHourly().getWindSpeed10m().size() ||
                    i >= result.getHourly().getUvIndex().size()) {
                    Log.w(TAG, "Skipping hourly forecast at index " + i + " due to missing data");
                    continue;
                }
                
                // Parse date
                long dateMillis = 0;
                try {
                    Date date = DATE_FORMAT.parse(result.getHourly().getTime().get(i));
                    if (date != null) {
                        dateMillis = date.getTime() / 1000;
                        Log.d(TAG, "Parsed hourly date: " + result.getHourly().getTime().get(i) + " -> " + dateMillis);
                    }
                } catch (ParseException e) {
                    Log.e(TAG, "Failed to parse hourly time: " + result.getHourly().getTime().get(i), e);
                    continue;
                }

                Hourly hourly = new Hourly(
                        new Date(dateMillis * 1000),
                        dateMillis,
                        false, // daylight - we don't have this information from Open-Meteo
                        result.getHourly().getWeatherText().get(i),
                        WeatherCode.Companion.getInstance(String.valueOf(result.getHourly().getWeatherCode().get(i))),
                        new Temperature(
                                result.getHourly().getTemperature2m().get(i).intValue(),
                                result.getHourly().getTemperature2m().get(i).intValue(),
                                result.getHourly().getTemperature2m().get(i).intValue(),
                                result.getHourly().getTemperature2m().get(i).intValue(),
                                result.getHourly().getTemperature2m().get(i).intValue(),
                                null,
                                null
                        ),
                        new Precipitation(
                                result.getHourly().getPrecipitation().get(i).floatValue(),
                                null,
                                null,
                                null,
                                null
                        ),
                        new PrecipitationProbability(
                                result.getHourly().getPrecipitationProbability().get(i).floatValue(),
                                null,
                                null,
                                null,
                                null
                        ),
                        new Wind(
                                String.valueOf(result.getHourly().getWindDirection10m().get(i).intValue()) + "°",
                                new WindDegree(result.getHourly().getWindDirection10m().get(i).floatValue(), false),
                                result.getHourly().getWindSpeed10m().get(i).floatValue(),
                                "" // wind level
                        ),
                        new UV(result.getHourly().getUvIndex().get(i).intValue(), null, null)
                );
                hourlyList.add(hourly);
            }

            Base base = new Base(
                    location.getCityId(),
                    System.currentTimeMillis(),
                    new Date(),
                    System.currentTimeMillis(),
                    new Date(),
                    System.currentTimeMillis()
            );

            Weather weather = new Weather(
                    base,
                    current,
                    null, // historical data
                    dailyList,
                    hourlyList,
                    new ArrayList<>(), // minutely
                    new ArrayList<>() // alerts
            );
            
            Log.d(TAG, "Weather conversion completed successfully");
            return weather;
        } catch (Exception e) {
            Log.e(TAG, "Error converting OpenMeteo result", e);
            return null;
        }
    }
}