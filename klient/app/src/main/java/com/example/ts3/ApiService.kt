package com.example.ts3// app/src/main/java/com/example/tss/ApiService.kt

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("send-email")
    fun sendEmail(@Body emailRequest: EmailRequest): Call<Void>
    @POST("/login") // Укажи правильный эндпоинт, который у тебя на сервере
    fun login(@Body request: LoginRequest): Call<Void>
}
