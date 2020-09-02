package com.lt.jumpforresultdemo.util

import android.app.Activity

//简化版AppManager
object AppManager {
    private val activityStack: ArrayDeque<Activity> = ArrayDeque()

    /**
     * 加入队列
     */
    fun addActivity(activity: Activity) = activityStack.add(activity)

    /**
     * 获取指定class的
     */
    fun <T : Activity> getActivity(cls: Class<out T>): T? {
        for (value in activityStack) {
            if (value.javaClass == cls) {
                return value as? T
            }
        }
        return null
    }

    fun removeActivity(activity: Activity?): AppManager {
        if (activity != null) {
            activityStack.remove(activity)
        }
        return this
    }
}