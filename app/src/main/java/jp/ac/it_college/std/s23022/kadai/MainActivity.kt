package jp.ac.it_college.std.s23022.kadai

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val apiKey = "d07028bf961be5ccff1498d9b8bb991f" // OpenWeatherMapのAPIキーをここに入力

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etCityName = findViewById<EditText>(R.id.et_city_name)
        val btnGetWeather = findViewById<Button>(R.id.btn_get_weather)
        val tvWeatherInfo = findViewById<TextView>(R.id.tv_weather_info)

        // Retrofitのセットアップ
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherApi = retrofit.create(WeatherApi::class.java)

        btnGetWeather.setOnClickListener {
            val cityName = etCityName.text.toString()

            if (cityName.isNotEmpty()) {
                val call = weatherApi.getWeatherData(cityName, apiKey)
                call.enqueue(object : Callback<WeatherResponse> {
                    override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                        if (response.isSuccessful) {
                            val weatherResponse = response.body()
                            weatherResponse?.let {
                                val weatherInfo = "都市: $cityName\n" +
                                        "気温: ${it.main.temp}°C\n" +
                                        "湿度: ${it.main.humidity}%\n" +
                                        "天気: ${it.weather[0].description}"
                                tvWeatherInfo.text = weatherInfo
                            }
                        } else {
                            tvWeatherInfo.text = "Error: ${response.message()}"
                        }
                    }

                    override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                        tvWeatherInfo.text = "天気情報の取得に失敗し明日"
                    }
                })
            }
        }
    }
}