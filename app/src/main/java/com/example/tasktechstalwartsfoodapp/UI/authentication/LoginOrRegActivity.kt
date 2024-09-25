package com.example.tasktechstalwartsfoodapp.UI.authentication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tasktechstalwartsfoodapp.R
import com.example.tasktechstalwartsfoodapp.adapter.LoginSignUpTabAdapter
import com.example.tasktechstalwartsfoodapp.databinding.ActivityLoginOrRegBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginOrRegActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginOrRegBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginOrRegBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tabLayout.let {
            it.addTab(it.newTab().setText("Login"))
            it.addTab(it.newTab().setText("Register"))
            it.tabGravity = TabLayout.GRAVITY_FILL
        }

        val adapter = LoginSignUpTabAdapter(this, binding.tabLayout.tabCount)
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Login"
                1 -> "Register"
                else -> null
            }
        }.attach()

    }
}