package com.example.adresrepository.country

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.adresrepository.R

class CountryAdapter(context: Context, list: List<Country>) : ArrayAdapter<Country>(context, 0, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val country = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_country, parent, false)
        if (country != null) {
            val name = view.findViewById<TextView>(R.id.shortname)
            name.text = country.shortname
        }

        return view
    }
}