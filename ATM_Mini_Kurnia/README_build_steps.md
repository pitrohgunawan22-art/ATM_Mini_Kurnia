# Cara Mendapatkan APK Final (Lengkap)
Dokumentasi langkah demi langkah untuk membangun APK debug atau release yang ditandatangani.

## Opsi 1 — GitHub Actions (otomatis, direkomendasikan untuk non-technical users)
1. Buat repository GitHub baru.
2. Push seluruh isi folder `ATM_Mini_Kurnia` ke repo tersebut.
3. Buka Settings > Secrets and variables > Actions > New repository secret, tambahkan (jika Anda ingin menghasilkan signed release):
   - `KEYSTORE_BASE64` : isi file keystore (.jks/.keystore) yang sudah di-encode base64. Cara generate: `base64 my-release-key.jks | pbcopy` (mac) atau `base64 -w 0 my-release-key.jks` (linux).
   - `KEYSTORE_PASSWORD` : password keystore.
   - `KEY_ALIAS` : alias key.
   - `KEY_PASSWORD` : password key (sering sama dengan keystore password).
4. Pergi ke tab Actions, jalankan workflow "Android Build and Sign (Release)" atau push ke branch `main` untuk memicu job.
5. Setelah job selesai, unduh artifact `signed-apk` dari run Actions — biasanya berisi `app-release-signed.apk` atau `app-debug.apk`.

## Opsi 2 — Build Lokal di PC (Android Studio)
Prasyarat: Android Studio terinstall (disarankan).

1. Ekstrak ZIP dan buka folder `ATM_Mini_Kurnia` di Android Studio.
2. Biarkan Gradle sync. Jika diminta update plugin/kotlin, ikuti panduan Android Studio.
3. Build > Build Bundle(s) / APK(s) > Build APK(s).
4. APK debug akan berada di `app/build/outputs/apk/debug/app-debug.apk`.
5. Untuk release signed, siapkan keystore dan ikuti langkah signing Android Studio (Build > Generate Signed Bundle / APK...).
6. Alternatif CLI (jika sudah memasang SDK & Gradle wrapper):
   - `chmod +x ./gradlew`
   - `./gradlew assembleDebug`
   - `./gradlew assembleRelease` (untuk release)

## Opsi 3 — Build di Android (Termux) — advanced
1. Install Termux, pasang paket openjdk, wget, unzip. (Termux pada Android modern tidak sempurna untuk full Android builds; ini untuk advanced users)
2. Pasang Android SDK command-line tools (rumit). Saya sarankan gunakan PC atau GitHub Actions.

## Cara men-generate KEYSTORE_BASE64 di Linux/Mac
- Linux:
  ```bash
  base64 -w 0 my-release-key.jks > my-release-key.jks.base64
  ```
- Mac:
  ```bash
  base64 my-release-key.jks > my-release-key.jks.base64
  ```

## Catatan keamanan
- Jangan commit keystore atau password secara publik. Gunakan GitHub Secrets.
- APK debug bukan untuk distribusi — gunakan signed release APK untuk publik.