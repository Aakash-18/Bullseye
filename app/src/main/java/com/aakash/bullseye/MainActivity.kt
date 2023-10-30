package com.aakash.bullseye

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.aakash.bullseye.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var sliderValue = 34
    private var targetValue = newTargetValue()
    private var totalScore = 0
    private var currentRound = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        startNewGame()


        binding.hitMeButton.setOnClickListener{
            showResult()
            totalScore = totalScore + pointsForCurrentRound()
            binding.gameScoreTextView?.text = totalScore.toString()
            binding.gameRoundTextView?.text = currentRound.toString()
        }

        binding.infoButton?.setOnClickListener {
            navigateToAboutPage()
        }

        binding.startOverButton?.setOnClickListener {
            startNewGame()
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                sliderValue = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekbar: SeekBar?) {

            }

        })

    }

    private fun navigateToAboutPage() {
        val intent =  Intent(this,AboutActivity::class.java)
        startActivity(intent)
    }

    private fun differenceAmount() = kotlin.math.abs(sliderValue - targetValue)

    private fun newTargetValue() = Random.nextInt(1,100)

    private fun pointsForCurrentRound(): Int {
        val maxScore = 100
        var bonus = 0
        val difference = differenceAmount()
        if (difference == 0){
            bonus = 100
        } else if (difference == 1){
            bonus = 50
        }

        return ((maxScore -  kotlin.math.abs(sliderValue - targetValue)) + bonus)
    }

    private fun startNewGame() {
        totalScore = 0
        currentRound = 1
        sliderValue = 50
        targetValue = newTargetValue()

        binding.gameScoreTextView?.text = totalScore.toString()
        binding.gameRoundTextView?.text = currentRound.toString()
        binding.targetTextView?.text = targetValue.toString()
        binding.seekBar.progress = sliderValue
    }

    private fun showResult() {
        val dialogTitle = alertTitle()
        val dialogMessage = getString(R.string.result_dialog_message,sliderValue,pointsForCurrentRound())

        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.setPositiveButton(R.string.result_dialog_button_text){
            dialog, _ -> dialog.dismiss()
            targetValue = Random.nextInt(1,100)
            binding.targetTextView?.text = targetValue.toString()

            currentRound += 1
            binding.gameRoundTextView?.text = currentRound.toString()
        }

        builder.create().show()
    }

    private fun alertTitle(): String {
        val difference = differenceAmount()
        val title: String = when {
            difference == 0 -> {
                getString(R.string.alert_title_1)
            }
            difference < 5 -> {
                getString(R.string.alert_title_2)
            }
            difference <= 10 -> {
                getString(R.string.alert_title_3)
            }
            else -> {
                getString(R.string.alert_title_4)
            }
        }
        return title
    }
}

