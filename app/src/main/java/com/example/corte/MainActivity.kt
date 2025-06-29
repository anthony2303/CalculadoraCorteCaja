package com.example.corte

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.corte.data.AppDatabase
import com.example.corte.data.CutEntry
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = AppDatabase.getDatabase(this)
        val edtEfectivo = findViewById<EditText>(R.id.edtEfectivo)
        val edtTpv = findViewById<EditText>(R.id.edtTpv)
        val edtPropinas = findViewById<EditText>(R.id.edtPropinas)
        val edtRetiros = findViewById<EditText>(R.id.edtRetiros)
        val edtPedidos = findViewById<EditText>(R.id.edtPedidos)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnShare = findViewById<Button>(R.id.btnShare)
        val btnHistory = findViewById<Button>(R.id.btnHistory)

        btnSave.setOnClickListener {
            val efectivo = edtEfectivo.text.toString().toDoubleOrNull() ?: 0.0
            val tpv = edtTpv.text.toString().toDoubleOrNull() ?: 0.0
            val propinas = edtPropinas.text.toString().toDoubleOrNull() ?: 0.0
            val retiros = edtRetiros.text.toString().toDoubleOrNull() ?: 0.0
            val pedidos = edtPedidos.text.toString().toIntOrNull() ?: 0

            val entry = CutEntry(
                date = System.currentTimeMillis(),
                efectivo = efectivo,
                tpv = tpv,
                propinas = propinas,
                retiros = retiros,
                pedidos = pedidos
            )
            lifecycleScope.launch {
                db.cutDao().insertEntry(entry)
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Corte guardado", Toast.LENGTH_SHORT).show()
                    edtEfectivo.text.clear()
                    edtTpv.text.clear()
                    edtPropinas.text.clear()
                    edtRetiros.text.clear()
                    edtPedidos.text.clear()
                }
            }
        }

        btnShare.setOnClickListener {
            val efectivo = edtEfectivo.text.toString().toDoubleOrNull() ?: 0.0
            val tpv = edtTpv.text.toString().toDoubleOrNull() ?: 0.0
            val propinas = edtPropinas.text.toString().toDoubleOrNull() ?: 0.0
            val retiros = edtRetiros.text.toString().toDoubleOrNull() ?: 0.0
            val pedidos = edtPedidos.text.toString().toIntOrNull() ?: 0

            val message = "He hecho un total de $$tpv en TPV, $$efectivo en efectivo, un total de $pedidos pedidos, gané $$propinas de propina en TPV y de retiros hice $$retiros"
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
                setPackage("com.whatsapp")
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "WhatsApp no está instalado", Toast.LENGTH_SHORT).show()
            }
        }

        btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
    }
}
