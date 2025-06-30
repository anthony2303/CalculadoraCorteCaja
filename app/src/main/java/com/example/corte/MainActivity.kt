package com.example.corte

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.corte.ui.theme.CorteCajaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CorteCajaTheme {
                // TODO: añade aquí tu UI para repartidor con estilo “soft restaurante”
            }
        }
    }
}
