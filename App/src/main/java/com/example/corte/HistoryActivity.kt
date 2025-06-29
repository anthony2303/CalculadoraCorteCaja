package com.example.corte

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.corte.data.AppDatabase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HistoryActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        db = AppDatabase.getDatabase(this)
        val tvHistory = findViewById<TextView>(R.id.tvHistory)
        lifecycleScope.launch {
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
            val entries = db.cutDao().getEntriesInRange(first.timeInMillis, last.timeInMillis)
            val sb = StringBuilder()
            entries.forEach {
                val dateStr = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(it.date))
                sb.append("$dateStr - Efectivo: ${it.efectivo}, TPV: ${it.tpv}, Propinas: ${it.propinas}, Retiros: ${it.retiros}, Pedidos: ${it.pedidos}\n")
            }
            runOnUiThread {
                tvHistory.text = if (sb.isNotEmpty()) sb.toString() else "No hay registros este mes."
            }
        }
    }
}