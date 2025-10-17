#!/usr/bin/env bash
set -e
echo "Build & (optionally) sign APK script for ATM_Mini_Kurnia"
ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT_DIR"

if [ ! -f gradlew ]; then
  echo "### Warning: gradlew not present. You may need to generate the Gradle wrapper or use Android Studio."
  echo "Try: gradle wrapper or open in Android Studio."
fi

chmod +x ./gradlew || true

# Optional: if KEystore base64 present, decode to release.keystore
if [ -n "$KEYSTORE_BASE64" ]; then
  echo "Decoding KEYSTORE_BASE64 to release.keystore"
  echo "$KEYSTORE_BASE64" | base64 --decode > release.keystore
fi

echo "Starting assembleDebug..."
./gradlew clean assembleDebug --no-daemon

APK_PATH=$(find app/build/outputs -name "app-debug.apk" | head -n 1)
if [ -z "$APK_PATH" ]; then
  echo "Debug APK not found."
else
  mkdir -p dist
  cp "$APK_PATH" dist/
  echo "Debug APK copied to dist/$(basename "$APK_PATH")"
fi

# If user provided keystore variables, attempt assembleRelease and sign
if [ -n "$KEYSTORE_BASE64" ] && [ -n "$KEYSTORE_PASSWORD" ] && [ -n "$KEY_ALIAS" ] && [ -n "$KEY_PASSWORD" ]; then
  echo "Building release and attempting signing..."
  ./gradlew assembleRelease --no-daemon || true
  unsigned=$(find app/build/outputs -name "*-release-unsigned.apk" | head -n 1)
  if [ -z "$unsigned" ]; then
    unsigned=$(find app/build/outputs -name "*release.apk" | head -n 1)
  fi
  if [ -n "$unsigned" ]; then
    jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore release.keystore -storepass "$KEYSTORE_PASSWORD" -keypass "$KEY_PASSWORD" "$unsigned" "$KEY_ALIAS"
    mkdir -p dist
    cp "$unsigned" dist/app-release-signed.apk
    echo "Signed APK placed in dist/app-release-signed.apk"
  else
    echo "No release APK found to sign."
  fi
fi

echo "Done."
