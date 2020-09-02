package com.lt.jumpforresultdemo.util

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.FragmentActivity

/**
 * creator: lt  2020/9/2  lt.dygzs@qq.com
 * effect :
 * warning:
 */

/**
 * 使用callback的方式来执行startActivityForResult方法,就不用来回查找代码了,提高了可读性
 * 更安全的回调(即使下面的activity被回收了也能使重建的activity拿到数据)
 * 注意事项: 如果当前Activity重写了onActivityResult,需要调用super方法
 *          同一个class的activity不能在未回调时再次调用,否则会有回调冲突
 *          无法修改重建后的上层函数中的局部变量
 *
 * @param intent 跳转的intent
 * @param result_ok 是否去判断result_ok,如果是false,就不判断
 * @param callback 成功的回调
 */
fun <T : FragmentActivity> T.jumpForResult(intent: Intent, result_ok: Boolean = true, callback: T.(Intent?) -> Unit) = supportFragmentManager
    .beginTransaction()
    .add(ResultCallbackFragment<T>().setCallbackAndIntent(this, callback, intent, result_ok), ContextConstant.TAG)
    .commitAllowingStateLoss()

/**
 * [T]是自身的泛型,[A]是跳转到的页面
 */
inline fun <T : FragmentActivity, reified A : Activity> T.jumpForResult(initIntent: (intent: Intent) -> Unit = {}, result_ok: Boolean = true, noinline callback: T.(Intent?) -> Unit) =
    jumpForResult(Intent(this, A::class.java).apply(initIntent), result_ok, callback)