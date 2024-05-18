package com.example.adresrepository.country

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.example.adresrepository.SingleFragmentActivity

class CountryListActivity: SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?): Intent?{
            val intent = Intent(packageContext, CountryListActivity::class.java)
            return intent
        }
    }

    override fun createFragment(): Fragment = CountryListFragment()
}