package com.example.adresrepository.region

import android.content.Context
import android.content.Intent
import com.example.adresrepository.SingleFragmentActivity

class RegionAddActivity : SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?): Intent{
            val intent = Intent(packageContext, RegionAddActivity::class.java)
            return intent
        }
    }

    override fun createFragment(): RegionAddFragment {
        return RegionAddFragment.newInstance()
    }
}