package com.pkpk.join.utils

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import com.pikachu.utils.utils.SharedPreferencesUtils
import java.util.Locale


object LanguageUtils {

    private const val KEY = "language"

    enum class Language(val lag: String) {
        CN("cn"), EN("en"), SYS("sys")
    }

    fun init(context: Context) {
        val readString = SharedPreferencesUtils.readString(KEY)
        if (readString.isNullOrEmpty()) return
        toggle(context, readString)
    }

    fun getLanguageCode() = when (SharedPreferencesUtils.readString(KEY)) {
        Language.CN.lag -> Language.CN
        Language.EN.lag -> Language.EN
        Language.SYS.lag -> Language.SYS
        else -> Language.SYS
    }


    fun toggle(context: Context, languageCode: Language) = toggle(context, languageCode.lag)
    private fun toggle(context: Context, languageCode: String) {
        val locale = if (languageCode == Language.SYS.lag) getSysLag() else Locale(languageCode)
        Locale.setDefault(locale)
        val resources = context.resources
        resources.updateConfiguration(
            resources.configuration.apply { setLocale(locale) },
            resources.displayMetrics
        )
        SharedPreferencesUtils.write(KEY, languageCode)
        // restartApp(context)
    }

    private fun getSysLag() = Resources.getSystem().configuration.locale


    fun restartApp(context: Context) {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

}