package com.example.corte

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.corte.databinding.ActivityHistoryBinding
import com.example.corte.data.AppDatabase
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

        CoroutineScope(Dispatchers.IO).launch {
            val orders = db.orderDao().getAll()
            val withdrawals = db.withdrawalDao().getAll()

            val totalOrders = orders.size
            val totalCash = orders.filter { it.paymentType == "CASH" }.sumOf { it.amount }
            val totalCard = orders.filter { it.paymentType == "CARD" }.sumOf { it.amount }
            val totalTips = orders.sumOf { it.tip }
            val totalWithdrawals = withdrawals.sumOf { it.amount }
            val netCash = totalCash - totalWithdrawals

            withContext(Dispatchers.Main) {
                binding.tvOrders.text = getString(R.string.format_orders, totalOrders)
                binding.tvCash.text = getString(R.string.format_cash, totalCash)
                binding.tvCard.text = getString(R.string.format_card, totalCard)
                binding.tvTips.text = getString(R.string.format_tips, totalTips)
                binding.tvWithdrawals.text = getString(R.string.format_withdrawals, totalWithdrawals)
                binding.tvNetCash.text = getString(R.string.format_net_cash, netCash)
            }
        }
    }
}
