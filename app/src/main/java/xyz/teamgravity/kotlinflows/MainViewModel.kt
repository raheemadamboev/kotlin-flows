package xyz.teamgravity.kotlinflows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
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
        //collectCountdown()
        //simpleFlowOperators()
        //launchInOperator()
        //terminalOperatorCount()
        //terminalOperatorReduce()
        terminalOperatorFold()
    }

    private fun collectCountdown() {
        viewModelScope.launch {
            // damn they don't collect same flow, two different flow count down started as i delayed collecting this one
            // one for ui collecting as state, one for viewmodel itself, two different count down?
            delay(2_000)
            countDownFlow.collect { count ->
                println("raheem: $count")
            }
        }
    }

    private fun simpleFlowOperators() {
        viewModelScope.launch {
            countDownFlow.filter { time ->
                time % 2 == 0
            }.map { time ->
                time * time
            }.onEach { time ->
                println("raheem: $time onEach")
            }.collect { time ->
                println("raheem: $time collect")
            }
        }
    }

    private fun launchInOperator() {
        countDownFlow.onEach { time ->
            println("raheem: $time")
        }.launchIn(viewModelScope)
    }

    private fun terminalOperatorCount() {
        viewModelScope.launch {
            val evenNumberCount = countDownFlow.onEach { time ->
                println("raheem: $time")
            }.count { time ->
                time % 2 == 0
            }

            println("raheem: even number count: $evenNumberCount")
        }
    }

    private fun terminalOperatorReduce() {
        viewModelScope.launch {
            val result = countDownFlow.onEach { time ->
                println("raheem: $time")
            }.reduce { accumulator, value ->
                accumulator + value
            }

            println("raheem: all added: $result")
        }
    }

    private fun terminalOperatorFold() {
        viewModelScope.launch {
            val result = countDownFlow.onEach { time ->
                println("raheem: $time")
            }.fold(100) { accumulator, value ->
                accumulator + value
            }

            println("raheem: all added initially 100: $result")
        }
    }
}