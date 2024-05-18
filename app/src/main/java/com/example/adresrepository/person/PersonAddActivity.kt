package com.example.adresrepository.person

import android.content.Context
import android.content.Intent
import com.example.adresrepository.SingleFragmentActivity

class PersonAddActivity : SingleFragmentActivity() {
    companion object{


        fun newIntent(packageContext: Context?): Intent{
            val intent = Intent(packageContext, PersonAddActivity::class.java)
            return intent
        }
    }

    override fun createFragment(): PersonAddFragment {
        return PersonAddFragment.newInstance()
    }
}