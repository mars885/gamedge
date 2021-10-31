package com.paulrybitskyi.gamedge.commons.testing

import com.paulrybitskyi.gamedge.core.Logger

class FakeLogger : Logger {

    var infoMessage = ""
    var debugMessage = ""
    var warningMessage = ""
    var errorMessage = ""

    override fun info(tag: String, message: String, throwable: Throwable?) {
        infoMessage = message
    }

    override fun debug(tag: String, message: String, throwable: Throwable?) {
        debugMessage = message
    }

    override fun warning(tag: String, message: String, throwable: Throwable?) {
        warningMessage = message
    }

    override fun error(tag: String, message: String, throwable: Throwable?) {
        errorMessage = message
    }
}
