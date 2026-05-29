package edu.guigu.accountbook

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.guigu.accountbook.databinding.ActivityMainBinding
import edu.guigu.accountbook.ui.task.Task1Activity
import edu.guigu.accountbook.ui.task.Task2Activity
import edu.guigu.accountbook.ui.task.Task3Activity
import edu.guigu.accountbook.ui.task.Task4Activity
import edu.guigu.accountbook.ui.task.Task5Activity
import edu.guigu.accountbook.ui.task.Task6Activity
import edu.guigu.accountbook.ui.task.Task7Activity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.cardTask1.setOnClickListener { startActivity(Intent(this, Task1Activity::class.java)) }
        binding.cardTask2.setOnClickListener { startActivity(Intent(this, Task2Activity::class.java)) }
        binding.cardTask3.setOnClickListener { startActivity(Intent(this, Task3Activity::class.java)) }
        binding.cardTask4.setOnClickListener { startActivity(Intent(this, Task4Activity::class.java)) }
        binding.cardTask5.setOnClickListener { startActivity(Intent(this, Task5Activity::class.java)) }
        binding.cardTask6.setOnClickListener { startActivity(Intent(this, Task6Activity::class.java)) }
        binding.cardTask7.setOnClickListener { startActivity(Intent(this, Task7Activity::class.java)) }
    }
}
