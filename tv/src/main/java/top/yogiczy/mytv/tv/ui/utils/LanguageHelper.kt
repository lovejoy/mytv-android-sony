package top.yogiczy.mytv.tv.ui.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

object LanguageHelper {
    
    /**
     * 应用语言设置
     */
    fun applyLanguage(context: Context, languageCode: String) {
        val locale = when (languageCode) {
            "zh" -> Locale.CHINESE
            "en" -> Locale.ENGLISH
            else -> getSystemLocale()
        }
        
        setLocale(context, locale)
    }
    
    /**
     * 应用语言设置并重启Activity
     */
    fun applyLanguageAndRestart(activity: Activity, languageCode: String) {
        val locale = when (languageCode) {
            "zh" -> Locale.CHINESE
            "en" -> Locale.ENGLISH
            else -> getSystemLocale()
        }
        
        setLocale(activity, locale)
        
        // 重启Activity以应用新语言
        val intent = activity.intent
        activity.finish()
        activity.startActivity(intent)
    }
    
    /**
     * 获取系统默认语言
     */
    private fun getSystemLocale(): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Locale.getDefault(Locale.Category.DISPLAY)
        } else {
            Locale.getDefault()
        }
    }
    
    /**
     * 设置应用语言
     */
    private fun setLocale(context: Context, locale: Locale) {
        Locale.setDefault(locale)
        
        val configuration = Configuration(context.resources.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(locale)
        } else {
            @Suppress("DEPRECATION")
            configuration.locale = locale
        }
        
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
    }
    
    /**
     * 获取当前应用语言代码
     */
    fun getCurrentLanguageCode(): String {
        val currentLocale = Locale.getDefault()
        return when (currentLocale.language) {
            "zh" -> "zh"
            "en" -> "en"
            else -> ""
        }
    }
}
