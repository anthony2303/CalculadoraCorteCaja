package com.example.corte

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.corte.data.AppDatabase
import com.example.corte.databinding.ActivityHistoryBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private val db by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Carga el historial de cortes del mes actual
        CoroutineScope(Dispatchers.IO).launch {
            val now = Calendar.getInstance()
            val start = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }
            val end = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH, now.getActualMaximum(Calendar.DAY_OF_MONTH))
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
            }

            val entries = db.cutDao().getEntriesInRange(start.timeInMillis, end.timeInMillis)
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val text = if (entries.isNotEmpty()) {
                entries.joinToString("\n") { e ->
                    "${sdf.format(Date(e.date))} - E:${"%.2f".format(e.efectivo)} " +
                    "TPV:${"%.2f".format(e.tpv)} P:${"%.2f".format(e.propinas)} " +
                    "R:${"%.2f".format(e.retiros)} Ped:${e.pedidos}"
                }
            } else {
                "No hay registros este mes."
            }

            runOnUiThread {
                binding.tvHistory.text = text
            }
        }
    }
}
