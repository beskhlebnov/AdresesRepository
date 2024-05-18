package com.example.adresrepository.region

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.adresrepository.R
import com.example.adresrepository.country.Country
import com.example.adresrepository.country.CountryAdapter
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class RegionAddFragment : Fragment() {

    companion object{ fun newInstance() = RegionAddFragment() }

    private val client = OkHttpClient()
    private var countryId = 0
    private lateinit var nameField: EditText
    private var countrys = mutableListOf<Country>()
    private lateinit var countrySpinner: Spinner
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        client.newCall(Request.Builder().url("http://192.168.1.4:5000/country")
            .build()).enqueue(object : Callback{

            override fun onFailure(call: Call, e: IOException){ Log.e("CallBackError", e.toString())}

            override fun onResponse(call: Call, response: Response) {
                val objectcList = JSONObject(response.body()!!.string()).getJSONArray("list")
                countrys.clear()
                for(i in 0..<objectcList.length()){
                    println(JSONObject(objectcList.get(i).toString()))
                    val iter = JSONObject(objectcList.get(i).toString())
                    countrys.add(Country(iter.getInt("id"), iter.getString("fullname"), iter.getString("shortname")))
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.add_region_fragment, container, false)
        saveButton = v.findViewById(R.id.save)
        backButton = v.findViewById(R.id.back)
        cancelButton = v.findViewById(R.id.cancel)
        countrySpinner = v.findViewById(R.id.country_spinner)

        nameField = v.findViewById(R.id.name)

        countrySpinner.adapter = CountryAdapter(requireContext(), countrys)
        countrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
               countryId=countrys.get(position).id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        backButton.setOnClickListener {goBack()}
        saveButton.setOnClickListener {
            if (nameField.text.toString().isEmpty() && countryId==0){
                Toast.makeText(context, "Заполните все поля!", Toast.LENGTH_LONG).show()
            }
            else {
                val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("name", nameField.text.toString())
                    .addFormDataPart("country_id", countryId.toString())
                    .build()
                val request =
                    Request.Builder().url("http://192.168.1.4:5000/region").post(body).build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.e("onFailLoginRequest", e.toString())
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val objectc = JSONObject(response.body()!!.string())
                        val statusa: String = objectc.getString("status")
                        if (statusa == "200") {goBack()}
                    }
                })
            }
        }

        cancelButton.setOnClickListener {goBack()}
        return v
    }

    fun goBack(){
        val intent = RegionListActivity.newIntent(context)
        context?.startActivity(intent)
    }
}