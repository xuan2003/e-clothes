package tw.edu.pu.csim.s1102294.e_clothes.Match

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageView
import tw.edu.pu.csim.s1102294.e_clothes.R

class New_Match : AppCompatActivity(), GestureDetector.OnGestureListener {

    lateinit var gDetector: GestureDetector
    lateinit var imghat: ImageView
    lateinit var imgclothes: ImageView
    lateinit var imgpants: ImageView
    lateinit var imgshoes: ImageView
    lateinit var btnDress: Button
    lateinit var next: ImageView
    lateinit var previous: ImageView

    val hat = arrayOf(R.drawable.c, R.drawable.k, R.drawable.l)
    val clothes = arrayOf(R.drawable.a, R.drawable.i, R.drawable.j)
    val pants = arrayOf(R.drawable.b, R.drawable.g, R.drawable.h)
    val shoes = arrayOf(R.drawable.f, R.drawable.d, R.drawable.e)
    var currentImageIndex1 = 0
    var currentImageIndex2 = 0
    var currentImageIndex3 = 0
    var currentImageIndex4 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_match)

        imghat = findViewById(R.id.imghat)
        imgclothes = findViewById(R.id.imgclothes)
        imgpants = findViewById(R.id.imgpants)
        imgshoes = findViewById(R.id.imgshoes)
        btnDress = findViewById(R.id.btnDress)
        next = findViewById(R.id.next)
        previous = findViewById(R.id.previous)

        gDetector = GestureDetector(this, this)
        val userUid = intent.getStringExtra("userUid")

        previous.setOnClickListener {
            val intent1 = Intent(this, new_match_dress::class.java)
            if (userUid != null) {
                intent1.putExtra("userUid", userUid)
            }
            startActivity(intent1)
            finish()
        }

        btnDress.setOnClickListener {
            val intent1 = Intent(this, new_match_dress::class.java)
            if (userUid != null) {
                intent1.putExtra("userUid", userUid)
            }
            startActivity(intent1)
            finish()
        }

        next.setOnClickListener {
            val intent1 = Intent(this, Edit_Label::class.java)
            if (userUid != null) {
                intent1.putExtra("userUid", userUid)
            }
            startActivity(intent1)
            finish()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gDetector.onTouchEvent(event)
        return true
    }

    override fun onDown(p0: MotionEvent): Boolean {
        return true
    }

    override fun onShowPress(p0: MotionEvent) {
    }

    override fun onSingleTapUp(p0: MotionEvent): Boolean {
        return true
    }

    override fun onScroll(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        return true
    }

    override fun onLongPress(p0: MotionEvent) {

    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        val density = Resources.getSystem().displayMetrics.density
        val dpValue = 120 // 要转换为像素的 dp 值
        val pixels = (dpValue * density).toInt()

        if (Math.abs(velocityX) > Math.abs(velocityY) &&  e1.y >= (pixels+imghat.top) && e1.y <= (pixels+imghat.bottom)) {
            //Math.abs(velocityX) > Math.abs(velocityY)判斷是水平滑動還是垂直
            if (e1.x >= e2.x) {
                currentImageIndex1++
                if (currentImageIndex1 >= hat.size) {
                    currentImageIndex1 = 0
                }
            } else {
                currentImageIndex1--
                if (currentImageIndex1 < 0) {
                    currentImageIndex1 = hat.size - 1
                }
            }
        }else if(Math.abs(velocityX) > Math.abs(velocityY) &&  e1.y >= (pixels+imgclothes.top) && e1.y <= (pixels+imgclothes.bottom)) {
            if (e1.x >= e2.x) {
                currentImageIndex2++
                if (currentImageIndex2 >= clothes.size) {
                    currentImageIndex2 = 0
                }
            } else {
                currentImageIndex2--
                if (currentImageIndex2 < 0) {
                    currentImageIndex2 = clothes.size - 1
                }
            }
        }else if(Math.abs(velocityX) > Math.abs(velocityY) &&  e1.y >= (pixels+imgpants.top) && e1.y <= (pixels+imgpants.bottom)) {
            if (e1.x >= e2.x) {
                currentImageIndex3++
                if (currentImageIndex3 >= pants.size) {
                    currentImageIndex3 = 0
                }
            } else {
                currentImageIndex3--
                if (currentImageIndex3 < 0) {
                    currentImageIndex3 = pants.size - 1
                }
            }
        }else if(Math.abs(velocityX) > Math.abs(velocityY) &&  e1.y >= (pixels+imgshoes.top) && e1.y <= (pixels+imgshoes.bottom)) {
            if (e1.x >= e2.x) {
                currentImageIndex4++
                if (currentImageIndex4 >= shoes.size) {
                    currentImageIndex4 = 0
                }
            } else {
                currentImageIndex4--
                if (currentImageIndex4 < 0) {
                    currentImageIndex4 = shoes.size - 1
                }
            }
        }
        imghat.setImageResource(hat[currentImageIndex1])
        imgclothes.setImageResource(clothes[currentImageIndex2])
        imgpants.setImageResource(pants[currentImageIndex3])
        imgshoes.setImageResource(shoes[currentImageIndex4])
        return true
    }

}