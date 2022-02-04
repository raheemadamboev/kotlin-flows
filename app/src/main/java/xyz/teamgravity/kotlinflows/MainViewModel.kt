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

    private val _counter = MutableStateFlow(0)
    val counter: StateFlow<Int> = _counter.asStateFlow()

    private val _sharedFlow = MutableSharedFlow<Int>(replay = 5)
    val sharedFlow: SharedFlow<Int> = _sharedFlow.asSharedFlow()

    init {
        //collectCountdown()
        //simpleFlowOperators()
        //launchInOperator()
        //terminalOperatorCount()
        //terminalOperatorReduce()
        //terminalOperatorFold()
        //foodCollectAwaits()
        observeSharedFlow()
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

    private fun foodCollectAwaits() {
        val food = flow<String> {
            emit("Apple")
            println("raheem: Apple emit")
            emit("Plow")
            println("raheem: Plow emit")
            emit("Juice")
            println("raheem: Juice emit")
        }

        viewModelScope.launch {
//            food.collect { food ->
//                println("raheem: eating $food")
//                delay(1_500L)
//                println("raheem: finished $food")
//            } // waits for collect to finish before emitting

//            food.collectLatest { food ->
//                println("raheem: eating $food")
//                delay(1_500L)
//                println("raheem: finished $food")
//            } // flow emits all without waiting

//            food.buffer()
//                .collect { food ->
//                    println("raheem: eating $food")
//                    delay(1_500L)
//                    println("raheem: finished $food")
//                } // create new coroutine for collect operator with buffer(), if emit is faster, it's result will suspend (waits for collect to finish)

            food.conflate()
                .collect { food ->
                    println("raheem: eating $food")
                    delay(1_500L)
                    println("raheem: finished $food")
                } // create new coroutine for collector with conflate(), but it only gets the last result (or should I say up-to-date?)
        }
    }

    fun incrementCounter() {
        _counter.value += 1
    }

    fun squareRoot(n: Int) {
        viewModelScope.launch {
            _sharedFlow.emit(n * n)
        }
    }

    private fun observeSharedFlow() {
        squareRoot(5)

        viewModelScope.launch {
            sharedFlow.collect { result ->
                delay(2_000L)
                println("raheem: $result")
            }
        }

        viewModelScope.launch {
            sharedFlow.collect { result ->
                delay(5_000L)
                println("raheem: $result")
            }
        }

        //squareRoot(5)
    }
}