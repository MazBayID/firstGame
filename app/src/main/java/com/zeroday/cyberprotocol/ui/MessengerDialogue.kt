package com.zeroday.cyberprotocol.ui

/**
 * Authored, deterministic dialogue for the opening campaign.
 *
 * These responses are presentation content only: they do not change relationships,
 * mission access, or progression. Game-state changes remain explicit ViewModel actions.
 */
internal object MessengerDialogue {
    fun freyaResponse(message: String): String = when (message) {
        "Dimengerti. Kita mulai dari mana?" ->
            "Mulai dari satu hal yang paling dekat: buka Browser A.E.G.I.S., masuk dengan kredensial yang kuberikan, lalu unduh berkas misi. Setelah itu kita baca berkasnya melalui Terminal. Aku akan jelaskan setiap langkah sebelum kamu diminta mengerjakannya sendiri."
        "Apa fungsi Terminal?" ->
            "Terminal adalah tempat memberi perintah teks kepada lingkungan kerja di dalam game. Ini bukan shell Android dan tidak membuka sistem ponselmu. Ketik help untuk melihat perintah yang diizinkan, seperti ls untuk melihat berkas dan cat untuk membaca isinya."
        "Bagaimana membaca berkas misi?" ->
            "Setelah message_fragment.txt diunduh, buka Terminal dan ketik cat message_fragment.txt. Perintah cat hanya menampilkan isi berkas; ia tidak menjalankan kode Python. Perhatikan teks yang muncul, karena itulah data yang diminta oleh tugas pertama."
        "Apa itu variabel Python?" ->
            "Variabel adalah nama yang kita gunakan untuk menyimpan sebuah nilai. Tanda = berarti memberikan nilai kepada nama tersebut. Teks harus berada di antara tanda kutip. Contohnya: signal = \"teks yang kamu baca\". Baris berikutnya, print(signal), menampilkan nilai yang disimpan."
        "Bagaimana menjalankan dan mengirim kode?" ->
            "Tulis kode pada Code Editor. Tekan JALANKAN DI TERMINAL: game akan menyimpan skrip ke ruang kerja dan menjalankan perintah python signal.py pada Terminal. Periksa keluarannya. Jika sudah berhasil dan kodenya tidak berubah, buka Browser > Mission Control untuk mengirimkannya ke evaluator."
        "Masih banyak yang harus kupelajari." ->
            "Bagus. Aku justru lebih khawatir kalau kamu berpura-pura sudah paham. Kita akan membangun kemampuanmu dari tugas yang benar-benar kamu kerjakan."
        "Mengapa kamu memilihku?" ->
            "Aku tahu pertanyaan itu akan muncul. Untuk sekarang, anggap saja ada alasan yang belum bisa kubagikan lewat kanal ini. Tunjukkan dulu bagaimana kamu menghadapi hal yang belum kamu ketahui."
        else ->
            "Aku mengerti. Periksa briefing dan bukti yang tersedia, lalu kita bahas hasilnya bersama. Jangan menyimpulkan sesuatu sebelum datanya mendukung."
    }

    fun nexusResponse(
        message: String,
        activeMissionTitle: String?,
        completedMissionCount: Int
    ): String = when (message) {
        "Tampilkan status personel." ->
            "STATUS PERSONEL: RECRUIT. Misi terverifikasi: $completedMissionCount. Misi aktif: ${activeMissionTitle ?: "tidak ada"}. Clearance mengikuti checkpoint kampanye yang tersimpan."
        "Di mana catatan operasiku?" ->
            "Catatan personel tersedia melalui BERKAS PERSONEL. Bukti dan sumber pemeriksaan dapat ditinjau melalui ANALISIS. Akses aplikasi mengikuti izin akun lokal."
        "Apakah sistem ini berjalan offline?" ->
            "Benar. Kampanye inti, runtime Python lokal, evaluasi misi, dan penyimpanan progres dirancang berjalan pada perangkat ini tanpa layanan AI atau server eksternal."
        else ->
            "Permintaan diterima. Aku hanya akan merujuk pada data dan izin yang tersedia di dalam misi aktif."
    }
}
