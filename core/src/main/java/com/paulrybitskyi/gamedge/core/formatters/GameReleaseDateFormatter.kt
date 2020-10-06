/*
 * Copyright 2020 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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
import com.paulrybitskyi.gamedge.domain.games.entities.Game
import com.paulrybitskyi.gamedge.domain.games.entities.ReleaseDate
import com.paulrybitskyi.gamedge.domain.games.entities.ReleaseDateCategory
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


interface GameReleaseDateFormatter {

    fun formatReleaseDate(game: Game): String

}


internal class GameReleaseDateFormatterImpl(
    private val stringProvider: StringProvider
) : GameReleaseDateFormatter {


    private companion object {

        private const val COMPLETE_DATE_FORMATTING_PATTERN = "MMM dd, yyyy"
        private const val DAYLESS_DATE_FORMATTING_PATTERN = "MMMM yyyy"

    }


    override fun formatReleaseDate(game: Game): String {
        val date = game.findFirstReleaseDate()
            ?: return stringProvider.getString(R.string.unknown)

        return when(val category = date.category) {
            ReleaseDateCategory.YYYY_MMMM_DD -> date.formatCompleteDate()
            ReleaseDateCategory.YYYY_MMMM -> date.formatDaylessDate()
            ReleaseDateCategory.YYYY -> date.formatDateWithYearOnly()

            ReleaseDateCategory.YYYYQ1,
            ReleaseDateCategory.YYYYQ2,
            ReleaseDateCategory.YYYYQ3,
            ReleaseDateCategory.YYYYQ4 -> date.formatDateWithYearAndQuarter()

            else -> throw IllegalStateException("Unknown category: $category.")
        }
    }


    private fun Game.findFirstReleaseDate(): ReleaseDate? {
        return releaseDates
            .filter {
                it.category != ReleaseDateCategory.UNKNOWN &&
                it.category != ReleaseDateCategory.TBD &&
                it.date != null &&
                it.year != null
            }
            .minByOrNull { it.date!! }
    }


    private fun ReleaseDate.formatCompleteDate(): String {
        val releaseLocalDateTime = toLocalDateTime()
        val currentDateTime = LocalDateTime.now()
        val isReleaseDateInPast = currentDateTime.isAfter(releaseLocalDateTime)
        val isReleaseDateInFuture = currentDateTime.isBefore(releaseLocalDateTime)
        val formattedReleaseDate = DateTimeFormatter
            .ofPattern(COMPLETE_DATE_FORMATTING_PATTERN)
            .format(releaseLocalDateTime)

        return buildString {
            append(formattedReleaseDate)

            val relativeTimestamp = when {
                isReleaseDateInPast -> getPastRelativeTimestamp(releaseLocalDateTime, currentDateTime)
                isReleaseDateInFuture -> getFutureRelativeTimestamp(releaseLocalDateTime, currentDateTime)
                else -> null
            }

            if(relativeTimestamp != null) {
                append(" (")
                append(relativeTimestamp)
                append(")")
            }
        }
    }


    private fun ReleaseDate.formatDaylessDate(): String {
        return DateTimeFormatter
            .ofPattern(DAYLESS_DATE_FORMATTING_PATTERN)
            .format(toLocalDateTime())
    }


    private fun ReleaseDate.formatDateWithYearOnly(): String {
        return checkNotNull(year).toString()
    }


    private fun ReleaseDate.formatDateWithYearAndQuarter(): String {
        return stringProvider.getString(
            R.string.year_with_quarter_template,
            checkNotNull(year),
            category.getQuarterString()
        )
    }


    private fun ReleaseDateCategory.getQuarterString(): String {
        return stringProvider.getString(
            when(this) {
                ReleaseDateCategory.YYYYQ1 -> R.string.year_quarter_first
                ReleaseDateCategory.YYYYQ2 -> R.string.year_quarter_second
                ReleaseDateCategory.YYYYQ3 -> R.string.year_quarter_third
                ReleaseDateCategory.YYYYQ4 -> R.string.year_quarter_fourth

                else -> throw IllegalStateException("Unknown category $this.")
            }
        )
    }


    private fun ReleaseDate.toLocalDateTime(): LocalDateTime {
        return LocalDateTime.ofInstant(
            Instant.ofEpochSecond(checkNotNull(date)),
            ZoneId.systemDefault()
        )
    }


    private fun getPastRelativeTimestamp(
        pastReleaseDateTime: LocalDateTime,
        currentDateTime: LocalDateTime
    ): String {
        val yearCount = ChronoUnit.YEARS.between(pastReleaseDateTime, currentDateTime).toInt()
        if(yearCount > 0L) return getQuantityString(R.plurals.past_relative_timestamp_year, yearCount)

        val monthCount = ChronoUnit.MONTHS.between(pastReleaseDateTime, currentDateTime).toInt()
        if(monthCount > 0L) return getQuantityString(R.plurals.past_relative_timestamp_month, monthCount)

        val dayCount = ChronoUnit.DAYS.between(pastReleaseDateTime, currentDateTime).toInt()
        if(dayCount > 0L) return getQuantityString(R.plurals.past_relative_timestamp_day, dayCount)

        val hourCount = ChronoUnit.HOURS.between(pastReleaseDateTime, currentDateTime).toInt()
        if(hourCount > 0L) return getQuantityString(R.plurals.past_relative_timestamp_hour, hourCount)

        val minuteCount = ChronoUnit.MINUTES.between(pastReleaseDateTime, currentDateTime).toInt()
        if(minuteCount > 0L) return getQuantityString(R.plurals.past_relative_timestamp_minute, minuteCount)

        val secondCount = ChronoUnit.SECONDS.between(pastReleaseDateTime, currentDateTime).toInt()
        if(secondCount > 0L) return getQuantityString(R.plurals.past_relative_timestamp_second, secondCount)

        throw IllegalStateException(
            "Could not calculate the past relative timestamp between $pastReleaseDateTime and $currentDateTime."
        )
    }


    private fun getFutureRelativeTimestamp(
        futureReleaseDateTime: LocalDateTime,
        currentDateTime: LocalDateTime
    ): String {
        val yearCount = ChronoUnit.YEARS.between(currentDateTime, futureReleaseDateTime).toInt()
        if(yearCount > 0L) return getQuantityString(R.plurals.future_relative_timestamp_year, yearCount)

        val monthCount = ChronoUnit.MONTHS.between(currentDateTime, futureReleaseDateTime).toInt()
        if(monthCount > 0L) return getQuantityString(R.plurals.future_relative_timestamp_month, monthCount)

        val dayCount = ChronoUnit.DAYS.between(currentDateTime, futureReleaseDateTime).toInt()
        if(dayCount > 0L) return getQuantityString(R.plurals.future_relative_timestamp_day, dayCount)

        val hourCount = ChronoUnit.HOURS.between(currentDateTime, futureReleaseDateTime).toInt()
        if(hourCount > 0L) return getQuantityString(R.plurals.future_relative_timestamp_hour, hourCount)

        val minuteCount = ChronoUnit.MINUTES.between(currentDateTime, futureReleaseDateTime).toInt()
        if(minuteCount > 0L) return getQuantityString(R.plurals.future_relative_timestamp_minute, minuteCount)

        val secondCount = ChronoUnit.SECONDS.between(currentDateTime, futureReleaseDateTime).toInt()
        if(secondCount > 0L) return getQuantityString(R.plurals.future_relative_timestamp_second, secondCount)

        throw IllegalStateException(
            "Could not calculate the future relative timestamp between $currentDateTime and $futureReleaseDateTime."
        )
    }


    private fun getQuantityString(id: Int, quantity: Int): String {
        return stringProvider.getQuantityString(id, quantity, quantity)
    }


}