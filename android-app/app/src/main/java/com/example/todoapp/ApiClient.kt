package com.example.todoapp

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

// Data classes
data class Todo(
    val id: String,
    val title: String,
    val completed: Boolean
)

data class CreateTodoRequest(val title: String)
data class UpdateTodoRequest(val completed: Boolean)

// API interface
interface TodoApi {
    @GET("api/todos")
    suspend fun getTodos(): List<Todo>

    @POST("api/todos")
    suspend fun createTodo(@Body request: CreateTodoRequest): Todo

    @PUT("api/todos/{id}")
    suspend fun updateTodo(@Path("id") id: String, @Body request: UpdateTodoRequest): Todo

    @DELETE("api/todos/{id}")
    suspend fun deleteTodo(@Path("id") id: String)
}

// API client singleton
object ApiClient {
    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: TodoApi = retrofit.create(TodoApi::class.java)
}
