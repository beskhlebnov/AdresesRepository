package com.example.adresrepository.city

import android.content.Context
import android.content.Intent
import com.example.adresrepository.SingleFragmentActivity

class CityAddActivity : SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?): Intent{
            val intent = Intent(packageContext, CityAddActivity::class.java)
            return intent
        }
    }

    override fun createFragment(): CityAddFragment {
        return CityAddFragment.newInstance()
    }
}