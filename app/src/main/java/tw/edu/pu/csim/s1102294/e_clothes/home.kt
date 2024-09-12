package tw.edu.pu.csim.s1102294.e_clothes

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
                    // Log the response body to debug
                    Log.d("WeatherResponse", "Response Body: ${response.body()}")

                    if (response.isSuccessful) {
                        val weatherResponse = response.body()
                        if (weatherResponse != null) {
                            val sb = StringBuilder()
                            val locations = weatherResponse.records?.location
                            if (locations != null) {
                                locations.forEach { location ->
                                    if (location.locationName == locationCity) {
                                        sb.append("Location: ${location.locationName}\n")
                                        location.weatherElement.forEach { weatherElement ->
                                            sb.append("Element Name: ${weatherElement.elementName}\n")
                                            weatherElement.time.forEach { time ->
                                                val parameterName = time.parameter.parameterName
                                                val parameterUnit = time.parameter.parameterUnit
                                                val startTime = time.startTime
                                                val endTime = time.endTime
                                                sb.append("Start Time: $startTime, End Time: $endTime\n")
                                                sb.append("Parameter Name: $parameterName, Parameter Unit: $parameterUnit\n")
                                            }
                                        }
                                    }
                                }
                            } else {
                                sb.append("No location data available.")
                            }
                            runOnUiThread {
                                textView.text = sb.toString()
                            }
                        } else {
                            runOnUiThread {
                                textView.text = "Weather response body is null."
                            }
                        }
                    } else {
                        runOnUiThread {
                            textView.text = "Error: ${response.errorBody()?.string()}"
                        }
                    }
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    runOnUiThread {
                        textView.text = "Failed to get weather data: ${t.message}"
                    }
                }
            })
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

}