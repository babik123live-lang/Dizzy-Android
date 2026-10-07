plugins { id("com.android.application"); id("org.jetbrains.kotlin.android") }
android {
 namespace="com.dizzy.remake"; compileSdk=35
 defaultConfig { applicationId="com.dizzy.remake"; minSdk=23; targetSdk=35; versionCode=1; versionName="0.1.0" }
 sourceSets["main"].java.srcDir("../../remake/src")
 compileOptions {
  sourceCompatibility=JavaVersion.VERSION_17
  targetCompatibility=JavaVersion.VERSION_17
 }
 kotlinOptions { jvmTarget="17" }
}
