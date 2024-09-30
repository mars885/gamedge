# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Protobuf
-keep class * extends com.google.protobuf.GeneratedMessageLite { *; }

# Prevents issues like ths one: https://github.com/Kotlin/kotlinx.serialization/issues/2385
# Custom rule that keeps a class that meets the following criteria:
# - Is annotated by the @Serialization annotation
# - Lives inside a package or subpackages of com.paulrybitskyi.gamedge.feature
# - Has a name that ends with the "Route" word
-keep @kotlinx.serialization.Serializable class com.paulrybitskyi.gamedge.feature.**.*Route

# Prevents issues like ths one: https://github.com/square/retrofit/issues/3774
-keep,allowobfuscation,allowshrinking class com.github.michaelbull.result.Result

# Prevents issues where the @Apicalypse annotation is not being kept by ProGuard
# and, as a result, the API queries are genereted incorrectly
-keepclassmembers,allowobfuscation,allowshrinking class ** {
    @com.paulrybitskyi.gamedge.igdb.apicalypse.**.Apicalypse <fields>;
}
