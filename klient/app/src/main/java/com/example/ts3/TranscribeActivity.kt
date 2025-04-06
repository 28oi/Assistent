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

@Suppress("DEPRECATION")
class TranscribeActivity : AppCompatActivity() {

    companion object {
        private const val PICK_FILE_REQUEST = 1
    }

    private lateinit var messagesRecyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private var messages: MutableList<Message> = mutableListOf()
    private lateinit var nextPageButton: Button

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
                type = "*/*"
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

                // Запускаем фейковую транскрибацию (замени на реальный API вызов)
                simulateTranscription()
            }
        }
    }

    private fun addTranscriptionMessage(text: String) {
        val message = Message(text, "12:00")
        messages.add(message)
        messageAdapter.notifyItemInserted(messages.size - 1)
        messagesRecyclerView.scrollToPosition(messages.size - 1)
    }

    private fun simulateTranscription() {
        messagesRecyclerView.postDelayed({
            addTranscriptionMessage("Транскрипция завершена: \"Пример распознанной речи из файла.\"")
            nextPageButton.visibility = View.VISIBLE
        }, 2000)
    }
}
