package com.zeroday.cyberprotocol.data

data class StoryBeat(
    val title: String,
    val eyebrow: String,
    val body: String,
    val button: String = "LANJUTKAN"
)

/** Authored narrative for the approved First Contact chapter. */
object Campaign01Story {
    val beats = listOf(
        StoryBeat("PENGIRIM TAK DIKENAL", "BAB 001  /  KONTAK PERTAMA",
            "Larut malam, sebuah perangkat yang jarang kamu gunakan tiba-tiba menyala karena satu notifikasi terenkripsi.\n\nTak ada nama yang dikenal. Tak ada nomor. Hanya kanal aman yang meminta perhatianmu.", "BUKA PESAN"),
        StoryBeat("UNDANGAN", "TRANSMISI MASUK",
            "“Kami tahu kamu bukan agen terlatih. Justru karena itulah pesan ini mungkin mengejutkanmu. Seseorang melihat potensi dalam dirimu yang belum pernah kamu sadari.”\n\n“Sebuah organisasi bernama A.E.G.I.S. meminta percakapan rahasia.”", "TERIMA KONTAK"),
        StoryBeat("ORGANISASI", "A.E.G.I.S.  /  MANILA",
            "A.E.G.I.S. adalah organisasi intelijen internasional independen. Kegiatannya mencakup analisis intelijen, investigasi, operasi siber, riset teknis, dan operasi lapangan.\n\nMarkasnya tidak tercantum pada peta publik. Aksesnya dibatasi. Kasus-kasusnya bersifat rahasia. Organisasi ini tidak merekrut sembarang orang.", "PERIKSA BERKAS RAHASIA"),
        StoryBeat("BERKAS RAHASIA 001", "CATATAN REKRUTMEN  /  TERBATAS",
            "Berkas ini memuat penilaian singkat yang disiapkan sebelum kontak dilakukan. Tidak dijelaskan mengapa kamu dipilih. Beberapa bagian disembunyikan.\n\nSatu kalimat masih terlihat:\n\n“Potensi tidak sama dengan pengalaman. Kandidat ini memiliki yang pertama. Yang kedua bisa dipelajari.”", "MULAI TUGAS PERTAMA"),
        StoryBeat("TUGAS PERTAMA", "DEKRIPSI TERPANDU  /  LAPIS 01",
            "Sebuah pesan singkat tersembunyi di balik instruksi sederhana. NEXUS, sistem komunikasi dan intelijen A.E.G.I.S., akan membimbing langkah pertamamu.\n\nSimpan pesan ke dalam variable, lalu gunakan print() untuk menampilkannya. Ini adalah sandi latihan fiktif, bukan sistem keamanan kriptografi sungguhan.", "BUKA EDITOR KODE"),
        StoryBeat("VERIFIKASI SISTEM", "DEKRIPSI TERPANDU  /  LAPIS 02",
            "Pesan kedua tiba. Kali ini strukturnya sudah tidak asing. Terapkan konsep Python yang sama tanpa sekadar menyalin jawaban pertama.\n\nHasil yang benar membuktikan bahwa kamu dapat mengikuti instruksi dasar dan memeriksa keluarannya.", "VERIFIKASI PESAN"),
        StoryBeat("REKRUTMEN RESMI", "A.E.G.I.S.  /  PERSONEL",
            "Kanal aman mengonfirmasi bahwa kamu diterima sebagai Recruit. Aksesmu masih terbatas, catatan personelmu baru dibuat, dan pelatihanmu baru saja dimulai.\n\nSebelum berkas ditutup, NEXUS menampilkan satu catatan tambahan: dua sistem internal menyimpan waktu penerimaan pesan yang berbeda. Selisihnya kecil, sumbernya belum diketahui, dan belum cukup untuk membuktikan adanya gangguan.\n\nFreya hanya meninggalkan satu pesan: \"Simpan pertanyaan itu. Kita tidak akan menuduh siapa pun hanya karena satu angka yang aneh.\"",
            "MASUK KE A.E.G.I.S.")
    )

    val codeTemplates = listOf(
        "signal = \"ACCESS GRANTED\"\nprint(signal)",
        "message = \"IDENTITY VERIFIED\"\nprint(message)"
    )
}
