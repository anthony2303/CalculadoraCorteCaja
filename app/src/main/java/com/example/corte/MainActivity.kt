package com.example.corte

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar
import com.example.corte.databinding.ActivityMainBinding
import com.example.corte.data.AppDatabase
import com.example.corte.data.DeliveryEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val db by lazy { AppDatabase.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener { saveEntry() }
        binding.btnClear.setOnClickListener { clearFields() }
        binding.btnShare.setOnClickListener { shareWhatsapp() }
        binding.btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
        loadDebt()
    }

    private fun saveEntry() {
        val efectivo = binding.edtEfectivo.text.toString().toDoubleOrNull() ?: 0.0
        val tpv = binding.edtTpv.text.toString().toDoubleOrNull() ?: 0.0
        val propinas = binding.edtPropinas.text.toString().toDoubleOrNull() ?: 0.0
        val retiros = binding.edtRetiros.text.toString().toDoubleOrNull() ?: 0.0
        val pedidos = binding.edtPedidos.text.toString().toIntOrNull() ?: 0

        val entry = DeliveryEntry(
            timestamp = System.currentTimeMillis(),
            efectivo = efectivo,
            tpv = tpv,
            propinas = propinas,
            retiros = retiros,
            pedidos = pedidos
        )
        CoroutineScope(Dispatchers.IO).launch {
            db.deliveryDao().insert(entry)
            loadDebtFromDb()
        }
        Toast.makeText(this, getString(R.string.msg_guardado), Toast.LENGTH_SHORT).show()
        clearFields()
    }

    private fun loadDebt() {
        CoroutineScope(Dispatchers.IO).launch {
            loadDebtFromDb()
        }
    }

    private suspend fun loadDebtFromDb() {
        val (first, last) = currentMonthRange()
        val deuda = db.deliveryDao().getDebtInRange(first, last)
        withContext(Dispatchers.Main) {
            binding.tvDeuda.text = getString(R.string.label_deuda_actual, deuda)
        }
    }

    private fun clearFields() {
        binding.edtEfectivo.text.clear()
        binding.edtTpv.text.clear()
        binding.edtPropinas.text.clear()
        binding.edtRetiros.text.clear()
        binding.edtPedidos.text.clear()
    }

    private fun shareWhatsapp() {
        val efectivo = binding.edtEfectivo.text.toString().toDoubleOrNull() ?: 0.0
        val tpv = binding.edtTpv.text.toString().toDoubleOrNull() ?: 0.0
        val propinas = binding.edtPropinas.text.toString().toDoubleOrNull() ?: 0.0
        val retiros = binding.edtRetiros.text.toString().toDoubleOrNull() ?: 0.0
        val pedidos = binding.edtPedidos.text.toString().toIntOrNull() ?: 0
        val deuda = efectivo - retiros

        val message = getString(
            R.string.msg_resumen,
            efectivo,
            tpv,
            propinas,
            retiros,
            pedidos,
            deuda
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
            setPackage("com.whatsapp")
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, R.string.msg_no_whatsapp, Toast.LENGTH_SHORT).show()
        }
    }

    private fun currentMonthRange(): Pair<Long, Long> {
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
        return first.timeInMillis to last.timeInMillis
    }
}
