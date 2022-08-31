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
 * 针对LaunchFlag的验证 不同的标志位搭配启动模式效果也不同
 * 1.Intent.FLAG_ACTIVITY_CLEAR_TOP(清顶)
 * 默认情况下
 * 通过这种方式来打开栈内已存在的activity会将前面的activity都销毁再重新创建新的展示在栈顶 销毁顺序从下往上
 * 实际测试
 *
 * LaunchFlagActivity@5968e56 栈顶           通过这种方式启动LaunchModeActivity后
 * LaunchFlagActivity@ec70a5                1. LaunchFlagActivity@6edfd9d onDestroy   先将启动的之前的activity 销毁
 * LaunchFlagActivity@6edfd9d               2. LaunchFlagActivity@ec70a5 onDestroy
 * LaunchModeActivity@7be2cab 栈底           3.LaunchFlagActivity@5968e56  栈顶正在交互的activity设置为pause
 *                                          4.LaunchModeActivity@7be2cab onDestroy 栈底的旧activity销毁
 *                                          5.LaunchModeActivity@52ca3b5 onCreate 创建新的activity在栈顶 旧的栈顶activity stop destroy销毁
 *
 * SingleTop情况下
 *
 *
 * LaunchFlagActivity@5968e56 栈顶         通过这种方式启动LaunchModeSingleTopActivity后
 * LaunchFlagActivity@ec70a5               1. LaunchFlagActivity@6edfd9d销毁
 * LaunchFlagActivity@6edfd9d               2.LaunchFlagActivity@ec70a5 销毁
 * LaunchModeSingleTopActivity@7be2cab 栈底  3.LaunchFlagActivity@5968e56  栈顶正在交互的activity设置为pause
 *                                          4.LaunchModeSingleTopActivity@7be2cab onNewIntent 启动activity重新启动交互
 *                                          5.LaunchModeSingleTopActivity@7be2cab onRestart start resume
 *                                          6.LaunchFlagActivity@5968e56 之前交互的activity stop 销毁
 *
 * 2.FLAG_ACTIVITY_NEW_TASK (新task创建)
 *  默认情况下通过flag新建默认activity 不会出现创建新task的情况
 *  LaunchFlagActivity@d638de3 onCreate TaskId:49
 *  LaunchModeActivity@43f476e onCreate TaskId:49
 *
 *  通过flag新建SingleTask activity  还是不会出现创建新task的情况
 *  LaunchFlagActivity@7be2cab onCreate TaskId:51
 *  LaunchModeSingleTaskActivity@2712d5e onCreate TaskId:51
 *
 *  通过flag新建SingleTask activity 设置新的taskaffinity 发现创建了新task 并发生了task切换 新的task正在展示
 *  LaunchFlagActivity@7be2cab onCreate TaskId:55
 *  LaunchModeSingleTaskAffinityActivity@982d65b onCreate TaskId:56
 *
 *  普通新建SingleTask activity 设置新的taskaffinity 发现创建了新task 并发生了task切换 新的task正在展示
 *  LaunchFlagActivity@7be2cab onCreate TaskId:60
 *  LaunchModeSingleTaskAffinityActivity@8e7d37 onCreate TaskId:61
 *
 * 普通新建默认 activity 设置新的taskaffinity 不会出现创建新task的情况
 * LaunchFlagActivity@7be2cab onCreate TaskId:64
 * LaunchModeAffinityActivity@9889809 onCreate TaskId:64
 *
 *  通过flag新建默认 activity 设置新的taskaffinity 发现创建了新task 并发生了task切换 新的task正在展示
 *  LaunchFlagActivity@fd6c830 onCreate TaskId:69
 * LaunchModeAffinityActivity@98ee425 onCreate TaskId:70
 *
 *  通过这些例子我们可以总结出一些事情
 *  要创建新的task 只有几种方式
 *  1。 设置 SingleInstance  TaskAffinity无需设置
 *  2. SingleTask 需要设置taskaffinity才能创建
 *  3.默认模式和singletop模式 需要同时设置TaskAffinity和FLAG_ACTIVITY_NEW_TASK 才能创建新task
 *
 *
 *
 * 3.FLAG_ACTIVITY_CLEAR_TASK 需要配合FLAG_ACTIVITY_NEW_TASK 才生效 清除启动activity关联的栈
 *
 *  1.启动页栈1 LaunchFlagActivity@7be2cab onCreate TaskId:79
 *  2。跳转到栈2  LaunchModeSingleTaskAffinityActivity@bfcece6 onCreate TaskId:80
 *  3.栈2内部跳转默认flag  LaunchFlagActivity@5968e56 onCreate TaskId:80
 *  4.flag跳转FLAG_ACTIVITY_CLEAR_TASK｜FLAG_ACTIVITY_NEW_TASK
 *
 *
 * 4.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS  将Activity从Recents中排除 任务管理器
 * 5.FLAG_ACTIVITY_REORDER_TO_FRONT 将已存在的activity从后台返回前台 会经过 onpause onresume 过程
 *
 * 6.FLAG_ACTIVITY_SINGLE_TOP
 * 与launchMode="singleTop"具有相同的行为
 *
 * 6.一些Flag结合的实现
 * 将这个activity放至栈底或者清空栈后再把这个activity压进栈去
 * Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK
 *
 *
 *
 *
 *
 *
 *
 */
class LaunchFlagActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "$this onCreate TaskId:${taskId}")
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.tv_test).setText("打开LaunchFlagActivity")
        findViewById<TextView>(R.id.tv_test2).setText("打开LaunchModeActivity通过FLAG_ACTIVITY_CLEAR_TOP")
        findViewById<TextView>(R.id.tv_test3).setText("打开LaunchModeSingleTopActivity通过FLAG_ACTIVITY_CLEAR_TOP")
        findViewById<TextView>(R.id.tv_test4).setText("打开LaunchModeActivity通过FLAG_ACTIVITY_NEW_TASK")
        findViewById<TextView>(R.id.tv_test5).setText("打开LaunchModeSingleTaskActivity通过FLAG_ACTIVITY_NEW_TASK")
        findViewById<TextView>(R.id.tv_test6).setText("打开LaunchModeSingleTaskAffinityActivity通过FLAG_ACTIVITY_NEW_TASK")
        findViewById<TextView>(R.id.tv_test7).setText("普通打开LaunchModeSingleTaskAffinityActivity")
        findViewById<TextView>(R.id.tv_test8).setText("普通打开LaunchModeAffinityActivity")
        findViewById<TextView>(R.id.tv_test9).setText("普通打开LaunchModeAffinityActivity通过FLAG_ACTIVITY_NEW_TASK")
        findViewById<TextView>(R.id.tv_test10).setText("普通打开LaunchModeActivity通过FLAG_ACTIVITY_CLEAR_TASK|FLAG_ACTIVITY_NEW_TASK")
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
        startActivity(Intent(this@LaunchFlagActivity, LaunchFlagActivity::class.java))
    }

    fun test2(view: View) {
        val intent =Intent(this@LaunchFlagActivity, LaunchModeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    fun test3(view: View) {
        val intent =Intent(this@LaunchFlagActivity, LaunchModeSingleTopActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    fun test4(view: View) {
        val intent =Intent(this@LaunchFlagActivity, LaunchModeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    fun test5(view: View) {
        val intent =Intent(this@LaunchFlagActivity, LaunchModeSingleTaskActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    fun test6(view: View) {
        val intent =Intent(this@LaunchFlagActivity, LaunchModeSingleTaskAffinityActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    fun test7(view: View) {
        val intent =Intent(this@LaunchFlagActivity, LaunchModeSingleTaskAffinityActivity::class.java)
        startActivity(intent)
    }

    fun test8(view: View) {
        val intent =Intent(this@LaunchFlagActivity, LaunchModeAffinityActivity::class.java)
        startActivity(intent)
    }

    fun test9(view: View) {
        val intent =Intent(this@LaunchFlagActivity, LaunchModeAffinityActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }


    fun test10(view: View) {
        val intent =Intent(this@LaunchFlagActivity, LaunchModeAffinityActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}