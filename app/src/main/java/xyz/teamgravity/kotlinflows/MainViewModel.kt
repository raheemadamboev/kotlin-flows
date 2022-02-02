package xyz.teamgravity.kotlinflows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

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

    init {
        collectCountdown()
    }

    private fun collectCountdown() {
        viewModelScope.launch {
            // damn they don't collect same flow, two different flow count down started as i delayed collecting this one
            // one for ui collecting as state, one for viewmodel itself, two different count down?
            delay(1_000)
            countDownFlow.collect { count ->
                println("raheem: $count")
            }
        }
    }
}