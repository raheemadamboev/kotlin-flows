package xyz.teamgravity.kotlinflows

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class MainViewModel : ViewModel() {

    val countDownFlow = flow {
        val start = 10
        var current = start
        while (current > 0) {
            emit(current)
            delay(1_000L)
            current--
        }
        emit(current)
    }
}