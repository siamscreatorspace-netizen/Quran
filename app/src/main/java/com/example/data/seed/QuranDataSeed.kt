package com.example.data.seed

import com.example.data.quran.Ayah
import com.example.data.quran.HadithRecord
import com.example.data.quran.Surah

object QuranDataSeed {

    // Complete Directory of 114 Surahs with authentic metadata
    val allSurahs: List<Surah> = listOf(
        Surah(1, "الفاتحة", "আল-ফাতিহা", "Al-Fatihah", "সূচনা", "The Opening", "মাক্কী", 7),
        Surah(2, "البقرة", "আল-বাকারা", "Al-Baqarah", "গাভী", "The Cow", "মাদানী", 286),
        Surah(3, "آل عمران", "আলে ইমরান", "Ali 'Imran", "ইমরানের পরিবার", "Family of Imran", "মাদানী", 200),
        Surah(4, "النساء", "আন-নিসা", "An-Nisa", "নারী", "The Women", "মাদানী", 176),
        Surah(5, "المائدة", "আল-মায়িদাহ", "Al-Ma'idah", "খাদ্যপূর্ণ থালা", "The Table Spread", "মাদানী", 120),
        Surah(6, "الأنعام", "আল-আনআম", "Al-An'am", "গৃহপালিত পশু", "The Cattle", "মাক্কী", 165),
        Surah(7, "الأعراف", "আল-আ'রাফ", "Al-A'raf", "উঁচু স্থান", "The Heights", "মাক্কী", 206),
        Surah(8, "الأنفال", "আল-আনফাল", "Al-Anfal", "যুদ্ধলব্ধ ধনসম্পদ", "The Spoils of War", "মাদানী", 75),
        Surah(9, "التوبة", "আত-তাওবাহ", "At-Tawbah", "অনুশোচনা", "The Repentance", "মাদানী", 129),
        Surah(10, "يونس", "ইউনুস", "Yunus", "নবী ইউনুস", "Jonah", "মাক্কী", 109),
        Surah(11, "هود", "হুদ", "Hud", "নবী হুদ", "Hud", "মাক্কী", 123),
        Surah(12, "يوسف", "ইউসুফ", "Yusuf", "নবী ইউসুফ", "Joseph", "মাক্কী", 111),
        Surah(13, "الرعد", "আর-রাদ", "Ar-Ra'd", "বজ্রনাদ", "The Thunder", "মাদানী", 43),
        Surah(14, "إبراهيم", "ইব্রাহিম", "Ibrahim", "নবী ইব্রাহিম", "Abraham", "মাক্কী", 52),
        Surah(15, "الحجر", "আল-হিজর", "Al-Hijr", "পাথুরে পাহাড়", "The Rocky Tract", "মাক্কী", 99),
        Surah(16, "النحل", "আন-নাহল", "An-Nahl", "মৌমাছি", "The Bee", "মাক্কী", 128),
        Surah(17, "الإسراء", "আল-ইসরা", "Al-Isra", "নৈশ ভ্রমণ", "The Night Journey", "মাক্কী", 111),
        Surah(18, "الكهف", "আল-কাহফ", "Al-Kahf", "গুহা", "The Cave", "মাক্কী", 110),
        Surah(19, "مريم", "মারইয়াম", "Maryam", "মারইয়াম", "Mary", "মাক্কী", 98),
        Surah(20, "طه", "ত্বা-হা", "Ta-Ha", "ত্বা-হা", "Ta-Ha", "মাক্কী", 135),
        Surah(21, "الأنبياء", "আল-আম্বিয়া", "Al-Anbiya", "নবীগণ", "The Prophets", "মাক্কী", 112),
        Surah(22, "الحج", "আল-হজ্জ", "Al-Hajj", "হজ্জ", "The Pilgrimage", "মাদানী", 78),
        Surah(23, "المؤمنون", "আল-মুমিনূন", "Al-Mu'minun", "বিশ্বাসীগণ", "The Believers", "মাক্কী", 118),
        Surah(24, "النور", "আন-নূর", "An-Nur", "আলো", "The Light", "মাদানী", 64),
        Surah(25, "الفرقان", "আল-ফুরকান", "Al-Furqan", "সত্য-মিথ্যার পার্থক্যকারী", "The Criterion", "মাক্কী", 77),
        Surah(26, "الشعراء", "আশ-শুয়ারা", "Ash-Shu'ara", "কবিগণ", "The Poets", "মাক্কী", 227),
        Surah(27, "النمل", "আন-নামল", "An-Naml", "পিপীলিকা", "The Ant", "মাক্কী", 93),
        Surah(28, "القصص", "আল-কাসাস", "Al-Qasas", "কাহিনী", "The Stories", "মাক্কী", 88),
        Surah(29, "العنكبوت", "আল-আনকাবূত", "Al-'Ankabut", "মাকড়সা", "The Spider", "মাক্কী", 69),
        Surah(30, "الروم", "আর-রূম", "Ar-Rum", "রোমীয়রা", "The Romans", "মাক্কী", 60),
        Surah(31, "لقمان", "লুকমান", "Luqman", "জ্ঞানী লুকমান", "Luqman", "মাক্কী", 34),
        Surah(32, "السجدة", "আস-সাজদাহ", "As-Sajdah", "সিজদা", "The Prostration", "মাক্কী", 30),
        Surah(33, "الأحزاب", "আল-আহযাব", "Al-Ahzab", "জোটবদ্ধ দল", "The Combined Forces", "মাদানী", 73),
        Surah(34, "سبإ", "সাবা", "Saba", "সাবা জাতি", "Sheba", "মাক্কী", 54),
        Surah(35, "فاطر", "ফাতির", "Fatir", "আদি স্রষ্টা", "Originator", "মাক্কী", 45),
        Surah(36, "يس", "ইয়াসীন", "Ya-Sin", "ইয়াসীন", "Ya-Sin", "মাক্কী", 83),
        Surah(37, "الصافات", "আস-সাফফাত", "As-Saffat", "সারিবদ্ধ দলসমূহ", "Those Who Set The Ranks", "মাক্কী", 182),
        Surah(38, "ص", "সোয়াদ", "Sad", "সোয়াদ", "The Letter Sad", "মাক্কী", 88),
        Surah(39, "الزمر", "আয-যুমার", "Az-Zumar", "দলবদ্ধ জনতা", "The Crowds", "মাক্কী", 75),
        Surah(40, "غافر", "গাফির", "Ghafir", "ক্ষমাশীল", "The Forgiver", "মাক্কী", 85),
        Surah(41, "فصلت", "ফুসসিলাত", "Fussilat", "সুস্পষ্ট বিবরণ", "Explained in Detail", "মাক্কী", 54),
        Surah(42, "الشورى", "আশ-শুরা", "Ash-Shura", "পরামর্শ", "The Consultation", "মাক্কী", 53),
        Surah(43, "الزخرف", "আয-যুখরুফ", "Az-Zukhruf", "স্বর্ণালঙ্কার", "The Ornaments of Gold", "মাক্কী", 89),
        Surah(44, "الدخان", "আদ-দুখান", "Ad-Dukhan", "ধোঁয়া", "The Smoke", "মাক্কী", 59),
        Surah(45, "الجاثية", "আল-জাসিয়া", "Al-Jathiyah", "নতজানু", "The Kneeling", "মাক্কী", 37),
        Surah(46, "الأحقاف", "আল-আহকাফ", "Al-Ahqaf", "বালির পাহাড়", "The Curved Sand-hills", "মাক্কী", 35),
        Surah(47, "محمد", "মুহাম্মদ", "Muhammad", "নবী মুহাম্মদ (সা.)", "Muhammad", "মাদানী", 38),
        Surah(48, "الفتح", "আল-ফাতহ", "Al-Fath", "বিজয়", "The Victory", "মাদানী", 29),
        Surah(49, "الحجرات", "আল-হুজুরাত", "Al-Hujurat", "বাসগৃহসমূহ", "The Rooms", "মাদানী", 18),
        Surah(50, "ق", "ক্বাফ", "Qaf", "ক্বাফ", "The Letter Qaf", "মাক্কী", 45),
        Surah(51, "الذاريات", "আয-যারিয়াত", "Adh-Dhariyat", "বিক্ষিপ্তকারী বাতাস", "The Winnowing Winds", "মাক্কী", 60),
        Surah(52, "الطور", "আত-তূর", "At-Tur", "তূর পাহাড়", "The Mount", "মাক্কী", 49),
        Surah(53, "النجم", "আন-নাজম", "An-Najm", "তারা", "The Star", "মাক্কী", 62),
        Surah(54, "القمر", "আল-ক্বামার", "Al-Qamar", "চাঁদ", "The Moon", "মাক্কী", 55),
        Surah(55, "الرحمن", "আর-রাহমান", "Ar-Rahman", "পরম দয়াময়", "The Beneficent", "মাদানী", 78),
        Surah(56, "الواقعة", "আল-ওয়াকিয়া", "Al-Waqi'ah", "নিশ্চিত মহাপ্রলয়", "The Inevitable", "মাক্কী", 96),
        Surah(57, "الحديد", "আল-হাদীদ", "Al-Hadid", "লোহা", "The Iron", "মাদানী", 29),
        Surah(58, "المجادلة", "আল-মুজাদালাহ", "Al-Mujadila", "অভিযোগকারিণী", "The Pleading Woman", "মাদানী", 22),
        Surah(59, "الحشر", "আল-হাশর", "Al-Hashr", "সমাবেশ ও নির্বাসন", "The Exile", "মাদানী", 24),
        Surah(60, "الممتحنة", "আল-মুমতাহানাহ", "Al-Mumtahanah", "পরীক্ষিত নারী", "She That Is To Be Examined", "মাদানী", 13),
        Surah(61, "الصف", "আস-সাফ", "As-Saff", "সারিবদ্ধ সৈন্যদল", "The Ranks", "মাদানী", 14),
        Surah(62, "الجمعة", "আল-জুমুআহ", "Al-Jumu'ah", "শুক্রবার", "The Congregation", "মাদানী", 11),
        Surah(63, "المنافقون", "আল-মুনাফিকুন", "Al-Munafiqun", "কপট বিশ্বাসীগণ", "The Hypocrites", "মাদানী", 11),
        Surah(64, "التغابن", "আত-তাগাবুন", "At-Taghabun", "লাভ-ক্ষতির দিন", "Mutual Disillusion", "মাদানী", 18),
        Surah(65, "الطلاق", "আত-ত্বালাক", "At-Talaq", "তালাক", "The Divorce", "মাদানী", 12),
        Surah(66, "التحريم", "আত-তাহরীম", "At-Tahrim", "নিষিদ্ধকরণ", "The Prohibition", "মাদানী", 12),
        Surah(67, "الملك", "আল-মুলক", "Al-Mulk", "সার্বভৌম কর্তৃত্ব", "The Sovereignty", "মাক্কী", 30),
        Surah(68, "القلم", "আল-কলম", "Al-Qalam", "কলম", "The Pen", "মাক্কী", 52),
        Surah(69, "الحاقة", "আল-হাক্কাহ", "Al-Haqqah", "সুনিশ্চিত সত্য", "The Reality", "মাক্কী", 52),
        Surah(70, "المعارج", "আল-মাআরিজ", "Al-Ma'arij", "উন্নয়নের সোপান", "The Ascending Stairways", "মাক্কী", 44),
        Surah(71, "نوح", "নূহ", "Nuh", "নবী নূহ", "Noah", "মাক্কী", 28),
        Surah(72, "الجن", "আল-জ্বিন", "Al-Jinn", "জ্বিন জাতি", "The Jinn", "মাক্কী", 28),
        Surah(73, "المزمل", "আল-মুযযাম্মিল", "Al-Muzzammil", "বস্ত্রাবৃত", "The Enshrouded One", "মাক্কী", 20),
        Surah(74, "المدثر", "আল-মুদ্দাসসির", "Al-Muddaththir", "চাদরাবৃত", "The Cloaked One", "মাক্কী", 56),
        Surah(75, "القيامة", "আল-ক্বিয়ামাহ", "Al-Qiyamah", "পুনরুত্থান দিবস", "The Resurrection", "মাক্কী", 40),
        Surah(76, "الإنسان", "আল-ইনসান", "Al-Insan", "মানবজাতি", "The Human", "মাদানী", 31),
        Surah(77, "المرسلات", "আল-মুরসালাত", "Al-Mursalat", "প্রেরিত বাতাস", "The Emissaries", "মাক্কী", 50),
        Surah(78, "النبإ", "আন-নাবা", "An-Naba", "মহাবার্তা", "The Tidings", "মাক্কী", 40),
        Surah(79, "النازعات", "আন-নাযিয়াত", "An-Nazi'at", "উৎপাটনকারী", "Those Who Drag Forth", "মাক্কী", 46),
        Surah(80, "عبس", "আবাসা", "'Abasa", "ভ্রুকুটি করলেন", "He Frowned", "মাক্কী", 42),
        Surah(81, "التكوير", "আত-তাকভীর", "At-Takwir", "অন্ধকারাচ্ছন্নকরণ", "The Overthrowing", "মাক্কী", 29),
        Surah(82, "الانفطار", "আল-ইনফিতার", "Al-Infitar", "বিদীর্ণ হওয়া", "The Cleaving", "মাক্কী", 19),
        Surah(83, "المطففين", "আল-মুতাফফিফীন", "Al-Mutaffifin", "পরিমাপে কারচুপিকারী", "Defrauding", "মাক্কী", 36),
        Surah(84, "الانشقاق", "আল-ইনশিকাক", "Al-Inshiqaq", "খণ্ড-বিখণ্ড হওয়া", "The Splitting Open", "মাক্কী", 25),
        Surah(85, "البروج", "আল-বুরূজ", "Al-Buruj", "নক্ষত্রপুঞ্জ", "The Mansions of the Stars", "মাক্কী", 22),
        Surah(86, "الطارق", "আত-তারিক", "At-Tariq", "রাতের উদিত তারকা", "The Morning Star", "মাক্কী", 17),
        Surah(87, "الأعلى", "আল-আ'লা", "Al-A'la", "সর্বোচ্চ", "The Most High", "মাক্কী", 19),
        Surah(88, "الغاشية", "আল-গাশিয়াহ", "Al-Ghashiyah", "আচ্ছন্নকারী মহাবিপর্যয়", "The Overwhelming", "মাক্কী", 26),
        Surah(89, "الفجر", "আল-ফজর", "Al-Fajr", "ঊষা / ভোর", "The Dawn", "মাক্কী", 30),
        Surah(90, "البلد", "আল-বালাদ", "Al-Balad", "নগরী", "The City", "মাক্কী", 20),
        Surah(91, "الشمس", "আশ-শামস", "Ash-Shams", "সূর্য", "The Sun", "মাক্কী", 15),
        Surah(92, "الليل", "আল-লাইল", "Al-Layl", "রাত", "The Night", "মাক্কী", 21),
        Surah(93, "الضحى", "আদ-দুহা", "Ad-Duha", "পূর্বাহ্ণ / রৌদ্রোজ্জ্বল সকাল", "The Morning Hours", "মাক্কী", 11),
        Surah(94, "الشرح", "আল-ইনশিরাহ", "Ash-Sharh", "বক্ষ প্রশস্তকরণ", "The Relief", "মাক্কী", 8),
        Surah(95, "التين", "আত-তীন", "At-Tin", "ডুমুর", "The Fig", "মাক্কী", 8),
        Surah(96, "العلق", "আল-আলাক", "Al-'Alaq", "রক্তপিণ্ড", "The Clot", "মাক্কী", 19),
        Surah(97, "القدر", "আল-ক্বদর", "Al-Qadr", "মর্যাদাপূর্ণ রাত", "The Power", "মাক্কী", 5),
        Surah(98, "البينة", "আল-বায়্যিনাহ", "Al-Bayyinah", "সুস্পষ্ট প্রমাণ", "The Clear Proof", "মাদানী", 8),
        Surah(99, "الزلزلة", "আল-যিলযাল", "Az-Zalzalah", "ভূমিকম্প", "The Earthquake", "মাদানী", 8),
        Surah(100, "العاديات", "আল-আদিয়াত", "Al-'Adiyat", "অভিযানকারী অশ্ব", "The Courser", "মাক্কী", 11),
        Surah(101, "القارعة", "আল-ক্বারিয়াহ", "Al-Qari'ah", "মহাবিপদ", "The Calamity", "মাক্কী", 11),
        Surah(102, "التكاثر", "আত-তাকাসুর", "At-Takathur", "প্রাচুর্যের প্রতিযোগিতা", "The Rivalry in World Increase", "মাক্কী", 8),
        Surah(103, "العصر", "আল-আসর", "Al-'Asr", "সময় / যুগ", "The Declining Day", "মাক্কী", 3),
        Surah(104, "الهمزة", "আল-হুমাযাহ", "Al-Humazah", "পরনিন্দাকারী", "The Traducer", "মাক্কী", 9),
        Surah(105, "الفيل", "আল-ফীল", "Al-Fil", "হাতি", "The Elephant", "মাক্কী", 5),
        Surah(106, "قريش", "কুরাইশ", "Quraysh", "কুরাইশ বংশ", "Quraysh", "মাক্কী", 4),
        Surah(107, "الماعون", "আল-মাউন", "Al-Ma'un", "সাহায্য-সহযোগিতা", "Small Kindnesses", "মাক্কী", 7),
        Surah(108, "الكوثر", "আল-কাওসার", "Al-Kawthar", "প্রাচুর্য / কাওসার ঝরনা", "Abundance", "মাক্কী", 3),
        Surah(109, "الكافرون", "আল-কাফিরুন", "Al-Kafirun", "অবিশ্বাসী দল", "The Disbelievers", "মাক্কী", 6),
        Surah(110, "النصر", "আন-নাসর", "An-Nasr", "ঐশ্বরিক সাহায্য", "The Divine Support", "মাদানী", 3),
        Surah(111, "المسد", "আল-লাহাব / আল-মাসাদ", "Al-Masad", "খেজুরের পাকানো রশি", "The Palm Fibre", "মাক্কী", 5),
        Surah(112, "الإخلاص", "আল-ইখলাস", "Al-Ikhlas", "একনিষ্ঠতা / তাওহীদ", "The Sincerity", "মাক্কী", 4),
        Surah(113, "الفلق", "আল-ফালাক", "Al-Falaq", "ভোরের আলো", "The Daybreak", "মাক্কী", 5),
        Surah(114, "الناس", "আন-নাস", "An-Nas", "মানবজাতি", "Mankind", "মাক্কী", 6)
    )

    // Helper to format 3-digit surah and ayah for authentic EveryAyah CDN URLs
    fun getAudioUrl(surah: Int, ayah: Int): String {
        val sStr = String.format("%03d", surah)
        val aStr = String.format("%03d", ayah)
        return "https://everyayah.com/data/Alafasy_128kbps/$sStr$aStr.mp3"
    }

    // Curated Authentic Verses with Verified Bengali Translations
    val featuredAyahs: List<Ayah> = listOf(
        // Surah Al-Fatihah
        Ayah(1, 1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", "শুরু করছি আল্লাহর নামে যিনি পরম করুণাময়, অতি দয়ালু।", "In the name of Allah, the Entirely Merciful, the Especially Merciful.", getAudioUrl(1, 1)),
        Ayah(1, 2, "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", "সমস্ত প্রশংসা আল্লাহ তাআলার যিনি সকল সৃষ্টিজগতের প্রতিপালক।", "[All] praise is [due] to Allah, Lord of the worlds -", getAudioUrl(1, 2)),
        Ayah(1, 3, "الرَّحْمَٰنِ الرَّحِيمِ", "যিনি পরম করুণাময় ও পরম দয়ালু।", "The Entirely Merciful, the Especially Merciful,", getAudioUrl(1, 3)),
        Ayah(1, 4, "مَالِكِ يَوْمِ الدِّينِ", "যিনি প্রতিফল দিবসের একমাত্র মালিক।", "Sovereign of the Day of Recompense.", getAudioUrl(1, 4)),
        Ayah(1, 5, "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", "আমরা কেবল আপনারই ইবাদত করি এবং কেবল আপনারই সাহায্য প্রার্থনা করি।", "It is You we worship and You we ask for help.", getAudioUrl(1, 5)),
        Ayah(1, 6, "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ", "আমাদেরকে সরল-সঠিক পথ প্রদর্শন করুন।", "Guide us to the straight path -", getAudioUrl(1, 6)),
        Ayah(1, 7, "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ", "সেসব লোকের পথ যাদের আপনি অনুগ্রহ দান করেছেন, তাদের পথ নয় যারা ক্রোধের শিকার এবং পথভ্রষ্টও নয়।", "The path of those upon whom You have bestowed favor, not of those who have evoked [Your] anger or of those who are astray.", getAudioUrl(1, 7)),

        // Ayat al-Kursi (2:255)
        Ayah(2, 255, "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَنْ ذَا الَّذِي يَشْفَعُ عِنْدَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ", "আল্লাহ, তিনি ছাড়া কোনো সত্য উপাস্য নেই, তিনি চিরঞ্জীব, সর্বসত্তার ধারক। তন্দ্রা বা ঘুম তাঁকে স্পর্শ করে না। আকাশমণ্ডলী ও পৃথিবীতে যা কিছু আছে সবই তাঁর। কে আছে যে তাঁর অনুমতি ছাড়া তাঁর কাছে সুপারিশ করবে? তাদের সম্মুখে ও পেছনে যা কিছু আছে তা তিনি জানেন। তাঁর জ্ঞানের কোনো কিছুই তারা আয়ত্ত করতে পারে না, তিনি যতটুকু ইচ্ছা করেন তা ছাড়া। তাঁর সিংহাসন আকাশ ও পৃথিবী জুড়ে বিস্তৃত এবং এ দুটোর রক্ষণাবেক্ষণ তাঁকে বিন্দুমাত্র ক্লান্ত করে না। তিনি সর্বোচ্চ, মহান।", "Allah - there is no deity except Him, the Ever-Living, the Sustainer of all existence. Neither drowsiness overtakes Him nor sleep...", getAudioUrl(2, 255)),

        // Surah Al-Ikhlas (112)
        Ayah(112, 1, "قُلْ هُوَ اللَّهُ أَحَدٌ", "বলুন, তিনিই আল্লাহ, একক ও অদ্বিতীয়।", "Say, 'He is Allah, [who is] One.'", getAudioUrl(112, 1)),
        Ayah(112, 2, "اللَّهُ الصَّمَدُ", "আল্লাহ অমুখাপেক্ষী, সবাই তাঁর মুখাপেক্ষী।", "Allah, the Eternal Refuge.", getAudioUrl(112, 2)),
        Ayah(112, 3, "لَمْ يَلِدْ وَلَمْ يُولَدْ", "তিনি কাউকে জন্ম দেননি এবং তিনিও কারো থেকে জন্ম নেননি।", "He neither begets nor is born,", getAudioUrl(112, 3)),
        Ayah(112, 4, "وَلَمْ يَكُنْ لَهُ كُفُوًا أَحَدٌ", "এবং তাঁর সমকক্ষ কেউই নেই।", "Nor is there to Him any equivalent.", getAudioUrl(112, 4)),

        // Surah Al-Asr (103)
        Ayah(103, 1, "وَالْعَصْرِ", "সময়ের শপথ,", "By time,", getAudioUrl(103, 1)),
        Ayah(103, 2, "إِنَّ الْإِنْسَانَ لَفِي خُسْرٍ", "নিশ্চয়ই মানুষ চরম ক্ষতির মধ্যে রয়েছে,", "Indeed, mankind is in loss,", getAudioUrl(103, 2)),
        Ayah(103, 3, "إِلَّا الَّذِينَ آمَنُوا وَعَمِلُوا الصَّالِحَاتِ وَتَوَاصَوْا بِالْحَقِّ وَتَوَاصَوْا بِالصَّبْرِ", "তারা ছাড়া যারা ঈমান এনেছে, সৎকাজ করেছে এবং পরস্পরকে সত্যের উপদেশ দিয়েছে ও ধৈর্যের উপদেশ দিয়েছে।", "Except for those who have believed and done righteous deeds and advised each other to truth and advised each other to patience.", getAudioUrl(103, 3)),

        // Surah Al-Kawthar (108)
        Ayah(108, 1, "إِنَّا أَعْطَيْنَاكَ الْكَوْثَرَ", "নিশ্চয় আমি আপনাকে কাওসার (অফুরন্ত কল্যাণ ও হাউয) দান করেছি।", "Indeed, We have granted you, [O Muhammad], al-Kawthar.", getAudioUrl(108, 1)),
        Ayah(108, 2, "فَصَلِّ لِرَبِّكَ وَانْحَرْ", "অতএব আপনার প্রতিপালকের উদ্দেশ্যে সালাত আদায় করুন এবং কুরবানী করুন।", "So pray to your Lord and sacrifice [to Him alone].", getAudioUrl(108, 2)),
        Ayah(108, 3, "إِنَّ شَانِئَكَ هُوَ الْأَبْتَرُ", "নিশ্চয় আপনার শত্রুই নির্বংশ, শিকড়হীন।", "Indeed, your enemy is the one cut off.", getAudioUrl(108, 3)),

        // Surah Al-Mulk (67:1-2)
        Ayah(67, 1, "تَبَارَكَ الَّذِي بِيَدِهِ الْمُلْكُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ", "বরকতময় তিনি যাঁর হাতে সর্বময় কর্তৃত্ব, এবং তিনি সব কিছুর উপর সর্বশক্তিমান।", "Blessed is He in whose hand is dominion, and He is over all things competent -", getAudioUrl(67, 1)),
        Ayah(67, 2, "الَّذِي خَلَقَ الْمَوْتَ وَالْحَيَاةَ لِيَبْلُوَكُمْ أَيُّكُمْ أَحْسَنُ عَمَلًا ۚ وَهُوَ الْعَزِيزُ الْغَفُورُ", "যিনি মৃত্যু ও জীবন সৃষ্টি করেছেন যাতে তোমাদের পরীক্ষা করতে পারেন যে, কাজে তোমাদের মধ্যে কে সর্বোত্তম? আর তিনি পরাক্রমশালী, পরম ক্ষমাশীল।", "[He] who created death and life to test you [as to] which of you is best in deed - and He is the Exalted in Might, the Forgiving -", getAudioUrl(67, 2))
    )

    // Authentic Hadith Records from Sahih al-Bukhari & Sahih Muslim
    val authenticHadiths: List<HadithRecord> = listOf(
        HadithRecord(
            id = "hdt_bukhari_1",
            collectionNameBn = "সহীহ আল-বুখারী",
            collectionNameEn = "Sahih al-Bukhari",
            hadithNumber = "হাদিস ১",
            chapterBn = "ওহীর সূচনা পর্ব",
            chapterEn = "Book of Revelation",
            arabicText = "إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ، وَإِنَّمَا لِكُلِّ امْرِئٍ مَا نَوَى",
            bengaliText = "নিশ্চয়ই সমস্ত কাজের ফলাফল নিয়তের উপর নির্ভরশীল। প্রত্যেক ব্যক্তি তাই পাবে যা সে নিয়ত করেছে।",
            englishText = "Actions are judged by motives, so each person will have but what he intended.",
            authenticity = "সহীহ (Sahih) - মুত্তাফাক্ব আলাইহ",
            teachingBn = "ইসলামে প্রতিটি সৎকাজের প্রতিদান ও গ্রহণযোগ্যতা মানুষের আন্তরিক বিশুদ্ধ নিয়তের উপর নির্ভরশীল।",
            teachingEn = "Purity of intention is the essential foundation for every good deed in Islam."
        ),
        HadithRecord(
            id = "hdt_bukhari_13",
            collectionNameBn = "সহীহ আল-বুখারী",
            collectionNameEn = "Sahih al-Bukhari",
            hadithNumber = "হাদিস ১৩",
            chapterBn = "ঈমান অধ্যায়",
            chapterEn = "Book of Belief",
            arabicText = "لاَ يُؤْمِنُ أَحَدُكُمْ حَتَّى يُحِبَّ لأَخِيهِ مَا يُحِبُّ لِنَفْسِهِ",
            bengaliText = "তোমাদের কেউ ততক্ষণ পর্যন্ত পূর্ণ ঈমানদার হতে পারবে না, যতক্ষণ না সে তার ভাইয়ের জন্য তাই পছন্দ করে যা নিজের জন্য পছন্দ করে।",
            englishText = "None of you will believe until he loves for his brother what he loves for himself.",
            authenticity = "সহীহ (Sahih) - সহীহ বুখারী ও মুসলিম",
            teachingBn = "সত্যিকার মুমিন অপরের কল্যাণকামী হয় এবং পরশ্রীকাতরতা বর্জন করে।",
            teachingEn = "True believers genuinely wish goodness, peace, and prosperity for others."
        ),
        HadithRecord(
            id = "hdt_muslim_223",
            collectionNameBn = "সহীহ মুসলিম",
            collectionNameEn = "Sahih Muslim",
            hadithNumber = "হাদিস ২২৩",
            chapterBn = "পবিত্রতা অধ্যায়",
            chapterEn = "Book of Purification",
            arabicText = "الطُّهُورُ شَطْرُ الإِيمَانِ وَالْحَمْدُ لِلَّهِ تَمْلأُ الْمِيزَانَ",
            bengaliText = "পবিত্রতা ঈমানের অর্ধেক, আর 'আলহামদুলিল্লাহ' পাল্লা পূর্ণ করে দেয়।",
            englishText = "Purity is half of faith, and 'Alhamdulillah' (praise be to Allah) fills the scale.",
            authenticity = "সহীহ (Sahih) - সহীহ মুসলিম",
            teachingBn = "বাহ্যিক ও অভ্যন্তরীণ পবিত্রতা অবলম্বন করা এবং আল্লাহর সার্বক্ষণিক কৃতজ্ঞতা প্রকাশ করা ঈমানের মূল দাবি।",
            teachingEn = "Cleanliness and constant gratitude to Allah are central pillars of Islamic life."
        ),
        HadithRecord(
            id = "hdt_bukhari_6011",
            collectionNameBn = "সহীহ আল-বুখারী",
            collectionNameEn = "Sahih al-Bukhari",
            hadithNumber = "হাদিস ৬০১১",
            chapterBn = "সদাচরণ ও আদব অধ্যায়",
            chapterEn = "Good Manners and Form",
            arabicText = "تَبَسُّمُكَ فِي وَجْهِ أَخِيكَ لَكَ صَدَقَةٌ",
            bengaliText = "তোমার ভাইয়ের মুখের দিকে তাকিয়ে তোমার মুচকি হাসিও একটি সদকা (পুণ্যকর্ম)।",
            englishText = "Smiling in the face of your brother is charity for you.",
            authenticity = "সহীহ (Sahih) - তিরমিযী ও বুখারী আদাবুল মুফরাদ",
            teachingBn = "মানুষের সাথে হাসিমুখে কথা বলা ও সদয় আচরণ করা দ্বীনি পুণ্যের কাজ।",
            teachingEn = "Kindness, smiling, and warm human interactions are counted as acts of charity."
        ),
        HadithRecord(
            id = "hdt_bukhari_5027",
            collectionNameBn = "সহীহ আল-বুখারী",
            collectionNameEn = "Sahih al-Bukhari",
            hadithNumber = "হাদিস ৫০২৭",
            chapterBn = "কুরআনের ফযিলত অধ্যায়",
            chapterEn = "Virtues of the Quran",
            arabicText = "خَيْرُكُمْ مَنْ تَعَلَّمَ الْقُرْآنَ وَعَلَّمَهُ",
            bengaliText = "তোমাদের মধ্যে সর্বোত্তম সেই ব্যক্তি যে নিজে কুরআন শিক্ষা করে এবং অন্যকে তা শিক্ষা দেয়।",
            englishText = "The best among you are those who learn the Quran and teach it.",
            authenticity = "সহীহ (Sahih) - সহীহ বুখারী",
            teachingBn = "কুরআনের জ্ঞান অর্জন এবং মানবসমাজে তা ছড়িয়ে দেয়ার মর্যাদা অপরিসীম।",
            teachingEn = "Studying the Quran and sharing its understanding holds the highest merit."
        )
    )
}
