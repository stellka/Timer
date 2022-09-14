package edu.skillbox.timerforme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.skillbox.timerforme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.digitalClock.timerState
        binding.digitalClock.addUpdateListener(binding.analogClock::setCurrentTime)
    }
}