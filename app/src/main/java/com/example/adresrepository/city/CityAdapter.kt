package com.example.adresrepository.city

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.adresrepository.R

class CityAdapter(context: Context, list: List<City>) : ArrayAdapter<City>(context, 0, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val city = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_city, parent, false)
        if (city != null) {
            val name = view.findViewById<TextView>(R.id.name)
            name.text = city.name
        }

        return view
    }
}