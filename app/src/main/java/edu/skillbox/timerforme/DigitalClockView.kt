package edu.skillbox.timerforme

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.coroutines.*
import java.text.SimpleDateFormat

class DigitalClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val start: Button
    private val stop: Button
    private val reset: Button
    private val textView: TextView
    private var counterListeners = mutableSetOf<(TimeState) -> Unit>()

    private val dateFormat = SimpleDateFormat("hh:mm:ss")
    private val myScope = CoroutineScope(Dispatchers.Default + Job())
    private var process: Job? = null

    var timerState = TimeState(0, false)
        set(value) {
            if (value == field)
                return
            field = value
            textView.text =
                "${(value.time / 60 / 60) % 24} : ${(value.time / 60) % 60} : ${value.time % 60}"
            counterListeners.forEach { it(value) }
        }


    init {
        val root = inflate(context, R.layout.view_combine, this)
        start = root.findViewById(R.id.start)
        stop = root.findViewById(R.id.stop)
        reset = root.findViewById(R.id.reset)
        textView = root.findViewById(R.id.text_view)

        start.setOnClickListener {
            if (!timerState.isPlayed) {
                stop.visibility = Button.VISIBLE
                start.visibility = Button.INVISIBLE
                startTimer()
                textView.text =
                    "${(timerState.time / 60 / 60) % 24} : ${(timerState.time / 60) % 60} : ${timerState.time % 60}"
            } else {
                start.visibility = Button.VISIBLE
                stop.visibility = Button.INVISIBLE
            }

            timerState.isPlayed = !timerState.isPlayed

            stop.setOnClickListener {
                timerState.isPlayed = false
                start.visibility = Button.VISIBLE
                stop.visibility = Button.INVISIBLE
                process?.cancel()
            }

            reset.setOnClickListener {
                timerState.isPlayed = false
                process?.cancel()
                timerState.time = 0
                textView.text =
                    "${(timerState.time / 60 / 60) % 24} : ${(timerState.time / 60) % 60} : ${timerState.time % 60}"
                start.visibility = Button.VISIBLE
                stop.visibility = Button.INVISIBLE
            }
        }
        stop.setOnClickListener {
            timerState.isPlayed = false
            start.visibility = Button.VISIBLE
            stop.visibility = Button.INVISIBLE
            process?.cancel()
        }
        reset.setOnClickListener {
            timerState.isPlayed = false
            start.visibility = Button.VISIBLE
            stop.visibility = Button.INVISIBLE
            process?.cancel()
            timerState.time = 0
            textView.text =
                "${(timerState.time / 60 / 60) % 24} : ${(timerState.time / 60) % 60} : ${timerState.time % 60}"

        }
    }

    private fun startTimer() {
        process = myScope.launch(Dispatchers.Main) {
            while (true) {
                timerState.time++
                timerState.isPlayed = true
                textView.text =
                    "${(timerState.time / 60 / 60) % 24} : ${(timerState.time / 60) % 60} : ${timerState.time % 60}"
                delay(1000)
            }
        }
    }

    fun addUpdateListener(listener: (TimeState) -> Unit) {
        counterListeners.add(listener)
        listener(timerState)
    }

    fun removeUpdateListener(listener: (TimeState) -> Unit) = counterListeners.remove(listener)

}