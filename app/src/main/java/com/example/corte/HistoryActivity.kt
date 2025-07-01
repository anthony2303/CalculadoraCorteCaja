package com.example.corte

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.corte.databinding.ActivityHistoryBinding
import com.example.corte.data.AppDatabase
import com.example.corte.data.DeliveryEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryActivity : AppCompatActivity() {
  private lateinit var binding: ActivityHistoryBinding
  private val db by lazy { AppDatabase.getInstance(this) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityHistoryBinding.inflate(layoutInflater)
    setContentView(binding.root)

    // Por defecto, muestro últimas 24h:
    val now = System.currentTimeMillis()
    val oneDayAgo = now - 24 * 60 * 60 * 1000

    CoroutineScope(Dispatchers.IO).launch {
      val entries: List<DeliveryEntry> = db.deliveryDao().getEntriesInRange(oneDayAgo, now)
      val displayList: List<String> = entries.map { "${it.amount} @ ${java.text.DateFormat.getDateTimeInstance().format(it.timestamp)}" }
      withContext(Dispatchers.Main) {
        if (displayList.isEmpty()) {
          binding.tvEmpty.visibility = android.view.View.VISIBLE
        } else {
          binding.tvEmpty.visibility = android.view.View.GONE
          binding.listView.adapter = ArrayAdapter(this@HistoryActivity, android.R.layout.simple_list_item_1, displayList)
        }
      }
    }
  }
}
