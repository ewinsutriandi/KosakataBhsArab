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
        NUMBER_TO_WORD  // Type B: Number shown, choose word
    }
}

data class QuizLevel(
    val levelId: Int,
    val questions: List<QuizQuestion>,
    val totalQuestions: Int,
    val correctAnswers: Int = 0,
    val incorrectAnswers: Int = 0
) {
    fun isCompleted(): Boolean = (correctAnswers + incorrectAnswers) == totalQuestions
}