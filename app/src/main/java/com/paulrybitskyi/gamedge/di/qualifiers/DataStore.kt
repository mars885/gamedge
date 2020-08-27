package com.paulrybitskyi.gamedge.di.qualifiers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
annotation class DataStore(val type: Type) {


    enum class Type {

        SERVER,
        DATABASE,
        CACHE

    }


}