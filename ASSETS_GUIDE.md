# CYBER PROTOCOL — ASSETS GUIDE

> **Panduan produksi, penamaan, spesifikasi, penempatan, dan implementasi aset game.**

| Properti | Ketetapan |
|---|---|
| Game | Cyber Protocol |
| Repository | `MazBayID/firstGame` |
| Platform | Android |
| UI framework | Kotlin + Jetpack Compose |
| Art direction | Hybrid Cinematic Realism + Futuristic Technology |
| Presentation target | Maximum visual quality, dengan optimasi perangkat |
| Prioritas pengembangan | Menyelesaikan FIRST CONTACT; katalog mencakup keseluruhan campaign |
| Status dokumen | Panduan kerja yang dapat diperbarui seiring implementasi |

---

## Daftar isi

1. [Tujuan dan cara menggunakan panduan](#1-tujuan-dan-cara-menggunakan-panduan)
2. [Identitas visual dan art direction](#2-identitas-visual-dan-art-direction)
3. [Prinsip produksi aset](#3-prinsip-produksi-aset)
4. [Ukuran, pixel, density, dan format](#4-ukuran-pixel-density-dan-format)
5. [Struktur folder dan penempatan](#5-struktur-folder-dan-penempatan)
6. [Aturan ID dan nama file](#6-aturan-id-dan-nama-file)
7. [Master asset inventory](#7-master-asset-inventory)
8. [Spesifikasi setiap kategori](#8-spesifikasi-setiap-kategori)
9. [Implementasi di Android dan Compose](#9-implementasi-di-android-dan-compose)
10. [Animasi, efek, dan audio](#10-animasi-efek-dan-audio)
11. [Optimasi dan batas teknis](#11-optimasi-dan-batas-teknis)
12. [Alur kerja membuat aset](#12-alur-kerja-membuat-aset)
13. [Quality assurance](#13-quality-assurance)
14. [Katalog campaign dari awal sampai akhir](#14-katalog-campaign-dari-awal-sampai-akhir)
15. [Checklist aset dan status produksi](#15-checklist-aset-dan-status-produksi)
16. [Istilah penting](#16-istilah-penting)

---

# 1. Tujuan dan cara menggunakan panduan

Dokumen ini menjelaskan **apa yang harus dibuat, bagaimana bentuknya, berapa ukuran kerjanya, bagaimana menamainya, di mana menyimpannya, dan bagaimana game menggunakannya**.

Panduan ini bukan perintah untuk membuat seluruh aset sekaligus. Fungsinya adalah menjadi peta produksi jangka panjang agar aset yang dibuat hari ini tetap cocok ketika campaign bertambah.

### 1.1 Prinsip penting

- Katalog boleh mencakup seluruh campaign, tetapi hanya konten cerita yang sudah ditetapkan yang boleh dianggap final.
- Jangan menciptakan karakter, bukti, lokasi, atau kejadian baru hanya untuk mengisi daftar aset.
- Aset yang belum diketahui detailnya diberi status **TBD (To Be Defined)**.
- Aset yang sudah ada di repository harus diperiksa sebelum dibuat ulang.
- Satu aset harus memiliki satu identitas/ID yang stabil.
- File sumber/master harus dipertahankan; jangan hanya menyimpan file hasil kompresi.
- Aset belum dianggap selesai hanya karena gambarnya sudah dibuat. Aset harus diintegrasikan dan diuji di layar game.
- Kualitas visual maksimum tidak berarti semua file harus berukuran sangat besar.

### 1.2 Tingkat prioritas

| Prioritas | Arti | Contoh |
|---|---|---|
| P0 — Essential | Dibutuhkan agar alur inti berfungsi atau dapat dipahami | Logo, portrait karakter yang tampil, bukti wajib, ikon aplikasi |
| P1 — High | Sangat berpengaruh pada kualitas presentasi | Background utama, portrait ekspresif, artwork kasus |
| P2 — Polish | Memperkuat atmosfer dan detail | Glitch, ambient particles, variasi dekorasi |
| P3 — Optional | Tambahan yang tidak menghalangi permainan | Alternatif tema, collectible dekoratif |

### 1.3 Status produksi

Gunakan status berikut pada inventory:

1. **PLANNED** — kebutuhan sudah tercatat.
2. **SPECIFIED** — ukuran, format, penggunaan, dan referensi sudah ditentukan.
3. **REFERENCE APPROVED** — konsep visual sudah disetujui.
4. **IN PRODUCTION** — sedang dibuat.
5. **ART APPROVED** — hasil visual sudah diperiksa.
6. **OPTIMIZED** — ekspor runtime sudah dibuat.
7. **INTEGRATED** — sudah dipasang ke game.
8. **TESTED** — sudah diperiksa pada perangkat/layout sasaran.
9. **FINAL** — diterima sebagai versi produksi.

---

# 2. Identitas visual dan art direction

## 2.1 Arah visual resmi

Cyber Protocol memakai **hybrid cinematic realism**:

- Realisme sinematik menjadi fondasi karakter, lingkungan, objek, dan dokumen.
- Teknologi futuristik digunakan untuk sistem A.E.G.I.S., antarmuka digital, perangkat, dan efek.
- Visual harus terasa seperti techno-thriller modern, bukan dunia neon yang berlebihan.
- Setiap elemen harus mendukung fungsi, cerita, atau suasana.

### 2.2 Pembagian karakter visual

| Elemen | Arah visual |
|---|---|
| Dunia nyata | Cinematic realism, material believable, pencahayaan natural |
| A.E.G.I.S. | Profesional, rahasia, presisi, modern |
| Teknologi | Futuristik tetapi masuk akal dan fungsional |
| Investigasi | Forensik, dokumenter, detail, mudah dibaca |
| Messenger | Personal, jelas, terasa seperti alat komunikasi sungguhan |
| Terminal/Code Editor | Teknis, fokus pada teks, minim dekorasi |
| Efek | Terukur; tidak menutupi informasi |
| Campaign artwork | Cinematic composition, identitas visual kuat |

### 2.3 Warna

Warna final harus dikunci dalam satu palette token setelah art direction sheet disetujui. Jangan memilih warna baru secara acak untuk tiap layar.

Palet awal yang disarankan (dapat disesuaikan setelah uji kontras):

| Token | Nilai awal | Fungsi |
|---|---|---|
| `background_primary` | `#080D14` | Latar utama |
| `background_secondary` | `#101925` | Panel/lapisan kedua |
| `surface_elevated` | `#172332` | Kartu dan panel terangkat |
| `text_primary` | `#E8EEF5` | Teks utama |
| `text_secondary` | `#9AAABD` | Teks sekunder |
| `accent_primary` | `#55C7D9` | Aksen teknologi |
| `accent_secondary` | `#6D8DFF` | Aksen sistem |
| `status_success` | `#63C995` | Status berhasil |
| `status_warning` | `#E6B65C` | Peringatan |
| `status_danger` | `#E36D78` | Error/bahaya |
| `border_subtle` | `#293849` | Garis pemisah |

Nilai di atas adalah **proposal palette**, bukan klaim bahwa seluruh UI saat ini sudah menggunakannya. Implementasikan sebagai theme/color tokens Kotlin, bukan gambar berisi warna yang ditempel di setiap layar.

### 2.4 Pencahayaan dan material

- Gunakan pencahayaan sinematik yang punya sumber masuk akal.
- Pertahankan arah cahaya dan temperatur warna dalam satu adegan.
- Material kaca, logam, plastik, kain, dan layar harus dapat dibedakan.
- Glow hanya untuk sumber cahaya, status aktif, atau teknologi yang memang memancarkan cahaya.
- Hindari bloom/glow berlebihan yang mengurangi kontras teks.
- Hindari tekstur noise yang membuat UI terlihat kotor atau sulit dibaca.

### 2.5 Tipografi

Tentukan satu keluarga font utama dan satu keluarga monospace untuk tampilan teknis.

- UI dan dialog: font sans-serif yang sangat mudah dibaca.
- Terminal dan kode: monospace.
- Judul chapter: boleh memakai display typeface, tetapi tetap terbaca.
- Dokumen bukti: jangan membuat teks penting menjadi bagian permanen dari tekstur gambar jika teks harus diterjemahkan, diperbesar, atau diakses pembaca layar.

Lisensi font wajib diperiksa sebelum digunakan.

---

# 3. Prinsip produksi aset

## 3.1 Master source dan runtime export

Setiap aset raster penting memiliki dua bentuk:

1. **Master/source** — file kerja berkualitas tinggi dan dapat diedit.
2. **Runtime export** — file final yang telah dipotong, dikompresi, diberi nama resource, dan siap dipakai aplikasi.

Contoh:

- Master: `CP_CHAR_FREYA_PORTRAIT_NEUTRAL_master.png`
- Runtime: `char_freya_portrait_neutral.webp`

Jangan menimpa file master ketika melakukan kompresi.

## 3.2 Jangan merasterisasi semua UI

Gunakan gambar untuk hal yang memang merupakan karya visual:
- Artwork karakter.
- Background.
- Ilustrasi.
- Tekstur.
- Logo yang kompleks.
- Bukti foto yang memang berupa foto.

Gunakan Compose untuk:
- Tombol.
- Kartu.
- Tab.
- Panel.
- Teks.
- Progress bar.
- Status.
- Dialog.
- Daftar file.
- Chat bubble.
- Terminal output.
- Layout dokumen yang teksnya perlu dibaca.

Dengan begitu, UI tetap responsif, dapat diterjemahkan, dapat diakses, dan tidak pecah ketika ukuran layar berubah.

## 3.3 Buat aset modular

Pisahkan komponen yang perlu berubah secara independen.

Contoh portrait karakter:
- Artwork portrait tanpa nama.
- Nama karakter ditampilkan sebagai teks Compose.
- Status/role ditampilkan sebagai komponen UI.
- Background portrait dipisahkan bila perlu.

Contoh evidence card:
- Foto bukti sebagai gambar.
- Judul, timestamp, sumber, dan status verifikasi sebagai teks/data.
- Border, badge, dan layout dibuat dengan Compose.

## 3.4 Jangan menanam informasi cerita ke dekorasi

Timestamp, nomor bukti, nama, hasil analisis, dan metadata yang harus akurat sebaiknya berasal dari data game. Gambar hanya menjadi representasi visualnya.

Untuk RX-14, desain harus mempertahankan perbedaan antara fakta terverifikasi, hipotesis, informasi yang diperselisihkan, dan hal yang belum diketahui. Artwork tidak boleh menyiratkan pelaku atau penyebab yang belum dibuktikan.

---

# 4. Ukuran, pixel, density, dan format

Bagian ini penting karena **pixel gambar tidak sama dengan dp Android**.

## 4.1 Pixel vs dp

- **px (pixel):** jumlah titik pada file gambar.
- **dp (density-independent pixel):** satuan layout Android.
- **sp (scale-independent pixel):** satuan ukuran teks.
- **DPI/density:** faktor yang menghubungkan dp dengan pixel fisik.

Rumus dasar:

`px = dp × densityScale`

Contoh: komponen 100 dp pada layar density 3x membutuhkan sekitar 300 px untuk aset raster yang dibuat pada skala tersebut.

Jangan menetapkan ukuran UI hanya dengan pixel gambar. Ukuran tombol, jarak, dan panel ditentukan dengan dp di Compose.

## 4.2 Kanvas referensi

Gunakan kanvas referensi **1080 × 2400 px (portrait 9:20)** untuk menyusun konsep full-screen portrait dan mengevaluasi komposisi.

Ini adalah kanvas kerja, bukan ukuran layar wajib. Game harus tetap beradaptasi pada rasio layar yang lebih pendek/panjang, landscape bila didukung, cutout, dan system bars.

Untuk artwork landscape:
- Referensi 1920 × 1080 px (16:9).
- Siapkan safe composition di bagian tengah.
- Jangan menaruh wajah, logo, atau informasi penting dekat area yang mudah terpotong.

## 4.3 Rekomendasi ukuran raster

Angka berikut adalah **target awal produksi**, bukan batas mutlak. Buat ukuran final berdasarkan ukuran tampil terbesar, crop, density, dan kebutuhan perangkat.

| Jenis aset | Ukuran master yang disarankan | Catatan |
|---|---:|---|
| App icon / emblem sederhana | 512 × 512 px | Vector lebih baik jika bentuknya sederhana |
| UI icon raster | 96–192 px | Utamakan vector |
| Small decorative texture | 256–512 px | Dapat tile/reuse |
| Character avatar | 512 × 512 px | Untuk daftar/kontak |
| Character portrait | 1024 × 1024 px | Portrait dialog square |
| Character half/full portrait | 1024 × 1536 px | Transparan bila diperlukan |
| Full-screen portrait background | 1080 × 2400 px | Periksa crop berbagai rasio |
| Landscape environment | 1920 × 1080 px | Gunakan saat layar/adegan landscape |
| Case/mission cover | 1200 × 1600 px | Komposisi vertikal |
| Evidence photograph | 1200–2048 px sisi panjang | Pertahankan detail penting |
| Document texture | 1024 × 1536 px | Teks dokumen dirender terpisah |
| Large cinematic key art | 2048 px atau lebih pada sisi panjang | Hanya bila tampil besar |
| Achievement icon | 256–512 px | Vector juga cocok |
| Button texture | 512 × 256 px atau vector | Jangan rasterisasi teks tombol |

**Jangan otomatis membuat semua gambar 4K.** Jika gambar hanya tampil selebar 300 dp, file 4K dapat membuang memori tanpa peningkatan yang terlihat.

## 4.4 Crop dan safe area

Untuk setiap background atau portrait:
- Tentukan focal point (wajah, objek, atau area utama).
- Sisakan ruang untuk teks dan komponen UI.
- Buat komposisi yang masih terbaca ketika bagian tepi terpotong.
- Jangan menaruh informasi penting pada area yang tertutup status bar, navigation bar, kamera, atau dialog.
- Tentukan apakah gambar menggunakan `Crop`, `Fit`, atau `FillBounds` saat diimplementasikan.

Buat preview minimal pada:
- Rasio portrait tinggi.
- Rasio portrait pendek.
- Layar kecil.
- Layar dengan cutout/system bars.

## 4.5 Format file

| Format | Gunakan untuk | Hindari untuk |
|---|---|---|
| PNG | Transparansi, logo raster, pixel-perfect UI, source lossless | Foto besar tanpa kebutuhan transparansi |
| WebP lossy | Foto, portrait, background, artwork runtime | Master source yang perlu diedit berulang |
| WebP lossless | Gambar transparan/flat yang cocok dikompresi lossless | Semua aset secara otomatis tanpa uji |
| SVG | Source vector dan ikon | File SVG kompleks yang tidak didukung langsung sebagai Android resource |
| Android Vector Drawable XML | Ikon/logo vector yang dipakai sebagai resource Android | Artwork foto/tekstur kompleks |
| JPEG | Foto tanpa transparansi bila pipeline memerlukannya | Aset yang butuh alpha |
| OGG | Audio runtime terkompresi | Master editing |
| WAV | Audio source dan efek pendek yang perlu kualitas PCM | Musik panjang tanpa kompresi untuk runtime |
| TTF/OTF | Master font | File runtime tanpa pemeriksaan lisensi dan dukungan |

**Catatan Android:** file SVG umumnya perlu dikonversi menjadi Android Vector Drawable XML atau raster sebelum dipakai sebagai resource native. Jangan mengasumsikan Compose dapat memuat sembarang SVG melalui `painterResource`.

## 4.6 Transparansi

Gunakan alpha/transparency untuk:
- Portrait karakter tanpa background.
- Logo.
- Icon.
- Overlay.
- Particle/sprite effect.

Periksa tepi transparan pada background terang dan gelap. Hindari halo putih/hitam akibat proses export.

---

# 5. Struktur folder dan penempatan

Repository saat ini adalah aplikasi Android. Gunakan lokasi runtime Android yang sesuai dengan jenis aset.

## 5.1 Struktur source asset yang direkomendasikan

Simpan file master dan file kerja di folder produksi terpisah dari resource Android runtime.

Struktur kerja yang disarankan:

```text
asset_source/
├── brand/
│   ├── logos/
│   ├── emblems/
│   └── typography/
├── characters/
│   ├── freya/
│   ├── nexus/
│   └── future_characters/
├── environments/
│   ├── aegis/
│   └── campaign_locations/
├── props/
├── story/
│   ├── recruitment/
│   ├── first_contact/
│   └── cases/
│       └── rx14/
├── ui_reference/
├── effects/
├── audio_source/
├── animation_source/
└── exports/
    ├── review/
    └── runtime/
```

Folder ini adalah **rekomendasi struktur produksi**. Buat hanya saat workflow aset mulai digunakan; jangan membuat folder kosong massal tanpa kebutuhan.

## 5.2 Lokasi runtime Android

| Lokasi | Isi | Cara akses umum |
|---|---|---|
| `app/src/main/res/drawable/` | Vector drawable XML, drawable density-aware, shape XML | `R.drawable.nama` |
| `app/src/main/res/drawable-nodpi/` | Raster yang ingin disimpan tanpa auto density scaling resource | `R.drawable.nama` |
| `app/src/main/res/drawable-*/` | Raster dengan density qualifier bila memang dibutuhkan | `R.drawable.nama` |
| `app/src/main/res/font/` | Font runtime | `R.font.nama` |
| `app/src/main/res/raw/` | Audio atau file raw yang perlu resource ID | `R.raw.nama` |
| `app/src/main/assets/` | File dengan struktur folder/nama yang perlu dibuka melalui AssetManager | `context.assets.open(...)` |
| `app/src/main/res/values/` | Resource values, bukan gambar artwork | resource XML |

Repository saat ini sudah memiliki `drawable/`, `drawable-nodpi/`, `assets/`, dan resource values. Periksa isi yang sudah ada sebelum menambahkan file baru.

### Kapan memakai drawable-nodpi?

Gunakan untuk raster yang dimensinya ingin dipertahankan apa adanya oleh Android resource system, misalnya background atau portrait besar yang ukurannya diatur melalui Compose.

Namun, `nodpi` **bukan berarti** gambar akan otomatis cocok dengan layar. Layout tetap harus menentukan ukuran tampil dan content scale. Ukuran file tetap memengaruhi memori.

### Kapan memakai assets?

Gunakan `app/src/main/assets/` untuk:
- File dengan nested directory yang ingin dibuka berdasarkan path.
- Konten yang dikelola sebagai file, bukan resource ID.
- Bundel data visual/audio tertentu.

Untuk gambar biasa yang dipanggil sebagai resource Compose, drawable biasanya lebih sederhana.

## 5.3 Contoh struktur runtime

Contoh target organisasi setelah aset benar-benar tersedia:

```text
app/src/main/res/
├── drawable/
│   ├── aegis_logo.xml
│   ├── ic_messenger.xml
│   ├── ic_terminal.xml
│   └── panel_background.xml
├── drawable-nodpi/
│   ├── bg_main_menu.webp
│   ├── bg_aegis_operations.webp
│   ├── char_freya_portrait_neutral.webp
│   ├── char_freya_portrait_serious.webp
│   ├── case_rx14_cover.webp
│   └── evidence_rx14_record_a.webp
├── font/
│   ├── interface_regular.ttf
│   └── terminal_mono.ttf
└── raw/
    ├── music_main_theme.ogg
    ├── ambience_operations.ogg
    └── sfx_notification.ogg
```

Ini adalah contoh penempatan, **bukan pernyataan bahwa file-file tersebut sudah ada**. Nama resource Android harus lowercase dan memakai underscore.

---

# 6. Aturan ID dan nama file

Setiap aset memiliki dua nama:

1. **Asset ID** — identitas permanen untuk katalog, komunikasi, dan tracking.
2. **Runtime filename/resource name** — nama file yang sesuai aturan Android.

## 6.1 Format Asset ID

Gunakan:

`CP_<CATEGORY>_<SUBCATEGORY>_<SUBJECT>_<VARIANT>`

Contoh:

| Asset ID | Arti |
|---|---|
| `CP_BRAND_LOGO_PRIMARY` | Logo utama |
| `CP_BRAND_AEGIS_EMBLEM_PRIMARY` | Emblem A.E.G.I.S. |
| `CP_CHAR_FREYA_PORTRAIT_NEUTRAL` | Portrait netral Freya |
| `CP_CHAR_NEXUS_AVATAR_DEFAULT` | Avatar default NEXUS |
| `CP_ENV_AEGIS_OPERATIONS_MAIN` | Lingkungan Operations Center |
| `CP_PROP_DEVICE_SECURE_PHONE_01` | Perangkat komunikasi |
| `CP_EVID_RX14_RECORD_A` | Bukti record A RX-14 |
| `CP_UI_TERMINAL_BACKGROUND` | Background terminal |
| `CP_FX_SIGNAL_GLITCH_01` | Efek signal glitch |
| `CP_AUDIO_UI_NOTIFICATION_01` | Bunyi notifikasi |
| `CP_ANIM_APP_OPEN_MESSENGER` | Animasi pembukaan Messenger |
| `CP_CAMPAIGN_FIRST_CONTACT_COVER` | Cover First Contact |

Aturan:
- Prefix `CP_` selalu digunakan.
- Gunakan huruf kapital dan underscore untuk Asset ID.
- Jangan mengubah ID ketika file dipindah.
- Jangan memakai satu ID untuk dua aset berbeda.
- Variasi harus eksplisit, misalnya `NEUTRAL`, `SERIOUS`, `SELECTED`, `DISABLED`.
- Nomor urut digunakan jika ada beberapa objek sejenis.

## 6.2 Format nama file runtime

Gunakan lowercase snake_case:

`<category>_<subject>_<variant>.<extension>`

Contoh:
- `char_freya_portrait_neutral.webp`
- `char_freya_portrait_serious.webp`
- `bg_main_menu.webp`
- `case_rx14_cover.webp`
- `evidence_rx14_record_a.webp`
- `sfx_notification.ogg`

Jangan gunakan:
- Spasi.
- Huruf kapital.
- Nama seperti `final_final2_revisi.png`.
- Nama generik seperti `image1.png`, `newasset.png`, atau `test.png`.

## 6.3 Versi dan revisi

Versi master dicatat dalam manifest, bukan dengan menggandakan nama runtime secara acak.

Contoh:
- Asset ID tetap: `CP_CHAR_FREYA_PORTRAIT_NEUTRAL`
- Master revision: `v003`
- Runtime filename tetap: `char_freya_portrait_neutral.webp`

Jika revisi baru menggantikan aset, catat perubahan dan periksa semua penggunaan.

---

# 7. Master asset inventory

Bagian ini adalah daftar kebutuhan utama. Nama yang bersifat deskriptif adalah nama kerja, bukan berarti karakter/lokasi/cerita baru sudah disahkan.

## 7.1 Brand identity

| ID | Aset | Spesifikasi awal | Prioritas |
|---|---|---|---|
| `CP_BRAND_LOGO_PRIMARY` | Logo Cyber Protocol | Vector master; export transparan 1024 px | P0 |
| `CP_BRAND_AEGIS_EMBLEM_PRIMARY` | Emblem A.E.G.I.S. | Vector master; export 1024 px | P0 |
| `CP_BRAND_SPLASH_ART` | Splash artwork | 1080 × 2400 px portrait | P1 |
| `CP_BRAND_TITLE_ART` | Title artwork | 1920 × 1080 px landscape + adaptasi portrait bila perlu | P1 |
| `CP_BRAND_CLASSIFICATION_MARKS` | Tanda klasifikasi | Vector | P1 |
| `CP_BRAND_DEPARTMENT_MARKS` | Insignia departemen | TBD, hanya jika departemen ditetapkan | P2 |
| `CP_BRAND_COLOR_SYSTEM` | Palette | Color tokens Kotlin | P0 |
| `CP_BRAND_TYPE_SYSTEM` | Typography | Font files + type scale | P0 |

## 7.2 Character assets

### Karakter yang sudah disebut dalam fondasi cerita

| ID | Aset | Isi minimum | Prioritas |
|---|---|---|---|
| `CP_CHAR_FREYA_PORTRAIT_NEUTRAL` | Freya — neutral | Portrait dialog | P0 |
| `CP_CHAR_FREYA_PORTRAIT_SERIOUS` | Freya — serious | Portrait dialog | P1 |
| `CP_CHAR_FREYA_AVATAR` | Freya — avatar | Square contact image | P0 |
| `CP_CHAR_NEXUS_AVATAR_DEFAULT` | NEXUS — default | System/avatar identity | P0 |
| `CP_CHAR_NEXUS_STATE_ACTIVE` | NEXUS — active state | Visual state/icon | P1 |
| `CP_CHAR_PLAYER_AVATAR_DEFAULT` | Player avatar | Placeholder/default profile | P1 |

Ekspresi tambahan Freya (concerned, surprised, happy, sad, angry, suspicious) dibuat jika dialog/story benar-benar membutuhkannya. Jangan membuat semua ekspresi hanya demi jumlah.

Untuk NEXUS, tentukan terlebih dahulu apakah representasinya berupa emblem, abstract AI visualization, waveform, atau avatar. Jangan otomatis menggambarkannya sebagai manusia.

### Karakter campaign mendatang

Tambahkan satu entri untuk setiap karakter baru setelah identitas naratifnya ditetapkan:
- Nama dan peran.
- Hubungan dengan A.E.G.I.S./pemain.
- Deskripsi visual yang disetujui.
- Portrait default.
- Ekspresi yang diperlukan.
- Avatar kecil.
- Variasi pakaian/lokasi hanya jika dibutuhkan cerita.
- Batas konsistensi wajah, rambut, pakaian, dan aksesori.

## 7.3 Environment assets

| ID | Lingkungan | Aset minimum | Prioritas |
|---|---|---|---|
| `CP_ENV_AEGIS_OPERATIONS_MAIN` | Operations Center | Main background | P1 |
| `CP_ENV_AEGIS_BRIEFING_ROOM` | Briefing Room | Background | P1 |
| `CP_ENV_AEGIS_INVESTIGATION_ROOM` | Investigation Room | Background | P1 |
| `CP_ENV_AEGIS_SERVER_ROOM` | Server Room | Background, detail props jika dipakai | P2 |
| `CP_ENV_AEGIS_HEADQUARTERS` | Headquarters | Establishing artwork | P1 |
| `CP_ENV_RECRUITMENT_SCENE_01` | Recruitment scene | Sesuai adegan final | P1 |
| `CP_ENV_FIRST_CONTACT_SCENE_01` | First Contact scene | Sesuai adegan final | P1 |
| `CP_ENV_RX14_LOCATION_01` | RX-14 location | TBD sampai lokasi kasus ditetapkan | P1 |
| `CP_ENV_CAMPAIGN_LOCATION_*` | Lokasi campaign mendatang | TBD | P2 |

Setiap lokasi yang benar-benar muncul harus punya:
- Establishing artwork atau background yang sesuai.
- Versi crop yang aman untuk layout.
- Catatan pencahayaan dan waktu.
- Props penting.
- Asset ID.
- Catatan scene tempat aset digunakan.

Jangan membuat banyak variasi hari/malam bila adegan tidak memerlukannya.

## 7.4 Props dan teknologi

| ID kelompok | Contoh aset | Prioritas |
|---|---|---|
| `CP_PROP_DEVICE_*` | Smartphone, laptop, workstation, headset | P1 |
| `CP_PROP_AEGIS_*` | Perangkat/terminal bermerek A.E.G.I.S. | P1 |
| `CP_PROP_NETWORK_*` | Server, router, kabel, storage | P2 |
| `CP_PROP_FORENSIC_*` | Digital recorder, scanner, storage evidence | P2 |
| `CP_PROP_SECURITY_*` | Camera, access panel, sensor | P2 |

Buat prop sebagai:
- Full object artwork bila menjadi fokus adegan.
- Detail/crop bila hanya menjadi pendukung background.
- Icon/thumbnail bila hanya tampil di daftar.

## 7.5 Story, document, dan evidence assets

### Recruitment dan First Contact

| ID | Aset | Bentuk |
|---|---|---|
| `CP_STORY_RECRUITMENT_INVITATION` | Recruitment invitation | Dokumen/attachment UI |
| `CP_STORY_AEGIS_MESSAGE_*` | Pesan resmi | Data teks + layout Messenger |
| `CP_STORY_FIRST_CONTACT_BRIEFING` | Briefing | Layout dokumen |
| `CP_STORY_FIRST_CONTACT_MESSAGE_FRAGMENT` | Message fragment | File virtual dan preview |
| `CP_STORY_FIRST_CONTACT_IDENTITY_FRAGMENT` | Identity fragment | File virtual dan preview |
| `CP_STORY_FIRST_CONTACT_TRAINING_COVER` | Cover training | Artwork |

**Penting:** `message_fragment.txt` dan `identity_fragment.txt` adalah file konten virtual yang disebut dalam gameplay. Keduanya tidak otomatis menjadi gambar. Isi teks tetap berupa data/file yang bisa dibaca player; gambar preview hanya tambahan bila diperlukan.

### RX-14

| ID | Aset | Bentuk | Catatan |
|---|---|---|---|
| `CP_CASE_RX14_COVER` | Case cover | Artwork 1200 × 1600 px |
| `CP_EVID_RX14_RECORD_A` | Record pertama | File/data + visualisasi |
| `CP_EVID_RX14_RECORD_B` | Record kedua | File/data + visualisasi |
| `CP_EVID_RX14_TIMELINE` | Perbandingan waktu | UI/vector/data visualization |
| `CP_EVID_RX14_SOURCE_CARD_*` | Source cards | Data + Compose card |
| `CP_CASE_RX14_ANALYSIS_ART` | Analysis visual | Diagram berbasis fakta |
| `CP_CASE_RX14_DEBRIEF_ART` | Debrief artwork | Artwork pendukung |

Fakta yang sudah ditetapkan: dua record transmisi menunjukkan waktu 09:41:12 dan 09:41:16, selisih empat detik. Penyebab selisih tersebut belum terbukti. Visual tidak boleh mengubah selisih itu menjadi bukti sabotase, pelaku, atau manipulasi.

### Campaign mendatang

Buat kategori kosong/placeholder pada manifest, tetapi jangan menentukan nama bukti, foto, dokumen, atau pelaku sebelum konten chapter disetujui.

## 7.6 UI assets

UI utama harus dirakit dengan Compose. Aset raster/vector yang diperlukan meliputi:

| ID kelompok | Isi |
|---|---|
| `CP_UI_GLOBAL_*` | Logo kecil, icon navigation, status, divider/ornament |
| `CP_UI_MENU_*` | Menu artwork, campaign thumbnails |
| `CP_UI_MESSENGER_*` | Attachment preview, avatar placeholder, media thumbnail |
| `CP_UI_BROWSER_*` | Site identity, empty/loading/error illustrations |
| `CP_UI_REPOSITORY_*` | File type icons jika bukan vector/system icon |
| `CP_UI_TERMINAL_*` | Terminal identity/illustration, bukan gambar teks terminal |
| `CP_UI_EDITOR_*` | Editor identity, status illustration |
| `CP_UI_OPERATIONS_*` | Case artwork, evidence thumbnails |
| `CP_UI_PROFILE_*` | Avatar placeholder, profile decoration |
| `CP_UI_ACHIEVEMENT_*` | Achievement icons |
| `CP_UI_LOADING_*` | Loading illustration/animation bila dibutuhkan |

Gunakan vector drawable untuk ikon sederhana dan konsisten. Jangan membuat setiap tombol sebagai file gambar terpisah; tombol, state, label, dan padding adalah komponen Compose.

## 7.7 Campaign presentation

| ID kelompok | Aset |
|---|---|
| `CP_CAMPAIGN_INTRO_*` | Intro key art dan title |
| `CP_CAMPAIGN_RECRUITMENT_*` | Recruitment artwork |
| `CP_CAMPAIGN_FIRST_CONTACT_*` | Chapter cover, mission covers, completion artwork |
| `CP_CAMPAIGN_RX14_*` | Case cover, case result, debrief artwork |
| `CP_CAMPAIGN_CHAPTER_*` | Artwork chapter mendatang, TBD |
| `CP_CAMPAIGN_ENDING_*` | Ending artwork, TBD sampai ending ditetapkan |
| `CP_CAMPAIGN_CREDITS_*` | Credits background/ornament |

---

# 8. Spesifikasi setiap kategori

## 8.1 Logo dan emblem

**Buat:**
- Versi utama.
- Versi horizontal/stacked jika komposisi memerlukannya.
- Versi terang dan gelap.
- Versi monochrome.
- Versi icon/small-size.

**Ukuran master:**
- Vector source: scalable.
- Raster export: 512 px, 1024 px, dan 2048 px bila diperlukan.

**Background:** transparan untuk logo overlay.

**Penempatan:** splash, title screen, menu, loading, dokumen resmi, dan emblem aplikasi.

**Implementasi:** Android Vector Drawable atau PNG/WebP transparan. Jangan meregangkan logo.

## 8.2 Character portrait

**Buat:**
- Portrait neutral untuk setiap karakter yang berbicara.
- Ekspresi tambahan hanya sesuai kebutuhan dialog.
- Avatar square untuk contact list.
- Full/half-body hanya jika adegan membutuhkan.

**Ukuran master:**
- Avatar: 512 × 512 px.
- Dialog portrait: 1024 × 1024 px.
- Half/full portrait: 1024 × 1536 px atau lebih jika tampil besar.

**Background:**
- Transparan untuk karakter yang ditumpuk di atas UI/background.
- Background terpisah bila portrait merupakan scene artwork.

**Penempatan:**
- Messenger contact.
- Dialogue.
- Character profile.
- Campaign introduction.

**Penting:** pertahankan konsistensi wajah, bentuk rambut, usia visual, warna pakaian, dan aksesori antar ekspresi. Buat satu approved character reference sheet sebelum menghasilkan banyak variasi.

## 8.3 Background dan environment

**Buat:**
- Establishing artwork.
- Main background.
- Detail/crop pendukung jika diperlukan.
- Alternate lighting hanya jika scene memerlukan.

**Ukuran kerja:**
- Portrait: 1080 × 2400 px.
- Landscape: 1920 × 1080 px.
- Hero scene: 2048 px atau lebih pada sisi panjang bila kualitas tampil membutuhkannya.

**Penempatan:** belakang konten Compose; foreground panel harus tetap punya kontras.

**Implementasi:** image painter dengan contentScale yang dipilih berdasarkan komposisi. Jangan mengandalkan satu crop untuk semua rasio layar.

## 8.4 Evidence dan dokumen

Bedakan:
1. **Evidence source:** data asli yang diperiksa player.
2. **Evidence visual:** foto, screenshot, diagram, atau ilustrasi yang mewakili source.
3. **Evidence UI:** layout yang menampilkan nama, asal, timestamp, status, dan hubungan bukti.

**Ukuran:**
- Foto evidence: 1200–2048 px sisi panjang.
- Thumbnail: 320–512 px sisi panjang.
- Dokumen background: 1024 × 1536 px bila tekstur visual memang diperlukan.

Teks penting harus menjadi teks/data UI, bukan dipanggang ke background. Ini mencegah typo, menjaga lokalisasi, dan memungkinkan zoom/accessibility.

## 8.5 Icon

Prioritas:
1. Compose Material/vector icon yang sudah sesuai.
2. Custom Android Vector Drawable untuk ikon identitas game.
3. PNG/WebP hanya jika bentuknya painterly/bertekstur dan vector tidak memadai.

Ukuran raster acuan:
- 24 dp icon umum: export sekitar 72 px pada target 3x.
- 32 dp icon: sekitar 96 px pada target 3x.
- 48 dp icon: sekitar 144 px pada target 3x.

Ukuran dp ditentukan di Compose; angka pixel adalah ukuran sumber untuk raster 3x, bukan ukuran layar.

Setiap custom icon perlu state:
- Default.
- Selected/active bila digunakan.
- Disabled bila digunakan.
- Warning/error bila relevan.

Jangan membuat semua state jika aplikasi tidak memiliki state tersebut.

---

# 9. Implementasi di Android dan Compose

Bagian ini menunjukkan cara aset dipakai. Sesuaikan nama package/import dengan kode aktual.

## 9.1 Memakai drawable resource

File:
`app/src/main/res/drawable-nodpi/bg_main_menu.webp`

Contoh Compose:

```kotlin
@Composable
fun MainMenuBackground() {
    Image(
        painter = painterResource(R.drawable.bg_main_menu),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
}
```

Gunakan `contentDescription = null` jika gambar murni dekoratif. Jika gambar menyampaikan informasi yang tidak tersedia pada teks lain, berikan deskripsi aksesibilitas yang bermakna.

## 9.2 Character portrait

```kotlin
@Composable
fun CharacterPortrait(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(R.drawable.char_freya_portrait_neutral),
        contentDescription = "Freya Knox",
        contentScale = ContentScale.Fit,
        modifier = modifier
    )
}
```

Ekspresi berbeda sebaiknya dipilih berdasarkan state/dialog karakter, bukan dengan mengedit satu gambar saat runtime.

## 9.3 Vector drawable

Custom icon dapat dibuat sebagai Android Vector Drawable XML di `res/drawable/`.

```kotlin
Icon(
    painter = painterResource(R.drawable.ic_messenger),
    contentDescription = "Messenger"
)
```

Gunakan tint dari theme jika ikon memang dirancang monochrome. Jangan memberi tint pada artwork full-color.

## 9.4 Membuka file dari assets

Untuk file yang ditempatkan di `app/src/main/assets/`:

```kotlin
context.assets.open("loading/animation.json").use { stream ->
    val content = stream.bufferedReader().readText()
}
```

Contoh di atas hanya pola akses file teks. Untuk gambar/audio, gunakan decoder/player yang sesuai dan hindari membaca file besar seluruhnya ke memory sebagai String.

## 9.5 Audio resource

File audio di `res/raw/` dapat diakses menggunakan resource ID. Pilih API playback yang sesuai dengan kebutuhan aplikasi, lifecycle, dan jumlah suara simultan. Jangan memulai playback dari Composable body tanpa pengelolaan lifecycle/effect yang benar.

## 9.6 UI tetap dibangun dengan Compose

Contoh prinsip:
- Background: artwork.
- Card: Compose Surface.
- Border: Compose border.
- Label: Text.
- Metadata: data model + Text.
- Icon: Vector Drawable.
- Status: color token + shape.
- Animation: Compose animation atau asset animation yang sesuai.

Jangan membuat screenshot seluruh layar sebagai satu gambar background. Itu akan membuat tombol tidak interaktif, teks tidak adaptif, dan layar sulit diterjemahkan.

## 9.7 Penempatan berdasarkan layar

| Layar/fitur | Aset yang ditempatkan | Dibangun dengan |
|---|---|---|
| Splash | Logo, background, optional animation | Image + Compose |
| Main Menu | Background/key art, logo, campaign thumbnail | Image + Compose layout |
| Messenger | Avatar, attachment thumbnail | Image + Compose chat UI |
| Browser | Logo/site identity, illustration | Vector/Image + Compose |
| File Repository | File type icons, previews | Vector/Image + Compose list |
| Terminal | Optional identity art | Compose text/terminal UI |
| Code Editor | Optional identity art | Compose editor UI |
| Operations Center | Case cover, evidence thumbnails | Image + Compose panels |
| Analysis | Diagram/plot bila dibutuhkan | Compose/vector/data visualization |
| Debrief | Artwork, badge, character portrait | Image + Compose |
| Profile/Achievements | Avatar, achievement icons | Image/vector + Compose |

---

# 10. Animasi, efek, dan audio

## 10.1 Animasi

Pilih teknologi sesuai bentuk gerak:

| Jenis | Pendekatan |
|---|---|
| Fade, slide, scale, expand | Compose animation |
| Loading indicator sederhana | Compose animation |
| Icon vector motion | Animated vector atau Compose |
| Character sprite | Sprite sheet/frame sequence bila dibutuhkan |
| Complex illustration motion | Lottie/format animasi yang sudah didukung project, setelah dependensi diperiksa |
| Cinematic video | Video asset hanya jika benar-benar dibutuhkan dan ukuran dikendalikan |

Jangan menambahkan library animasi baru sebelum memeriksa dependency dan kebutuhan aktual.

## 10.2 Frame animation

Jika menggunakan sprite sheet:
- Semua frame harus punya ukuran cell sama.
- Tentukan urutan frame.
- Tentukan frame rate/durasi.
- Pastikan tidak ada padding yang tidak disengaja.
- Uji loop dan transisi frame pertama/terakhir.
- Hindari sheet sangat besar jika frame bisa dipisah atau dianimasikan dengan cara lebih ringan.

Target awal:
- UI micro-animation: 150–300 ms.
- Feedback/status: 200–500 ms.
- Ambient loop: 2–8 detik sesuai gerak.
- Character reaction: sesuai ritme dialog.

Angka tersebut adalah panduan awal, bukan aturan mutlak.

## 10.3 Visual effects

Kelompok efek:
- Signal glitch/interference.
- Scan line/scanning.
- Data transfer.
- Holographic highlight.
- Notification pulse.
- Focus/selection.
- Mission completion.
- Screen transition.
- Ambient particles.

Efek dapat dibuat dengan Compose Canvas/graphics, vector, atau sprite. Pilih metode paling sederhana yang menghasilkan kualitas yang diperlukan.

Jangan memakai efek terus-menerus di semua layar. Beri jeda visual agar momen penting terasa istimewa.

## 10.4 Audio

Kelompok:
- Music: theme, menu, ambience, investigation, suspense, debrief, ending.
- UI SFX: click, select, back, notification, success, error.
- System SFX: download, terminal execution, data transfer, alert.
- Environment: room tone, server hum, rain, traffic jika relevan.
- Voice: character dialogue, radio, system announcement jika diproduksi.

Spesifikasi awal:
- Sample rate source: 44.1 kHz atau 48 kHz.
- Runtime music: OGG dengan bitrate yang diuji berdasarkan kualitas dan ukuran.
- UI SFX: pendek, bersih, tidak terlalu keras.
- WAV untuk source/editing; OGG untuk runtime bila sesuai.
- Peak/mastering harus menyisakan headroom dan tidak clipping.
- Volume music, voice, ambience, dan SFX harus dapat diseimbangkan.

Jangan memakai musik, voice, atau sound effect yang lisensinya tidak jelas. Simpan catatan sumber dan lisensi.

---

# 11. Optimasi dan batas teknis

Target visual maksimum harus tetap berjalan baik di perangkat Android.

## 11.1 Ukuran memori gambar

Perkiraan kasar bitmap RGBA saat didekode:

`memory ≈ width × height × 4 bytes`

Contoh:
- 1080 × 2400 RGBA ≈ 9.9 MiB.
- 2048 × 2048 RGBA ≈ 16 MiB.
- 4096 × 4096 RGBA ≈ 64 MiB.

File WebP 1 MiB dapat tetap memerlukan sekitar 10 MiB ketika didekode menjadi bitmap 1080 × 2400. Kompresi file tidak sama dengan ukuran memory runtime.

## 11.2 Aturan optimasi

- Decode pada ukuran yang mendekati ukuran tampil.
- Jangan memuat semua background campaign sekaligus.
- Lepaskan referensi/cached image yang tidak lagi dibutuhkan sesuai lifecycle.
- Gunakan thumbnail untuk daftar evidence.
- Load artwork besar saat layar membutuhkannya.
- Hindari banyak layer transparan besar yang saling menumpuk.
- Uji scrolling, perpindahan layar, dan memory pressure.
- Periksa ukuran APK setelah penambahan aset.
- Gunakan kembali aset modular bila tidak membuat tampilan repetitif.

## 11.3 Target pemeriksaan

Catat:
- Ukuran file asset.
- Resolusi.
- Estimasi memory decoded.
- Waktu load.
- Performa saat screen transition.
- Penggunaan memory pada layar dengan banyak thumbnail.
- Ukuran APK sebelum/sesudah batch aset.

Jangan menetapkan angka budget final sebelum mengukur baseline aplikasi dan perangkat target.

---

# 12. Alur kerja membuat aset

Ikuti alur yang sama untuk setiap aset penting.

## Langkah 1 — Tentukan fungsi

Jawab:
- Aset ini untuk apa?
- Di layar/adegan mana muncul?
- Apakah aset dekoratif atau membawa informasi?
- Apakah pemain perlu berinteraksi dengannya?
- Apakah sudah ada aset serupa?

## Langkah 2 — Buat spesifikasi

Catat:
- Asset ID.
- Nama file.
- Kategori.
- Deskripsi.
- Ukuran master.
- Rasio aspek.
- Format.
- Transparansi.
- Focal point.
- Safe area.
- Lokasi penempatan.
- Prioritas.
- Referensi dan lisensi.

## Langkah 3 — Siapkan reference

Reference harus menentukan:
- Bentuk/komposisi.
- Warna.
- Lighting.
- Material.
- Sudut kamera.
- Identitas karakter/objek.
- Elemen yang harus dipertahankan.

Untuk karakter, gunakan reference sheet yang disetujui agar wajah tidak berubah antar gambar.

## Langkah 4 — Produksi master

Buat file berkualitas tinggi dan simpan di source folder. Jangan langsung menghapus layer atau file kerja.

## Langkah 5 — Review art

Periksa:
- Komposisi.
- Konsistensi.
- Anatomi/proporsi bila karakter.
- Detail material.
- Keterbacaan.
- Crop.
- Artefak.
- Kesesuaian narasi.

## Langkah 6 — Export runtime

- Resize sesuai ukuran tampil.
- Pilih format.
- Kompres.
- Buat transparansi yang benar.
- Gunakan nama file runtime.
- Simpan master revision dan metadata.

## Langkah 7 — Integrasi

- Tempatkan di folder Android yang tepat.
- Panggil dari Compose/resource ID.
- Pastikan tidak menggandakan resource.
- Periksa crop, scaling, tint, dan layering.

## Langkah 8 — Uji perangkat

Uji pada layar kecil dan besar bila tersedia. Periksa memory, loading, dan tampilan saat berpindah layar.

## Langkah 9 — Tandai status

Ubah status menjadi TESTED/FINAL hanya setelah integrasi dan pemeriksaan benar-benar dilakukan.

---

# 13. Quality assurance

Setiap aset harus melewati checklist yang sesuai.

## 13.1 Pemeriksaan visual

- [ ] Komposisi sesuai art direction.
- [ ] Warna sesuai palette.
- [ ] Lighting konsisten.
- [ ] Tidak ada artefak/tepi rusak.
- [ ] Tidak ada elemen terpotong secara tidak sengaja.
- [ ] Transparansi benar.
- [ ] Detail tetap terbaca pada ukuran tampil.
- [ ] Tidak ada teks palsu/acak pada artwork yang dianggap dokumen.
- [ ] Tidak ada watermark yang tidak diizinkan.

## 13.2 Pemeriksaan teknis

- [ ] Dimensi sesuai spesifikasi.
- [ ] Format benar.
- [ ] Nama file valid untuk Android.
- [ ] Asset ID terdaftar.
- [ ] Ukuran file diperiksa.
- [ ] Master source disimpan.
- [ ] Lisensi/sumber dicatat.
- [ ] Tidak ada duplikasi yang tidak diperlukan.

## 13.3 Pemeriksaan integrasi

- [ ] Resource berhasil dibaca.
- [ ] Tidak ada resource ID yang salah.
- [ ] Crop dan contentScale benar.
- [ ] Tidak menutupi tombol/teks.
- [ ] Aman untuk berbagai rasio layar.
- [ ] Accessibility description sesuai.
- [ ] Tidak ada memory spike yang tidak wajar.
- [ ] Perpindahan layar tetap lancar.

## 13.4 Pemeriksaan narasi

- [ ] Karakter sesuai identitas yang ditetapkan.
- [ ] Lokasi sesuai adegan.
- [ ] Dokumen tidak bertentangan dengan cerita.
- [ ] Bukti punya provenance yang jelas.
- [ ] Fakta, hipotesis, disputed information, dan unknown tetap terpisah.
- [ ] Tidak ada detail cerita yang diciptakan oleh artwork.

---

# 14. Katalog campaign dari awal sampai akhir

Bagian ini memastikan seluruh perjalanan pemain punya cakupan aset. Detail masa depan tetap TBD sampai story/campaign design ditetapkan.

## 14.1 Game opening

Kebutuhan:
- Splash/logo.
- Main title artwork.
- Menu background.
- Menu emblem/icons.
- Intro music.
- UI navigation SFX.
- Loading treatment.

## 14.2 Recruitment

Kebutuhan:
- Recruitment invitation.
- Messenger contact/avatar.
- Freya portrait dan ekspresi yang diperlukan.
- A.E.G.I.S. emblem.
- Official message attachment.
- Recruitment scene artwork.
- Notification SFX.
- Dialogue transitions.

## 14.3 First Contact training

Kebutuhan:
- First Contact cover.
- Mentor portrait.
- Message fragment dan identity fragment sebagai file virtual.
- Attachment preview jika dibutuhkan.
- Browser identity/visual.
- File Repository icon dan file preview.
- Terminal identity.
- Code Editor identity dan status feedback.
- Training mission completion artwork.
- Training SFX.

## 14.4 RX-14 investigation

Kebutuhan:
- RX-14 case cover.
- Evidence record A/B (data dan presentasi).
- Timeline comparison.
- Evidence/source cards.
- Analysis diagram.
- Operations Center case visuals.
- Debrief artwork.
- Alert/notification sound yang sesuai.

Jangan membuat visual yang menyatakan penyebab atau pelaku RX-14 sebelum bukti cerita menetapkannya.

## 14.5 Campaign chapters mendatang

Untuk setiap chapter yang disetujui, buat paket berikut:

**Chapter package**
- Chapter key art.
- Chapter title treatment.
- Mission cover untuk setiap misi.
- Karakter baru dan portrait.
- Lokasi baru.
- Props khusus.
- Dokumen dan evidence.
- UI extension bila diperlukan.
- Audio ambience/SFX khusus.
- Animation/transition khusus bila diperlukan.
- Completion/debrief artwork.

Jumlah dan isi paket mengikuti narasi yang disetujui. Tidak semua chapter memerlukan aplikasi, UI, atau efek baru.

## 14.6 Final campaign dan ending

Setelah ending ditetapkan, inventarisasi:
- Final chapter key art.
- Final mission cover.
- Character state/portrait yang relevan.
- Final location artwork.
- Evidence dan document assets.
- Final operational presentation.
- Ending scene/artwork.
- Ending music.
- Credits background/typography.
- Completion/reward artwork.

Jangan mengunci desain ending sebelum cerita final disepakati.

---

# 15. Checklist aset dan status produksi

Gunakan tabel berikut sebagai template. Tambahkan baris per aset; jangan menandai aset selesai berdasarkan rencana saja.

| Asset ID | Nama/file | Priority | Status | Lokasi runtime | Catatan |
|---|---|---|---|---|---|
| `CP_BRAND_LOGO_PRIMARY` | `aegis/cyber_protocol_logo` | P0 | PLANNED | TBD | Perlu konsep final |
| `CP_BRAND_AEGIS_EMBLEM_PRIMARY` | `aegis_emblem` | P0 | PLANNED | TBD | Perlu versi terang/gelap |
| `CP_CHAR_FREYA_AVATAR` | `char_freya_avatar` | P0 | PLANNED | TBD | Periksa aset existing |
| `CP_CHAR_FREYA_PORTRAIT_NEUTRAL` | `char_freya_portrait_neutral` | P0 | PLANNED | TBD | Buat character reference |
| `CP_CHAR_NEXUS_AVATAR_DEFAULT` | `char_nexus_avatar_default` | P0 | PLANNED | TBD | Tentukan representasi visual |
| `CP_CAMPAIGN_FIRST_CONTACT_COVER` | `first_contact_cover` | P1 | PLANNED | TBD | Chapter artwork |
| `CP_CASE_RX14_COVER` | `case_rx14_cover` | P1 | PLANNED | TBD | Jangan menyiratkan penyebab |
| `CP_UI_TERMINAL_BACKGROUND` | `terminal_background` | P2 | PLANNED | TBD | Pastikan teks tetap Compose |

Status dan lokasi pada template ini harus diperbarui setelah pemeriksaan repository. Baris PLANNED bukan bukti bahwa aset belum ada; cek resource existing terlebih dahulu.

### Template entri aset

Salin untuk setiap aset baru:

```text
Asset ID:
Display name:
Category:
Description:
Story/feature usage:
Priority:
Production status:

Master filename:
Runtime filename:
Master dimensions:
Runtime dimensions:
Aspect ratio:
Format:
Transparency:
Focal point / safe area:

Runtime location:
Compose screen/component:
Implementation notes:

Reference/source:
License:
Revision:
Review notes:
```

---

# 16. Istilah penting

| Istilah | Arti |
|---|---|
| Asset ID | Identitas stabil untuk satu aset |
| Master/source | File asli berkualitas tinggi yang dapat diedit |
| Runtime export | File hasil ekspor untuk dipakai game |
| Resolution | Jumlah pixel gambar |
| Aspect ratio | Perbandingan lebar dan tinggi |
| Density | Hubungan pixel fisik dan dp Android |
| dp | Satuan layout Android |
| sp | Satuan ukuran teks Android |
| Alpha | Informasi transparansi |
| Focal point | Titik utama perhatian dalam komposisi |
| Safe area | Area yang aman dari crop/overlay UI |
| Crop | Memotong gambar agar memenuhi area |
| Vector drawable | Grafik vector yang digunakan sebagai Android resource |
| Sprite sheet | Satu gambar berisi beberapa frame animasi |
| Asset manifest | Daftar metadata seluruh aset |
| Decoded memory | Memori bitmap setelah gambar dibuka oleh aplikasi |
| Provenance | Asal-usul dan sumber suatu bukti |

---

## Prinsip akhir

Cyber Protocol harus terasa sebagai satu dunia yang utuh.

- Karakter harus konsisten dan meyakinkan.
- Lingkungan harus memiliki atmosfer dan identitas.
- Dokumen harus terasa autentik, tetapi informasi penting tetap akurat dan terbaca.
- Aplikasi harus terasa seperti perangkat kerja yang benar-benar digunakan.
- Efek dan audio harus memperkuat momen, bukan mengganggu.
- Semua aset harus memiliki tujuan, lokasi, dan cara implementasi yang jelas.

**Target kita adalah presentasi visual premium dengan kualitas setinggi mungkin yang tetap dapat berjalan stabil di Android.**

Dokumen ini adalah acuan produksi. Perbarui ketika keputusan art direction, story, struktur runtime, atau kebutuhan teknis berubah. Jangan biarkan katalog mengalahkan prioritas utama: menyelesaikan game yang dapat dimainkan dari awal sampai akhir.
