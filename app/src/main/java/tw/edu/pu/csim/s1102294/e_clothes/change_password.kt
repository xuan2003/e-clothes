package tw.edu.pu.csim.s1102294.e_clothes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import tw.edu.pu.csim.s1102294.e_clothes.Match.edit_Profile

class change_password : AppCompatActivity() {
    lateinit var btn_submit: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        btn_submit = findViewById(R.id.btn_submit)
        btn_submit.setOnClickListener {
            Toast.makeText(this, "更改成功!", Toast.LENGTH_SHORT).show()
            val intent2 = Intent(this, edit_Profile::class.java)
            startActivity(intent2)
            finish()
        }
    }
}