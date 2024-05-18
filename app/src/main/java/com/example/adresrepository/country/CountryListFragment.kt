package com.example.adresrepository.country

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.adresrepository.person.PersonListActivity
import com.example.adresrepository.R
import com.example.adresrepository.region.RegionListActivity
import com.example.adresrepository.city.CityListActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class CountryListFragment: Fragment() {
    private lateinit var buttonPerson: Button
    private lateinit var buttonRegion: Button
    private lateinit var buttonCity: Button
    private var recycleView: RecyclerView? = null
    private var adapter: CountryAdapter? = null
    private lateinit var addButton: ImageButton
    private val client = OkHttpClient()
    private var countrys = mutableListOf<Country>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.list_country_fragment, container, false)
        buttonPerson = view.findViewById(R.id.person)
        buttonRegion = view.findViewById(R.id.region)
        buttonCity = view.findViewById(R.id.city)
        buttonCity.setOnClickListener {context?.startActivity(CityListActivity.newIntent(context))}
        buttonPerson.setOnClickListener { context?.startActivity(
            PersonListActivity.newIntent(
                context
            )
        )}
        buttonRegion.setOnClickListener {context?.startActivity(RegionListActivity.newIntent(context))}
        recycleView = view.findViewById(R.id.region_recycler_view)
        recycleView!!.layoutManager = LinearLayoutManager(activity)
        addButton = view.findViewById(R.id.add)
        addButton.setOnClickListener {
            val intent = CountryAddActivity.newIntent(context)
            context?.startActivity(intent)
        }
        getInfo()
        return view
    }

    private fun getInfo(){
        val request = Request.Builder().url("http://192.168.1.4:5000/country").build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) { Log.e("GGG", e.toString()) }
            override fun onResponse(call: Call, response: Response) {
                val objectcList = JSONObject(response.body()!!.string()).getJSONArray("list")
                countrys.clear()
                for(i in 0..<objectcList.length()){
                    val iter = JSONObject(objectcList.get(i).toString())
                    countrys.add(Country(iter.getInt("id"), iter.getString("fullname"), iter.getString("shortname")))
                }
            }
        })

        if (adapter == null){
            adapter = CountryAdapter(countrys)
            recycleView!!.adapter = adapter
        }

    }

    private class Holder (item: View?): ViewHolder(item!!), View.OnClickListener {
        var personTextView: TextView? = itemView.findViewById(R.id.list_item_person_person)
        var adresTextView: TextView? = itemView.findViewById(R.id.list_item_person_adres)


        private lateinit var country: Country

        @SuppressLint("SetTextI18n")
        fun bindCountry(country1: Country, position: Int){
            this.country = country1
            personTextView?.text = country1.fullname
            adresTextView?.text = country1.shortname
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val context = v!!.context
            val intent = CountryUpdateActivity.newIntent(context, country.id)
            context.startActivity(intent)
        }
    }
    private class CountryAdapter(countries: List<Country>?): RecyclerView.Adapter<Holder?>(){
        private var countries: List<Country>? = null
        init { this.countries = countries }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_list_person, parent,false)
            return Holder(view)
        }

        override fun getItemCount(): Int {
            return  countries!!.size
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            val country = countries!![position]
            holder.bindCountry(country, holder.absoluteAdapterPosition)

        }
    }

}