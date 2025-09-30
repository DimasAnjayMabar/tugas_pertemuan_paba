package test1.nrp.pertemuan8

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.ActivityRecognition

class TrackerService : Service(), SensorEventListener {
    private val TAG = "TrackerService"

    private val TRANSITION_RECEIVER_ACTION = "test1.nrp.pertemuan8.ACTIVITY_TRANSITIONS"
    private lateinit var pendingIntent: PendingIntent
    private lateinit var activityReceiver: BroadcastReceiver
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var notificationManager: NotificationManagerCompat

    private val NOTIFICATION_CHANNEL_ID_SERVICE_RUNNING = "tracker_service_running"
    private val NOTIFICATION_CHANNEL_ID_JUMP_DETECTED = "tracker_jump_detected"
    private val NOTIFICATION_ID_SERVICE_RUNNING = 1
    private val NOTIFICATION_ID_JUMP_DETECTED = 2

    private fun setupActivityRecognition(){
        Log.d(TAG, "setupActivityRecognition started")
        try {
            val intent = Intent(TRANSITION_RECEIVER_ACTION)
            pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            activityReceiver = object : BroadcastReceiver(){
                override fun onReceive(context: Context, intent: Intent){
                    Log.d(TAG, "ActivityReceiver onReceive: ${intent.action}")
                    // Placeholder untuk ActivityTransitionResult
                }
            }
            val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                RECEIVER_NOT_EXPORTED
            } else {
                Context.RECEIVER_NOT_EXPORTED
            }
            registerReceiver(activityReceiver, IntentFilter(TRANSITION_RECEIVER_ACTION), flags)
            Log.d(TAG, "Activity recognition setup completed")
        } catch (e: Exception) {
            Log.e(TAG, "Error in setupActivityRecognition: ${e.message}", e)
        }
    }

    private fun setupJumpDetector(){
        Log.d(TAG, "setupJumpDetector started")
        try {
            sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            if (accelerometer == null) {
                Log.e(TAG, "No accelerometer sensor found!")
            } else {
                Log.d(TAG, "Accelerometer sensor found: ${accelerometer?.name}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in setupJumpDetector: ${e.message}", e)
        }
    }

    private fun createNotificationChannels() {
        Log.d(TAG, "createNotificationChannels started")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID_SERVICE_RUNNING,
                "Tracker Service Running",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows when tracker service is running"
            }

            val jumpChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID_JUMP_DETECTED,
                "Jump Detected",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications when jump is detected"
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
            manager.createNotificationChannel(jumpChannel)
            Log.d(TAG, "Notification channels created")
        }
    }

    private fun startJumpDetector(){
        Log.d(TAG, "startJumpDetector called")
        try {
            accelerometer?.also { sensor ->
                val success = sensorManager.registerListener(
                    this,
                    sensor,
                    SensorManager.SENSOR_DELAY_NORMAL
                )
                if (success) {
                    Log.d(TAG, "Accelerometer listener registered successfully")
                } else {
                    Log.e(TAG, "Failed to register accelerometer listener")
                }
            } ?: run {
                Log.e(TAG, "Cannot start jump detector: accelerometer is null")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in startJumpDetector: ${e.message}", e)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        Log.d(TAG, "onBind called")
        return null
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d(TAG, "onAccuracyChanged: sensor=${sensor?.name}, accuracy=$accuracy")
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "=== SERVICE onCreate STARTED ===")
        try {
            notificationManager = NotificationManagerCompat.from(this)
            createNotificationChannels()
            setupActivityRecognition()
            setupJumpDetector()
            Log.d(TAG, "=== SERVICE onCreate COMPLETED ===")
        } catch (e: Exception) {
            Log.e(TAG, "=== SERVICE onCreate FAILED: ${e.message} ===", e)
        }
    }

    @RequiresPermission(Manifest.permission.ACTIVITY_RECOGNITION)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "=== SERVICE onStartCommand STARTED ===")
        Log.d(TAG, "Intent: ${intent?.action}, flags: $flags, startId: $startId")

        try {
            val notification = buildServiceRunningNotification("Aplikasi tracker service sedang berjalan").build()
            startForeground(NOTIFICATION_ID_SERVICE_RUNNING, notification)
            Log.d(TAG, "Service started in foreground")

            startJumpDetector()
            Log.d(TAG, "=== SERVICE onStartCommand COMPLETED ===")
        } catch (e: Exception) {
            Log.e(TAG, "=== SERVICE onStartCommand FAILED: ${e.message} ===", e)
        }

        return START_STICKY
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            val jumpThreshold = 10.0

            // Log sensor data untuk debugging
            Log.d(TAG, "Accelerometer - X: $x, Y: $y, Z: $z")

            if (Math.abs(y) > jumpThreshold) {
                Log.d(TAG, "=== LOMPATAN TERDETEKSI! (y: $y) ===")

                try {
                    val mainActivityIntent = Intent(this, MainActivity::class.java).apply {
                        action = MainActivity.ACTION_SERVICE_STOPPED_JUMP
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    }

                    val pendingMainActivityIntent = PendingIntent.getActivity(
                        this,
                        1,
                        mainActivityIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                    val jumpNotification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID_JUMP_DETECTED)
                        .setContentTitle("Lompatan terdeteksi!")
                        .setContentText("Service berhenti, click untuk membuka aplikasi")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentIntent(pendingMainActivityIntent)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_EVENT)
                        .build()

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || notificationManager.areNotificationsEnabled()) {
                        notificationManager.notify(NOTIFICATION_ID_JUMP_DETECTED, jumpNotification)
                        Log.d(TAG, "Jump notification sent")
                    } else {
                        Log.w(TAG, "Izin post_notification belum ada")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error showing jump notification: ${e.message}", e)
                }

                Log.d(TAG, "Stopping service due to jump detection")
                stopSelf()
            }
        }
    }

    private fun buildServiceRunningNotification(contentText: String): NotificationCompat.Builder {
        Log.d(TAG, "Building service running notification")
        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingNotificationIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID_SERVICE_RUNNING)
            .setContentTitle("Pelacak Aktivitas Latar")
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingNotificationIntent)
    }

    @RequiresPermission(Manifest.permission.ACTIVITY_RECOGNITION)
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "=== SERVICE onDestroy STARTED ===")
        try {
            // Hapus komentar ActivityRecognition jika sudah diimplementasikan

            ActivityRecognition.getClient(this).removeActivityTransitionUpdates(pendingIntent)
                .addOnSuccessListener { Log.d(TAG, "Activity update was removed") }
                .addOnFailureListener { e -> Log.e(TAG, "Failed to remove activity updates", e) }

            unregisterReceiver(activityReceiver)
            Log.d(TAG, "Activity receiver unregistered")

            sensorManager.unregisterListener(this)
            Log.d(TAG, "Sensor listener unregistered")

            Log.d(TAG, "=== SERVICE onDestroy COMPLETED ===")
        } catch (e: Exception) {
            Log.e(TAG, "=== SERVICE onDestroy ERROR: ${e.message} ===", e)
        }
    }
}