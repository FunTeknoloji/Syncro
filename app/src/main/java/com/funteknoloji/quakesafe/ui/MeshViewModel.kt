package com.funteknoloji.quakesafe.ui

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.AndroidViewModel
import com.funteknoloji.quakesafe.R
import android.telephony.SmsManager
import com.funteknoloji.quakesafe.data.AppDatabase
import com.funteknoloji.quakesafe.data.entities.ContactEntity
import com.funteknoloji.quakesafe.data.entities.MessageEntity
import com.funteknoloji.quakesafe.mesh.MeshManager
import com.funteknoloji.quakesafe.service.ForegroundMeshService
import androidx.lifecycle.viewModelScope
import com.funteknoloji.quakesafe.service.HardwareManager
import com.funteknoloji.quakesafe.voice.VoicePTTService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MeshViewModel(application: Application) : AndroidViewModel(application) {
    private val hardwareManager = HardwareManager(application)
    private val meshManager = MeshManager(application)
    private val db = AppDatabase.getDatabase(application)

    private val _isMeshActive = MutableStateFlow(false)
    val isMeshActive: StateFlow<Boolean> = _isMeshActive

    private var voiceService: VoicePTTService? = null
    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording

    private val voiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as VoicePTTService.LocalBinder
            voiceService = binder.getService()
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            voiceService = null
        }
    }

    init {
        getApplication<Application>().bindService(
            Intent(getApplication(), VoicePTTService::class.java),
            voiceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onCleared() {
        getApplication<Application>().unbindService(voiceConnection)
        super.onCleared()
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
        voiceService?.startRecording { data ->
            meshManager.sendData(data) // Send voice packet over mesh
        }
    }

    fun stopPTT() {
        _isRecording.value = false
        voiceService?.stopRecording()
    }

    // Hardware Tools
    private val _isSirenOn = MutableStateFlow(false)
    val isSirenOn: StateFlow<Boolean> = _isSirenOn

    private val _isFlashlightOn = MutableStateFlow(false)
    val isFlashlightOn: StateFlow<Boolean> = _isFlashlightOn

    // Settings
    private val _fontSize = MutableStateFlow(16f)
    val fontSize: StateFlow<Float> = _fontSize

    private val _isHotspotRelayEnabled = MutableStateFlow(false)
    val isHotspotRelayEnabled: StateFlow<Boolean> = _isHotspotRelayEnabled

    fun setFontSize(size: Float) { _fontSize.value = size }
    fun toggleHotspotRelay(enabled: Boolean) { _isHotspotRelayEnabled.value = enabled }

    // Contacts
    val contacts = db.contactDao().getAllContacts()
    val emergencyContacts = db.contactDao().getEmergencyContacts()

    fun addContact(name: String, phone: String, isEmergency: Boolean) {
        viewModelScope.launch {
            db.contactDao().insertContact(ContactEntity(phone, name, isEmergency))
        }
    }

    fun sendEmergencySms(message: String) {
        viewModelScope.launch {
            try {
                val smsManager = getApplication<Application>().getSystemService(SmsManager::class.java)
                val list = db.contactDao().getEmergencyContacts().first()
                if (list.isEmpty()) return@launch

                list.forEach { contact ->
                    try {
                        smsManager?.sendTextMessage(contact.phoneNumber, null, message, null, null)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Chat
    val messages = db.messageDao().getAllMessages()

    fun sendMessage(text: String) {
        viewModelScope.launch {
            val message = MessageEntity(
                senderId = "Me",
                content = text,
                timestamp = System.currentTimeMillis(),
                priority = 0,
                isSent = true
            )
            db.messageDao().insertMessage(message)
            meshManager.sendData(text.toByteArray())
        }
    }

    fun toggleSiren() {
        _isSirenOn.value = !_isSirenOn.value
        if (_isSirenOn.value) {
            hardwareManager.playSiren(R.raw.siren)
        } else {
            hardwareManager.stopSiren()
        }
    }

    fun toggleFlashlight() {
        _isFlashlightOn.value = !_isFlashlightOn.value
        hardwareManager.setFlashlight(_isFlashlightOn.value)
    }

    fun startStrobe() = hardwareManager.startStrobe()
    fun stopStrobe() = hardwareManager.stopStrobe()
}
