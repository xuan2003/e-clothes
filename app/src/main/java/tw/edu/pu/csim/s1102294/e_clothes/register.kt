package tw.edu.pu.csim.s1102294.e_clothes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import java.util.*

class register : AppCompatActivity() {
    lateinit var btn_register: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btn_register = findViewById(R.id.btn_register)
        btn_register.setOnClickListener {

            Toast.makeText(this, "註冊成功", Toast.LENGTH_SHORT).show()
            val timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    val intent1 = Intent(this@register, login::class.java)
                    startActivity(intent1)
                    finish()
                }
            }, 2000L) //3秒後跳轉頁面

        }
    }
}