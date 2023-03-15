package com.ecng.eyeboard

import android.content.Context
import android.graphics.*
import android.os.Environment
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.sqrt

class CanvasActivity(context: Context?) : View(context) {
    private val yolo = YOLO()
    private lateinit var iris: FloatArray
    private  val initX = FloatArray(3)
    private  val initY = FloatArray(3)
    private var paint: Paint = Paint()
    private var dot: Paint = Paint()
    private var dot2: Paint = Paint()
    private var text: Paint = Paint()
    private var path: Path = Path()
    private var xPos: Float = 0F
    private var yPos: Float = 0F
    private var xScale: Float = 0F
    private var yScale: Float = 0F
    private var i = 0
    private var n = 0
    private var rnd = 0

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

        text.isAntiAlias = true
        text.color = Color.RED
        text.textSize = 50F

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paint)
        canvas.drawPoint(700F, 500F, dot)
        canvas.drawText("A", 725F, 525F, text)
        canvas.drawPoint(1300F, 500F, dot)
        canvas.drawText("B", 1325F, 525F, text)
        canvas.drawPoint(700F, 1100F, dot)
        canvas.drawText("C", 725F, 1125F, text)
//        canvas.drawPoint(1300F, 1100F, dot2)

        canvas.drawPoint(800F, 600F, dot2)
        canvas.drawPoint(900F, 600F, dot)
        canvas.drawText("2", 925F, 625F, text)
        canvas.drawPoint(1000F, 600F, dot2)
        canvas.drawPoint(1100F, 600F, dot)
        canvas.drawText("3", 1125F, 625F, text)
        canvas.drawPoint(1200F, 600F, dot2)

        canvas.drawPoint(800F, 700F, dot)
        canvas.drawText("6", 825F, 725F, text)
        canvas.drawPoint(900F, 700F, dot2)
        canvas.drawPoint(1000F, 700F, dot2)
        canvas.drawPoint(1100F, 700F, dot2)
        canvas.drawPoint(1200F, 700F, dot)
        canvas.drawText("7", 1225F, 725F, text)

        canvas.drawPoint(800F, 800F, dot2)
        canvas.drawPoint(900F, 800F, dot2)
        canvas.drawPoint(1000F, 800F, dot)
        canvas.drawText("1", 1025F, 825F, text)
        canvas.drawPoint(1100F, 800F, dot2)
        canvas.drawPoint(1200F, 800F, dot2)

        canvas.drawPoint(800F, 900F, dot)
        canvas.drawText("4", 825F, 925F, text)
        canvas.drawPoint(900F, 900F, dot2)
        canvas.drawPoint(1000F, 900F, dot2)
        canvas.drawPoint(1100F, 900F, dot2)
        canvas.drawPoint(1200F, 900F, dot)
        canvas.drawText("5", 1225F, 925F, text)

        canvas.drawPoint(800F, 1000F, dot2)
        canvas.drawPoint(900F, 1000F, dot)
        canvas.drawText("8", 925F, 1025F, text)
        canvas.drawPoint(1000F, 1000F, dot2)
        canvas.drawPoint(1100F, 1000F, dot)
        canvas.drawText("9", 1125F, 1025F, text)
        canvas.drawPoint(1200F, 1000F, dot2)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                iris = yolo.iris()
                if(i<=2)
                {
                    initX[i] = iris[0]
                    initY[i] = iris[1]
                    i++
                    Log.d("IRIS", "Coordinates = " + Arrays.toString(iris))
                } else {
                    xScale = initX[1]/initX[0]
                    yScale = initY[2]/initY[0]
                    xPos = (kotlin.math.abs(iris[0] - initX[0]) * xScale ) + initX[0]
                    yPos = (kotlin.math.abs(iris[1] - initY[0]) * yScale ) + initY[0]
                    Log.d("POINTER", "Coordinates = $xPos, $yPos")
                    Log.d("SCALE", "Values = $xScale, $yScale")
                    path.addCircle(xPos, yPos, 2F, Path.Direction.CW)
                    val format = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm:ss")
                    val data = LocalDateTime.now().format(format).toString() +
                            " | ID= " + rnd.toString() +
                            " | Point= " + n.toString() +
                            " | Accuracy= " + checkPoint(xPos, yPos, n) +
                            "% | Coordinates= [" + String.format("%.2f", xPos) +
                            ", " + String.format("%.2f", yPos) + "]\n"
//                    Log.d("DATA", data)
                    writeData(data, "data.txt")
                }
                return true
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_UP -> {
//                Runtime.getRuntime().exec("adb shell input tap")
            }
            else -> return false
        }
        invalidate()
        return true
    }

    private fun checkPoint(x: Float, y: Float, n: Int): String {
        var accuracy = 0F
        if(n==1)
            accuracy = getDistance(x, y, 1000F, 800F)
        if(n==2)
            accuracy = getDistance(x, y, 900F, 600F)
        if(n==3)
            accuracy = getDistance(x, y, 1100F, 600F)
        if(n==4)
            accuracy = getDistance(x, y, 800F, 900F)
        if(n==5)
            accuracy = getDistance(x, y, 1200F, 900F)
        if(n==6)
            accuracy = getDistance(x, y, 800F, 700F)
        if(n==7)
            accuracy = getDistance(x, y, 1200F, 700F)
        if(n==8)
            accuracy = getDistance(x, y, 900F, 1000F)
        if(n==9)
            accuracy = getDistance(x, y, 1100F, 1000F)
        var accuracyPerc = ((565.685 - accuracy)/565.685) * 100
        return String.format("%.2f", accuracyPerc)
    }

    private fun getDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val dx = x2 - x1
        val dy = y2 - y1
        return sqrt((dx * dx) + (dy * dy))
    }

    private fun writeData(text: String, filename: String) {
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val file = File(directory, filename)
        val fileWriter = FileWriter(file, true)
        fileWriter.write(text)
        Log.d("FILE", "$directory\n $text")
        fileWriter.close()
    }

    fun setN(N: Int) {
        n = N
    }
    fun setRnd(Rnd : Int) {
        rnd = Rnd
    }

    companion object {
        init {
            System.loadLibrary("eyeboard")
        }
    }
}

