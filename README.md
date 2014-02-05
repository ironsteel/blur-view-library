# Blur view library

[![Build Status](https://travis-ci.org/masconsult/blur-view-library.png?branch=master)](https://travis-ci.org/masconsult/blur-view-library)

FrameLayout view which blurs the parent view behind it using render script blur intrinsic from render script support library.

This library is a work in progress mainly becouse of performance issues when the parent view is not static (ie ScrollView, ListView).
Which means that its recomended for use with less dynamic layout.

## Requirements
+ ADT version >= 22.2
+ SDK Tools version >= 22.2
+ Android Build Tools version >= 18.1.0

## Sample

+ Sample app included in blur-view-sample

## Usage

+ Clone
+ Import blur-view-library in your IDE 
+ Reference the library from your application project (e.g. project.properties)
+ Add android:hardwareAccelerated="false" in your activity declaration in AndroidManifest.xml
+ Declare custom namespace in your xml layout

Example:
```xml
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:blurView="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">    
```


+ Example usage in layout

```xml
    <!-- Views who add an image backround goes here -->

    <eu.masconsult.blurview.library.FrameLayoutWithBluredBackground
        android:layout_width="match_parent"
        android:layout_height="80dp"
        android:layout_gravity="bottom"
        blurView:blurRadius="5" 
        >

        <!-- Child views here -->
    </eu.masconsult.blurview.library.FrameLayoutWithBluredBackground>
```
**Default blur radius is 15 if omitted in xml layout.**

+ Get/Set blur radius from code

```java
    view.setBlurRadius(val);
    view.getBlurRaduis();
```

## Alternative libraries

- BlurEffectForAndroidDesign - <https://github.com/PomepuyN/BlurEffectForAndroidDesign>
- android\_annblur - <https://github.com/harism/android_anndblur>

## Credits

- Background image is from Nicolas Pomepuy's Blur Effect for Android Design -project<br>
  https://github.com/PomepuyN/BlurEffectForAndroidDesign
