package com.mrcodesniper.deepintobasic

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity


/**
 * 启动模式
 * 1.冷启动打开 与默认相同
 * 2.退到后台点击图标  与默认相同
 *   新建其他页面退到后台点击图标   回到新建页生命周期一样
 * 3.退到后台唤醒  与默认相同
 *   新建其他页面退到后台唤醒  回到新建页生命周期一样
 * 4.退出页面  与默认相同
 * 5.旋转屏幕 默认情况下 先销毁再重建会进行状态保存和恢复  与默认相同
 * 6.启动一个新相同页面覆盖
 *  旧页面不可交互 pause
 *  发现已经在栈顶触发onNewIntent
 *  页面重新可交互 resume
 * 7.新界面退出回旧界面
 *   平常的退出 pause->stop->destroy
 */
class LaunchModeSingleTopActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "$this onCreate ")
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
        startActivity(Intent(this, LaunchModeActivity::class.java))
    }

    fun test2(view: View) {
        startActivity(Intent(this, LaunchModeSingleTopActivity::class.java))
    }

    fun test3(view: View) {
        startActivity(Intent(this, LaunchModeSingleTaskActivity::class.java))
    }

}