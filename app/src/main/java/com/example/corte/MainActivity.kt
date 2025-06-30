package com.example.corte

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
        try {
            // Inflate y vista
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Botones
            binding.btnSave.setOnClickListener { saveCorte() }
            binding.btnShare.setOnClickListener { shareWhatsapp() }
            binding.btnHistory.setOnClickListener {
                startActivity(Intent(this, HistoryActivity::class.java))
            }

            // Carga últimos totales
            loadLastTotals()

        } catch (e: Exception) {
            // Muestra el error en un Toast y registra en Logcat
            Toast.makeText(this, "Error al iniciar: ${e.message}", Toast.LENGTH_LONG).show()
            Log.e("MainActivity", "Error en onCreate", e)
            finish()
        }
    }

    private fun loadLastTotals() {
        CoroutineScope(Dispatchers.IO).launch {
            val now = Calendar.getInstance()
            val start = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH, 1); set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0)
            }
            val end = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH, now.getActualMaximum(Calendar.DAY_OF_MONTH))
                set(Calendar.HOUR_OF_DAY, 23); set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
            }
            val entries = db.cutDao().getEntriesInRange(start.timeInMillis, end.timeInMillis)
            if (entries.isNotEmpty()) {
                val last = entries.first()
                runOnUiThread { displayTotals(last) }
            }
        }
    }

    private fun displayTotals(entry: CutEntry) {
        binding.tvEfectivo.text = "Efectivo: $${"%.2f".format(entry.efectivo)}"
        binding.tvTpv.text = "TPV: $${"%.2f".format(entry.tpv)}"
        binding.tvPropinas.text = "Propinas: $${"%.2f".format(entry.propinas)}"
        binding.tvPagados.text = "Pagados: ${entry.pedidos} pedidos"
        binding.tvRetiros.text = "Retiros: $${"%.2f".format(entry.retiros)}"
        val deuda = entry.efectivo - entry.retiros - entry.propinas
        binding.tvDeuda.text = "Deuda actual: $${"%.2f".format(deuda)}"
    }

    private fun saveCorte() {
        val efectivo = binding.tvEfectivo.text.substringAfter("$").toDoubleOrNull() ?: 0.0
        val tpv = binding.tvTpv.text.substringAfter("$").toDoubleOrNull() ?: 0.0
        val propinas = binding.tvPropinas.text.substringAfter("$").toDoubleOrNull() ?: 0.0
        val retiros = binding.tvRetiros.text.substringAfter("$").toDoubleOrNull() ?: 0.0
        val pedidos = binding.tvPagados.text.substringBefore(" pedidos").toIntOrNull() ?: 0

        val entry = CutEntry(
            System.currentTimeMillis(),
            efectivo,
            tpv,
            propinas,
            retiros,
            pedidos
        )
        CoroutineScope(Dispatchers.IO).launch {
            db.cutDao().insertEntry(entry)
            runOnUiThread { displayTotals(entry) }
        }
    }

    private fun shareWhatsapp() {
        val msg = buildString {
            append("Corte de Caja\n")
            append("${binding.tvEfectivo.text}\n")
            append("${binding.tvTpv.text}\n")
            append("${binding.tvPropinas.text}\n")
            append("${binding.tvPagados.text}\n")
            append("${binding.tvRetiros.text}\n")
            append("${binding.tvDeuda.text}")
        }
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, msg)
            setPackage("com.whatsapp")
            startActivity(this)
        }
    }
}
