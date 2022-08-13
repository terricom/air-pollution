package com.example.airpollution

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.airpollution.adapter.RecordAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initLayout()
    }

    private fun initLayout() {
        recyclerView.adapter = RecordAdapter()
    }
}