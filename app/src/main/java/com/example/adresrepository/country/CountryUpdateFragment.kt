package com.example.adresrepository.country

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.adresrepository.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class CountryUpdateFragment : Fragment() {

    companion object{
        const val ARG_COUNTRY_ID = "country_id"
        fun newInstance(personId: Int?) =
            CountryUpdateFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_COUNTRY_ID, personId)
                }
            }
    }
    private val client = OkHttpClient()
    private var countryId = 0
    private var country: Country? = null
    private lateinit var fullname_field: EditText
    private lateinit var shortname_field: EditText
    private lateinit var editButton: Button
    private lateinit var deleteButton: Button
    private lateinit var backButton: ImageButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        countryId = (requireActivity().intent.getSerializableExtra(ARG_COUNTRY_ID) as Int?)!!
        val request = Request.Builder().url("http://192.168.1.4:5000/country/${countryId}").build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) { Log.e("GGG", e.toString()) }
            override fun onResponse(call: Call, response: Response) {
                val objectCountry = JSONObject(response.body()!!.string())
                println(objectCountry.toString())
                country = Country(objectCountry.getInt("id"), objectCountry.getString("fullname"), objectCountry.getString("shortname"))
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.update_country_fragment, container, false)
        editButton = v.findViewById(R.id.edit)
        backButton = v.findViewById(R.id.back)
        deleteButton = v.findViewById(R.id.delete)

        editButton.setOnClickListener {
            if (fullname_field.text.toString().isEmpty() && shortname_field.text.toString().isEmpty()){
                Toast.makeText(context, "Заполните все поля!", Toast.LENGTH_LONG).show()
            }
            else {
            val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("fullname", fullname_field.text.toString())
                .addFormDataPart("shortname", shortname_field.text.toString())
                .build()
            val request = Request.Builder().url("http://192.168.1.4:5000/country/${countryId}").put(body).build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Toast.makeText(context, "Сервис не доступен повторите попытку позже!", Toast.LENGTH_LONG).show()
                    Log.e("onFailLoginRequest", e.toString())
                }
                override fun onResponse(call: Call, response: Response) {
                    val objectc = JSONObject(response.body()!!.string())
                    val statusa: String = objectc.getString("status")
                    if (statusa == "200"){goBack()}
                }
            })
            }
        }

        backButton.setOnClickListener {goBack()}

        deleteButton.setOnClickListener {
            val request = Request.Builder().url("http://192.168.1.4:5000/country/${countryId}").delete().build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) { Log.e("GGG", e.toString()) }
                override fun onResponse(call: Call, response: Response) {
                    val objectc = JSONObject(response.body()!!.string())
                    val statusa: String = objectc.getString("status")
                    if (statusa == "200"){ goBack()}
                    else{
                        Toast.makeText(context, "Произошла ошибка!", Toast.LENGTH_LONG).show()
                    }

                }
            })
        }

        fullname_field = v.findViewById(R.id.fullname)
        fullname_field.setText(country?.fullname)
        shortname_field = v.findViewById(R.id.shortname)
        shortname_field.setText(country?.shortname)
        return v
    }

    fun goBack(){
        val intent = CountryListActivity.newIntent(context)
        context?.startActivity(intent)
    }
}