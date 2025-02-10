package trajkovic.pora.memorymap

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import trajkovic.pora.memorymap.databinding.ActivityMainBinding
import trajkovic.pora.memorymap.fragments.AddLogFragment
import trajkovic.pora.memorymap.fragments.ListFragment
import trajkovic.pora.memorymap.fragments.MapsFragment
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    //TODO: Add fragment tags for saving and restoring fragments

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "Notifications enabled", Toast.LENGTH_SHORT).show()
                scheduleNotifications()
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 (API 33) and above
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
            else{
                scheduleNotifications()
            }
        }
        else
        {
            scheduleNotifications()
        }

        if (intent?.action == "OPEN_FRAGMENT") {
            val fragmentName = intent.getStringExtra("FRAGMENT_TO_OPEN")
            if (fragmentName == "AddLogFragment") {
                replaceFragment(AddLogFragment())
                binding.bottomNavigationView.selectedItemId = R.id.addMenuButton
            }
        }
        else {
            replaceFragment(ListFragment())
        }

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.listMenuButton -> { replaceFragment(ListFragment()); true }
                R.id.addMenuButton -> { replaceFragment(AddLogFragment()); true }
                R.id.mapMenuButton -> { replaceFragment(MapsFragment()); true }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction().replace(binding.fragmentContainerView.id,fragment).commit()
    }

    private fun scheduleNotifications() {
        val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(1, TimeUnit.MINUTES).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "ReminderWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}