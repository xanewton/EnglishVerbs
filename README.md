English Verbs
=============

<a href='https://play.google.com/store/apps/details?id=com.xengar.android.englishverbs'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' height=90px/></a>

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


## License

Copyright 2017 Angel Newton

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.


