package com.example

import com.example.data.seed.QuestionBankSeed
import com.example.data.seed.QuranDataSeed
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testAll114SurahsPresent() {
        assertEquals(114, QuranDataSeed.allSurahs.size)
        val fatihah = QuranDataSeed.allSurahs.first()
        assertEquals(1, fatihah.number)
        assertEquals("الفاتحة", fatihah.nameArabic)
        assertEquals("আল-ফাতিহা", fatihah.nameBengali)
        assertEquals(7, fatihah.totalAyahs)

        val nas = QuranDataSeed.allSurahs.last()
        assertEquals(114, nas.number)
        assertEquals("الناس", nas.nameArabic)
        assertEquals(6, nas.totalAyahs)
    }

    @Test
    fun testAuthenticHadiths() {
        assertTrue(QuranDataSeed.authenticHadiths.isNotEmpty())
        for (h in QuranDataSeed.authenticHadiths) {
            assertNotNull(h.id)
            assertNotNull(h.collectionNameBn)
            assertTrue(h.authenticity.contains("সহীহ") || h.authenticity.contains("Sahih"))
            assertFalse(h.arabicText.isBlank())
            assertFalse(h.bengaliText.isBlank())
        }
    }

    @Test
    fun testFingerprintGenerationAndDuplicateDetection() {
        val fp1 = QuestionBankSeed.generateFingerprint("QURAN", "উম্মুল কুরআন কোনটি?")
        val fp2 = QuestionBankSeed.generateFingerprint("QURAN", "  উম্মুল   কুরআন কোনটি?  ")
        val fpDifferent = QuestionBankSeed.generateFingerprint("QURAN", "পবিত্র কুরআনের সবচেয়ে দীর্ঘ আয়াত কোনটি?")

        assertEquals(fp1, fp2)
        assertFalse(fp1 == fpDifferent)
        assertEquals(16, fp1.length)
    }

    @Test
    fun testQuestionBankCoverage() {
        assertTrue(QuestionBankSeed.initialQuestions.size >= 10)
        for (q in QuestionBankSeed.initialQuestions) {
            assertTrue(q.correctIndex in 0..3)
            assertFalse(q.optionABn.isBlank())
            assertFalse(q.optionBBn.isBlank())
            assertFalse(q.optionCBn.isBlank())
            assertFalse(q.optionDBn.isBlank())
            assertFalse(q.referenceSource.isBlank())
        }
    }
}
