echo "Installing debug APK using Android SDK at: $1"
rm $1/platform-tools/app-debug.apk
cp app/build/outputs/apk/app-debug.apk $1/platform-tools
cd $1/platform-tools/
adb install app-debug.apk
