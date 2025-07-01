package com.example.corte

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.corte.databinding.ActivityMainBinding
import com.example.corte.data.AppDatabase
import com.example.corte.data.CutEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
  private lateinit var binding: ActivityMainBinding
  private val db by lazy { AppDatabase.getInstance(this) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.btnSave.setOnClickListener {
      val amount = binding.etAmount.text.toString().toDoubleOrNull() ?: return@setOnClickListener
      val now = System.currentTimeMillis()
      CoroutineScope(Dispatchers.IO).launch {
        db.cutDao().insert(CutEntry(amount = amount, timestamp = now))
      }
    }

    binding.btnHistory.setOnClickListener {
      startActivity(Intent(this, HistoryActivity::class.java))
    }
  }
}
