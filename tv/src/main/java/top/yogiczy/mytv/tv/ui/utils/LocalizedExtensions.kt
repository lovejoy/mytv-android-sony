package top.yogiczy.mytv.tv.ui.utils

import android.content.Context
import top.yogiczy.mytv.tv.R

/**
 * 本地化的音频声道数扩展函数
 */
fun Int.humanizeAudioChannels(context: Context): String {
    return when (this) {
        1 -> context.getString(R.string.ui_audio_mono)
        2 -> context.getString(R.string.ui_audio_stereo)
        3 -> context.getString(R.string.ui_audio_2_1_channel)
        4 -> context.getString(R.string.ui_audio_4_0_quad)
        5 -> context.getString(R.string.ui_audio_5_0_surround)
        6 -> context.getString(R.string.ui_audio_5_1_surround)
        7 -> context.getString(R.string.ui_audio_6_1_surround)
        8 -> context.getString(R.string.ui_audio_7_1_surround)
        10 -> context.getString(R.string.ui_audio_dolby_atmos_7_1_2)
        12 -> context.getString(R.string.ui_audio_dolby_atmos_7_1_4)
        else -> context.getString(R.string.ui_audio_channels_format, this)
    }
}

/**
 * 本地化的语言代码扩展函数
 */
fun String.humanizeLanguage(context: Context): String {
    return when (this.lowercase()) {
        "zh" -> context.getString(R.string.ui_language_chinese)
        "chs" -> context.getString(R.string.ui_language_simplified_chinese)
        "en" -> context.getString(R.string.ui_language_english)
        else -> this
    }
}
