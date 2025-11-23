# Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Gson
-keep class com.example.todoapp.Todo { *; }
-keep class com.example.todoapp.CreateTodoRequest { *; }
-keep class com.example.todoapp.UpdateTodoRequest { *; }
