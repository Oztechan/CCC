package mustafaozhan.github.com.mycurrencies.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.model.ResponseAll
import mustafaozhan.github.com.mycurrencies.rest.ApiClient
import mustafaozhan.github.com.mycurrencies.rest.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService = ApiClient.get().create(ApiInterface::class.java)

        val myCall = apiService.getByBase("EUR")
        myCall.enqueue(object : Callback<ResponseAll> {
            override fun onResponse(call: Call<ResponseAll>?, response: Response<ResponseAll>?) {

                Log.d("1 Euro ", "${response!!.body()!!.rates!!.tRY.toString()} Turkish Lira")
            }

            override fun onFailure(call: Call<ResponseAll>?, t: Throwable?) {

            }

        })


    }
}