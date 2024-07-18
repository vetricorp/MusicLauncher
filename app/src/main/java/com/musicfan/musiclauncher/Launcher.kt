package com.musicfan.musiclauncher

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.Explode
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import com.musicfan.musiclauncher.adaptor.AppAdaptor
import com.musicfan.musiclauncher.databinding.ActivityLauncherBinding


class Launcher : AppCompatActivity() {

    private lateinit var mDetector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(" Log package ", "Setup apps")
        with(window) {
            requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
            clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            exitTransition = Explode()
        }

        val binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val installedAppsRecyclerView = binding.installedAppsRecyclerView
        installedAppsRecyclerView.adapter = AppAdaptor(this)
        installedAppsRecyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(
            this,
            3,
            androidx.recyclerview.widget.GridLayoutManager.VERTICAL,
            false
        )

        val context: Context = this
        val listener: SimpleOnGestureListener = object : SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                Log.d("On double tap", "onDoubleTapEvent: $e")
                val i = Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN")
                i.putExtra("android.intent.extra.KEY_CONFIRM", true)
                startActivity(i)
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                Toast.makeText(context, "onLongPress", Toast.LENGTH_SHORT).show()
            }
        }

        val detector = GestureDetector(listener)

        detector.setOnDoubleTapListener(listener)
        detector.setIsLongpressEnabled(true)

        window.decorView.setOnTouchListener { _, event -> detector.onTouchEvent(event) }
    }
}