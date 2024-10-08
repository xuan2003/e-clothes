package tw.edu.pu.csim.s1102294.e_clothes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                val intent1 = Intent(this@MainActivity, whether_to_login::class.java)
                startActivity(intent1)
                finish()
            }
        }, 3000L) //3秒後跳轉頁面
    }
}