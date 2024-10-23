package com.paulrybitskyi.gamedge.core.providers

import com.paulrybitskyi.hiltbinder.BindType
import java.util.Locale
import javax.inject.Inject

interface LocaleProvider {
    fun getLocale(): Locale
}

@BindType
internal class LocaleProviderImpl @Inject constructor() : LocaleProvider {

    override fun getLocale(): Locale {
        // App only supports the English language
        return Locale.ENGLISH
    }
}
