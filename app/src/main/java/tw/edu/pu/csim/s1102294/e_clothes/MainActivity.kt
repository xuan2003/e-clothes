package tw.edu.pu.csim.s1102294.e_clothes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userUid = intent.getStringExtra("userUid")

        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                val intent1 = Intent(this@MainActivity, login::class.java)
                if (userUid != null) {
                    intent1.putExtra("userUid", userUid)
                }
                startActivity(intent1)
                finish()
            }
        }, 5000L) //5秒後跳轉
    }
}