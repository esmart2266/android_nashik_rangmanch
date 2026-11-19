# ------------------ Core Settings ------------------

# Preserve annotations and generic type info (CRITICAL for Retrofit generics, Kotlin)
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes InnerClasses
-keepattributes Exceptions
-keepattributes EnclosingMethod
-keepattributes SourceFile,LineNumberTable

# Suppress common library warnings that don't indicate real issues
-dontwarn org.bouncycastle.jsse.**
-dontwarn org.conscrypt.**
-dontwarn org.openjsse.**
-dontwarn com.facebook.android.BuildConfig
-dontwarn rx.** # For RxJava/RxAndroid
-dontwarn io.reactivex.** # For RxJava2
-dontwarn org.apache.http.** # Common warning for Android API < 23
-dontwarn com.google.android.gms.** # For Google Play Services, including Firebase
-dontwarn androidx.** # For AndroidX libraries

# Suppress warning for missing RTSP parser class in ExoPlayer (keep if you use ExoPlayer)
-dontwarn com.google.android.exoplayer2.source.rtsp.RtspMessageChannel$MessageParser$ReadingState
-dontwarn com.google.android.exoplayer2.ext.** # If using any ExoPlayer extensions

# Suppress note logging (optional, but good for release builds to reduce log spam)
-dontnote okhttp3.**, okio.**, retrofit2.**

# ------------------ Retrofit / OkHttp (CRITICAL FOR YOUR ERROR) ------------------

# Keep all Retrofit classes and their methods/fields
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }

# Keep Call & Callback interfaces required by Retrofit for generic types
-keep class retrofit2.Call
-keep interface retrofit2.Callback

# Keep OkHttp classes
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# Okio (OkHttp's dependency)
-keep class okio.** { *; }
-keep interface okio.** { *; }

# Keep javax.annotation, sometimes needed by Retrofit/Gson for annotations
-keep class javax.annotation.** { *; }
-dontwarn javax.annotation.**


# ------------------ Gson Configuration (for com.squareup.retrofit2:converter-gson) ------------------

# Keep Gson-specific classes
-keep class com.google.gson.Gson
-keep class com.google.gson.TypeAdapter
-keep class com.google.gson.reflect.TypeToken
-keep class com.google.gson.stream.** { *; }

# Keep classes used with @JsonAdapter, @Expose, etc.
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# ------------------ Kotlin (CRITICAL if using Kotlin for models/API) ------------------
# Required for Kotlin reflection/metadata
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }

# If using Kotlin Coroutines
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# ------------------ RxJava2 and Retrofit2-RxJava2-Adapter ------------------
-keep class io.reactivex.rxjava2.** { *; }
-keep class com.jakewharton.retrofit2.adapter.rxjava2.** { *; }
-keep class com.jakewharton.rxbinding2.** { *; }
-dontwarn io.reactivex.rxjava2.**
-dontwarn com.jakewharton.retrofit2.adapter.rxjava2.**
-dontwarn com.jakewharton.rxbinding2.**


# ------------------ App Specific Model Classes (ADJUST PACKAGE NAMES CAREFULLY) ------------------

# Keep all fields and methods of your data model classes for Gson/Retrofit deserialization
# MAKE SURE 'in.gov.pmc.psrs.model.**' IS YOUR ACTUAL MODELS PACKAGE
-keep class in.gov.nmc.rangmach.model.** { *; }
-keepclassmembers class in.gov.nmc.rangmach.model.** {
    <fields>;
    <methods>;
}

# Also keep inner classes of models if you have them, e.g., for nested responses
-keep class in.gov.nmc.rangmach.model.**$* { *; }

# Keep API service interfaces and their methods (CRITICAL for Retrofit to find them)
# MAKE SURE 'in.gov.pmc.psrs.api.**' IS YOUR ACTUAL API INTERFACE PACKAGE
-keep interface in.gov.nmc.rangmach.api.** { *; }
-keep class in.gov.nmc.rangmach.api.**$* { *; } # Keep inner classes if any in your API interfaces


# ------------------ Firebase (Messaging, Analytics, Crashlytics) ------------------
-keep class com.google.firebase.** { *; }
-keep interface com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# ------------------ Google Play Services (Auth, Location) ------------------
# Often covered by Firebase rules, but good to be explicit for specific services
-keep class com.google.android.gms.** { *; }
-keep interface com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

# ------------------ Glide (Image Loading Library) ------------------
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** { *; }
-keep class com.bumptech.glide.integration.okhttp3.OkHttpLibraryGlideModule # If using OkHttp integration (likely)

# ------------------ Other Libraries (General rules) ------------------

# MultiDex (if needed for older Android versions)
-keep class androidx.multidex.MultiDexApplication
-keep class androidx.multidex.MultiDex
-keep class androidx.multidex.MultiDexExtractor

# Stetho (Debugging, consider disabling in Release builds via build flavors)
-keep class com.facebook.stetho.** { *; }
-dontwarn com.facebook.stetho.**

# PhotoView
-keep class com.github.chrisbanes.photoview.** { *; }
-dontwarn com.github.chrisbanes.photoview.**

# Compressor (Image Compression)
-keep class id.zelory.compressor.** { *; }
-dontwarn id.zelory.compressor.**

# Material Date Range Picker
-keep class com.borax12.materialdaterangepicker.** { *; }
-dontwarn com.borax12.materialdaterangepicker.**

# Material DateTime Picker (wdullaer)
-keep class com.wdullaer.materialdatetimepicker.** { *; }
-dontwarn com.wdullaer.materialdatetimepicker.**

# Android GIF Drawable
-keep class pl.droidsonroids.gif.** { *; }
-dontwarn pl.droidsonroids.gif.**

# EasyImage
-keep class com.github.jkwiecien.easyimage.** { *; }
-dontwarn com.github.jkwiecien.easyimage.**

# uCrop
-keep class com.yalantis.ucrop.** { *; }
-dontwarn com.yalantis.ucrop.**


# Material EditText (rengwuxian)
-keep class com.rengwuxian.materialedittext.** { *; }
-dontwarn com.rengwuxian.materialedittext.**


# SmartLocation
-keep class io.nlopez.smartlocation.** { *; }
-dontwarn io.nlopez.smartlocation.**

# Jodd-Util
-keep class org.jodd.** { *; }
-dontwarn org.jodd.**

# ------------------ General Android Components ------------------

# Keep all custom views used in XML
-keep class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Keep Activity/Fragment lifecycle methods
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}
-keepclassmembers class * extends android.app.Fragment {
    public void *(android.view.View);
}
-keepclassmembers class * extends androidx.fragment.app.Fragment {
    public void *(android.view.View);
}

# Prevent stripping logging
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** i(...);
    public static *** v(...);
    public static *** w(...);
    public static *** e(...);
    public static *** wtf(...);
}