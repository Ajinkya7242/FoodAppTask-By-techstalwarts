package com.example.tasktechstalwartsfoodapp.UI.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.tasktechstalwartsfoodapp.MainActivity
import com.example.tasktechstalwartsfoodapp.R
import com.example.tasktechstalwartsfoodapp.databinding.FragmentLoginBinding
import com.example.tasktechstalwartsfoodapp.viewmodel.FirebaseAuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FirebaseAuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.cvLogin.setOnClickListener{
            val email=binding.etEmailLogin.text.toString()
            val password=binding.etPassLogin.text.toString()
            binding.cvProgressbar.isVisible=true
            binding.cvLogin.isEnabled=false

            if(validateInput(email,password)){
                loginWithFirebase(email,password)
            }
            else{
                binding.cvProgressbar.isVisible=false
                binding.cvLogin.isEnabled=true

            }

        }

        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(requireContext(),ForgotPassActivity::class.java))
            requireActivity().finish()
        }


        return binding.root
    }

    private fun loginWithFirebase(email: String, pass: String) {
        viewModel.loginUser(email, pass)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                binding.cvLogin.isEnabled=true
                binding.cvProgressbar.isVisible=false
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
                Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
            }.onFailure {
                binding.cvLogin.isEnabled=true
                binding.cvProgressbar.isVisible=false
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInput(email: String, password: String) :Boolean{
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()||email.isEmpty()){
            binding.etEmailLogin.requestFocus()
            binding.etEmailLogin.error="Invalid Email"
            return false
        }
        else if(password.isEmpty()||password.length<6){
            binding.etPassLogin.requestFocus()
            binding.etPassLogin.error="Invalid Password"
            return false
        }
        else{
            return true
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}