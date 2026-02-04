package com.funteknoloji.quakesafe.service

import android.content.Context
import android.hardware.camera2.CameraManager
import android.media.MediaPlayer
import com.funteknoloji.quakesafe.R
import kotlinx.coroutines.*

class HardwareManager(private val context: Context) {
    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private var cameraId: String? = null
    private var strobeJob: Job? = null
    private var mediaPlayer: MediaPlayer? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    init {
        try {
            cameraId = cameraManager.cameraIdList.firstOrNull()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setFlashlight(enabled: Boolean) {
        strobeJob?.cancel()
        try {
            cameraId?.let { cameraManager.setTorchMode(it, enabled) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun startStrobe(intervalMs: Long = 100) {
        strobeJob?.cancel()
        strobeJob = scope.launch {
            var state = false
            while (isActive) {
                state = !state
                cameraId?.let { cameraManager.setTorchMode(it, state) }
                delay(intervalMs)
            }
        }
    }

    fun stopStrobe() {
        strobeJob?.cancel()
        setFlashlight(false)
    }

    fun playSiren(resourceId: Int) {
        stopSiren()
        mediaPlayer = MediaPlayer.create(context, resourceId).apply {
            isLooping = true
            start()
        }
    }

    fun stopSiren() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
