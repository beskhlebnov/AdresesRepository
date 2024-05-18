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

class CountryAddFragment : Fragment() {

    companion object{ fun newInstance() = CountryAddFragment() }

    private val client = OkHttpClient()
    private lateinit var fullname_field: EditText
    private lateinit var shortname_field: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var backButton: ImageButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.add_country_fragment, container, false)
        saveButton = v.findViewById(R.id.save)
        backButton = v.findViewById(R.id.back)
        cancelButton = v.findViewById(R.id.cancel)
        fullname_field = v.findViewById(R.id.fullname)
        shortname_field = v.findViewById(R.id.shortname)
        backButton.setOnClickListener {goBack()}
        saveButton.setOnClickListener {
            if (fullname_field.text.toString().isEmpty() && shortname_field.text.toString().isEmpty()){
                Toast.makeText(context, "Заполните все поля!", Toast.LENGTH_LONG).show()
            }
            else {
                val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("fullname", fullname_field.text.toString())
                    .addFormDataPart("shortname", shortname_field.text.toString())
                    .build()
                val request =
                    Request.Builder().url("http://192.168.1.4:5000/country").post(body).build()

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
        val intent = CountryListActivity.newIntent(context)
        context?.startActivity(intent)
    }
}