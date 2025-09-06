package com.alza.quiz.irfanvocab.util


import android.content.Context
import android.content.SharedPreferences
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import androidx.core.content.edit

@Serializable
data class QuizStats(
    val levelId: Int,
    val correctAnswers: Int,
    val incorrectAnswers: Int,
    val timestamp: Long = System.currentTimeMillis()
)

object StatsUtil {
    private const val PREFS_NAME = "QuizStatsPrefs"
    private const val KEY_STATS_NUMBER = "quiz_stats_number"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveStats(context: Context, levelId: Int, correctAnswers: Int, incorrectAnswers: Int) {
        val statsList = getStats(context).toMutableList()
        statsList.add(QuizStats(levelId, correctAnswers, incorrectAnswers))
        val json = Json.encodeToString(statsList)
        getPrefs(context).edit { putString(KEY_STATS_NUMBER, json) }
    }

    fun getStats(context: Context): List<QuizStats> {
        val json = getPrefs(context).getString(KEY_STATS_NUMBER, null)
        return if (json != null) {
            try {
                Json.decodeFromString<List<QuizStats>>(json)
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    fun clearStats(context: Context) {
        getPrefs(context).edit { remove(KEY_STATS_NUMBER) }
    }
}