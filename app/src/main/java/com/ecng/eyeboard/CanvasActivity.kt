package com.ecng.eyeboard

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.util.*

class CanvasActivity(context: Context?) :
    View(context) {
//    private val timer = Timer()
    private val yolo = YOLO()
    private lateinit var iris: FloatArray
    private var paint: Paint = Paint()
    private var path: Path = Path()
    private var xPos: Float = 0.0f
    private var yPos: Float = 0.0f

    init {
        paint.isAntiAlias = true
        paint.color = Color.GREEN
        paint.strokeJoin = Paint.Join.ROUND
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 20f
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
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        irisPos()
        xPos = iris[0] + 200
        yPos = iris[1] + 400
        Log.d("IRIS", "Coordinates = $xPos, $yPos")

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(xPos, yPos)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(xPos, yPos)
            }
            MotionEvent.ACTION_UP -> {}
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

