package com.alza.quiz.irfanvocab.util

import android.content.Context
import com.alza.quiz.irfanvocab.model.Vocabulary
import kotlinx.serialization.json.Json

fun loadVocabularyFromJson(context: Context,assetFilename:String): List<Vocabulary> {
    val jsonString = context.assets.open(assetFilename).bufferedReader().use { it.readText() }
    return Json.decodeFromString(jsonString)
}