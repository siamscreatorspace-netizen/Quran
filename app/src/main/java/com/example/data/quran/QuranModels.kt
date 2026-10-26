package com.example.data.quran

data class Surah(
    val number: Int,
    val nameArabic: String,
    val nameBengali: String,
    val nameEnglish: String,
    val meaningBengali: String,
    val meaningEnglish: String,
    val revelationType: String, // "মাক্কী" / "মাদানী" ("Makki" / "Madani")
    val totalAyahs: Int
)

data class Ayah(
    val surahNumber: Int,
    val ayahNumber: Int,
    val arabicText: String,
    val bengaliTranslation: String,
    val englishTranslation: String,
    val audioUrl: String
)

data class HadithRecord(
    val id: String,
    val collectionNameBn: String,
    val collectionNameEn: String,
    val hadithNumber: String,
    val chapterBn: String,
    val chapterEn: String,
    val arabicText: String,
    val bengaliText: String,
    val englishText: String,
    val authenticity: String = "সহীহ (Sahih)",
    val teachingBn: String,
    val teachingEn: String
)
