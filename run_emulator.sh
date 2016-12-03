echo "Running emulator with Android JDK located at: $1"
export ANDROID_HOME=$1
$1/tools/android delete avd -n wmb_test_device
$1/tools/android create avd -t android-21 -b google_apis/armeabi-v7a -n wmb_test_device
$1/tools/emulator @wmb_test_device
