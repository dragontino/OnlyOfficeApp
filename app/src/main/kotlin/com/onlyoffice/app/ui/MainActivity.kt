package com.onlyoffice.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.onlyoffice.app.ui.navigation.NavigationScreen
import com.onlyoffice.app.ui.theme.OnlyOfficeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OnlyOfficeAppTheme {
                NavigationScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}