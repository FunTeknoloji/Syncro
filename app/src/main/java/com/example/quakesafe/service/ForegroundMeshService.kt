package com.example.quakesafe.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.quakesafe.mesh.MeshManager

class ForegroundMeshService : Service() {
    private lateinit var meshManager: MeshManager

    override fun onCreate() {
        super.onCreate()
        meshManager = MeshManager(this)
        createNotificationChannel()
        startForeground(2, createNotification())
        meshManager.startMesh()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel("mesh_service", "Mesh Service", NotificationManager.IMPORTANCE_LOW)
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, "mesh_service")
            .setContentTitle("Mesh Active")
            .setContentText("Participating in QuakeSafe mesh network")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        meshManager.stopMesh()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
