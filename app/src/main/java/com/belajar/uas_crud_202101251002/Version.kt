package com.belajar.uas_crud_202101251002

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class Version : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_version)

        val actionbar = supportActionBar
        actionbar!!.title = "Version"
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}