package trajkovic.pora.memorymap

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import trajkovic.pora.memorymap.databinding.ActivityMainBinding
import trajkovic.pora.memorymap.fragments.ListFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    //TODO: Add support for LiveData in the Adapter, Create MyApplication with the database instance

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
        val fragment = ListFragment()
        supportFragmentManager.beginTransaction().replace(binding.fragmentContainerView.id,fragment).commit()
    }
}