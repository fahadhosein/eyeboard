// Tencent is pleased to support the open source community by making ncnn available.
//
// Copyright (C) 2021 THL A29 Limited, a Tencent company. All rights reserved.
//
// Licensed under the BSD 3-Clause License (the "License"); you may not use this file except
// in compliance with the License. You may obtain a copy of the License at
//
// https://opensource.org/licenses/BSD-3-Clause
//
// Unless required by applicable law or agreed to in writing, software distributed
// under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
// CONDITIONS OF ANY KIND, either express or implied. See the License for the
// specific language governing permissions and limitations under the License.

// Sources:
// https://github.com/Tencent/ncnn
// https://github.com/nihui/ncnn-android-nanodet
// https://github.com/FeiGeChuanShu/ncnn-android-yolov8

package com.ecng.eyeboard

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity(), SurfaceHolder.Callback {
    private val yolo = YOLO()
    private lateinit var canvasActivity: CanvasActivity
    private lateinit var count: TextView
    private var clearBtn: Button? = null
    private var canvasLayout: RelativeLayout? = null
    private var spinnerMod: Spinner? = null
    private var spinnerCurMod = 0
    private var spinnerPro: Spinner? = null
    private var spinnerCurPro = 0
    private var cameraMod: SurfaceView? = null
    private var cameraCurMod = 0
    private var increment: Button? = null
    private var decrement: Button? = null
    var n = 1;

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        cameraMod = findViewById<View>(R.id.cameraView) as SurfaceView
        cameraMod!!.holder.setFormat(PixelFormat.RGBA_8888)
        cameraMod!!.holder.addCallback(this)

        canvasLayout = findViewById(R.id.canvasView)
        canvasActivity = CanvasActivity(this)
        canvasLayout!!.addView(canvasActivity)

        clearBtn = findViewById(R.id.clearBtn)
        clearBtn!!.setOnClickListener {
            finish()
            startActivity(intent)
        }

        spinnerPro = findViewById<View>(R.id.spinnerProcess) as Spinner
        spinnerPro!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                arg0: AdapterView<*>?,
                arg1: View,
                position: Int,
                id: Long
            ) {
                if (position != spinnerCurPro) {
                    spinnerCurPro = position
                    reloadModel()
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }

        spinnerMod = findViewById<View>(R.id.spinnerModel) as Spinner
        spinnerMod!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                arg0: AdapterView<*>?,
                arg1: View,
                position: Int,
                id: Long
            ) {
                if (position != spinnerCurMod) {
                    spinnerCurMod = position
                    reloadModel()
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }

        count = findViewById(R.id.count)
        count.text = n.toString()
        canvasActivity.setN(n)

        increment = findViewById(R.id.incrementBtn)
        increment!!.setOnClickListener{
            if (n>=12)
                n=9
            else
                n++
            count.text = n.toString()
            canvasActivity.setN(n)
        }

        decrement = findViewById(R.id.decrementBtn)
        decrement!!.setOnClickListener{
            if (n<=1)
                n=1
            else
                n--
            count.text = n.toString()
            canvasActivity.setN(n)
        }

        reloadModel()
    }

    private fun reloadModel() {
        val modelInit = yolo.loadModel(assets, spinnerCurMod, spinnerCurPro)
        if (!modelInit) {
            Log.e("MainActivity", "Unable to Load Model!")
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        yolo.setOutputWindow(holder.surface)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {}

    public override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.CAMERA),
                REQUEST_CAMERA
            )
        }
        yolo.openCamera(cameraCurMod)
    }

    public override fun onPause() {
        super.onPause()
        yolo.closeCamera()
    }

    companion object {
        const val REQUEST_CAMERA = 100
    }
}

