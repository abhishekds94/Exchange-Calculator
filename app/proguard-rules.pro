# Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp3.** { *; }
-keep interface com.squareup.okhttp3.** { *; }
-dontwarn com.squareup.okhttp3.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
-dontwarn org.openjsse.**
-dontwarn sun.misc.**

-keep interface retrofit2.** { *; }
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Hilt
-keep class com.google.dagger.hilt.** { *; }
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel { <init>(...); }
-keep @dagger.hilt.android.scopes.* class * { *; }
-keep @dagger.hilt.android.qualifiers.* class * { *; }

# Serialization
-keep class kotlinx.serialization.** { *; }
-keep interface kotlinx.serialization.** { *; }
-keep class kotlin.serialization.** { *; }

# Room
-keep class androidx.room.** { *; }
-keep interface androidx.room.** { *; }
-keep @androidx.room.Entity class * { *; }
-keepclassmembers class * {
    @androidx.room.Insert <methods>;
    @androidx.room.Delete <methods>;
    @androidx.room.Update <methods>;
    @androidx.room.Query <methods>;
}

# Coil
-keep class coil.** { *; }
-keep interface coil.** { *; }

# Data models - Keep model classes used for serialization
-keep class com.exchangecalculator.app.data.model.** { *; }
-keep class com.exchangecalculator.app.domain.model.** { *; }

# API responses
-keepclassmembers class * {
    *** *;
}

# Keep all public classes and their public members
-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

# Keep enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep Parcelable implementations
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Keep Kotlin metadata
-keepclassmembers class ** {
    *** Companion;
}

-keep class kotlin.Metadata { *; }

# Preserve line numbers
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Optimization
-optimizationpasses 5
-dontobfuscate
-verbose

# Remove logging in release builds
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}