package com.levi.jokesmachine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.levi.jokesmachine.ui.theme.JokesForAllTheme
import com.levi.jokesmachine.ui.views.JokesScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JokesForAllTheme {
                JokesScreen()
            }
        }
    }
}
