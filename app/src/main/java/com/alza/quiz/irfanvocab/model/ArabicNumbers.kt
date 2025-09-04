package com.alza.quiz.irfanvocab.model

class ArabicNumber(val number: Int) {
    val arabicDigits: String
        get() = number.toString()
            .map { westernToArabicDigit(it) }
            .joinToString("")

    val arabicWords: String
        get() = numberToArabicWords(number)

    companion object {
        private fun westernToArabicDigit(c: Char): Char {
            return when (c) {
                '0' -> '٠'
                '1' -> '١'
                '2' -> '٢'
                '3' -> '٣'
                '4' -> '٤'
                '5' -> '٥'
                '6' -> '٦'
                '7' -> '٧'
                '8' -> '٨'
                '9' -> '٩'
                else -> c
            }
        }

        private fun numberToArabicWords(number: Int): String {
            val units = arrayOf(
                "", "وَاحِدٌ", "اِثْنَانِ", "ثَلَاثَةٌ", "أَرْبَعَةٌ", "خَمْسَةٌ",
                "سِتَّةٌ", "سَبْعَةٌ", "ثَمَانِيَةٌ", "تِسْعَةٌ"
            )
            val teens = arrayOf(
                "عَشْرَةٌ", "أَحَدَ عَشَرَ", "اِثْنَا عَشَرَ", "ثَلَاثَةَ عَشَرَ",
                "أَرْبَعَةَ عَشَرَ", "خَمْسَةَ عَشَرَ", "سِتَّةَ عَشَرَ",
                "سَبْعَةَ عَشَرَ", "ثَمَانِيَةَ عَشَرَ", "تِسْعَةَ عَشَرَ"
            )
            val tens = arrayOf(
                "", "عَشْرَةٌ", "عِشْرُونَ", "ثَلَاثُونَ", "أَرْبَعُونَ", "خَمْسُونَ",
                "سِتُّونَ", "سَبْعُونَ", "ثَمَانُونَ", "تِسْعُونَ"
            )
            val hundreds = arrayOf(
                "", "مِائَةٌ", "مِائَتَانِ", "ثَلَاثُمِائَةٍ", "أَرْبَعُمِائَةٍ",
                "خَمْسُمِائَةٍ", "سِتُّمِائَةٍ", "سَبْعُمِائَةٍ", "ثَمَانُمِائَةٍ",
                "تِسْعُمِائَةٍ"
            )
            val thousands = arrayOf(
                "", "أَلْفٌ", "أَلْفَانِ", "ثَلَاثَةُ آلَافٍ", "أَرْبَعَةُ آلَافٍ",
                "خَمْسَةُ آلَافٍ", "سِتَّةُ آلَافٍ", "سَبْعَةُ آلَافٍ",
                "ثَمَانِيَةُ آلَافٍ", "تِسْعَةُ آلَافٍ"
            )

            fun convertBelowThousand(n: Int): String {
                val parts = mutableListOf<String>()
                val h = n / 100
                val t = (n % 100) / 10
                val u = n % 10
                if (h > 0) parts.add(hundreds[h])
                if (t == 1) {
                    parts.add(teens[u])
                } else {
                    if (u > 0) parts.add(units[u])
                    if (t > 0) parts.add(tens[t])
                }
                return parts.joinToString(" وَ ")
            }

            if (number == 0) return "صِفْرٌ"
            if (number < 0) return "سَالِبٌ ${numberToArabicWords(-number)}"

            val parts = mutableListOf<String>()
            val tenThousands = number / 10000
            val thousandsRemainder = (number % 10000) / 1000
            val remainder = number % 1000

            if (tenThousands > 0) {
                when (tenThousands) {
                    1 -> parts.add("عَشْرَةُ آلَافٍ")
                    2 -> parts.add("عِشْرُونَ أَلْفٍ")
                    in 3..10 -> parts.add("${numberToArabicWords(tenThousands)} آلَافٍ")
                    else -> parts.add("${numberToArabicWords(tenThousands)} أَلْفٍ")
                }
            }
            if (thousandsRemainder > 0) {
                when (thousandsRemainder) {
                    1 -> parts.add("أَلْفٌ")
                    2 -> parts.add("أَلْفَانِ")
                    in 3..10 -> parts.add("${numberToArabicWords(thousandsRemainder)} آلَافٍ")
                    else -> parts.add("${numberToArabicWords(thousandsRemainder)} أَلْفٍ")
                }
            }
            if (remainder > 0) {
                parts.add(convertBelowThousand(remainder))
            }
            return parts.joinToString(" وَ ")
        }
    }
}