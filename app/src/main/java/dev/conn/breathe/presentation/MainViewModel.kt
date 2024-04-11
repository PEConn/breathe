package dev.conn.breathe.presentation

import android.app.Application
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class TimerState(val count: Int) {
    fun next() = TimerState(Math.floorMod(count - 1, 4))
}


// A little on the higher end of OK.
// val smallVibration = VibrationEffect.createOneShot(100, 180);
// Too much
// val bigVibration = VibrationEffect.createOneShot(400, 255);

val smallVibration = VibrationEffect.createOneShot(100, 150);
val bigVibration = VibrationEffect.createOneShot(200, 200);

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _timerState = MutableStateFlow(TimerState(0))
    val timerState: StateFlow<TimerState> = _timerState

    private val _runningState = MutableStateFlow(false)
    val runningState: StateFlow<Boolean> = _runningState

    private var runningTimer: Job? = null

    private val vibrator: Vibrator = application.getSystemService(Vibrator::class.java)

    private fun updateTimer(state: TimerState) {
        _timerState.value = state

        // TODO: Where should this live?
        if (state.count == 0) {
            // vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK))
            vibrator.vibrate(bigVibration)
        } else {
            // vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
            vibrator.vibrate(smallVibration)
        }
    }

    fun startTimer() {
        runningTimer?.cancel()

        _runningState.value = true

        runningTimer = viewModelScope.launch {
            updateTimer(TimerState(0))
            while (true) {
                delay(1000)
                updateTimer(timerState.value.next())
            }
        }
    }

    fun stopTimer() {
        runningTimer?.cancel()
        _runningState.value = false
    }
}