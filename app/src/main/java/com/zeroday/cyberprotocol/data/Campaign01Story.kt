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
        StoryBeat("ORIENTASI A.E.G.I.S.", "A.E.G.I.S.  /  MANILA",
            "Tautan undangan membawamu ke kanal orientasi resmi. A.E.G.I.S. adalah organisasi intelijen internasional independen yang menangani analisis, investigasi, operasi siber, riset teknis, dan operasi lapangan. Markas pusatnya berada di Manila.\n\nUndangan tersebut menyebutkan bahwa seseorang telah memberikan perintah untuk merekrutmu. Identitas pemberi perintah tidak tercantum dalam berkas yang dapat kamu akses. Organisasi tidak akan mengarang jawaban hanya untuk mengisi kekosongan itu.",
            "PERIKSA BERKAS RAHASIA"),
        StoryBeat("BERKAS RAHASIA 001", "CATATAN REKRUTMEN  /  TERBATAS",
            "Berkas orientasi berisi penilaian singkat dan fragmen pesan latihan. Tidak ada penjelasan tentang siapa yang mengusulkan namamu.\n\nSatu kalimat masih terlihat:\n\n“Potensi tidak sama dengan pengalaman. Kandidat ini memiliki yang pertama. Yang kedua bisa dipelajari.”\n\nTugas awal dirancang untuk menguji ketelitian, bukan pengalaman sebagai agen.",
            "MULAI TUGAS PERTAMA"),
        StoryBeat("TUGAS PERTAMA", "DEKRIPSI TERPANDU  /  LAPIS 01",
            "Sebuah fragmen pesan ditemukan pada kanal latihan. Freya Knox, mentor yang kini menghubungimu, akan membimbing langkah pertamamu.\n\nSimpan fragmen ke dalam variabel bernama signal, lalu gunakan print() untuk menampilkannya. Ini adalah sandi latihan fiktif, bukan sistem keamanan kriptografi sungguhan.",
            "BUKA EDITOR KODE"),
        StoryBeat("VERIFIKASI SISTEM", "DEKRIPSI TERPANDU  /  LAPIS 02",
            "Sinyal pertama membuka pesan verifikasi. Gunakan kembali konsep variabel dan output, tetapi simpan pesan kedua pada variabel message.\n\nFreya akan membantu menjelaskan tujuan tugas. Setelah itu, kamu harus menulis dan memeriksa kode sendiri sebelum mengirim hasilnya untuk diverifikasi.",
            "VERIFIKASI PESAN"),
        StoryBeat("REKRUTMEN RESMI", "A.E.G.I.S.  /  PERSONEL",
            "Dua tugas orientasi selesai diverifikasi. A.E.G.I.S. kini menawarkan status Recruit dengan akses terbatas. Kamu menerima profil personel dan izin awal untuk ruang kerja latihan.\n\nFreya tidak membahas siapa yang mengeluarkan perintah perekrutan. Ia hanya memastikan bahwa pelatihanmu akan berlanjut dan setiap tugas memiliki alasan yang jelas.",
            "LANJUTKAN ORIENTASI"),
        StoryBeat("SELISIH PERTAMA", "CATATAN INTERNAL  /  BELUM TERVERIFIKASI",
            "Dalam pemeriksaan rutin, kamu menemukan dua catatan penerimaan untuk satu transmisi latihan. Isi pesannya sama, tetapi waktu yang dicatat oleh dua sistem berbeda beberapa detik.\n\nSelisih itu belum membuktikan manipulasi. Zona waktu, antrean pemrosesan, atau sinkronisasi jam dapat menghasilkan perbedaan. Tugasmu bukan menebak penyebabnya—melainkan memeriksa data yang tersedia.",
            "MULAI AUDIT JEJAK"),
        StoryBeat("AUDIT JEJAK SISTEM", "OPERASI 001-A  /  LOG REVIEW",
            "A.E.G.I.S. memberikan potongan log fiktif dari sistem latihan. Beberapa baris berisi ERROR, sedangkan sisanya hanya informasi atau peringatan.\n\nGunakan perulangan for untuk memeriksa setiap baris, lalu if untuk menghitung entri ERROR. Jangan mengubah atau menghapus sumber data. Catat jumlah yang benar agar tim dapat menentukan langkah pemeriksaan berikutnya.",
            "BUKA AUDIT LOG"),
        StoryBeat("INVENTARIS REKAMAN", "OPERASI 001-B  /  SOURCE CHECK",
            "Sumber kedua berisi daftar referensi rekaman yang diterima oleh sistem lain. Sebelum membandingkan isinya, hitung dahulu berapa banyak referensi yang tersedia.\n\nGunakan len() untuk menghitung jumlah item dalam list records, simpan hasilnya pada record_count, lalu tampilkan. Jumlah item bukan bukti bahwa seluruh rekaman valid—itu hanya ukuran awal inventaris.",
            "BUKA INVENTARIS"),
        StoryBeat("LAPORAN AWAL", "A.E.G.I.S.  /  HASIL PEMERIKSAAN",
            "Audit menemukan sejumlah entri ERROR dan inventaris menunjukkan jumlah rekaman yang diterima oleh sumber kedua. Kedua hasil tersebut kini tercatat dengan asal data masing-masing.\n\nBelum ada bukti yang cukup untuk menyimpulkan bahwa seseorang mengubah waktu penerimaan transmisi. Freya meminta agar laporan tetap menyebutkan batas temuan: apa yang benar-benar terlihat, apa yang baru dugaan, dan apa yang belum diketahui.\n\nKasus pertama belum selesai. Namun, kamu sudah belajar satu hal penting: data yang tampak sederhana dapat memberi kesimpulan berbeda jika konteks sumbernya diabaikan.",
            "TUTUP LAPORAN")
    )

    val codeTemplates = listOf(
        "signal = \"ACCESS GRANTED\"\nprint(signal)",
        "message = \"IDENTITY VERIFIED\"\nprint(message)",
        "count = 0\nfor event in events:\n    if \"ERROR\" in event:\n        count += 1\nprint(count)",
        "record_count = len(records)\nprint(record_count)"
    )

    fun codeTemplateForStage(stage: Int): String = when (stage) {
        4 -> codeTemplates[0]
        5 -> codeTemplates[1]
        8 -> codeTemplates[2]
        9 -> codeTemplates[3]
        else -> ""
    }
}
