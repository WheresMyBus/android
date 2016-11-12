echo "Building debug APK using Adroid SDK located at: $1"
sudo sed -i -e 's/http\:\/\/localhost\:5000\//http\:\/\/wheresmybus-api\.herokuapp\.com\//g' app/src/main/java/controllers/WMBController.java
sudo sed -i -e 's/http\:\/\/localhost\:5000\//http\:\/\/wheresmybus-api\.herokuapp\.com\//g' app/src/main/java/controllers/OBAController.java
export ANDROID_HOME=$1
chmod u+x gradlew
gradlew assembleDebug
