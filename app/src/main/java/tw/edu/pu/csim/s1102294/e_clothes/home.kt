package tw.edu.pu.csim.s1102294.e_clothes

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.ScrollingMovementMethod
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tw.edu.pu.csim.s1102294.e_clothes.Community.Friends
import tw.edu.pu.csim.s1102294.e_clothes.Community.Personal_Page
import tw.edu.pu.csim.s1102294.e_clothes.Match.Match_home
import tw.edu.pu.csim.s1102294.e_clothes.clothes.New_clothes
import tw.edu.pu.csim.s1102294.e_clothes.weather.RetrofitClient
import tw.edu.pu.csim.s1102294.e_clothes.weather.WeatherResponse
import tw.edu.pu.csim.s1102294.e_clothes.weather.WeatherService
import android.util.Log
import android.view.View
import android.widget.Button
import java.text.SimpleDateFormat
import java.util.*


class home : AppCompatActivity() {
    lateinit var Match: ImageView
    lateinit var Home: ImageView
    lateinit var Friend: ImageView
    lateinit var Clothes: ImageView
    lateinit var Personal_page: ImageView

    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private var locationManager: LocationManager? = null
    private lateinit var weatherService: WeatherService
    lateinit var textView: TextView

    lateinit var today_morning_time: TextView
    lateinit var today_morning_weather: ImageView
    lateinit var today_morning_temperature: TextView

    lateinit var today_night_time: TextView
    lateinit var today_night_weather: ImageView
    lateinit var today_night_temperature: TextView

    lateinit var tomorrow_morning_time: TextView
    lateinit var tomorrow_morning_weather: ImageView
    lateinit var tomorrow_morning_temperature: TextView

    lateinit var tomorrow_night_time: TextView
    lateinit var tomorrow_night_weather: ImageView
    lateinit var tomorrow_night_temperature: TextView

    private val takePictureResult =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            val intent = Intent(this, New_clothes::class.java)
            intent.putExtra("capturedPhoto", bitmap) // 传递 Bitmap 对象而不是资源标识符
            startActivity(intent)
            finish()
        }

    private val permissionsResultCallback = registerForActivityResult(
        ActivityResultContracts.RequestPermission()){
        when (it) {
            true -> {
//                txv.text = "您允許拍照權限，歡迎使用拍照功能！"
                takePictureResult.launch(null)
            }
            false -> {
//                txv.text = "抱歉，您尚未允許拍照權限，無法使用相機功能。"
            }
        }


    }


    private val handler = Handler(Looper.getMainLooper())
    private lateinit var locationCity: String
    private val updateWeatherRunnable = object : Runnable {
        override fun run() {
            getWeather(locationCity)
            handler.postDelayed(this, 3600000) // 1 hour
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        Home = findViewById(R.id.Home)
        Home.setOnClickListener {
//            Home.text = ""
            val intent1 = Intent(this, home::class.java)
            startActivity(intent1)
            finish()
        }

        Match = findViewById(R.id.Match)
        Match.setOnClickListener {
//            Match.text = ""
            val intent2 = Intent(this, Match_home::class.java)
            startActivity(intent2)
            finish()
        }

        Clothes = findViewById(R.id.Clothes)
        Clothes.setOnClickListener {
//            textView9.text = "123"
            checkPermission()
        }

        Friend = findViewById(R.id.Friend)
        Friend.setOnClickListener {
            val intent1 = Intent(this, Friends::class.java)
            startActivity(intent1)
            finish()
        }

        Personal_page = findViewById(R.id.Personal_page)
        Personal_page.setOnClickListener {
            val intent1 = Intent(this, Personal_Page::class.java)
            startActivity(intent1)
            finish()
        }
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        textView = findViewById(R.id.textView)
        textView.setMovementMethod(ScrollingMovementMethod.getInstance())

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            startLocationUpdates()
        }

        today_morning_time = findViewById(R.id.today_morning_time)
        today_morning_weather = findViewById(R.id.today_morning_weather)
        today_morning_temperature = findViewById(R.id.today_morning_temperature)

        today_night_time = findViewById(R.id.today_night_time)
        today_night_weather = findViewById(R.id.today_night_weather)
        today_night_temperature = findViewById(R.id.today_night_temperature)

        tomorrow_morning_time = findViewById(R.id.tomorrow_morning_time)
        tomorrow_morning_weather = findViewById(R.id.tomorrow_morning_weather)
        tomorrow_morning_temperature = findViewById(R.id.tomorrow_morning_temperature)

        tomorrow_night_time = findViewById(R.id.tomorrow_night_time)
        tomorrow_night_weather = findViewById(R.id.tomorrow_night_weather)
        tomorrow_night_temperature = findViewById(R.id.tomorrow_night_temperature)

        weatherService = RetrofitClient.myWeatherApi().create(WeatherService::class.java)
    }

    private fun startLocationUpdates() {
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                showLocation(location)
            }
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000,
            1f,
            locationListener
        )
    }

    private fun showLocation(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude

        data class CityRange(val name: String, val minLatitude: Double, val maxLatitude: Double, val minLongitude: Double, val maxLongitude: Double)

        val cityRanges = listOf(
            CityRange("臺北市", 25.0, 25.2, 121.5, 121.6),
            CityRange("新北市", 24.8, 25.3, 121.3, 122.0),
            CityRange("桃園市", 24.7, 25.1, 121.0, 121.4),
            CityRange("臺中市", 24.0, 24.4, 120.5, 121.0),
            CityRange("臺南市", 22.9, 23.1, 120.0, 120.3),
            CityRange("高雄市", 22.5, 23.0, 120.2, 120.5),
            CityRange("基隆市", 25.1, 25.2, 121.7, 121.8),
            CityRange("新竹市", 24.7, 24.9, 120.9, 121.1),
            CityRange("嘉義市", 23.4, 23.5, 120.4, 120.5)
        )

        val city = cityRanges.find {
            latitude in it.minLatitude..it.maxLatitude && longitude in it.minLongitude..it.maxLongitude
        }?.name ?: "未知地點"

        if (city == "未知地點") {
            textView.text = "緯度: $latitude, 經度: $longitude"
        } else {
            textView.text = city
            getWeather(city)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            }
        }
    }

    private fun getWeather(locationCity: String) {
        val authorization = "CWA-A017743C-C744-4C39-9D6B-4CA2D7E6E086"
        weatherService.getWeatherApi(authorization, locationCity)
            .enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    if (response.isSuccessful) {
                        val weatherResponse = response.body()
                        if (weatherResponse != null) {
                            val locations = weatherResponse.records?.location
                            locations?.forEach { location ->
                                if (location.locationName == locationCity) {
                                    // Process weather elements
                                    location.weatherElement.forEach { weatherElement ->
                                        weatherElement.time.any {
                                            it.startTime.substring(11, 13).toInt() == 6
                                        }
                                        weatherElement.time.forEach { time ->
                                            val startTime = time.startTime
                                            val weatherCondition = time.parameter.parameterName
                                            val temperature = time.parameter.parameterName
//                                            val startHour = startTime.substring(11, 13).toInt()


                                            // Log for debugging
                                            Log.d("WeatherDebug", "Start Time: $startTime")

                                            // Handle today's 06:00 weather
                                            if (startTime.startsWith(getCurrentDate()) && startTime.substring(11, 16) == "06:00") {
//                                                today_morning_time.text = "123"
                                                today_morning_time.text = "Today 6 AM: ${startTime.substring(0, 16)}"
                                                today_morning_temperature.text = "Temperature: $temperature ˚C"
                                                setWeatherImage(today_morning_weather, weatherCondition)
                                            }

                                            if (startTime.substring(0, 10) == getCurrentDate() && startTime.substring(11, 16) == "18:00") {
                                                today_night_time.text = "${startTime.substring(0, 16)}"
                                                today_night_temperature.text = "$temperature ˚C"
                                                setWeatherImage(today_night_weather, weatherCondition)
                                            }

                                            if (startTime.substring(0, 10) == getNextDate() && startTime.substring(11, 16) == "06:00") {
                                                tomorrow_morning_time.text = "${startTime.substring(0, 16)}"
                                                tomorrow_morning_temperature.text = "$temperature ˚C"
                                                setWeatherImage(tomorrow_morning_weather, weatherCondition)
                                            }
                                            if (startTime.substring(0, 10) == getNextDate() && startTime.substring(11, 16) == "18:00") {
                                                tomorrow_night_time.text = "${startTime.substring(0, 16)}"
                                                tomorrow_night_temperature.text = "$temperature ˚C"
                                                setWeatherImage(tomorrow_night_weather, weatherCondition)
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    } else {
                        Log.e("WeatherError", "Response not successful: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    Log.e("WeatherError", "Failed to get weather data: ${t.message}")
                }
            })
    }


    private fun setWeatherImage(imageView: ImageView, weatherCondition: String) {
        when (weatherCondition) {
            "多雲","陰天","多雲時陰有霧","多雲時陰晨霧","陰時多雲有霧","陰時多雲晨霧" -> imageView.setImageResource(R.drawable.cloudy)
            "晴時多雲","多雲時晴","多雲時陰","陰時多雲","晴時多雲有霧","晴時多雲晨霧","多雲時晴有霧","多雲時晴晨霧" -> imageView.setImageResource(R.drawable.cloudy_and_sunny)
            "多雲有霧","多雲晨霧","有霧","晨霧","陰有霧","陰晨霧" -> imageView.setImageResource(R.drawable.fog)
            "雨天","晴午後陰短暫雨","晴午後陰短暫陣雨","陰短暫雨","陰短暫陣雨","陰午後短暫陣雨","多雲時陰有雨","多雲時陰陣雨","晴時多雲陣雨","多雲時晴陣雨","陰時多雲有雨","陰時多雲有陣雨","陰時多雲陣雨","陰有雨","陰有陣雨",
            "陰雨","陰陣雨","陣雨","午後陣雨","有雨","多雲局部雨或雪有霧","多雲時陰局部雨或雪有霧","陰時多雲局部雨或雪有霧","陰局部雨或雪有霧","短暫雨或雪有霧","有雨或雪有霧","短暫陣雨有霧","短暫陣雨晨霧","短暫雨有霧",
            "短暫雨晨霧","有雨有霧","陣雨有霧","短暫陣雨或雷雨有霧","陣雨或雷雨有霧"-> imageView.setImageResource(R.drawable.raining)
            "多雲陣雨","多雲短暫雨","多雲短暫陣雨","午後短暫陣雨","短暫陣雨","多雲時晴短暫陣雨","多雲時晴短暫雨","晴時多雲短暫陣雨","晴短暫陣雨","短暫雨","多雲時陰短暫雨","多雲時陰短暫陣雨","陰時多雲短暫雨","陰時多雲短暫陣雨",
            "晴午後多雲局部雨","晴午後多雲局部陣雨","晴午後多雲局部短暫雨","晴午後多雲局部短暫陣雨","晴午後多雲短暫雨","晴午後多雲短暫陣雨","晴午後局部雨","晴午後局部陣雨","晴午後局部短暫雨","晴午後局部短暫陣雨","晴午後陣雨",
            "晴午後短暫雨","晴午後短暫陣雨","晴時多雲午後短暫陣雨","多雲午後局部雨","多雲午後局部陣雨","多雲午後局部短暫雨","多雲午後局部短暫陣雨","多雲午後陣雨","多雲午後短暫雨","多雲午後短暫陣雨","多雲時陰午後短暫陣雨",
            "陰時多雲午後短暫陣雨","多雲時晴午後短暫陣雨","多雲局部雨","多雲局部陣雨","多雲局部短暫雨","多雲局部短暫陣雨","多雲時陰局部雨","多雲時陰局部陣雨","多雲時陰局部短暫雨","多雲時陰局部短暫陣雨","晴午後陰局部雨",
            "晴午後陰局部陣雨","晴午後陰局部短暫雨","晴午後陰局部短暫陣雨","陰局部雨","陰局部陣雨","陰局部短暫雨","陰局部短暫陣雨","陰時多雲局部雨","陰時多雲局部陣雨","陰時多雲局部短暫雨","陰時多雲局部短暫陣雨","多雲有霧有局部雨",
            "多雲有霧有局部陣雨","多雲有霧有局部短暫雨","多雲有霧有局部短暫陣雨","多雲有霧有陣雨","多雲有霧有短暫雨","多雲有霧有短暫陣雨","多雲局部雨有霧","多雲局部雨晨霧","多雲局部陣雨有霧","多雲局部陣雨晨霧","多雲局部短暫雨有霧",
            "多雲局部短暫雨晨霧","多雲局部短暫陣雨有霧","多雲局部短暫陣雨晨霧","多雲陣雨有霧","多雲短暫雨有霧","多雲短暫雨晨霧","多雲短暫陣雨有霧","多雲短暫陣雨晨霧","有霧有短暫雨","有霧有短暫陣雨","多雲時陰有霧有局部雨","多雲時陰有霧有局部陣雨",
            "多雲時陰有霧有局部短暫雨","多雲時陰有霧有局部短暫陣雨","多雲時陰有霧有陣雨","多雲時陰有霧有短暫雨","多雲時陰有霧有短暫陣雨","多雲時陰局部雨有霧","多雲時陰局部陣雨有霧","多雲時陰局部短暫雨有霧","多雲時陰局部短暫陣雨有霧","多雲時陰陣雨有霧",
            "多雲時陰短暫雨有霧","多雲時陰短暫雨晨霧","多雲時陰短暫陣雨有霧","多雲時陰短暫陣雨晨霧","陰有霧有陣雨","陰局部雨有霧","陰局部陣雨有霧","陰局部短暫陣雨有霧","陰時多雲有霧有局部雨","陰時多雲有霧有局部陣雨","陰時多雲有霧有局部短暫雨",
            "陰時多雲有霧有局部短暫陣雨","陰時多雲有霧有陣雨","陰時多雲有霧有短暫雨","陰時多雲有霧有短暫陣雨","陰時多雲局部雨有霧","陰時多雲局部陣雨有霧","陰時多雲局部短暫雨有霧","陰時多雲局部短暫陣雨有霧","陰時多雲陣雨有霧","陰時多雲短暫雨有霧",
            "陰時多雲短暫雨晨霧","陰時多雲短暫陣雨有霧","陰時多雲短暫陣雨晨霧","陰陣雨有霧","陰短暫雨有霧","陰短暫雨晨霧","陰短暫陣雨有霧","陰短暫陣雨晨霧"-> imageView.setImageResource(R.drawable.shower)
            "多雲局部陣雨或雪","多雲時陰有雨或雪","多雲時陰短暫雨或雪","多雲短暫雨或雪","陰有雨或雪","陰時多雲有雨或雪","陰時多雲短暫雨或雪","陰短暫雨或雪","多雲時陰有雪","多雲時陰短暫雪","多雲短暫雪","陰有雪","陰時多雲有雪","陰時多雲短暫雪","陰短暫雪",
            "有雨或雪","有雨或短暫雪","陰有雨或短暫雪","陰時多雲有雨或短暫雪","多雲時陰有雨或短暫雪","多雲有雨或短暫雪","多雲有雨或雪","多雲時晴有雨或雪","晴時多雲有雨或雪","晴有雨或雪","短暫雨或雪","多雲時晴短暫雨或雪","晴短暫雨或雪","晴時多雲短暫雨或雪",
            "有雪","多雲有雪","多雲時晴有雪","晴時多雲有雪","晴有雪","短暫雪","多雲時晴短暫雪","晴時多雲短暫雪","晴短暫雪","下雪","積冰","暴風雪"-> imageView.setImageResource(R.drawable.snow)
            "晴天","晴有霧","晴晨霧" -> imageView.setImageResource(R.drawable.sunny)
            "多雲陣雨或雷雨","多雲短暫陣雨或雷雨","多雲短暫雷陣雨","多雲雷陣雨","短暫陣雨或雷雨後多雲","短暫雷陣雨後多雲","短暫陣雨或雷雨","晴時多雲短暫陣雨或雷雨","晴短暫陣雨或雷雨","多雲時晴短暫陣雨或雷雨","午後短暫雷陣雨","多雲時陰陣雨或雷雨","多雲時陰短暫陣雨或雷雨",
            "多雲時陰短暫雷陣雨","多雲時陰雷陣雨", "晴陣雨或雷雨","晴時多雲陣雨或雷雨","多雲時晴陣雨或雷雨","陰時多雲有雷陣雨","陰時多雲陣雨或雷雨","陰時多雲短暫陣雨或雷雨","陰時多雲短暫雷陣雨","陰時多雲雷陣雨","陰有陣雨或雷雨","陰有雷陣雨","陰陣雨或雷雨",
            "陰雷陣雨","晴午後陰短暫陣雨或雷雨","晴午後陰短暫雷陣雨", "陰短暫陣雨或雷雨","陰短暫雷陣雨","雷雨","陣雨或雷雨後多雲","陰陣雨或雷雨後多雲","陰短暫陣雨或雷雨後多雲","陰短暫雷陣雨後多雲","陰雷陣雨後多雲","雷陣雨後多雲","陣雨或雷雨","雷陣雨","午後雷陣雨",
            "晴午後多雲陣雨或雷雨","晴午後多雲雷陣雨","晴午後陣雨或雷雨","晴午後雷陣雨","晴午後多雲局部陣雨或雷雨","晴午後多雲局部短暫陣雨或雷雨","晴午後多雲局部短暫雷陣雨","晴午後多雲局部雷陣雨","晴午後多雲短暫陣雨或雷雨","晴午後多雲短暫雷陣雨","晴午後局部短暫雷陣雨",
            "晴午後局部雷陣雨","晴午後短暫雷陣雨","晴雷陣雨","晴時多雲雷陣雨","晴時多雲午後短暫雷陣雨","多雲午後局部陣雨或雷雨","多雲午後局部短暫陣雨或雷雨","多雲午後局部短暫雷陣雨","多雲午後局部雷陣雨","多雲午後陣雨或雷雨","多雲午後短暫陣雨或雷雨","多雲午後短暫雷陣雨",
            "多雲午後雷陣雨","多雲時晴雷陣雨","多雲時晴午後短暫雷陣雨","多雲時陰午後短暫雷陣雨","陰時多雲午後短暫雷陣雨","陰午後短暫雷陣雨","多雲局部陣雨或雷雨","多雲局部短暫陣雨或雷雨","多雲局部短暫雷陣雨","多雲局部雷陣雨","多雲時陰局部陣雨或雷雨","多雲時陰局部短暫陣雨或雷雨",
            "多雲時陰局部短暫雷陣雨","多雲時陰局部雷陣雨","晴午後陰局部陣雨或雷雨","晴午後陰局部短暫陣雨或雷雨","晴午後陰局部短暫雷陣雨","晴午後陰局部雷陣雨","陰局部陣雨或雷雨","陰局部短暫陣雨或雷雨","陰局部短暫雷陣雨","陰局部雷陣雨","陰時多雲局部陣雨或雷雨",
            "陰時多雲局部短暫陣雨或雷雨","陰時多雲局部短暫雷陣雨","陰時多雲局部雷陣雨","多雲有陣雨或雷雨有霧","多雲有雷陣雨有霧","多雲有霧有陣雨或雷雨","多雲有霧有雷陣雨","多雲局部陣雨或雷雨有霧","多雲局部短暫陣雨或雷雨有霧","多雲局部短暫雷陣雨有霧",
            "多雲局部雷陣雨有霧","多雲陣雨或雷雨有霧","多雲短暫陣雨或雷雨有霧","多雲短暫雷陣雨有霧","多雲雷陣雨有霧","多雲時晴短暫陣雨或雷雨有霧","多雲時陰有陣雨或雷雨有霧","多雲時陰有雷陣雨有霧","多雲時陰有霧有陣雨或雷雨","多雲時陰有霧有雷陣雨","多雲時陰局部陣雨或雷雨有霧",
            "多雲時陰局部短暫陣雨或雷雨有霧","多雲時陰局部短暫雷陣雨有霧","多雲時陰局部雷陣雨有霧","多雲時陰陣雨或雷雨有霧","多雲時陰短暫陣雨或雷雨有霧","多雲時陰短暫雷陣雨有霧","多雲時陰雷陣雨有霧","陰局部陣雨或雷雨有霧","陰局部短暫陣雨或雷雨有霧","陰局部短暫雷陣雨有霧",
            "陰局部雷陣雨有霧","陰時多雲有陣雨或雷雨有霧","陰時多雲有雷陣雨有霧","陰時多雲有霧有陣雨或雷雨","陰時多雲有霧有雷陣雨","陰時多雲局部陣雨或雷雨有霧","陰時多雲局部短暫陣雨或雷雨有霧","陰時多雲局部短暫雷陣雨有霧","陰時多雲局部雷陣雨有霧","陰時多雲陣雨或雷雨有霧",
            "陰時多雲短暫陣雨或雷雨有霧","陰時多雲短暫雷陣雨有霧","陰時多雲雷陣雨有霧","陰短暫陣雨或雷雨有霧","陰短暫雷陣雨有霧","雷陣雨有霧"-> imageView.setImageResource(R.drawable.thunderstorm)

        }
    }




    private fun checkPermission() {
        val permission = ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            permissionsResultCallback.launch(android.Manifest.permission.CAMERA)
        } else {
//            txv.text = "您先前已允許拍照權限，歡迎使用拍照功能！"
            takePictureResult.launch(null)
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun getNextDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 1)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

}

