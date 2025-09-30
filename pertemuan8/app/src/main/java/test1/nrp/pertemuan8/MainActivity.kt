package test1.nrp.pertemuan8

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var txt: TextView
    private lateinit var btn: Button

    private val TAG = "MainActivity"

    private val permissionToRequest = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        arrayOf(
            Manifest.permission.ACTIVITY_RECOGNITION,
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }else{
        arrayOf(
            Manifest.permission.ACTIVITY_RECOGNITION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    companion object{
        const val ACTION_SERVICE_STOPPED_JUMP = "test1.nrp.pertemuan8.ACTION_SERVICE_STOPPED_JUMP"
        const val REQUEST_CODE_PERMISSION = 101
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionsResult: requestCode=$requestCode")

        if(requestCode == REQUEST_CODE_PERMISSION){
            var allPermissionGranted = true
            for(result in grantResults){
                if(result != android.content.pm.PackageManager.PERMISSION_GRANTED){
                    allPermissionGranted = false
                    break
                }
            }

            if(allPermissionGranted){
                Log.d(TAG, "All permissions granted, starting service")
                try {
                    val serviceIntent = Intent(this, TrackerService::class.java)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(serviceIntent)
                        Log.d(TAG, "startForegroundService called")
                    } else {
                        startService(serviceIntent)
                        Log.d(TAG, "startService called")
                    }
                    txt.text = "Layanan tracker berjalan di background"
                } catch (e: Exception) {
                    Log.e(TAG, "Error starting service: ${e.message}", e)
                    txt.text = "Error: ${e.message}"
                }
            }else{
                Log.w(TAG, "Not all permissions granted")
                txt.text = "Izin diperlukan untuk menjalankan layanan"
            }
        }
    }

    private fun handleIntent(intent: Intent){
        Log.d(TAG, "handleIntent: action=${intent.action}")
        if(intent.action == ACTION_SERVICE_STOPPED_JUMP){
            txt.text = "Background service sudah berhenti karena Anda melompat"
        }
    }

    override fun onNewIntent(intent: Intent){
        super.onNewIntent(intent)
        Log.d(TAG, "onNewIntent: action=${intent.action}")
        handleIntent(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate started")

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        Log.d(TAG, "setContentView completed")

        // Inisialisasi view setelah setContentView
        try {
            txt = findViewById(R.id.txt)
            btn = findViewById(R.id.btn)
            Log.d(TAG, "Views initialized successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing views: ${e.message}", e)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn.setOnClickListener {
            Log.d(TAG, "Button clicked")
            // Cek permission dulu sebelum request
            val deniedPermissions = permissionToRequest.filter { permission ->
                ActivityCompat.checkSelfPermission(this, permission) != android.content.pm.PackageManager.PERMISSION_GRANTED
            }.toTypedArray()

            Log.d(TAG, "Denied permissions: ${deniedPermissions.size}")

            if (deniedPermissions.isNotEmpty()) {
                ActivityCompat.requestPermissions(
                    this,
                    deniedPermissions,
                    REQUEST_CODE_PERMISSION
                )
                Log.d(TAG, "Requesting permissions: ${deniedPermissions.joinToString()}")
            } else {
                Log.d(TAG, "All permissions already granted, starting service directly")
                // Jika semua permission sudah granted, langsung start service
                try {
                    val serviceIntent = Intent(this, TrackerService::class.java)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(serviceIntent)
                    } else {
                        startService(serviceIntent)
                    }
                    txt.text = "Layanan tracker berjalan di background"
                } catch (e: Exception) {
                    Log.e(TAG, "Error starting service: ${e.message}", e)
                    txt.text = "Error: ${e.message}"
                }
            }
        }

        Log.d(TAG, "onCreate completed, handling intent")
        handleIntent(intent)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }
}