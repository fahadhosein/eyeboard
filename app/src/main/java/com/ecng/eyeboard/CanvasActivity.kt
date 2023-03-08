package com.ecng.eyeboard

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
import java.lang.Math.abs
import java.util.*


class CanvasActivity(context: Context?) : View(context) {
//    private val timer = Timer()
    private val yolo = YOLO()
    private lateinit var iris: FloatArray
    private var paint: Paint = Paint()
    private var dot: Paint = Paint()
    private var path: Path = Path()
    private var xPos: Float = 0.0f
    private var yPos: Float = 0.0f
    private var i = 0
    private var initX = 0.0F
    private var initY = 0.0F

    init {
        paint.isAntiAlias = true
        paint.color = Color.GREEN
        paint.strokeJoin = Paint.Join.ROUND
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 20F

        dot.isAntiAlias = true
        dot.color = Color.RED
        dot.strokeJoin = Paint.Join.ROUND
        dot.style = Paint.Style.STROKE
        dot.strokeWidth = 30F

    }

    fun irisPos(): FloatArray? {
//        val monitor = object : TimerTask() {
//            override fun run() {
                iris = yolo.iris()
                Log.d("IRIS", "Coordinates =" + Arrays.toString(iris))
//            }
//        }
//        timer.schedule(monitor, 100, 100)
        return iris;
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paint)
        canvas.drawPoint(600F, 500F, dot)
        canvas.drawPoint(1300F, 500F, dot)
        canvas.drawPoint(600F, 1100F, dot)
        canvas.drawPoint(1300F, 1100F, dot)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        irisPos()
        if(i==0)
        {
            initX = iris[0]
            initY = iris[1]
            i++

        } else {
            xPos = (kotlin.math.abs(iris[0] - initX) * 2.5F ) + initX
            yPos = (kotlin.math.abs(iris[1] - initY) * 4.5F ) + initY
            Log.d("POINTER", "Coordinates = $xPos, $yPos")
        }

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(xPos, yPos)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(xPos, yPos)
            }
            MotionEvent.ACTION_UP -> {
                Runtime.getRuntime().exec("adb shell input tap")
            }
            else -> return false
        }
        invalidate()
        return true
    }

    companion object {
        init {
            System.loadLibrary("eyeboard")
        }
    }
}

