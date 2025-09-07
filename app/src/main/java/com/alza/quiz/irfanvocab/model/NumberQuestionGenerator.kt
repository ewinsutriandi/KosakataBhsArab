package com.alza.quiz.irfanvocab.model

import kotlin.random.Random

object NumberQuestionGenerator {
    fun generateQuestions(
        count: Int,
        type: QuizQuestion.QuestionType,
        rangeStart: Int,
        rangeEnd: Int
    ): List<QuizQuestion> {
        val questions = mutableListOf<QuizQuestion>()
        val numbers = (rangeStart..rangeEnd).shuffled().take(count)

        numbers.forEach { number ->
            val arabicNumber = ArabicNumber(number)
            val (questionText, correctAnswer, choices) = when (type) {
                QuizQuestion.QuestionType.WORD_TO_NUMBER -> {
                    Triple(
                        arabicNumber.arabicWords,
                        arabicNumber.arabicDigits,
                        generateChoices(number, rangeStart, rangeEnd, type)
                    )
                }
                QuizQuestion.QuestionType.NUMBER_TO_WORD -> {
                    Triple(
                        arabicNumber.arabicDigits,
                        arabicNumber.arabicWords,
                        generateChoices(number, rangeStart, rangeEnd, type)
                    )
                }
                QuizQuestion.QuestionType.REGULAR_TO_ARABIC_NUMBER -> {
                    Triple(
                        number.toString(),
                        arabicNumber.arabicDigits,
                        generateChoices(number, rangeStart, rangeEnd, type)
                    )
                }
                QuizQuestion.QuestionType.ARABIC_TO_REGULAR_NUMBER -> {
                    Triple(
                        arabicNumber.arabicDigits,
                        number.toString(),
                        generateChoices(number, rangeStart, rangeEnd, type)
                    )
                }

                QuizQuestion.QuestionType.TRANSLATION -> TODO()
            }
            questions.add(
                QuizQuestion(
                    number = number,
                    questionType = type,
                    questionText = questionText,
                    choices = choices,
                    correctAnswer = correctAnswer
                )
            )
        }
        return questions
    }

    private fun generateChoices(
        correctNumber: Int,
        rangeStart: Int,
        rangeEnd: Int,
        questionType: QuizQuestion.QuestionType
    ): List<String> {
        val choices = mutableSetOf<String>()
        val correctArabicNumber = ArabicNumber(correctNumber)
        val correctChoice = when (questionType) {
            QuizQuestion.QuestionType.WORD_TO_NUMBER -> correctArabicNumber.arabicDigits
            QuizQuestion.QuestionType.NUMBER_TO_WORD -> correctArabicNumber.arabicWords
            QuizQuestion.QuestionType.REGULAR_TO_ARABIC_NUMBER -> correctArabicNumber.arabicDigits
            QuizQuestion.QuestionType.ARABIC_TO_REGULAR_NUMBER -> correctNumber.toString()
            QuizQuestion.QuestionType.TRANSLATION -> TODO()
        }
        choices.add(correctChoice)

        // Generate plausible distractors based on common diversions
        val distractorCandidates = mutableListOf<Int>()

        // Nearby numbers (e.g., ±1, ±2, ±10)
        listOf(1, 2, 10, -1, -2, -10).forEach { offset ->
            val candidate = correctNumber + offset
            if (candidate in rangeStart..rangeEnd && candidate != correctNumber) {
                distractorCandidates.add(candidate)
            }
        }

        // Digit swap for multi-digit numbers (e.g., 12 -> 21)
        if (correctNumber >= 10) {
            val digits = correctNumber.toString().toCharArray()
            if (digits.size >= 2) {
                val swapped = StringBuilder()
                swapped.append(digits[1])
                swapped.append(digits[0])
                if (digits.size > 2) swapped.append(digits.drop(2).joinToString(""))
                val swappedNum = swapped.toString().toIntOrNull()
                if (swappedNum != null && swappedNum in rangeStart..rangeEnd && swappedNum != correctNumber) {
                    distractorCandidates.add(swappedNum)
                }
            }
        }

        // Add unique distractors up to 3
        distractorCandidates.distinct().shuffled().take(3).forEach { distractorNum ->
            val distractorArabic = ArabicNumber(distractorNum)
            val distractorChoice = when (questionType) {
                QuizQuestion.QuestionType.WORD_TO_NUMBER -> distractorArabic.arabicDigits
                QuizQuestion.QuestionType.NUMBER_TO_WORD -> distractorArabic.arabicWords
                QuizQuestion.QuestionType.REGULAR_TO_ARABIC_NUMBER -> distractorArabic.arabicDigits
                QuizQuestion.QuestionType.ARABIC_TO_REGULAR_NUMBER -> distractorNum.toString()
                QuizQuestion.QuestionType.TRANSLATION -> TODO()
            }
            choices.add(distractorChoice)
        }

        // Fallback to random if fewer than 4 total choices
        while (choices.size < 4) {
            val randomNumber = Random.nextInt(rangeStart, rangeEnd + 1)
            if (randomNumber != correctNumber) {
                val randomArabicNumber = ArabicNumber(randomNumber)
                val choice = when (questionType) {
                    QuizQuestion.QuestionType.WORD_TO_NUMBER -> randomArabicNumber.arabicDigits
                    QuizQuestion.QuestionType.NUMBER_TO_WORD -> randomArabicNumber.arabicWords
                    QuizQuestion.QuestionType.REGULAR_TO_ARABIC_NUMBER -> randomArabicNumber.arabicDigits
                    QuizQuestion.QuestionType.ARABIC_TO_REGULAR_NUMBER -> randomNumber.toString()
                    QuizQuestion.QuestionType.TRANSLATION -> TODO()
                }
                choices.add(choice)
            }
        }

        return choices.shuffled()
    }
}