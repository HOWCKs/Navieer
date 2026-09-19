# Keep Servo embedding classes and JNI bindings
-keep class org.servo.servoview.** { *; }
-keepclassmembers class * {
    native <methods>;
}

# Keep Navieer classes
-keep class com.navieer.browser.** { *; }
