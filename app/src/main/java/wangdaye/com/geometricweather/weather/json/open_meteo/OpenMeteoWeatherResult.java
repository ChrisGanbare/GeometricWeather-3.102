package wangdaye.com.geometricweather.weather.json.open_meteo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class OpenMeteoWeatherResult {

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("timezone")
    private String timezone;

    @SerializedName("timezone_abbreviation")
    private String timezoneAbbreviation;

    @SerializedName("elevation")
    private double elevation;

    @SerializedName("current_weather")
    private CurrentWeather currentWeather;

    @SerializedName("hourly")
    private Hourly hourly;

    @SerializedName("daily")
    private Daily daily;
    
    @SerializedName("error")
    private Boolean error;
    
    @SerializedName("reason")
    private String reason;

    // Getters and setters

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getTimezoneAbbreviation() {
        return timezoneAbbreviation;
    }

    public void setTimezoneAbbreviation(String timezoneAbbreviation) {
        this.timezoneAbbreviation = timezoneAbbreviation;
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public CurrentWeather getCurrent() {
        return currentWeather;
    }

    public void setCurrent(CurrentWeather currentWeather) {
        this.currentWeather = currentWeather;
    }

    public Hourly getHourly() {
        return hourly;
    }

    public void setHourly(Hourly hourly) {
        this.hourly = hourly;
    }

    public Daily getDaily() {
        return daily;
    }

    public void setDaily(Daily daily) {
        this.daily = daily;
    }
    
    public Boolean getError() {
        return error;
    }
    
    public void setError(Boolean error) {
        this.error = error;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }

    public static class CurrentWeather {
        @SerializedName("time")
        private String time;

        @SerializedName("temperature")
        private double temperature;

        @SerializedName("windspeed")
        private double windspeed;

        @SerializedName("winddirection")
        private double winddirection;

        @SerializedName("weathercode")
        private int weathercode;

        @SerializedName("is_day")
        private int isDay;

        // Additional fields for better current weather data
        @SerializedName("relativehumidity_2m")
        private double relativeHumidity;

        @SerializedName("precipitation")
        private double precipitation;

        @SerializedName("uv_index")
        private double uvIndex;

        // Getters and setters

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public double getTemperature() {
            return temperature;
        }

        public void setTemperature(double temperature) {
            this.temperature = temperature;
        }

        public double getWindSpeed() {
            return windspeed;
        }

        public void setWindSpeed(double windspeed) {
            this.windspeed = windspeed;
        }

        public double getWindDirection() {
            return winddirection;
        }

        public void setWindDirection(double winddirection) {
            this.winddirection = winddirection;
        }

        public int getWeatherCode() {
            return weathercode;
        }

        public void setWeatherCode(int weathercode) {
            this.weathercode = weathercode;
        }

        public int getIsDay() {
            return isDay;
        }

        public void setIsDay(int isDay) {
            this.isDay = isDay;
        }

        public double getRelativeHumidity() {
            return relativeHumidity;
        }

        public void setRelativeHumidity(double relativeHumidity) {
            this.relativeHumidity = relativeHumidity;
        }

        public double getPrecipitation() {
            return precipitation;
        }

        public void setPrecipitation(double precipitation) {
            this.precipitation = precipitation;
        }

        public double getUvIndex() {
            return uvIndex;
        }

        public void setUvIndex(double uvIndex) {
            this.uvIndex = uvIndex;
        }

        public String getWeatherText() {
            // Map weather codes to text descriptions
            switch (weathercode) {
                case 0: return "Clear sky";
                case 1: case 2: case 3: return "Mainly clear, partly cloudy, and overcast";
                case 45: case 48: return "Fog and depositing rime fog";
                case 51: case 53: case 55: return "Drizzle: Light, moderate, and dense intensity";
                case 56: case 57: return "Freezing Drizzle: Light and dense intensity";
                case 61: case 63: case 65: return "Rain: Slight, moderate and heavy intensity";
                case 66: case 67: return "Freezing Rain: Light and heavy intensity";
                case 71: case 73: case 75: return "Snow fall: Slight, moderate, and heavy intensity";
                case 77: return "Snow grains";
                case 80: case 81: case 82: return "Rain showers: Slight, moderate, and violent";
                case 85: case 86: return "Snow showers slight and heavy";
                case 95: return "Thunderstorm: Slight or moderate";
                case 96: case 99: return "Thunderstorm with slight and heavy hail";
                default: return "Unknown";
            }
        }
    }

    public static class Hourly {
        @SerializedName("time")
        private List<String> time = new ArrayList<>();

        @SerializedName("temperature_2m")
        private List<Double> temperature2m = new ArrayList<>();

        @SerializedName("relativehumidity_2m")
        private List<Double> relativehumidity2m = new ArrayList<>();

        @SerializedName("apparent_temperature")
        private List<Double> apparentTemperature = new ArrayList<>();

        @SerializedName("weathercode")
        private List<Integer> weathercode = new ArrayList<>();

        @SerializedName("windspeed_10m")
        private List<Double> windspeed10m = new ArrayList<>();

        @SerializedName("winddirection_10m")
        private List<Double> winddirection10m = new ArrayList<>();

        @SerializedName("precipitation_probability")
        private List<Double> precipitationProbability = new ArrayList<>();

        @SerializedName("precipitation")
        private List<Double> precipitation = new ArrayList<>();

        @SerializedName("uv_index")
        private List<Double> uvIndex = new ArrayList<>();

        // Getters and setters

        public List<String> getTime() {
            return time;
        }

        public void setTime(List<String> time) {
            this.time = time;
        }

        public List<Double> getTemperature2m() {
            return temperature2m;
        }

        public void setTemperature2m(List<Double> temperature2m) {
            this.temperature2m = temperature2m;
        }

        public List<Double> getRelativehumidity2m() {
            return relativehumidity2m;
        }

        public void setRelativehumidity2m(List<Double> relativehumidity2m) {
            this.relativehumidity2m = relativehumidity2m;
        }

        public List<Double> getApparentTemperature() {
            return apparentTemperature;
        }

        public void setApparentTemperature(List<Double> apparentTemperature) {
            this.apparentTemperature = apparentTemperature;
        }

        public List<Integer> getWeatherCode() {
            return weathercode;
        }

        public void setWeatherCode(List<Integer> weathercode) {
            this.weathercode = weathercode;
        }

        public List<String> getWeatherText() {
            List<String> weatherTextList = new ArrayList<>();
            for (Integer code : weathercode) {
                weatherTextList.add(mapWeatherCodeToText(code));
            }
            return weatherTextList;
        }

        public List<Double> getWindSpeed10m() {
            return windspeed10m;
        }

        public void setWindSpeed10m(List<Double> windspeed10m) {
            this.windspeed10m = windspeed10m;
        }

        public List<Double> getWindDirection10m() {
            return winddirection10m;
        }

        public void setWindDirection10m(List<Double> winddirection10m) {
            this.winddirection10m = winddirection10m;
        }

        public List<Double> getPrecipitationProbability() {
            return precipitationProbability;
        }

        public void setPrecipitationProbability(List<Double> precipitationProbability) {
            this.precipitationProbability = precipitationProbability;
        }

        public List<Double> getPrecipitation() {
            return precipitation;
        }

        public void setPrecipitation(List<Double> precipitation) {
            this.precipitation = precipitation;
        }

        public List<Double> getUvIndex() {
            return uvIndex;
        }

        public void setUvIndex(List<Double> uvIndex) {
            this.uvIndex = uvIndex;
        }

        private String mapWeatherCodeToText(int code) {
            switch (code) {
                case 0: return "Clear sky";
                case 1: case 2: case 3: return "Mainly clear, partly cloudy, and overcast";
                case 45: case 48: return "Fog and depositing rime fog";
                case 51: case 53: case 55: return "Drizzle: Light, moderate, and dense intensity";
                case 56: case 57: return "Freezing Drizzle: Light and dense intensity";
                case 61: case 63: case 65: return "Rain: Slight, moderate and heavy intensity";
                case 66: case 67: return "Freezing Rain: Light and heavy intensity";
                case 71: case 73: case 75: return "Snow fall: Slight, moderate, and heavy intensity";
                case 77: return "Snow grains";
                case 80: case 81: case 82: return "Rain showers: Slight, moderate, and violent";
                case 85: case 86: return "Snow showers slight and heavy";
                case 95: return "Thunderstorm: Slight or moderate";
                case 96: case 99: return "Thunderstorm with slight and heavy hail";
                default: return "Unknown";
            }
        }
    }

    public static class Daily {
        @SerializedName("time")
        private List<String> time = new ArrayList<>();

        @SerializedName("weathercode")
        private List<Integer> weathercode = new ArrayList<>();

        @SerializedName("temperature_2m_max")
        private List<Double> temperature2mMax = new ArrayList<>();

        @SerializedName("temperature_2m_min")
        private List<Double> temperature2mMin = new ArrayList<>();

        @SerializedName("sunrise")
        private List<String> sunrise = new ArrayList<>();

        @SerializedName("sunset")
        private List<String> sunset = new ArrayList<>();

        @SerializedName("precipitation_probability_max")
        private List<Double> precipitationProbabilityMax = new ArrayList<>();

        @SerializedName("windspeed_10m_max")
        private List<Double> windspeed10mMax = new ArrayList<>();

        @SerializedName("winddirection_10m_dominant")
        private List<Double> winddirection10mDominant = new ArrayList<>();

        @SerializedName("uv_index_max")
        private List<Double> uvIndexMax = new ArrayList<>();

        @SerializedName("precipitation_sum")
        private List<Double> precipitationSum = new ArrayList<>();

        // Getters and setters

        public List<String> getTime() {
            return time;
        }

        public void setTime(List<String> time) {
            this.time = time;
        }

        public List<Integer> getWeatherCode() {
            return weathercode;
        }

        public void setWeatherCode(List<Integer> weathercode) {
            this.weathercode = weathercode;
        }

        public List<String> getWeatherText() {
            List<String> weatherTextList = new ArrayList<>();
            for (Integer code : weathercode) {
                weatherTextList.add(mapWeatherCodeToText(code));
            }
            return weatherTextList;
        }

        public List<Double> getTemperature2mMax() {
            return temperature2mMax;
        }

        public void setTemperature2mMax(List<Double> temperature2mMax) {
            this.temperature2mMax = temperature2mMax;
        }

        public List<Double> getTemperature2mMin() {
            return temperature2mMin;
        }

        public void setTemperature2mMin(List<Double> temperature2mMin) {
            this.temperature2mMin = temperature2mMin;
        }

        public List<String> getSunrise() {
            return sunrise;
        }

        public void setSunrise(List<String> sunrise) {
            this.sunrise = sunrise;
        }

        public List<String> getSunset() {
            return sunset;
        }

        public void setSunset(List<String> sunset) {
            this.sunset = sunset;
        }

        public List<Double> getPrecipitationProbabilityMax() {
            return precipitationProbabilityMax;
        }

        public void setPrecipitationProbabilityMax(List<Double> precipitationProbabilityMax) {
            this.precipitationProbabilityMax = precipitationProbabilityMax;
        }

        public List<Double> getWindSpeed10mMax() {
            return windspeed10mMax;
        }

        public void setWindSpeed10mMax(List<Double> windspeed10mMax) {
            this.windspeed10mMax = windspeed10mMax;
        }

        public List<Double> getWindDirection10mDominant() {
            return winddirection10mDominant;
        }

        public void setWindDirection10mDominant(List<Double> winddirection10mDominant) {
            this.winddirection10mDominant = winddirection10mDominant;
        }

        public List<Double> getUvIndexMax() {
            return uvIndexMax;
        }

        public void setUvIndexMax(List<Double> uvIndexMax) {
            this.uvIndexMax = uvIndexMax;
        }

        public List<Double> getPrecipitationSum() {
            return precipitationSum;
        }

        public void setPrecipitationSum(List<Double> precipitationSum) {
            this.precipitationSum = precipitationSum;
        }

        private String mapWeatherCodeToText(int code) {
            switch (code) {
                case 0: return "Clear sky";
                case 1: case 2: case 3: return "Mainly clear, partly cloudy, and overcast";
                case 45: case 48: return "Fog and depositing rime fog";
                case 51: case 53: case 55: return "Drizzle: Light, moderate, and dense intensity";
                case 56: case 57: return "Freezing Drizzle: Light and dense intensity";
                case 61: case 63: case 65: return "Rain: Slight, moderate and heavy intensity";
                case 66: case 67: return "Freezing Rain: Light and heavy intensity";
                case 71: case 73: case 75: return "Snow fall: Slight, moderate, and heavy intensity";
                case 77: return "Snow grains";
                case 80: case 81: case 82: return "Rain showers: Slight, moderate, and violent";
                case 85: case 86: return "Snow showers slight and heavy";
                case 95: return "Thunderstorm: Slight or moderate";
                case 96: case 99: return "Thunderstorm with slight and heavy hail";
                default: return "Unknown";
            }
        }
    }
}