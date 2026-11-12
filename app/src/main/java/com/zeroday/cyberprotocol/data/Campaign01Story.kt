package com.zeroday.cyberprotocol.data

data class StoryBeat(
    val title: String,
    val eyebrow: String,
    val body: String,
    val button: String = "LANJUTKAN",
    val completedButton: String? = null
)

/** Authored narrative for the approved First Contact chapter. */
object Campaign01Story {
    val beats = listOf(
        StoryBeat("ORIENTASI A.E.G.I.S.", "A.E.G.I.S.  /  MANILA",
            "Tautan undangan membawamu ke kanal orientasi resmi. A.E.G.I.S. adalah organisasi intelijen internasional independen yang menangani analisis, investigasi, operasi siber, riset teknis, dan operasi lapangan. Markas pusatnya berada di Manila.\n\nUndangan tersebut menyebutkan bahwa seseorang telah memberikan perintah untuk merekrutmu. Identitas pemberi perintah tidak tercantum dalam berkas yang dapat kamu akses. Organisasi tidak akan mengarang jawaban hanya untuk mengisi kekosongan itu.",
            "PERIKSA BERKAS RAHASIA"),
        StoryBeat("BERKAS RAHASIA 001", "CATATAN REKRUTMEN  /  TERBATAS",
            "Berkas orientasi berisi penilaian singkat dan fragmen pesan latihan. Tidak ada penjelasan tentang siapa yang mengusulkan namamu.\n\nSatu kalimat masih terlihat:\n\n“Potensi tidak sama dengan pengalaman. Kandidat ini memiliki yang pertama. Yang kedua bisa dipelajari.”\n\nTugas awal dirancang untuk menguji ketelitian, bukan pengalaman sebagai agen.",
            "MULAI TUGAS PERTAMA"),
        StoryBeat("TUGAS PERTAMA", "DEKRIPSI TERPANDU  /  LAPIS 01",
            "Sebuah fragmen pesan ditemukan pada kanal latihan. A.E.G.I.S. meminta kamu memeriksanya sebagai latihan pertamamu.\n\nSimpan fragmen ke dalam variabel bernama signal, lalu gunakan print() untuk menampilkannya. Ini adalah sandi latihan fiktif, bukan sistem keamanan kriptografi sungguhan.",
            "BUKA EDITOR KODE"),
        StoryBeat("VERIFIKASI SISTEM", "DEKRIPSI TERPANDU  /  LAPIS 02",
            "Sinyal pertama membuka pesan verifikasi. Gunakan kembali konsep variabel dan output, tetapi simpan pesan kedua pada variabel message.\n\nFreya akan membantu menjelaskan tujuan tugas. Setelah itu, kamu harus menulis dan memeriksa kode sendiri sebelum mengirim hasilnya untuk diverifikasi.",
            "VERIFIKASI PESAN"),
        StoryBeat("REKRUTMEN RESMI", "A.E.G.I.S.  /  PERSONEL",
            "Dua tugas orientasi selesai diverifikasi. A.E.G.I.S. kini menawarkan status Recruit dengan akses terbatas. Kamu menerima profil personel dan izin awal untuk ruang kerja latihan.\n\nFreya memastikan bahwa pelatihanmu akan berlanjut dan setiap tugas memiliki alasan yang jelas.",
            "LANJUTKAN ORIENTASI"),
        StoryBeat("SELISIH PERTAMA", "CATATAN INTERNAL  /  BELUM TERVERIFIKASI",
            "Dalam pemeriksaan rutin, kamu menemukan dua catatan penerimaan untuk satu transmisi latihan. Isi pesannya sama, tetapi waktu yang dicatat oleh dua sistem berbeda beberapa detik.\n\nSelisih itu belum membuktikan manipulasi. Zona waktu, antrean pemrosesan, atau sinkronisasi jam dapat menghasilkan perbedaan. Tugasmu bukan menebak penyebabnya—melainkan memeriksa data yang tersedia.",
            "MULAI AUDIT JEJAK"),
        StoryBeat("PEMERIKSAAN LOG", "OPERASI 001-A  /  SYSTEM REVIEW",
            "NEXUS menampilkan potongan log dari kanal latihan. Beberapa baris diberi tanda ERROR, sementara baris lain hanya berupa informasi atau peringatan.\n\nTanda ERROR membantu menentukan bagian sistem yang perlu diperiksa, tetapi tidak otomatis menjelaskan penyebab perbedaan waktu RX-14. Catat konteks sumbernya dan jangan mengubah log asli.",
            "LANJUTKAN PEMERIKSAAN"),
        StoryBeat("INVENTARIS SUMBER", "OPERASI 001-B  /  SOURCE INVENTORY",
            "A.E.G.I.S. menyediakan inventaris catatan penerimaan dari dua sistem. Daftar ini membantu memastikan referensi mana yang tersedia dan dari mana setiap catatan berasal.\n\nKamu tidak perlu menulis kode untuk tahap ini. Fokus pada identitas sumber, referensi RX-14, dan informasi waktu yang dapat diverifikasi.",
            "TINJAU SUMBER"),
        StoryBeat("LAPORAN AWAL", "A.E.G.I.S.  /  HASIL PEMERIKSAAN",
            "Pemeriksaan awal menemukan penanda ERROR pada log dan memastikan bahwa catatan dari dua sumber tersedia. Temuan tersebut dicatat bersama asal datanya.\n\nBelum ada bukti yang cukup untuk menyimpulkan bahwa seseorang mengubah waktu penerimaan transmisi. Freya meminta agar laporan tetap menyebutkan batas temuan: apa yang benar-benar terlihat, apa yang baru dugaan, dan apa yang belum diketahui.\n\nKasus pertama belum selesai. Namun, kamu sudah belajar satu hal penting: data yang tampak sederhana dapat memberi kesimpulan berbeda jika konteks sumbernya diabaikan.",
            "LANJUTKAN PENELUSURAN"),
        StoryBeat("PEMERIKSAAN REFERENSI RX-14", "OPERASI 001-C  /  RECEIPT TRACE",
            "Freya mengarahkanmu ke dua catatan yang memuat referensi RX-14. Buka sumber aslinya melalui Pusat Operasi dan periksa identitas sistem yang menerbitkan masing-masing catatan.\n\nJangan hanya mengandalkan ringkasan. Isi sumber asli dan asal pencatatannya adalah dasar pemeriksaan.",
            "BUKA PUSAT OPERASI"),
        StoryBeat("PERBANDINGAN DUA SUMBER", "OPERASI 001-D  /  SOURCE COMPARISON",
            "Kedua sumber kini tersedia untuk diperiksa. Referensinya sama, tetapi sistem pencatat dan waktu penerimaannya berbeda.\n\nBandingkan apa yang benar-benar tertulis pada masing-masing sumber. Jangan mengubah isi catatan, dan jangan menganggap perbedaan waktu sebagai bukti bahwa salah satu sumber telah dimanipulasi.",
            "LANJUTKAN ANALISIS"),
        StoryBeat("ANALISIS SELISIH", "OPERASI 001-E  /  INTELLIGENCE ANALYSIS",
            "A.E.G.I.S. mencatat 09:41:12, sedangkan arsip mencatat 09:41:16. Selisih empat detik dapat dihitung dari dua waktu tersebut.\n\nAngka itu adalah temuan yang dapat diverifikasi. Penyebabnya masih terbuka: sinkronisasi jam, antrean pemrosesan, atau mekanisme pencatatan yang berbeda tetap merupakan kemungkinan, bukan fakta yang sudah terbukti.",
            "TINJAU KESIMPULAN"),
        StoryBeat("KEPUTUSAN OPERASIONAL", "OPERASI 001-F  /  RESPONSE REVIEW",
            "Pemeriksaan sumber belum menjelaskan mengapa dua sistem mencatat waktu berbeda. Sebagai analis, kamu harus memilih langkah yang sebanding dengan kekuatan bukti.\n\nKamu dapat mencatat anomali, meminta log sinkronisasi atau antrean pemrosesan, maupun menyatakan bahwa bukti saat ini belum cukup untuk menentukan penyebabnya. Tidak ada kewajiban untuk menebak.",
            "BUKA PUSAT OPERASI",
            completedButton = "LANJUTKAN KE LAPORAN"),
        StoryBeat("LAPORAN INVESTIGASI", "A.E.G.I.S.  /  FIRST CONTACT REVIEW",
            "Dua sumber mencatat referensi transmisi yang sama dengan waktu penerimaan berbeda empat detik. Selisih tersebut kini dapat ditunjukkan melalui catatan yang memiliki sumber dan indeks jelas. Temuan ini tetap belum membuktikan manipulasi. Perbedaan dapat berasal dari antrean pemrosesan, sinkronisasi jam, atau mekanisme pencatatan. Freya menerima laporanmu dan menandai anomali ini untuk pemeriksaan lanjutan setelah bukti tambahan tersedia. Kamu menyelesaikan orientasi First Contact bukan karena semua pertanyaan sudah terjawab, tetapi karena kamu telah belajar membedakan fakta, dugaan, dan hal yang belum diketahui.",
            "SELESAIKAN FIRST CONTACT")
    )

    /** Returns the presentation variant without changing the canonical Indonesian story data. */
    fun localizedBeat(index: Int, language: com.zeroday.cyberprotocol.core.model.GameLanguage): StoryBeat =
        Campaign01StoryLocalization.localizedBeat(index, language)

    /**
     * Starter drafts contain objectives only. They intentionally never contain
     * executable answers; players must author the solution in the editor.
     */
    val codeTemplates = listOf(
        "# TODO: Buat variabel signal dan isi dengan pesan dari briefing.\n# TODO: Tampilkan nilai signal dengan print().",
        "# TODO: Buat variabel message untuk menyimpan pesan identitas.\n# TODO: Tampilkan nilai message dengan print().",
        "# TODO: Mulai penghitung dari 0.\n# TODO: Periksa setiap event dengan for dan if.\n# TODO: Tampilkan jumlah event ERROR.",
        "# TODO: Hitung jumlah item records menggunakan len().\n# TODO: Simpan hasilnya pada record_count lalu tampilkan.",
        "# TODO: Telusuri receipts menggunakan enumerate().\n# TODO: Gunakan if untuk memilih baris RX-14.\n# TODO: Tampilkan indeks dan isi baris.",
        "# TODO: Ambil receipts[0] sebagai primary.\n# TODO: Ambil receipts[1] sebagai archive.\n# TODO: Tampilkan kedua variabel.",
        "# TODO: Konversikan dua nilai receipt_seconds dengan int().\n# TODO: Hitung second_time - first_time dan simpan pada delta.\n# TODO: Tampilkan delta.",
        "# TODO: Konversikan dua nilai receipt_seconds menjadi integer.\n# TODO: Hitung selisih menggunakan abs() dan simpan pada difference.\n# TODO: Tampilkan difference."
    )

    private val legacySolutionTemplates = mapOf(
        4 to "signal = \"ACCESS GRANTED\"\nprint(signal)",
        5 to "message = \"IDENTITY VERIFIED\"\nprint(message)",
        8 to "count = 0\nfor event in events:\n    if \"ERROR\" in event:\n        count += 1\nprint(count)",
        9 to "record_count = len(records)\nprint(record_count)",
        11 to "for index, receipt in enumerate(receipts):\n    if \"RX-14\" in receipt:\n        print(index, receipt)",
        12 to "primary = receipts[0]\narchive = receipts[1]\nprint(primary)\nprint(archive)",
        13 to "first_time = int(receipt_seconds[0])\nsecond_time = int(receipt_seconds[1])\ndelta = second_time - first_time\nprint(delta)",
        14 to "difference = abs(int(receipt_seconds[1]) - int(receipt_seconds[0]))\nprint(difference)"
    )

    fun codeTemplateForStage(stage: Int): String = when (stage) {
        4 -> codeTemplates[0]
        5 -> codeTemplates[1]
        8 -> codeTemplates[2]
        9 -> codeTemplates[3]
        11 -> codeTemplates[4]
        12 -> codeTemplates[5]
        13 -> codeTemplates[6]
        14 -> codeTemplates[7]
        else -> ""
    }

    /** Discards only the exact answers bundled by older builds, preserving player drafts. */
    fun draftForStage(stage: Int, savedDraft: String?): String {
        if (savedDraft.isNullOrBlank() || savedDraft == legacySolutionTemplates[stage]) {
            return codeTemplateForStage(stage)
        }
        return savedDraft
    }
}
