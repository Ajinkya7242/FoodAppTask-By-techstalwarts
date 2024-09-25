package com.example.tasktechstalwartsfoodapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tasktechstalwartsfoodapp.UI.authentication.LoginFragment
import com.example.tasktechstalwartsfoodapp.UI.authentication.RegisterFragment

class LoginSignUpTabAdapter(fa: FragmentActivity, internal var totalTabs: Int) :
    FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return totalTabs
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LoginFragment()
            1 -> RegisterFragment()
            else -> LoginFragment()
        }
    }
}