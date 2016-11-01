echo "Running tests with Android SDK at : $1"
export ANDROID_HOME=$1
chmod u+x gradlew
gradlew build connectedCheck
