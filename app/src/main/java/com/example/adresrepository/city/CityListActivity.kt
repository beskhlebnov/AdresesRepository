package com.example.adresrepository.city

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.example.adresrepository.SingleFragmentActivity

class CityListActivity: SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?): Intent{
            val intent = Intent(packageContext, CityListActivity::class.java)
            return intent
        }
    }

    override fun createFragment(): Fragment = CityListFragment()
}