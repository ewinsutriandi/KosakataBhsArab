package com.alza.quiz.irfanvocab.model

data class QuizQuestion(
    val number: Int,
    val questionType: QuestionType,
    val questionText: String,
    val choices: List<String>,
    val correctAnswer: String
) {
    enum class QuestionType {
        WORD_TO_NUMBER, // Type A: Arabic word shown, choose number
        NUMBER_TO_WORD,  // Type B: Number shown, choose word
        REGULAR_TO_ARABIC_NUMBER, // Regular number shown, choose Arabic number
        ARABIC_TO_REGULAR_NUMBER,  // Arabic number shown, choose regular number
        TRANSLATION,
    }
}

data class ExerciseModel(
    val levelId: Int,
    val questions: List<QuizQuestion>,
    //val totalQuestions: Int,
    val exerciseType: ExerciseType,
    val correctAnswers: Int = 0,
    val incorrectAnswers: Int = 0
) {
    enum class ExerciseType {
        NUMBERS,
        ANIMALS,
        BODY_PARTS,
        FAMILY,
        FRUITS,
        COLORS,
        APPARELS,
    }
    fun isCompleted(): Boolean = (correctAnswers + incorrectAnswers) == questions.size
}