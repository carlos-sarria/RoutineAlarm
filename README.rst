=============
Routine Alarm
=============

.. figure:: ./screenshot.png

Routine Alarm is an application that allows you to set a sound signal at regular intervals to help with routine tasks. For example when sitting at the computer you might want to rest and walk every 30 minutes. You can program your alarm to emit a sound from its built-in library every 30 minutes starting from a specified time. This can be repeated each week day. To set a repeated alarm just use the "add" button at the bottom. It will create a default alarm that you can edit in place. Choose an "Interval" in minutes, the number of repetitions, the sound and the number of times the sound will repeat each time the alarm is triggered. Chose a starting time and which week days it will sound (all week is selected by default). You can set as many alarms as you wish. Alarms can overlap each other and will play on top of other sounds. You can delete them individually or all in one go from the top menu. 
It is recommended to keep the application running in the background as some vendors will disable the alarms as soon as the application is closed to save battery. Just open up the application and it will carry on as normal.

Building
--------
Just open the folder with Android Studio and you should be good to go. I use target API 31 and Jetpack Compose for Kotlin. This requires latest libraries from Google. 

These are the dependencies for Gradle (in gradle.build.kts)

.. code-block:: c

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.compose)
<<<<<<< Updated upstream
    api(libs.androidx.navigation.fragment.ktx)
=======
    api(libs.androidx.navigation.fragment.ktx)
>>>>>>> Stashed changes
