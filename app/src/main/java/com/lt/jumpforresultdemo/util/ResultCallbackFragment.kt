package com.lt.jumpforresultdemo.util

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import java.util.*

class ResultCallbackFragment<T : Activity> : Fragment() {
    companion object {
        /**
         * 静态变量暂存回调,防止页面被回收时回调也被回收掉
         */
        @JvmStatic
        private val map = HashMap<Class<out Activity>, Activity.(Intent?) -> Unit>()
    }

    var intent: Intent? = null
    var result_ok = true
    var clazz: Class<out Activity>? = null
    var callback: (T.(Intent?) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true//当设备旋转时，fragment会随托管activity一起销毁并重建。为true可保留fragment
        //如果到onCreate这一步,clazz还是为空,说明是Activity被重建了,这里可以取出保存在savedInstanceState中的clazz
        if (clazz == null) {
            clazz = savedInstanceState?.getSerializable("clazz") as? Class<Activity>
            result_ok = savedInstanceState?.getBoolean("result_ok") ?: true
        }
        if (activity?.isFinishing != false) {
            finishFragment()
            map.remove(clazz)
            return
        }
        //如果intent不为null,说明是创建完成第一次附加到Activity上,这里调用startActivityForResult来调起下一个页面
        if (intent != null)
            startActivityForResult(intent, ContextConstant.START_ACTIVITY_FOR_RESULT_REQUEST_CODE)
        intent = null
    }

    private fun finishFragment() {
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.remove(this)
            ?.commitAllowingStateLoss()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("clazz", clazz)
        outState.putBoolean("result_ok", result_ok)
        super.onSaveInstanceState(outState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        finishFragment()
        val clazz = clazz ?: throw NullPointerException()
        //移除static中的回调,保证有回调时不会内存泄漏
        val mCallback = map.remove(clazz) ?: callback ?: return
        val t = AppManager.getActivity(clazz) as? T ?: return
        if (resultCode == Activity.RESULT_OK && requestCode == ContextConstant.START_ACTIVITY_FOR_RESULT_REQUEST_CODE)
            mCallback(t, data)
        else if (!result_ok && requestCode == ContextConstant.START_ACTIVITY_FOR_RESULT_REQUEST_CODE)
            mCallback(t, data)
    }

    fun setCallbackAndIntent(
        t: T,
        callback: T.(Intent?) -> Unit,
        intent: Intent,
        result_ok: Boolean
    ): ResultCallbackFragment<T> {
        val tClass = t::class.java
        map[tClass] = callback as Activity.(Intent?) -> Unit
        this.callback = callback
        this.clazz = tClass
        this.intent = intent
        this.result_ok = result_ok
        return this
    }
}