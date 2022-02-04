package xyz.teamgravity.kotlinflows

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import xyz.teamgravity.kotlinflows.ui.theme.KotlinFlowsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinFlowsTheme {
                val viewmodel = viewModel<MainViewModel>()
                val countdown = viewmodel.countDownFlow.collectAsState(initial = 10)

                val counter = viewmodel.counter.collectAsState() // don't use StateFlow with Compose, use State instead

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = countdown.value.toString(),
                        fontSize = 25.sp
                    )

                    Text(
                        text = counter.value.toString(),
                        fontSize = 22.sp
                    )

                    Button(
                        onClick = {
                            viewmodel.incrementCounter()
                        }
                    ) {
                        Text(text = "Increment by one")
                    }
                }
            }
        }
    }
}