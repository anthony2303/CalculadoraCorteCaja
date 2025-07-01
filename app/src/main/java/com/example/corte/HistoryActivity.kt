package com.example.corte

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.corte.databinding.ActivityHistoryBinding
import com.example.corte.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private val db by lazy { AppDatabase.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadEntries()
    }

    private fun loadEntries() {
        CoroutineScope(Dispatchers.IO).launch {
            val now = Calendar.getInstance()
            val first = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }
            val last = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH, now.getActualMaximum(Calendar.DAY_OF_MONTH))
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
            }
            val entries = db.deliveryDao().getEntriesInRange(first.timeInMillis, last.timeInMillis)
            val deuda = db.deliveryDao().getDebtInRange(first.timeInMillis, last.timeInMillis)
            val df = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val historyText = entries.joinToString(separator = "\n") { e ->
                val deuda = e.efectivo - e.retiros
                "${df.format(Date(e.timestamp))} - Ef:${e.efectivo} TPV:${e.tpv} Prop:${e.propinas} Ret:${e.retiros} Ped:${e.pedidos} Deuda:$deuda"
            }
            withContext(Dispatchers.Main) {
                binding.tvHistory.text = if (historyText.isNotEmpty()) historyText else getString(R.string.msg_no_data)
                binding.tvDebt.text = getString(R.string.label_deuda_actual, deuda)
            }
        }
    }
}
