[![Build Status](https://travis-ci.org/WheresMyBus/android.svg?branch=master)](https://travis-ci.org/WheresMyBus/android)

Building and Testing on the UW CSE Linux VM
===========================================

Setting up the UW CSE Linux VM
------------------------------
1. Follow the instructions for installing  the VMWare player (Windows/Linux) or Fusion (OSX)at the url: `https://www.cs.washington.edu/lab/software/homeVMs/`
2. The VMWare player can be found at: `http://www.vmware.com/products/player/playerpro-evaluation.html`
3. Donwload and install the "Most Recent Linux VM" by following the instructions at the url: `https://www.cs.washington.edu/lab/software/homeVMs/linuxVM/`
4. Increase the available RAM to the UW CSE Linux VM image from 1 GB to at least 4 GB.

Installing the Android SDK in the VM
------------------------------------
1. In the VM, download the Android command line tools for Linux from the url: `https://developer.android.com/studio/index.html#downloads`
2. Unzip the tools at a location of your choice by copying the zipped files to the chosen parent directory and running the command: `tar xvf [ZIPPED_FILENAME]`, make note of the path to the unzipped directory called `android-sdk-linux`.
3. From the directory `android-sdk-linux/tools` run the command `android` to start up the Android SDK package manager. 
4. Select the following packages: From 'Tools' - Android SDK Tools, Android SDK Platform-tools, Android SDK Build-tools (for all revisions starting with 24); From 'Android 7.0 (API 24)' - SDK Platform, ARM EABIv7a System Image; From 'Extras' - Android Support Repository]
5. Click 'Install packages', accept all liscences and click 'Install', allow several minutes for the process to complete.

Building and Running the Android App
------------------------------------
1. Build the app by executing `build.sh [PATH_TO_ANDROID_SDK]`, for example: `build.sh /home/nbissiri/android-sdk-linux` the result is a file called `app-debug` in `app/build/outputs/apk`
2. Run the Android emulator by executing `run_emulator.sh [PATH_TO_ANDROID_SDK]`
3. Install the app onto the emulator by executing `install_apk.sh [PATH_TO_ANDROID_SDK]`

Running Tests
-------------
1. Run the Android emulator by executing `run_emulator.sh [PATH_TO_ANDROID_SDK]`
2. Run `run_tests.sh [PATH_TO_ANDROID_SDK]`, this will test building, connecting to the emulator, and will run our unit tests.

