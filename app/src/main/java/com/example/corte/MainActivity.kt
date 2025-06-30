package com.example.corte

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.corte.data.AppDatabase
import com.example.corte.data.CutEntry
import com.example.corte.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val db by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener { saveCorte() }
        binding.btnShare.setOnClickListener { shareWhatsapp() }
        binding.btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        loadLastTotals()
    }

    private fun loadLastTotals() {
        CoroutineScope(Dispatchers.IO).launch {
            // … carga y muestra la última entrada
        }
    }

    private fun saveCorte() {
        // … recoge valores, inserta en Room y actualiza UI
    }

    private fun shareWhatsapp() {
        // … construye mensaje y lanza Intent a WhatsApp
    }
}
