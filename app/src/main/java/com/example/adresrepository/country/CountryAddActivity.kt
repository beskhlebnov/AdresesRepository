package com.example.adresrepository.country

import android.content.Context
import android.content.Intent
import com.example.adresrepository.SingleFragmentActivity

class CountryAddActivity : SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?): Intent?{
            val intent = Intent(packageContext, CountryAddActivity::class.java)
            return intent
        }
    }

    override fun createFragment(): CountryAddFragment {
        return CountryAddFragment.newInstance()
    }
}