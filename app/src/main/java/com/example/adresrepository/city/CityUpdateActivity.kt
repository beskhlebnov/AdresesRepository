package com.example.adresrepository.city

import android.content.Context
import android.content.Intent
import com.example.adresrepository.SingleFragmentActivity

class CityUpdateActivity : SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?, id: Int?): Intent{
            val intent = Intent(packageContext, CityUpdateActivity::class.java)
            intent.putExtra(CityUpdateFragment.ARG_CITY_ID,id)
            return intent
        }
    }

    override fun createFragment(): CityUpdateFragment {
        val id = intent.getSerializableExtra(CityUpdateFragment.ARG_CITY_ID) as Int?
        return CityUpdateFragment.newInstance(id)
    }
}