package com.readsphere.app.presentation.reader

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.core.app.NotificationCompat
import com.readsphere.app.R
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class TTSBackgroundService : Service(), TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    companion object {
        const val CHANNEL_ID = "tts_playback_channel"
        const val NOTIFICATION_ID = 1001
        const val ACTION_PLAY = "com.readsphere.action.TTS_PLAY"
        const val ACTION_PAUSE = "com.readsphere.action.TTS_PAUSE"
        const val ACTION_STOP = "com.readsphere.action.TTS_STOP"
        const val EXTRA_TEXT = "extra_text"
        const val EXTRA_SPEED = "extra_speed"
        const val EXTRA_PITCH = "extra_pitch"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        tts = TextToSpeech(this, this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY -> {
                val text = intent.getStringExtra(EXTRA_TEXT) ?: return START_NOT_STICKY
                val speed = intent.getFloatExtra(EXTRA_SPEED, 1.0f)
                val pitch = intent.getFloatExtra(EXTRA_PITCH, 1.0f)

                if (isInitialized) {
                    speak(text, speed, pitch)
                }
            }
            ACTION_PAUSE -> {
                if (tts?.isSpeaking == true) {
                    tts?.stop()
                }
            }
            ACTION_STOP -> {
                tts?.stop()
                tts?.shutdown()
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.getDefault()
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {}
                override fun onDone(utteranceId: String?) {}
                override fun onError(utteranceId: String?) {}
            })
            isInitialized = true
        }
    }

    private fun speak(text: String, speed: Float, pitch: Float) {
        tts?.let {
            it.setSpeechRate(speed)
            it.setPitch(pitch)

            val notification = createNotification()
            startForeground(NOTIFICATION_ID, notification)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                it.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts_utterance")
            } else {
                @Suppress("DEPRECATION")
                it.speak(text, TextToSpeech.QUEUE_FLUSH, null)
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Text-to-Speech Playback",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notification for TTS background playback"
                setShowBadge(false)
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("ReadSphere")
            .setContentText("Reading aloud…")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }
}
