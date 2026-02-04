package com.example.quakesafe.ui

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.AndroidViewModel
import com.example.quakesafe.service.ForegroundMeshService
import com.example.quakesafe.voice.VoicePTTService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MeshViewModel(application: Application) : AndroidViewModel(application) {
    private val _isMeshActive = MutableStateFlow(false)
    val isMeshActive: StateFlow<Boolean> = _isMeshActive

    private var voiceService: VoicePTTService? = null
    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording

    private val voiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            // In a real app, we'd use a Binder to get the service instance
            // For now, we'll assume the service is started
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            voiceService = null
        }
    }

    fun toggleMesh(active: Boolean) {
        val intent = Intent(getApplication(), ForegroundMeshService::class.java)
        if (active) {
            getApplication<Application>().startForegroundService(intent)
        } else {
            getApplication<Application>().stopService(intent)
        }
        _isMeshActive.value = active
    }

    fun startPTT() {
        _isRecording.value = true
        // Logic to start recording via VoicePTTService
    }

    fun stopPTT() {
        _isRecording.value = false
        // Logic to stop recording and send data
    }
}
