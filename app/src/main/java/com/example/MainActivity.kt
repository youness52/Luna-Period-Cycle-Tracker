package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.LunaMainScreen
import com.example.ui.LunaViewModel
import com.example.ui.theme.LunaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LunaTheme {
                val viewModel: LunaViewModel = viewModel()
                LunaMainScreen(viewModel = viewModel)
            }
        }
    }
}
