package com.alza.quiz.irfanvocab.model

import android.content.Context
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.random.Random

@Serializable
data class Vocabulary(
    @SerialName("English") val english: String,
    @SerialName("Indonesian") val Indonesian: String,
    @SerialName("Arabic_Singular") val arabicSingular: String,
    @SerialName("Arabic_Species_Collective") val arabicPlural: String

)
enum class TransDirection{
    ARABIC_TO_INDONESIAN,
    INDONESIAN_TO_ARABIC,
}

val trFiles = mapOf(
    ExerciseModel.ExerciseType.ANIMALS to "animals.json",
    ExerciseModel.ExerciseType.APPARELS to "apparels.json",
    ExerciseModel.ExerciseType.BODY_PARTS to "body_parts.json",
    ExerciseModel.ExerciseType.COLORS to "colors.json",
    ExerciseModel.ExerciseType.FAMILY to "family.json",
    ExerciseModel.ExerciseType.FRUITS to "fruits.json",
    ExerciseModel.ExerciseType.CLASSROOM to "classroom.json",
    ExerciseModel.ExerciseType.JOBS to "jobs.json",
)

val trLevelNames = mapOf(
    ExerciseModel.ExerciseType.NUMBERS to "Bilangan",
    ExerciseModel.ExerciseType.ANIMALS to "Hewan",
    ExerciseModel.ExerciseType.APPARELS to "Pakaian",
    ExerciseModel.ExerciseType.BODY_PARTS to "Bagian tubuh",
    ExerciseModel.ExerciseType.COLORS to "Warna",
    ExerciseModel.ExerciseType.FAMILY to "Anggota keluarga",
    ExerciseModel.ExerciseType.FRUITS to "Buah-buahan",
    ExerciseModel.ExerciseType.CLASSROOM to "Ruang kelas",
    ExerciseModel.ExerciseType.JOBS to "Pekerjaan",
)

fun generateTranslationQuiz(direction: TransDirection,vocabList: List<Vocabulary>, numQuestions: Int, exerciseType: ExerciseModel.ExerciseType): List<QuizQuestion> {
    val questions = mutableListOf<QuizQuestion>()
    val shuffledVocab = vocabList.shuffled()
    val random = Random.Default

    for (i in 0 until numQuestions) {
        val vocab = shuffledVocab[i % shuffledVocab.size]

        val questionText: String
        val correctAnswer: String
        val choices = mutableListOf<String>()

        when (direction) {
            TransDirection.ARABIC_TO_INDONESIAN -> {
                questionText = vocab.arabicSingular
                correctAnswer = vocab.Indonesian
                choices.add(correctAnswer)
                while (choices.size < 4) {
                    val randomVocab = shuffledVocab.random()
                    if (randomVocab.Indonesian != correctAnswer && !choices.contains(randomVocab.Indonesian)) {
                        choices.add(randomVocab.Indonesian)
                    }
                }
            }
            TransDirection.INDONESIAN_TO_ARABIC -> {
                questionText = vocab.Indonesian
                correctAnswer = vocab.arabicSingular
                choices.add(correctAnswer)
                while (choices.size < 4) {
                    val randomVocab = shuffledVocab.random()
                    if (randomVocab.arabicSingular != correctAnswer && !choices.contains(randomVocab.arabicSingular)) {
                        choices.add(randomVocab.arabicSingular)
                    }
                }
            }

        }

        questions.add(QuizQuestion(
            number = i + 1,
            questionType = QuizQuestion.QuestionType.TRANSLATION,
            questionText = questionText,
            choices = choices.shuffled(),
            correctAnswer = correctAnswer
        ))
    }

    return questions
}

fun loadAndGenerateQuiz(direction: TransDirection,context: Context, exerciseType: ExerciseModel.ExerciseType, numQuestions: Int): List<QuizQuestion> {
    val fileName = trFiles[exerciseType] ?: throw IllegalArgumentException("No file for $exerciseType")
    val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    val vocabList: List<Vocabulary> = Json.decodeFromString(jsonString)
    return generateTranslationQuiz(direction,vocabList, numQuestions, exerciseType)
}