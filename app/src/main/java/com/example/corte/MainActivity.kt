package com.example.corte

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.corte.databinding.ActivityMainBinding
import com.example.corte.data.AppDatabase
import com.example.corte.data.DeliveryEntry
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

        binding.btnSave.setOnClickListener { saveEntry() }
        binding.btnShare.setOnClickListener { shareWhatsapp() }
        binding.btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
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
        }
        Toast.makeText(this, getString(R.string.msg_guardado), Toast.LENGTH_SHORT).show()
        clearFields()
        updateDebt(efectivo, retiros)
    }

    private fun updateDebt(efectivo: Double, retiros: Double) {
        val deuda = efectivo - retiros
        binding.tvDeuda.text = getString(R.string.label_deuda_actual, deuda)
    }

    private fun clearFields() {
        binding.edtEfectivo.text.clear()
        binding.edtTpv.text.clear()
        binding.edtPropinas.text.clear()
        binding.edtRetiros.text.clear()
        binding.edtPedidos.text.clear()
        binding.tvDeuda.text = getString(R.string.label_deuda_actual_default)
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
}
