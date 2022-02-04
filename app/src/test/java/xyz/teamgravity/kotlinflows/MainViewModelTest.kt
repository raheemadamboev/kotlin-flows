package xyz.teamgravity.kotlinflows

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    private lateinit var dispatchers: TestDispatchers
    private lateinit var viewmodel: MainViewModel

    @Before
    fun setUp() {
        dispatchers = TestDispatchers()
        viewmodel = MainViewModel(dispatchers)
    }

    @Test
    fun `countDown, properly counts down from 10 to 0`() = runBlocking {
        viewmodel.countDownFlow.test {
            for (i in 10 downTo 0) {
                dispatchers.dispatcher.advanceTimeBy(1_000L)
                val emission = awaitItem()
                assertThat(emission).isEqualTo(i)
            }
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `square number properly`() = runBlocking {
        val job = launch {
            viewmodel.sharedFlow.test {
                val emission = awaitItem()
                assertThat(emission).isEqualTo(9)
                cancelAndConsumeRemainingEvents()
            }
        }
        viewmodel.squareRoot(3)
        job.join()
        job.cancel()
    }
}