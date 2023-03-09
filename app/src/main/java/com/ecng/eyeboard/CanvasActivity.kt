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
    private  val initX = FloatArray(7)
    private  val initY = FloatArray(7)
    private var paint: Paint = Paint()
    private var dot: Paint = Paint()
    private var dot2: Paint = Paint()
    private var path: Path = Path()
    private var xPos: Float = 0.0f
    private var yPos: Float = 0.0f
    private var xScale: Float = 0.0f
    private var yScale: Float = 0.0f
    private var i = 0

    init {
        paint.isAntiAlias = true
        paint.color = Color.GREEN
        paint.strokeJoin = Paint.Join.ROUND
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10F

        dot.isAntiAlias = true
        dot.color = Color.RED
        dot.strokeJoin = Paint.Join.ROUND
        dot.style = Paint.Style.STROKE
        dot.strokeWidth = 20F

        dot2.isAntiAlias = true
        dot2.color = Color.CYAN
        dot2.strokeJoin = Paint.Join.ROUND
        dot2.style = Paint.Style.STROKE
        dot2.strokeWidth = 20F

    }

    private fun irisPos(): FloatArray? {
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

        canvas.drawPoint(700F, 600F, dot2)
        canvas.drawPoint(800F, 600F, dot2)
        canvas.drawPoint(900F, 600F, dot2)
        canvas.drawPoint(1000F, 600F, dot2)
        canvas.drawPoint(1100F, 600F, dot2)
        canvas.drawPoint(1200F, 600F, dot2)

        canvas.drawPoint(700F, 700F, dot2)
        canvas.drawPoint(800F, 700F, dot2)
        canvas.drawPoint(900F, 700F, dot2)
        canvas.drawPoint(1000F, 700F, dot2)
        canvas.drawPoint(1100F, 700F, dot2)
        canvas.drawPoint(1200F, 700F, dot2)

        canvas.drawPoint(700F, 800F, dot2)
        canvas.drawPoint(800F, 800F, dot2)
        canvas.drawPoint(900F, 800F, dot2)
        canvas.drawPoint(1000F, 800F, dot2)
        canvas.drawPoint(1100F, 800F, dot2)
        canvas.drawPoint(1200F, 800F, dot2)

        canvas.drawPoint(700F, 900F, dot2)
        canvas.drawPoint(800F, 900F, dot2)
        canvas.drawPoint(900F, 900F, dot2)
        canvas.drawPoint(1000F, 900F, dot2)
        canvas.drawPoint(1100F, 900F, dot2)
        canvas.drawPoint(1200F, 900F, dot2)

        canvas.drawPoint(700F, 1000F, dot2)
        canvas.drawPoint(800F, 1000F, dot2)
        canvas.drawPoint(900F, 1000F, dot2)
        canvas.drawPoint(1000F, 1000F, dot2)
        canvas.drawPoint(1100F, 1000F, dot2)
        canvas.drawPoint(1200F, 1000F, dot2)

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        irisPos()
        if(i<7)
        {
            initX[i] = iris[0]
            initY[i] = iris[1]
            i++
        } else {
            xScale = initX[3]/initX[0]
            yScale = initY[6]/initY[0]
            xPos = (kotlin.math.abs(iris[0] - initX[0]) * xScale ) + initX[0]
            yPos = (kotlin.math.abs(iris[1] - initY[0]) * yScale ) + initY[0]
            Log.d("POINTER", "Coordinates = $xPos, $yPos")
            Log.d("SCALE", "Values = $xScale, $yScale")
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
//                Runtime.getRuntime().exec("adb shell input tap")
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

