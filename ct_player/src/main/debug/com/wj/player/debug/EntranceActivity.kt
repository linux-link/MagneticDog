package com.wj.player.debug

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.wj.arch.utils.permission.LivePermission
import com.wj.arch.utils.permission.PermissionResult
import com.wj.player.LocalMediaUtils
import com.wj.player.R

class EntranceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_music_container)
        LivePermission(this).request(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).observe(this, object : Observer<PermissionResult> {
            override fun onChanged(result: PermissionResult) {
                if (result.isAllSuccess) {

                }
            }

        })
    }
}