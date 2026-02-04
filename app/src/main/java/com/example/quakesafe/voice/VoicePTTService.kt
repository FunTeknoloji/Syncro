package com.example.quakesafe.voice

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.AudioTrack
import android.media.MediaRecorder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VoicePTTService : Service() {
    private var audioRecord: AudioRecord? = null
    private var audioTrack: AudioTrack? = null
    private var isRecording = false
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(1, createNotification())
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel("voice_ptt", "PTT Voice", NotificationManager.IMPORTANCE_LOW)
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, "voice_ptt")
            .setContentTitle("PTT Active")
            .setContentText("Listening for voice...")
            .setSmallIcon(android.R.drawable.ic_btn_speak_now)
            .build()
    }

    @SuppressLint("MissingPermission")
    fun startRecording(onDataReady: (ByteArray) -> Unit) {
        val bufferSize = AudioRecord.getMinBufferSize(16000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT)
        audioRecord = AudioRecord(MediaRecorder.AudioSource.MIC, 16000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize)

        isRecording = true
        audioRecord?.startRecording()

        scope.launch {
            val buffer = ByteArray(bufferSize)
            while (isRecording) {
                val read = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                if (read > 0) {
                    val compressed = OpusCodec.encode(buffer.copyOfRange(0, read))
                    onDataReady(compressed)
                }
            }
        }
    }

    fun stopRecording() {
        isRecording = false
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
    }

    fun playAudio(data: ByteArray) {
        if (audioTrack == null) {
            val bufferSize = AudioTrack.getMinBufferSize(16000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT)
            audioTrack = AudioTrack(
                AudioAttributes.Builder()
                    .setUsage(2) // USAGE_COMMUNICATION
                    .setContentType(1) // CONTENT_TYPE_SPEECH
                    .build(),
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(16000)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build(),
                bufferSize, AudioTrack.MODE_STREAM, 0
            )
            audioTrack?.play()
        }
        val decompressed = OpusCodec.decode(data)
        audioTrack?.write(decompressed, 0, decompressed.size)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
