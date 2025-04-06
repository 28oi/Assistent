package com.example.ts3

import MessageAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import java.io.IOException
import java.io.File
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION")
class TranscribeActivity : AppCompatActivity() {

    companion object {
        private const val PICK_FILE_REQUEST = 1
        private const val API_URL = "https://2kbd6lcn-8000.euw.devtunnels.ms/traslate" // Замените на фактический URL API
    }

    private lateinit var messagesRecyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private var messages: MutableList<Message> = mutableListOf()
    private lateinit var nextPageButton: Button
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transcribe)


        messagesRecyclerView = findViewById(R.id.messagesRecyclerView)
        messageAdapter = MessageAdapter(messages)
        messagesRecyclerView.layoutManager = LinearLayoutManager(this)
        messagesRecyclerView.adapter = messageAdapter


        val roundButton: Button = findViewById(R.id.round_button)
        roundButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "audio/*" // Ограничиваем выбор только аудиофайлами
            }
            startActivityForResult(intent, PICK_FILE_REQUEST)
        }


        nextPageButton = findViewById(R.id.next_page_button)
        nextPageButton.setOnClickListener {

            val intent = Intent(this, NextActivity::class.java)
            startActivity(intent)
        }
    }

    // Обработка выбора файла
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            val selectedFileUri: Uri? = data.data
            selectedFileUri?.let {
                // Показываем статус в чате
                addTranscriptionMessage("Файл выбран: ${it.lastPathSegment}")
                addTranscriptionMessage("Запущена транскрибация...")

                // Отправляем файл на сервер для транскрибации
                sendAudioFileForTranscription(it)
            }
        }
    }

    private fun addTranscriptionMessage(text: String) {
        val message = Message(text, "12:00")
        messages.add(message)
        messageAdapter.notifyItemInserted(messages.size - 1)
        messagesRecyclerView.scrollToPosition(messages.size - 1)
    }

    private fun sendAudioFileForTranscription(fileUri: Uri) {
        try {
            // Получаем содержимое файла
            val inputStream = contentResolver.openInputStream(fileUri)
            val file = File(cacheDir, "audio_file")
            file.outputStream().use { outputStream ->
                inputStream?.copyTo(outputStream)
            }
            
            // Подготовка запроса
            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                    "audio_file",
                    file.name,
                    RequestBody.create(MediaType.parse("audio/*"), file)
                )
                .build()

            val request = Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .build()

            // Выполнение запроса асинхронно
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        addTranscriptionMessage("Ошибка при отправке: ${e.message}")
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body()?.string()
                    runOnUiThread {
                        if (response.isSuccessful && responseBody != null) {
                            addTranscriptionMessage("Транскрипция завершена: \"$responseBody\"")
                            nextPageButton.visibility = View.VISIBLE
                        } else {
                            addTranscriptionMessage("Ошибка сервера: ${response.code()}")
                        }
                    }
                }
            })
        } catch (e: Exception) {
            addTranscriptionMessage("Ошибка обработки файла: ${e.message}")
        }
    }

    // Метод для симуляции остаётся для отладки
    private fun simulateTranscription() {
        messagesRecyclerView.postDelayed({
            addTranscriptionMessage("Транскрипция завершена: \"Пример распознанной речи из файла.\"")
            nextPageButton.visibility = View.VISIBLE
        }, 2000)
    }
}
