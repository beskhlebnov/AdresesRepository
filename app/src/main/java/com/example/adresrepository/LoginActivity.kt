package com.example.adresrepository

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.example.adresrepository.person.PersonListActivity

class LoginActivity: SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?): Intent?{
            val intent = Intent(packageContext, PersonListActivity::class.java)
            return intent
        }
    }

    override fun createFragment(): Fragment = LoginFragment()
}