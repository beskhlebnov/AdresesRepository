package com.example.adresrepository.person

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
import com.example.adresrepository.R
import com.example.adresrepository.city.CityListActivity
import com.example.adresrepository.country.CountryListActivity
import com.example.adresrepository.region.RegionListActivity


import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class PersonListFragment: Fragment() {
    private lateinit var buttonCountry: Button
    private lateinit var buttonRegion: Button
    private lateinit var buttonCity: Button
    private lateinit var buttonAdd: ImageButton
    private var persons = mutableListOf<Person>()
    private var personRecycleView: RecyclerView? = null
    private var adapter: PersonAdapter? = null
    private val client = OkHttpClient()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.list_person_fragment, container, false)
        buttonCountry = view.findViewById(R.id.country)
        buttonRegion = view.findViewById(R.id.region)
        buttonCity = view.findViewById(R.id.city)
        buttonCity.setOnClickListener {context?.startActivity(CityListActivity.newIntent(context))}
        buttonCountry.setOnClickListener { context?.startActivity(CountryListActivity.newIntent(context))}
        buttonRegion.setOnClickListener {context?.startActivity(RegionListActivity.newIntent(context))}
        buttonAdd = view.findViewById(R.id.add)
        buttonAdd.setOnClickListener { context?.startActivity(PersonAddActivity.newIntent(context))}
        personRecycleView = view.findViewById(R.id.person_recycler_view)
        personRecycleView!!.layoutManager = LinearLayoutManager(activity)
        return view
    }

    override fun onResume() { super.onResume(); updateUI() }

    private fun updateUI(){
        val request = Request.Builder().url("http://192.168.1.4:5000/person").build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("GGG", e.toString())
            }
            override fun onResponse(call: Call, response: Response) {
                val objectc = JSONObject(response.body()!!.string())
                val listJson = objectc.getJSONArray("list")
                persons.clear()
                for(i in 0..<listJson.length()){
                    val iter = JSONObject(listJson.get(i).toString())
                    persons.add(Person(
                        iter.getInt("id"),
                        iter.getInt("city_id"),
                        iter.getString("person"),
                        iter.getString("street"),
                        iter.getString("building"),
                        iter.getString("office"),
                        iter.getString("city")
                    ))
                }
            }
        })


        if (adapter == null){
            adapter = PersonAdapter(persons)
            personRecycleView!!.adapter = adapter
        }

    }

    private class PersonHolder (item: View?): ViewHolder(item!!), View.OnClickListener {
        var personTextView: TextView? = itemView.findViewById(R.id.list_item_person_person)
        var adresTextView: TextView? = itemView.findViewById(R.id.list_item_person_adres)


        private lateinit var person: Person

        @SuppressLint("SetTextI18n")
        fun bindPerson(person: Person){
            this.person = person
            personTextView?.text = person.person
            adresTextView?.text = "${person.city} ${person.street} д ${person.building} кв ${person.office}"
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val context = v!!.context
            val intent = PersonUpdateActivity.newIntent(context, person.id)
            context.startActivity(intent)
        }
    }
    private class PersonAdapter(persons: List<Person>?): RecyclerView.Adapter<PersonHolder?>(){
        private var persons: List<Person>? = null
        init { this.persons = persons }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_list_person, parent,false)
            return PersonHolder(view)
        }

        override fun getItemCount(): Int {
            return  persons!!.size
        }

        override fun onBindViewHolder(holder: PersonHolder, position: Int) {
            val person = persons!![position]
            holder.bindPerson(person)

        }
    }

}