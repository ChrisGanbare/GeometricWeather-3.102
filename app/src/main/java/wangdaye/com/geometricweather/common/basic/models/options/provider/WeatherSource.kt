package wangdaye.com.geometricweather.common.basic.models.options.provider

import android.content.Context
import androidx.annotation.ColorInt
import wangdaye.com.geometricweather.R
import wangdaye.com.geometricweather.common.basic.models.options._basic.Utils
import wangdaye.com.geometricweather.common.basic.models.options._basic.VoiceEnum

enum class WeatherSource(
    override val id: String,
    @ColorInt val sourceColor: Int,
    val sourceUrl: String
): VoiceEnum {

    ACCU("accu", -0x10a7dd, "accuweather.com"),
    OWM("owm", -0x1491b5, "openweathermap.org"),
    MF("mf", -0xffa76e, "meteofrance.com"),
    CAIYUN("caiyun", -0xa14472, " caiyunapp.com"),
    OPEN_METEO("open_meteo", -0x808080, "open-meteo.com");

    companion object {

        @JvmStatic
        fun getInstance(
            value: String
        ): WeatherSource {
            if (value.lowercase().contains("owm")) {
                return OWM
            }
            if (value.lowercase().contains("mf")) {
                return MF
            }
            if (value.lowercase().contains("caiyun")
                || value.lowercase().contains("cn")) {
                return CAIYUN
            }
            if (value.lowercase().contains("open_meteo")) {
                return OPEN_METEO
            }
            return ACCU
        }
    }

    override val valueArrayId = R.array.weather_source_values
    override val nameArrayId = R.array.weather_sources
    // 与项目中其他地方保持一致，不需要语音功能时返回0
    override val voiceArrayId = 0

    override fun getName(context: Context) = Utils.getName(context, this)
    // 由于项目不需要使用语音功能，直接返回天气源名称而不是通过语音数组获取
    override fun getVoice(context: Context) = getName(context)
}