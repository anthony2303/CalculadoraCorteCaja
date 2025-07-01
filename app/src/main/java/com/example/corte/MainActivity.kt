package com.example.corte

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.corte.databinding.ActivityMainBinding
import com.example.corte.data.AppDatabase
import com.example.corte.data.OrderEntry
import com.example.corte.data.WithdrawalEntry
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
            val paymentType = if (binding.rbCash.isChecked) "CASH" else "CARD"
            val tip = if (paymentType == "CARD") binding.etTip.text.toString().toDoubleOrNull() ?: 0.0 else 0.0
            val now = System.currentTimeMillis()
            CoroutineScope(Dispatchers.IO).launch {
                db.orderDao().insert(
                    OrderEntry(amount = amount, paymentType = paymentType, tip = tip, timestamp = now)
                )
            }
            binding.etAmount.text.clear()
            binding.etTip.text.clear()
        }

        binding.btnWithdraw.setOnClickListener {
            val amount = binding.etWithdrawal.text.toString().toDoubleOrNull() ?: return@setOnClickListener
            val now = System.currentTimeMillis()
            CoroutineScope(Dispatchers.IO).launch {
                db.withdrawalDao().insert(WithdrawalEntry(amount = amount, timestamp = now))
            }
            binding.etWithdrawal.text.clear()
        }

        binding.btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
    }
}
