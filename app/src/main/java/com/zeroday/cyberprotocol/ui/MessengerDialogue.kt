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
            "Mulai dari briefing yang sedang terbuka. Baca tujuan pertama, periksa data yang disediakan, lalu coba tulis kode kecil. Kamu tidak harus menyelesaikan semuanya dalam sekali jalan."
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
