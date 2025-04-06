package com.example.ts3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NextActivity : AppCompatActivity() {

    private lateinit var pointsRecyclerView: RecyclerView
    private lateinit var pointsAdapter: PointsAdapter
    private var points: List<String> = listOf()  // Здесь будут храниться данные, полученные от бэкенда

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next)

        // Инициализация RecyclerView
        pointsRecyclerView = findViewById(R.id.pointsRecyclerView)
        pointsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Симуляция получения данных от бэкенда
        getDataFromBackend()

        // Инициализация адаптера
        pointsAdapter = PointsAdapter(points)
        pointsRecyclerView.adapter = pointsAdapter
    }

    private fun getDataFromBackend() {
        // Симулируем данные от бэкенда
        points = listOf(
            "Первый пункт: описание того, что произошло.",
            "Второй пункт: еще одно описание.",
            "Третий пункт: дальнейшие детали.",
            "Четвертый пункт: завершение процесса."
        )

        // Обновляем адаптер с новыми данными
        pointsAdapter.notifyDataSetChanged()
    }
}
