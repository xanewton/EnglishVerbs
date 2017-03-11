English Verbs
=============

![Scheme](/readmeImages/device-2017-03-11-172715.png)
![Scheme](/readmeImages/device-2017-03-11-172808.png)
![Scheme](/readmeImages/device-2017-03-11-172852.png)


Android application to learn english verb tenses.


Pre-requisites
--------------
- Android SDK 25 or Higher
- [Color Picker Module](http://www.materialdoc.com/color-picker/)


# Set up

Color Picker Module
-------------------

1.  Download repository from
  ```
  git clone https://android.googlesource.com/platform/frameworks/opt/colorpicker
  ```

2. Import a new module in android studio with the New/import module menu, choosing the path where the project was cloned.

3. Add dependency to app/build.gradle
```
apply plugin: 'com.android.application'

android {
    ...
}

dependencies {
    compile project(':colorpicker')
    ...
}

```

4. Add compileSdkVersion and buildToolsVersion in colorpicker/build.gradle to avoid Error buildToolsVersion is not specified
```
 apply plugin: 'com.android.library'

 android {

     compileSdkVersion 25
     buildToolsVersion "25.0.2"

     sourceSets.main {
         manifest.srcFile 'AndroidManifest.xml'
         java.srcDirs = ['src']
         res.srcDirs = ['res']
     }
 }
```
