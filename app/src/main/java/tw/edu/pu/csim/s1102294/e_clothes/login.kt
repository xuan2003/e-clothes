package tw.edu.pu.csim.s1102294.e_clothes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text
import java.util.*

class login : AppCompatActivity() {

    lateinit var btn_login: Button
    lateinit var txv_register: TextView
    lateinit var txv_forget: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val userUid = intent.getStringExtra("userUid")

        btn_login = findViewById(R.id.btn_login)
        btn_login.setOnClickListener {
            Toast.makeText(this, "登入成功", Toast.LENGTH_SHORT).show()
            val timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    val intent1 = Intent(this@login, home::class.java)
                    if (userUid != null) {
                        intent1.putExtra("userUid", userUid)
                    }
                    startActivity(intent1)
                    finish()
                }
            }, 2000L) //3秒後跳轉頁面
        }
        txv_register = findViewById(R.id.txv_register)
        txv_register.setOnClickListener {
            val intent2 = Intent(this, register::class.java)
            if (userUid != null) {
                intent2.putExtra("userUid", userUid)
            }
            startActivity(intent2)
            finish()
        }

        txv_forget = findViewById(R.id.txv_forget)
        txv_forget.setOnClickListener {
            val intent3 = Intent(this, forget_password::class.java)
            if (userUid != null) {
                intent3.putExtra("userUid", userUid)
            }
            startActivity(intent3)
            finish()
        }
    }

}