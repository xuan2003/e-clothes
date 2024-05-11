package tw.edu.pu.csim.s1102294.e_clothes.clothes

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tw.edu.pu.csim.s1102294.e_clothes.Match.Match_home
import tw.edu.pu.csim.s1102294.e_clothes.Match.New_Match
import tw.edu.pu.csim.s1102294.e_clothes.R
import tw.edu.pu.csim.s1102294.e_clothes.home

class New_clothes : AppCompatActivity() {

    lateinit var finish: ImageView
    lateinit var previous: ImageView
    lateinit var clothes: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_clothes)


        clothes = findViewById(R.id.clothes)

        val imageBitmap = intent.getParcelableExtra<Bitmap>("capturedPhoto") // 从 Intent 中获取 Bitmap 对象

        clothes.setImageBitmap(imageBitmap)

        finish = findViewById(R.id.finish)
        finish.setOnClickListener {
            Toast.makeText(this, "搭配完成", Toast.LENGTH_SHORT).show()
            val intent1 = Intent(this, home::class.java)
            startActivity(intent1)
            finish()

        }
        previous = findViewById(R.id.previous)
        previous.setOnClickListener {
            val intent1 = Intent(this, New_Match::class.java)
            startActivity(intent1)
            finish()
        }
    }
}