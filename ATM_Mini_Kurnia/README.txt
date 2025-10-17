ATM Mini Kurnia - Project skeleton (demo)
Contents: basic Android Studio project skeleton with:
 - MainActivity.kt (Kotlin)
 - ReceiptMaker.kt - generates sample receipts
 - logo_kurnia.png - sample logo

Important:
1) This is a project skeleton for demonstration. It does NOT include a ready-signed APK.
2) To build APK:
   - Open the ATM_Mini_Kurnia folder in Android Studio.
   - Let Gradle sync. If needed, update Gradle plugin and Kotlin versions.
   - Build > Build Bundle(s) / APK(s) > Build APK(s).
   - After build, install via USB adb: `adb install app/build/outputs/apk/debug/app-debug.apk`

3) For real printer integration:
   - Use a Bluetooth ESC/POS library or implement BluetoothSocket and write bytes (ESC/POS commands).
   - Test on a device with Bluetooth and pair printer first.
   - Add necessary runtime permission handling for Android 12+ (BLUETOOTH_CONNECT etc).

4) If you want, I can extend this project to add:
   - Real Bluetooth printer driver example (ESC/POS),
   - In-app e-wallet top-up integration (requires API credentials),
   - Generate and print PDF receipts,
   - Build an unsigned APK here (if build tools are available) - note: building may require Gradle and SDK.



=== Penambahan: Bluetooth ESC/POS Sample ===

Yang ditambahkan:
 - BluetoothPrinter.kt : class sederhana untuk koneksi RFCOMM (SPP) dan mengirim perintah ESC/POS.
 - MainActivity diperbarui: UI untuk memilih perangkat Bluetooth yang sudah dipasangkan, connect, dan tombol cetak.
 - Layout diperbarui: spinner daftar paired devices, tombol Refresh, Connect, Print, TopUp.

Cara uji cepat:
1) Pastikan printer thermal Bluetooth Anda sudah dipasangkan (paired) di Settings > Bluetooth.
2) Buka aplikasi (install APK yang dibangun) dan tekan Refresh, pilih perangkat, lalu Connect.
3) Jika terhubung, tekan 'Cetak Struk (Printer)' atau 'Top Up & Cetak'.
4) Jika cetak gagal, periksa dukungan encoding dan periksa log (adb logcat).

Catatan teknis:
 - Koneksi menggunakan UUID SPP 00001101-0000-1000-8000-00805F9B34FB (banyak printer thermal mendukung SPP).
 - Perintah potong kertas yang dikirim (GS V 0) mungkin tidak didukung di semua printer.
 - Untuk printer berbasis BLE (GATT) atau yang tidak menggunakan SPP, diperlukan driver berbeda.
 - Pastikan aplikasi meminta permission BLUETOOTH_CONNECT (Android 12+).


=== Fitur Lengkap yang Ditambahkan ===
- Pilihan encoding (UTF-8 / CP437) lewat Settings.
- Halaman TopUp dengan form nomor & nominal serta contoh share bukti.
- Kemampuan mencoba mencetak logo sebagai bitmap (PrinterUtils.bitmapToEscPos).
- Auto-reconnect sederhana: tombol Connect bisa dipanggil ulang; BluetoothPrinter memiliki disconnect().
- Contoh GitHub Actions workflow (.github/workflows/android.yml) untuk membangun APK secara otomatis saat Anda push ke repo GitHub (workflow akan menjalankan ./gradlew assembleDebug — Android SDK di runner akan di-setup oleh action).

=== Cara cepat mendapatkan APK siap install ===
Opsi A — Build lokal (direkomendasikan):
1) Ekstrak ZIP, buka folder `ATM_Mini_Kurnia` di Android Studio pada PC (Windows/Linux/Mac).
2) Biarkan Gradle sync; Android Studio akan mengunduh SDK/Gradle wrapper jika perlu.
3) Build > Build Bundle(s) / APK(s) > Build APK(s). Setelah selesai, Anda akan mendapat file DEBUG APK di `app/build/outputs/apk/debug/app-debug.apk`.
4) Transfer APK ke ponsel dan instal (aktifkan install dari sumber tak dikenal).

Opsi B — Gunakan GitHub + Actions (otomatis):
1) Buat repositori GitHub dan push seluruh folder project (termasuk .github/workflows/android.yml).
2) Buka tab Actions di repo dan jalankan workflow atau push ke branch main.
3) Setelah build selesai, unduh artifact `app-debug-apk` dari run Actions.

Catatan keamanan & legal:
- APK debug tidak signed untuk release. Untuk distribusi nyata, lakukan signing release yang benar.
- Integrasi e-wallet nyata memerlukan kredensial API resmi; contoh ini hanya simulasi.



=== FINAL PACKAGE ===
- build_and_install.sh : skrip bantu untuk build lokal dan optional sign.
- .github/workflows/android_sign_release.yml : workflow untuk membangun dan menandatangani APK di GitHub Actions (gunakan Secrets).
- README_build_steps.md : panduan lengkap.

Catatan: Saya tetap **tidak dapat membangun APK di sini** — gunakan salah satu opsi di README_build_steps.md.
