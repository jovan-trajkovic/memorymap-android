package trajkovic.pora.memorymap

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import trajkovic.pora.memorymap.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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
                //tempNotification()
                scheduleNotifications()
            }
        }
        else
        {
            //tempNotification()
            scheduleNotifications()
        }

        val navController = binding.fragmentContainerView.getFragment<NavHostFragment>().findNavController()
        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setupWithNavController(navController)

        onBackPressedDispatcher.addCallback(this) {
            val currentDestination = navController.currentDestination?.id
            val topLevelDestinations = setOf(R.id.listFragment, R.id.mapsFragment, R.id.addLogFragment)
            if (currentDestination in topLevelDestinations) {
                finish()
            } else {
                navController.popBackStack()
            }
        }
    }

    private fun scheduleNotifications() {
        //WorkManager.getInstance(this).cancelUniqueWork("ReminderWorker")

        val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(1, TimeUnit.MINUTES).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "ReminderWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    private fun tempNotification() {
        val oneTimeRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(10, TimeUnit.SECONDS) // Short delay for testing
            .build()

        WorkManager.getInstance(this).enqueue(oneTimeRequest)
    }
}