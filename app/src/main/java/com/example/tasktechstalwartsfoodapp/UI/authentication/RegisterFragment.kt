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
import com.example.tasktechstalwartsfoodapp.databinding.FragmentRegisterBinding
import com.example.tasktechstalwartsfoodapp.viewmodel.FirebaseAuthViewModel
import com.google.firebase.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    lateinit var binding:FragmentRegisterBinding
    private val viewModel:FirebaseAuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentRegisterBinding.inflate(layoutInflater,container,false)

        binding.cvRegister.setOnClickListener{
            val email=binding.etEmailReg.text.toString()
            val password=binding.etPassReg.text.toString()
            binding.cvProgressbar.isVisible=true
            binding.cvRegister.isEnabled=false
            if(validateInput(email,password)){
                regWithFirebase(email,password)
            }else{
                binding.cvProgressbar.isVisible=false
                binding.cvRegister.isEnabled=true
            }

        }

        return binding.root

    }

    private fun regWithFirebase(email: String, pass: String) {
        viewModel.registerUser(email, pass)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.registerResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                binding.cvProgressbar.isVisible=false
                binding.cvRegister.isEnabled=true
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
                Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
            }.onFailure {
                binding.cvProgressbar.isVisible=false
                binding.cvRegister.isEnabled=true
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInput(email: String, password: String) :Boolean{
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()||email.isEmpty()){
            binding.etEmailReg.requestFocus()
            binding.etEmailReg.error="Invalid Email"
            return false
        }
        else if(password.isEmpty()||password.length<6){
            binding.etPassReg.requestFocus()
            binding.etPassReg.error="Invalid Password"
            return false
        }
        else{
            return true
        }
    }
}