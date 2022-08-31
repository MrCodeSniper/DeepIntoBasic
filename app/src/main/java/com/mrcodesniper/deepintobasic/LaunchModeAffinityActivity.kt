package com.mrcodesniper.deepintobasic

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * 启动模式
 * 1.冷启动打开activity create->start->resume
 * 2.退到后台pause->stop>onSaveInstanceState
 * 3.退出页面  pause->stop->destroy
 * 4.后台唤醒 restart->start->resume
 * 5.旋转屏幕 默认情况下 先销毁再重建会进行状态保存和恢复 pause->stop->onSaveInstanceState->destroy->create->start->onRestore->resume
 * 6.启动一个新页面覆盖
 * 上一个界面先在后台停止交互 pre pause
 * 新的页面开始创建直到可以交互 after create start resume
 * 上一个界面再完全不可见并保存状态 pre stop onSaveInstanceState
 * 7.新界面退出回旧界面
 * 同理 新页面先停止交互 after pause
 * 旧页面状态恢复 restart->start->resume
 * 旧页面可交互后 新页面停止和销毁 after stop->destroy
 *
 */
class LaunchModeAffinityActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "$this onCreate TaskId:${taskId}")
        setContentView(R.layout.activity_main)
    }

    /**
     * 清单配置之后触发
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d(TAG, "$this onConfigurationChanged ")
    }

    /**
     * 发生在Stop之后
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "$this onSaveInstanceState ")
    }

    /**
     * 发生在onStart之后
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(TAG, "$this onRestoreInstanceState ")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d(TAG, "$this onNewIntent ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "$this resume ")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "$this onStart ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "$this onStop ")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "$this onRestart ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "$this onPause ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "$this onDestroy ")
    }

    fun test1(view: View) {
        startActivity(Intent(this@LaunchModeAffinityActivity, LaunchModeAffinityActivity::class.java))
    }

    fun test2(view: View) {
        startActivity(Intent(this@LaunchModeAffinityActivity, LaunchModeSingleTopActivity::class.java))
    }

    fun test3(view: View) {
        startActivity(Intent(this@LaunchModeAffinityActivity, LaunchModeSingleTaskActivity::class.java))
    }

    fun test4(view: View) {
        startActivity(Intent(this@LaunchModeAffinityActivity, LaunchFlagActivity::class.java))
    }
}