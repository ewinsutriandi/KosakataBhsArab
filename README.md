# Belajar Kosakata Arab: Aplikasi Kuis Sederhana dengan Kotlin dan Jetpack Compose

## Deskripsi
Proyek ini adalah aplikasi Android sederhana yang dirancang sebagai demo untuk pemula yang ingin belajar pengembangan aplikasi Android menggunakan **Kotlin** dan **Jetpack Compose**. Aplikasi ini berupa kuis edukasi untuk mempelajari kosakata Arab, saat ini berfokus pada bilangan (numbers) dengan konversi antara angka biasa (Western digits), angka Arab, dan kata-kata Arab. Ke depannya, proyek ini akan diperluas untuk mencakup kosakata lain seperti **buah-buahan**, **anggota keluarga**, **hewan**, **bagian tubuh** dan lainnya. Proyek ini masih dalam tahap pengembangan (**work in progress**).

Tujuan utama proyek ini adalah menunjukkan cara membangun UI modern dengan **Jetpack Compose**, menerapkan arsitektur **MVVM**, **modern navigation**, **state management**, **simple data persistence**, dan **resource management** yang sesuai dengan praktik terbaik pengembangan Android modern. Aplikasi ini mencakup level kuis, umpan balik interaktif, statistik permainan, dan efek suara, dirancang agar mudah dipahami oleh pemula.

## Fitur Utama
- **Pemilihan Level**: Pilih dari 5 level dengan rentang bilangan (1-10 hingga 1001-10000).
- **Jenis Pertanyaan Kuis**:
    - Kata Arab ke angka Arab.
    - Angka Arab ke kata Arab.
    - Angka biasa ke angka Arab.
    - Angka Arab ke angka biasa.
- **Umpan Balik Interaktif**: Dialog untuk jawaban salah, dialog ringkasan skor, dan animasi transisi antar pertanyaan.
- **Statistik Permainan**: Simpan dan tampilkan jumlah permainan, jawaban benar/salah, dan akurasi (%) per level menggunakan **SharedPreferences** dengan format JSON.
- **Efek Suara**: Pemutaran suara untuk jawaban benar/salah menggunakan **MediaPlayer**.
- **Modern Navigation**: Navigasi antar layar (pemilihan level dan kuis) dengan **Jetpack Navigation**.

## Teknologi yang Digunakan
- **Kotlin**: Bahasa utama untuk seluruh kode.
- **Jetpack Compose**: Library untuk UI deklaratif, menggantikan XML layouts. 
- **Material Design 3**: Tema modern untuk UI yang lebih konsisten, 
- **MVVM Architecture**: Menggunakan **ViewModel** dan **StateFlow** untuk **state management** reaktif, memisahkan logika bisnis dari UI.
- **Modern Navigation**: **Jetpack Navigation Compose** untuk rute sederhana menggunakan **NavController**.
- **Simple Data Persistence**: **SharedPreferences** dengan **kotlinx.serialization** untuk menyimpan statistik dalam format JSON.
- **Resource Management**: **MediaPlayer** dikelola untuk mencegah kebocoran memori saat memutar efek suara (.ogg).
- **Coroutines**: Untuk operasi asinkron seperti delay dan peluncuran efek suara.

## Cara Menjalankan Proyek  
- Clone repository: `git clone https://github.com/ewinsutriandi/KosakataBhsArab`.
- Buka proyek di Android Studio.


## Kontribusi
Silakan fork dan buat pull request untuk perbaikan atau fitur baru. Proyek ini dirancang untuk belajar bersama, jadi masukan dari komunitas sangat dihargai!

## Lisensi
MIT License – Bebas digunakan dan dimodifikasi untuk tujuan edukasi.

## Other
Readme ini sebagian besar ditulis oleh Grok. Enak kan koding zaman now?