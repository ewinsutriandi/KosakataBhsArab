package com.alza.quiz.irfanvocab.util

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes
import com.alza.quiz.irfanvocab.R
import java.io.IOException

object SoundUtil {
    private var mediaPlayer: MediaPlayer? = null

    private fun playSound(context: Context, @RawRes soundResId: Int) {
        // Terminate any existing playback
        stopSound()

        try {
            mediaPlayer = MediaPlayer.create(context, soundResId).apply {
                setOnCompletionListener {
                    // Release MediaPlayer when playback is complete
                    release()
                    mediaPlayer = null
                }
                start()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun playCorrectSound(context: Context) {
        playSound(context, R.raw.game_sound_correct)
    }

    fun playWrongSound(context: Context) {
        playSound(context, R.raw.game_sound_wrong)
    }

    fun stopSound() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    // Call this when the app or activity is destroyed to clean up resources
    fun release() {
        stopSound()
    }
}