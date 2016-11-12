echo "Building debug APK using Adroid SDK located at: $1"
sudo sed -i -e 's/http\:\/\/wheresmybus-api\.herokuapp\.com\//tcp\:\/\/0\.0\.0\.0\:5000/g' app/src/main/java/controllers/WMBController.java
export ANDROID_HOME=$1
chmod u+x gradlew
gradlew assembleDebug
