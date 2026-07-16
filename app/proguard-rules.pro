# ReadSphere ProGuard Rules

# Keep Room entities
-keep class com.readsphere.app.data.local.db.entity.** { *; }

# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

# Keep Kotlin serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.readsphere.app.**$$serializer { *; }
-keepclassmembers class com.readsphere.app.** {
    *** Companion;
}
-keepclasseswithmembers class com.readsphere.app.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep Apache POI
-keep class org.apache.poi.** { *; }
-keep class org.openxmlformats.** { *; }
-keep class org.apache.xmlbeans.** { *; }
-keep class com.microsoft.schemas.** { *; }
-dontwarn org.apache.poi.**
-dontwarn org.openxmlformats.**
-dontwarn org.apache.xmlbeans.**
-dontwarn com.microsoft.schemas.**

# Keep Pdfium
-keep class com.shockwave.pdfium.** { *; }
-dontwarn com.shockwave.pdfium.**

# ML Kit
-keep class com.google.mlkit.** { *; }
-dontwarn com.google.mlkit.**

# Coil
-dontwarn coil.**

# Jsoup
-dontwarn org.jsoup.**
