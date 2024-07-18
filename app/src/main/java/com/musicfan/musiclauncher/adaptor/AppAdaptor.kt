package com.musicfan.musiclauncher.adaptor

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.musicfan.musiclauncher.R
import com.musicfan.musiclauncher.model.AppInfo


class AppAdaptor(private var context: Context) : RecyclerView.Adapter<AppAdaptor.ViewHolder>() {

    final var ALLOWED_APPS = arrayOf("settings", "gallery", "camera", "music", "chrome", "vending", "clock", "video", "maps", "photo", "googleassistant", "googlequicksearchbox", "recorder", "youtube", "calculator");

    private var appsList: ArrayList<AppInfo> = ArrayList()

    init {
        setUpApps()
    }

    private fun setUpApps() {
        Log.i(" Log package ", "Setup apps")
        val pManager = context.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        pManager.getInstalledApplications(PackageManager.GET_META_DATA)
        val allApps = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Log.i(" Log package ", "TIRAMISU")
            pManager.queryIntentActivities(mainIntent, PackageManager.ResolveInfoFlags.of(0L))
        } else {
            Log.i(" Log package ", "NOT TIRAMISU")
            pManager.queryIntentActivities(mainIntent, 0)
        }
        for (ri in allApps) {
            if (ri.activityInfo.packageName == context.packageName) {
                continue
            }

            ALLOWED_APPS.iterator().forEach { app ->
                if (ri.activityInfo.packageName.contains(app)) {
                    Log.i(" Log package ", ri.activityInfo.packageName)
                    val app = AppInfo(
                        ri.loadLabel(pManager).toString(),
                        ri.activityInfo.loadIcon(pManager),
                        ri.activityInfo.packageName
                    )
                    Log.i(" Log package ", app.packageName)
                    appsList.add(app)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //This is what adds the code we've written in here to our target view
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.app_tile, parent, false)
        view.setOnClickListener {
            val textView = it.findViewById<TextView>(R.id.app_name)
            val packageName = textView.tag
            val launchIntent =
                context.packageManager.getLaunchIntentForPackage(packageName.toString())
            context.startActivity(launchIntent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Log.i(" Log package ", "LOLLIPOP")
                launchIntent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
            } else {
                Log.i(" Log package ", "NOT LOLLIPOP")
                launchIntent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appLabel: String = appsList[position].name
        val appPackage = appsList[position].packageName
        val appIcon: Drawable = appsList[position].appIcon
        val textView = holder.textView
        textView.tag = appPackage
        textView.text = appLabel
        val imageView = holder.img
        imageView.setImageDrawable(appIcon)
    }

    override fun getItemCount(): Int {
        return appsList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView
        var img: ImageView

        init {
            textView = itemView.findViewById(R.id.app_name)
            img = itemView.findViewById(R.id.app_icon)
        }
    }

}