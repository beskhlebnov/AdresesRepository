package com.example.adresrepository.country

import android.content.Context
import android.content.Intent
import com.example.adresrepository.SingleFragmentActivity

class CountryUpdateActivity : SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?, id: Int?): Intent?{
            val intent = Intent(packageContext, CountryUpdateActivity::class.java)
            intent.putExtra(CountryUpdateFragment.ARG_COUNTRY_ID,id)
            return intent
        }
    }

    override fun createFragment(): CountryUpdateFragment {
        val id = intent.getSerializableExtra(CountryUpdateFragment.ARG_COUNTRY_ID) as Int?
        return CountryUpdateFragment.newInstance(id)
    }
}