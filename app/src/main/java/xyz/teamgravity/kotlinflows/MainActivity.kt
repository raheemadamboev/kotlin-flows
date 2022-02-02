package xyz.teamgravity.kotlinflows

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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

                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = countdown.value.toString(),
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 25.sp
                    )
                }
            }
        }
    }
}