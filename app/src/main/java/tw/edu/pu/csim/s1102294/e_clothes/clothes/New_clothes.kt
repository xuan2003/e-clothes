package tw.edu.pu.csim.s1102294.e_clothes.clothes

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.marginLeft
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
    lateinit var Classification_name: TextView
    lateinit var add_label: ImageView
    lateinit var label: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_clothes)


        clothes = findViewById(R.id.clothes)

        val imageBitmap =
            intent.getParcelableExtra<Bitmap>("capturedPhoto") // 从 Intent 中获取 Bitmap 对象

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
        Classification_name = findViewById(R.id.Classification_name)

        val Classification: ImageView = findViewById(R.id.Classification)
        Classification.setOnClickListener { view ->
            showPopupMenu(view)
        }
        label = findViewById(R.id.label)
        add_label = findViewById<ImageView>(R.id.add_label)
        val label_layout = findViewById<LinearLayout>(R.id.label_layout) // 确保有一个父布局来容纳新按钮
        add_label.setOnClickListener {
            // 创建新的 Button
            val newtxv = TextView(this)
            newtxv.id = View.generateViewId() // 生成唯一的 ID
            newtxv.setPadding(10, 10, 10, 10)
            newtxv.text = label.text
            newtxv.textSize = 20f
            newtxv.setTextColor(Color.parseColor("#000000"))

            // 添加 Button 到根布局中
            label_layout.addView(newtxv)
//            Classification_name.text = "1"
        }
    }
    private fun showPopupMenu(view: View) {
        // 创建 PopupMenu 实例
        val popupMenu = PopupMenu(this, view)

        // 加载菜单资源
        popupMenu.menuInflater.inflate(R.menu.classification_menu, popupMenu.menu)

        // 设置菜单项点击监听器
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.hat -> {
                    Classification_name.text = "帽子"
                    true
                }
                R.id.hair_ornaments -> {
                    Classification_name.text = "髪飾"
                    true
                }
                R.id.clothes -> {
                    Classification_name.text = "上衣"
                    true
                }
                R.id.pants -> {
                    Classification_name.text = "褲子"
                    true
                }
                R.id.shoes -> {
                    Classification_name.text = "鞋子"
                    true
                }
                R.id.dress -> {
                    Classification_name.text = "洋裝"
                    true
                }
                else -> false
            }
        }

        // 显示弹出菜单
        popupMenu.show()


    }
}