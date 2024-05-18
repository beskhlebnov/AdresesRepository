package com.example.adresrepository
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.adresrepository.person.PersonListActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit


class LoginFragment: Fragment() {
    //Объявление переменных
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private val client = OkHttpClient.Builder()
        .connectTimeout(3, TimeUnit.SECONDS)
        .writeTimeout(3, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .build()
    private lateinit var loginButton: Button

    //Инициализация перменных
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.login_fragment, container, false)
        usernameField = v.findViewById(R.id.username)
        passwordField = v.findViewById(R.id.password)
        loginButton = v.findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val future = CallbackFuture(context)
            //Создание тела запроса
            val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("username", usernameField.text.toString())
                .addFormDataPart("password", passwordField.text.toString())
                .build()

            //Создание запроса
            val request = Request.Builder().url("http://192.168.1.4:5000/login").post(body).build()

            //Отправка запроса
            client.newCall(request).enqueue(future)

            //Обработка ответа
            val objectc = JSONObject(future.get()?.body()!!.string())
            val statusa: String = objectc.getString("status")
            when (statusa) {
                "200" -> {auth()}
                "401" -> {
                    Toast.makeText(context, "Неправильные данные!", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(context, "Сервис не доступен повторите попытку позже!", Toast.LENGTH_LONG).show()
                }
            }
        }
        return v
    }

    internal class CallbackFuture(var context: Context?) : CompletableFuture<Response?>(),
        Callback {

        //Действия при ошибке
        override fun onFailure(call: Call, e: IOException) {
            super.obtrudeException(e)
        }

        //Действия при удачной отправке запроса
        override fun onResponse(call: Call, response: Response) {
            super.complete(response)
        }
    }

    //Авторизация
    fun auth(){
        val intent = PersonListActivity.newIntent(context)
        context?.startActivity(intent)
    }
}