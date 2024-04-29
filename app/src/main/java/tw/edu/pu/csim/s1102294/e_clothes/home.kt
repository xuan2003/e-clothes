package tw.edu.pu.csim.s1102294.e_clothes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import tw.edu.pu.csim.s1102294.e_clothes.Match.Match_home

class home : AppCompatActivity() {
    lateinit var Match: TextView
    lateinit var New_Clothes: TextView
    lateinit var Home: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val userUid = intent.getStringExtra("userUid")

        Home = findViewById(R.id.Home)
        Home.setOnClickListener {
//            Home.text = ""
            val intent1 = Intent(this, Match_home::class.java)
            if (userUid != null) {
                intent1.putExtra("userUid", userUid)
            }
            startActivity(intent1)
            finish()
        }

        Match = findViewById(R.id.Match)
        Match.setOnClickListener {
//            Match.text = ""
            val intent2 = Intent(this, Match_home::class.java)
            if (userUid != null) {
                intent2.putExtra("userUid", userUid)
            }
            startActivity(intent2)
            finish()
        }

        New_Clothes = findViewById(R.id.New_clothes)
        New_Clothes.setOnClickListener {
            New_Clothes.text = ""
            val intent3 = Intent(this, New_Clothes::class.java)
            if (userUid != null) {
                intent3.putExtra("userUid", userUid)
            }
            startActivity(intent3)
            finish()
        }
    }
}