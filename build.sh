echo "Building debug APK using Adroid SDK located at: $1"
export ANDROID_HOME=$1
chmod u+x gradlew
gradlew assembleDebug
