package io.github.turskyi.texttospeechconverter

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var mTts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initListeners()
    }

    private fun initListeners() {
        mTts = TextToSpeech(this, OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = mTts.setLanguage(Locale.UK)
                if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    Toast.makeText(this, "Language not supported", Toast.LENGTH_SHORT).show()
                } else {
                    fab.isEnabled = true
                }
            } else {
                Toast.makeText(this, "Initialization failed", Toast.LENGTH_SHORT).show()
            }
        })

        fab.setOnClickListener {
            if (etWord.text.isNotEmpty()) {
                speak()
            } else {
                Toast.makeText(this, "Enter the word", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initView() {
        fab.isEnabled = false
    }

    private fun speak() {
        val text = etWord.text.toString()
        var pitch = seekBarPitch.progress.toFloat() / 50
        if (pitch < 0.1) pitch = 0.1f
        var speed = seekBarSpeed.progress.toFloat() / 50
        if (speed < 0.1) speed = 0.1f
        mTts.setPitch(pitch)
        mTts.setSpeechRate(speed)
        mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onDestroy() {
        mTts.stop()
        mTts.shutdown()
        super.onDestroy()
    }
}