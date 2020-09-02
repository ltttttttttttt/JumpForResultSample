package com.lt.jumpforresultdemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_b.*

/**
 * creator: lt  2020/9/2  lt.dygzs@qq.com
 * effect :
 * warning:
 */
class BActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b)
        bt.setOnClickListener {
            setResult(RESULT_OK, Intent().apply { putExtra("data", "myData") })
            finish()
        }
    }
}