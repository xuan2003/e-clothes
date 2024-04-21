package tw.edu.pu.csim.s1102294.e_clothes.Match

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tw.edu.pu.csim.s1102294.e_clothes.R

class Edit_Label : AppCompatActivity() {

    lateinit var finish: ImageView
    lateinit var previous: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_label)

        val userUid = intent.getStringExtra("userUid")
        finish = findViewById(R.id.finish)
        finish.setOnClickListener {
            Toast.makeText(this, "搭配完成", Toast.LENGTH_SHORT).show()

            CoroutineScope(Dispatchers.Main).launch {
                delay(1000)
                val intent1 = Intent(this@Edit_Label, Match_home::class.java)
                if (userUid != null) {
                    intent1.putExtra("userUid", userUid)
                }
                startActivity(intent1)
                finish()
            }
        }
        previous = findViewById(R.id.previous)
        previous.setOnClickListener {
            val intent1 = Intent(this, New_Match::class.java)
            if (userUid != null) {
                intent1.putExtra("userUid", userUid)
            }
            startActivity(intent1)
            finish()
        }
    }
}