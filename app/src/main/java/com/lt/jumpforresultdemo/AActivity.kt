package com.lt.jumpforresultdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lt.jumpforresultdemo.util.AppManager
import com.lt.jumpforresultdemo.util.ResultCallbackFragment
import com.lt.jumpforresultdemo.util.e
import com.lt.jumpforresultdemo.util.jumpForResult
import kotlinx.android.synthetic.main.activity_main.*

class AActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        "onCreate".e()
        AppManager.addActivity(this)
        setContentView(R.layout.activity_main)

        //todo 如果需要测试A页面被系统自动回收,可以在开发者选项中打开'不保留活动'
        tvTime.text = "onCreate时间戳:${System.currentTimeMillis()}"
        bt.setOnClickListener {
            jumpForResult<AActivity, BActivity> {
                val data = it?.getStringExtra("data")
                tvData.text = "接收数据:$data"

                tvData.post {
                    //post是因为回调是函数栈内同步调用,而移除fragment也是走的handler message,所以需要等待message在handler中执行完成
                    val field = ResultCallbackFragment::class.java.getDeclaredField("map")
                    field.isAccessible = true
                    "jumpForResult返回成功,数据=$data,当前页面存活fragment数量=${supportFragmentManager.fragments.size},static中被引用的回调数量=${(field.get(null) as Map<*, *>).size}".e()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        "onDestroy".e()
        AppManager.removeActivity(this)
    }
}