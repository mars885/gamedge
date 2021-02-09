/*
 * Copyright 2021 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.paulrybitskyi.gamedge.core.formatters

import com.paulrybitskyi.gamedge.core.R
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.hiltbinder.BindType
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject


interface RelativeDateFormatter {

    fun formatRelativeDate(dateTime: LocalDateTime): String

}


@BindType
internal class RelativeDateFormatterImpl @Inject constructor(
    private val stringProvider: StringProvider
) : RelativeDateFormatter {


    override fun formatRelativeDate(dateTime: LocalDateTime): String {
        val currentDateTime = LocalDateTime.now()
        val isDateTimeInFuture = currentDateTime.isBefore(dateTime)

        return if(isDateTimeInFuture) {
            formatFutureDate(dateTime, currentDateTime)
        } else {
            formatPastDate(dateTime, currentDateTime)
        }
    }


    private fun formatFutureDate(dateTime: LocalDateTime, currentDateTime: LocalDateTime): String {
        val yearCount = ChronoUnit.YEARS.between(currentDateTime, dateTime).toInt()
        if(yearCount > 0L) return getQuantityString(R.plurals.future_relative_timestamp_year, yearCount)

        val monthCount = ChronoUnit.MONTHS.between(currentDateTime, dateTime).toInt()
        if(monthCount > 0L) return getQuantityString(R.plurals.future_relative_timestamp_month, monthCount)

        val dayCount = ChronoUnit.DAYS.between(currentDateTime, dateTime).toInt()
        if(dayCount > 0L) return getQuantityString(R.plurals.future_relative_timestamp_day, dayCount)

        val hourCount = ChronoUnit.HOURS.between(currentDateTime, dateTime).toInt()
        if(hourCount > 0L) return getQuantityString(R.plurals.future_relative_timestamp_hour, hourCount)

        val minuteCount = ChronoUnit.MINUTES.between(currentDateTime, dateTime).toInt()
        if(minuteCount > 0L) return getQuantityString(R.plurals.future_relative_timestamp_minute, minuteCount)

        val secondCount = ChronoUnit.SECONDS.between(currentDateTime, dateTime).toInt()
        if(secondCount > 0L) return getQuantityString(R.plurals.future_relative_timestamp_second, secondCount)

        throw IllegalStateException("Could not format the future date $dateTime in a relative way.")
    }


    private fun formatPastDate(dateTime: LocalDateTime, currentDateTime: LocalDateTime): String {
        val yearCount = ChronoUnit.YEARS.between(dateTime, currentDateTime).toInt()
        if(yearCount > 0L) return getQuantityString(R.plurals.past_relative_timestamp_year, yearCount)

        val monthCount = ChronoUnit.MONTHS.between(dateTime, currentDateTime).toInt()
        if(monthCount > 0L) return getQuantityString(R.plurals.past_relative_timestamp_month, monthCount)

        val dayCount = ChronoUnit.DAYS.between(dateTime, currentDateTime).toInt()
        if(dayCount > 0L) return getQuantityString(R.plurals.past_relative_timestamp_day, dayCount)

        val hourCount = ChronoUnit.HOURS.between(dateTime, currentDateTime).toInt()
        if(hourCount > 0L) return getQuantityString(R.plurals.past_relative_timestamp_hour, hourCount)

        val minuteCount = ChronoUnit.MINUTES.between(dateTime, currentDateTime).toInt()
        if(minuteCount > 0L) return getQuantityString(R.plurals.past_relative_timestamp_minute, minuteCount)

        val secondCount = ChronoUnit.SECONDS.between(dateTime, currentDateTime).toInt()
        if(secondCount > 0L) return getQuantityString(R.plurals.past_relative_timestamp_second, secondCount)

        throw IllegalStateException("Could not format the past date $dateTime in a relative way.")
    }


    private fun getQuantityString(id: Int, quantity: Int): String {
        return stringProvider.getQuantityString(id, quantity, quantity)
    }


}