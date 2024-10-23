package com.paulrybitskyi.gamedge.common.testing

import com.paulrybitskyi.gamedge.core.providers.LocaleProvider
import java.util.Locale

class FakeLocaleProvider(
    private val locale: Locale = Locale.ENGLISH,
) : LocaleProvider {

    override fun getLocale(): Locale {
        return locale
    }
}
