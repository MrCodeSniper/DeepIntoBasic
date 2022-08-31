package com.mrcodesniper.deepintobasic

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * 启动模式 SingleTask和SingleInstance点击图标会跳转启动页
 * 1.冷启动打开 与默认相同
 * 2.退到后台点击图标
 *   pause->stop->onSaveInstance->onNewIntent->restart->start->resume
 *
 *   新建其他页面退到后台点击图标
 *   退到后台:新页面不可见 pause->stop->onSaveInstance
 *   点击图标会将启动页放到栈顶如果在栈内还会弹出之前的页面: 新页面destroy销毁 旧页面 onNewIntent->restart->start->resume重新交互
 *
 * 3.退到后台唤醒 与默认相同
 *   新建其他页面退到后台唤醒 还是返回到原页面
 * 4.退出页面 与默认相同
 * 5.旋转屏幕 默认情况下 先销毁再重建会进行状态保存和恢复  与默认相同
 * 6.启动一个新相同页面覆盖
 *  旧页面不可交互 pause
    发现已经在栈顶触发onNewIntent
    页面重新可交互 resume
 * 7.新界面退出回旧界面
 *
 *
 *  startActivityUncheck
 *
 *  isLaunchModeOneOf(LAUNCH_SINGLE_INSTANCE, LAUNCH_SINGLE_TASK)如果启动模式为栈内复用和单栈单实例
 *  final TaskRecord task = reusedActivity.getTaskRecord(); //获取ActivityRecord持有的栈记录
 *  //大多数情况下我们可能准备清空当前task或者回到task的初始状态
 *  final ActivityRecord top = task.performClearTaskForReuseLocked(mStartActivity,mLaunchFlags);
 *  if (top != null) {
 *  if (top.frontOfTask) {  top.getTaskRecord().setIntent(mStartActivity); //设置启动Activity为根Activity
 *  deliverNewIntent(top) ;将会调用该Activity的onNewIntent，一旦调用了mStartActivity，因为我们也设置了SingleTask或者SingleInstance,所以我们每次看到的都是mStartActivity
 *
 *  首页作为启动页面的场景不推荐singleTask 因为每次点击图标会顶到栈顶
 *
 */
class LaunchModeSingleTaskAffinityActivity : AppCompatActivity() {

    fun start(){ //适合启动页单独activity oncreate执行 一定会执行并且不在root finish掉 回到后台
        if (!this.isTaskRoot) { // 如果不是第一次启动 当前类不是该Task的根部，那么就是在其他页面启动 不在最底边
            val intent = intent
            if (intent != null) {
                val action = intent.action
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN == action) { // 当前类是从桌面启动的
                    Log.d(TAG,"$this finish ")
                    finish() // finish掉该类，直接打开该Task中现存的Activity
                    return
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "$this onCreate TaskId:${taskId}")
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.tv_test4).setText("跳转flag")
    }

    /**
     * 清单配置之后触发
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d(TAG,"$this onConfigurationChanged ")
    }

    /**
     * 发生在Stop之后
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG,"$this onSaveInstanceState ")
    }

    /**
     * 发生在onStart之后
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(TAG,"$this onRestoreInstanceState ")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d(TAG,"$this onNewIntent ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG,"$this resume ")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG,"$this onStart ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG,"$this onStop ")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG,"$this onRestart ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG,"$this onPause ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"$this onDestroy ")
    }

    fun test1(view: View) {
        startActivity(Intent(this, LaunchModeActivity::class.java))
    }

    fun test2(view: View) {
        startActivity(Intent(this, LaunchModeSingleTopActivity::class.java))
    }

    fun test3(view: View) {
        startActivity(Intent(this, LaunchModeSingleTaskAffinityActivity::class.java))
    }

    fun test4(view: View) {
        startActivity(Intent(this, LaunchFlagActivity::class.java))
    }

}