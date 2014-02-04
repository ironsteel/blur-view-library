# Blur view library

FrameLayout view which blurs the parent view behind it using render script blur intrinsic from render script support library.

This library is a work in progress mainly becouse of performance issues when the parent view is not static (ie ScrollView, ListView).
Which means that its recomended for use with less dynamic layout.

## Requirements
+ ADT version >= 22.2
+ SDK Tools version >= 22.2
+ Android Build Tools version >= 18.1.0


## Usage

+ Clone
+ Import blur-view-library in your IDE 
+ Reference the library from your application project (e.g. project.properties)
+ Delcare custom namespace in your xml layout

```xml
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:blurView="http://schemas.android.com/apk/res/{yout_application_package_goes_here}" <!-- Add this line -->
    android:layout_width="match_parent"
    android:layout_height="match_parent">    
```

+ Example usage in layout

```xml
    <!-- Views who add an image baground goes here -->

    <eu.masconsult.blurview.library.FrameLayoutWithBluredBackground
        android:layout_width="match_parent"
        android:layout_height="80dp"
        android:layout_gravity="bottom"
        blurView:blurRadius="5" <!-- This can be omitted, dafault value is 15 -->
        >

        <!-- Child views here -->
    </eu.masconsult.blurview.library.FrameLayoutWithBluredBackground>
```


