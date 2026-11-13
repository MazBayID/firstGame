package com.zeroday.cyberprotocol.ui

import com.zeroday.cyberprotocol.core.model.GameLanguage

/**
 * Authored, deterministic dialogue for the opening campaign.
 *
 * These responses are presentation content only: they do not change relationships,
 * mission access, or progression. Game-state changes remain explicit ViewModel actions.
 */
internal object MessengerDialogue {
    fun missionAttachment(activeMissionId: String?): String? = when (activeMissionId) {
        "first_contact.signal" -> "message_fragment.txt"
        "first_contact.identity" -> "identity_fragment.txt"
        else -> null
    }

    fun freyaIntroduction(
        activeMissionTitle: String?,
        completedMissionCount: Int,
        language: GameLanguage
    ): List<String> {
        if (language == GameLanguage.ENGLISH) {
            return buildList {
                add("Hello. I am Freya Knox. Welcome. I will guide you through the opening A.E.G.I.S. missions and explain what you need to do. We will take this one step at a time—there is no reason to pretend you understand something you have only just encountered.")
                add(
                    if (activeMissionTitle != null) {
                        "I can see your current task: $activeMissionTitle. Review the briefing and objectives one by one. We will learn the Terminal and Python from the basics before you work independently on technical tasks."
                    } else {
                        "If no task is active, reopen the mission sequence from Campaign. I will help you understand the instructions when the next task becomes available."
                    }
                )
                add("Your progress shows $completedMissionCount verified missions. There is no need to rush; what matters is understanding the reason behind each step.")
                add("For additional technical support, there is NEXUS. The system assists with intelligence communication and diagnostics, but it does not replace a mentor. I will continue helping you understand each step.")
                add("The Terminal accepts text commands in the game's workspace. Python is the language we will use to write instructions. We will begin with simple commands, reading files, storing values in variables, and displaying results.")
                add("After the basic introduction, open the A.E.G.I.S. Intranet through the internal Browser. Use personnel ID RECRUIT-014 and access code FIRSTCONTACT. This portal runs locally on your device.")
                add("For the First Signal task, I have also attached message_fragment.txt. Save it to the mission workspace, then read it in the Terminal. The Browser Repository and this attachment point to the same file.")
                if (activeMissionTitle == "Identity Verification" || activeMissionTitle == "Verifikasi Identitas") {
                    add("For Identity Verification, use identity_fragment.txt. Download it from the attachment or File Repository, read it with cat, then store its exact contents in the message variable. Do not guess the message.")
                }
            }
        }
        return buildList {
            add("Halo. Aku Freya Knox. Selamat datang. Aku akan mendampingimu selama rangkaian misi awal A.E.G.I.S. dan membantu menjelaskan apa yang perlu kamu lakukan. Kita jalani satu per satu—tidak ada alasan untuk berpura-pura sudah memahami sesuatu yang baru kamu temui.")
            add(if (activeMissionTitle != null) {
                "Aku melihat tugas aktifmu sekarang: $activeMissionTitle. Periksa briefing dan tujuan satu per satu. Kita akan mempelajari terminal dan Python dari dasar sebelum kamu mengerjakan tugas teknis secara mandiri."
            } else {
                "Kalau belum ada tugas aktif, buka kembali rangkaian misi dari Campaign. Aku akan membantumu memahami instruksi saat tugas berikutnya tersedia."
            })
            add("Progresmu tercatat pada $completedMissionCount misi yang sudah diverifikasi. Kita tidak perlu terburu-buru; yang penting kamu memahami alasan di balik setiap langkah.")
            add("Untuk dukungan teknis tambahan, ada NEXUS. Sistem ini membantu komunikasi intelijen dan diagnostik, tetapi bukan pengganti mentor. Aku tetap akan membimbingmu memahami setiap langkah.")
            add("Terminal adalah tempat memberi perintah teks kepada lingkungan kerja game. Python adalah bahasa yang akan kita gunakan untuk menulis instruksi. Kita mulai dari perintah sederhana, membaca berkas, menyimpan nilai ke variabel, lalu menampilkan hasilnya.")
            add("Setelah pengenalan dasar, buka A.E.G.I.S. Intranet melalui Browser internal. Gunakan ID personel RECRUIT-014 dan kode akses FIRSTCONTACT. Portal ini berjalan lokal di perangkatmu.")
            add("Untuk tugas Sinyal Pertama, aku juga melampirkan message_fragment.txt. Simpan berkas itu ke ruang kerja misi, lalu baca melalui Terminal. Browser Repository dan lampiran ini menunjuk berkas yang sama.")
            if (activeMissionTitle == "Identity Verification" || activeMissionTitle == "Verifikasi Identitas") {
                add("Untuk Verifikasi Identitas, gunakan identity_fragment.txt. Unduh berkas dari lampiran atau File Repository, baca dengan cat, lalu simpan isi persisnya ke variabel message. Jangan menebak isi pesan.")
            }
        }
    }

    fun nexusIntroduction(language: GameLanguage): List<String> =
        if (language == GameLanguage.ENGLISH) listOf(
            "The A.E.G.I.S. communication channel is active. Personnel status: Recruit. I am an analysis and communication support system, not an operational decision-maker.",
            "I can explain permitted data, help review technical results, and relay instructions approved by your mentor."
        ) else listOf(
            "Kanal komunikasi A.E.G.I.S. aktif. Status personel: Recruit. Aku adalah sistem pendukung analisis dan komunikasi, bukan pengambil keputusan operasional.",
            "Aku dapat menjelaskan data yang diizinkan, membantu memeriksa hasil teknis, dan meneruskan instruksi yang telah disahkan mentor."
        )

    fun freyaResponse(message: String, language: GameLanguage = GameLanguage.INDONESIAN): String {
        if (language == GameLanguage.ENGLISH) return when (message) {
            "Dimengerti. Kita mulai dari mana?" -> "Start with the nearest task: open the A.E.G.I.S. Browser, sign in with the credentials I gave you, and download the mission file. Then read it in the in-game Terminal. I will explain each step before asking you to try it yourself."
            "Apa fungsi Terminal?" -> "The Terminal accepts text commands inside the game's own workspace. It is not your Android shell and cannot access your phone's system. Type help to see allowed commands, such as ls to list files and cat to read one."
            "Bagaimana membaca berkas misi?" -> "After downloading message_fragment.txt, open Terminal and type cat message_fragment.txt. The cat command only displays the file; it does not execute Python. The displayed text is the data required by your first task."
            "Apa itu variabel Python?" -> "A variable is a name used to store a value. The = sign assigns a value to that name. Text must be enclosed in quotation marks. For example: signal = \"the text you read\". On the next line, print(signal) displays the stored value."
            "Bagaimana menjalankan dan mengirim kode?" -> "Write your code in the Code Editor. Select RUN IN TERMINAL: the game saves the script to the workspace and runs python signal.py in its Terminal. Check the output. If it succeeds and the code has not changed, open Browser > Mission Control to submit it to the evaluator."
            "Masih banyak yang harus kupelajari." -> "Good. I would be more concerned if you pretended to understand everything already. We will build your skills through tasks you actually complete."
            "Mengapa kamu memilihku?" -> "I knew that question would come up. For now, assume there is a reason I cannot share through this channel. Show me how you handle something you do not yet know."
            else -> "I understand. Review the briefing and available evidence, then we can discuss the results. Do not reach a conclusion before the data supports it."
        }
        return when (message) {
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
    }

    fun nexusResponse(
        message: String,
        activeMissionTitle: String?,
        completedMissionCount: Int,
        language: GameLanguage = GameLanguage.INDONESIAN
    ): String {
        if (language == GameLanguage.ENGLISH) return when (message) {
            "Tampilkan status personel." -> "PERSONNEL STATUS: RECRUIT. Verified missions: $completedMissionCount. Active mission: ${activeMissionTitle ?: "none"}. Clearance follows the saved campaign checkpoint."
            "Di mana catatan operasiku?" -> "Personnel records are available through PERSONNEL DOSSIER. Evidence and investigation sources can be reviewed through ANALYSIS. App access follows the local account permissions."
            "Apakah sistem ini berjalan offline?" -> "Yes. The core campaign, local Python runtime, mission evaluation, and progress storage are designed to run on this device without an AI service or external server."
            else -> "Request received. I will refer only to data and permissions available in the active mission."
        }
        return when (message) {
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
}
