package com.example.tasktechstalwartsfoodapp.UI.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.tasktechstalwartsfoodapp.MainActivity
import com.example.tasktechstalwartsfoodapp.R
import com.example.tasktechstalwartsfoodapp.databinding.ActivityForgotPassBinding
import com.example.tasktechstalwartsfoodapp.viewmodel.ForgotPassViewmodel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ForgotPassActivity : AppCompatActivity() {
    lateinit var binding:ActivityForgotPassBinding
    private val viewModel:ForgotPassViewmodel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityForgotPassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.cvForgotPass.setOnClickListener {
            val email=binding.etEmailReg.text.toString()
            binding.cvProgressbar.isVisible=true
            if(email.isEmpty()||!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.cvProgressbar.isVisible=false
                binding.etEmailReg.requestFocus()
                binding.etEmailReg.error="Enter Email"
            }
            else{
                forgotPass(email)
            }
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun forgotPass(email: String) {
        viewModel.forgotPassword(email)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.forgotPassResult.observe(this) { result ->
            result.onSuccess {
                binding.cvProgressbar.isVisible=false
                startActivity(Intent(this, LoginOrRegActivity::class.java))
                finish()
                Toast.makeText(this, "OTP has been sent to your email if it exists in our system.", Toast.LENGTH_SHORT).show()
            }.onFailure {
                binding.cvProgressbar.isVisible=false
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        startActivity(Intent(this,LoginOrRegActivity::class.java))
        finish()
    }
}
