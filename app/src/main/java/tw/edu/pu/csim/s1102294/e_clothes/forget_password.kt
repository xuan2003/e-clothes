package tw.edu.pu.csim.s1102294.e_clothes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import java.util.*

class forget_password : AppCompatActivity() {

    lateinit var btn_submit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        btn_submit = findViewById(R.id.btn_submit)
        btn_submit.setOnClickListener {

            Toast.makeText(this, "已寄送新密碼\n請重新登入", Toast.LENGTH_SHORT).show()
            val timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    val intent1 = Intent(this@forget_password, login::class.java)
                    startActivity(intent1)
                    finish()
                }
            }, 2000L) //3秒後跳轉頁面

        }
    }
}