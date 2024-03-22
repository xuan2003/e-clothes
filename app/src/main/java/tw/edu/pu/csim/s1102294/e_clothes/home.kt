package tw.edu.pu.csim.s1102294.e_clothes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import org.w3c.dom.Text
import tw.edu.pu.csim.s1102294.e_clothes.Match.Match_home

class home : AppCompatActivity() {
    lateinit var Match: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val userUid = intent.getStringExtra("userUid")

        Match = findViewById(R.id.Match)
        Match.setOnClickListener {
//            Match.text = ""
            val intent1 = Intent(this, Match_home::class.java)
            if (userUid != null) {
                intent1.putExtra("userUid", userUid)
            }
            startActivity(intent1)
            finish()
        }
    }
}