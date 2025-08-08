package top.yogiczy.mytv.tv

import android.app.PictureInPictureParams
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Rational
import android.view.KeyEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.tv.material3.Surface
import top.yogiczy.mytv.tv.ui.App
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.Configs
import top.yogiczy.mytv.tv.utlis.HttpServer
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {
    
    companion object {
        private const val TAG = "MainActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

        // 设置Kodi风格的全屏沉浸式模式
        setFullScreenImmersive()

        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowCompat.getInsetsController(window, window.decorView).let { insetsController ->
                insetsController.hide(WindowInsetsCompat.Type.statusBars())
                insetsController.hide(WindowInsetsCompat.Type.navigationBars())
                insetsController.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }

            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

            MyTvTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    App(
                        onBackPressed = {
                            finish()
                            exitProcess(0)
                        },
                    )
                }
            }
        }

        HttpServer.startService(applicationContext)
    }
    
    /**
     * 设置Kodi风格的全屏沉浸式模式，提高按键处理优先级
     */
    private fun setFullScreenImmersive() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let { controller ->
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            )
        }
    }
    
    /**
     * Kodi风格的按键事件拦截 - 在系统处理之前拦截频道按键和数字键
     */
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        when (event.keyCode) {
            KeyEvent.KEYCODE_CHANNEL_UP -> {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    Log.d(TAG, "Channel Up key intercepted")
                    handleChannelUp()
                }
                return true // 拦截事件，不传递给系统
            }
            KeyEvent.KEYCODE_CHANNEL_DOWN -> {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    Log.d(TAG, "Channel Down key intercepted")
                    handleChannelDown()
                }
                return true // 拦截事件，不传递给系统
            }
            // 拦截数字键，防止同时触发电视系统的频道切换
            KeyEvent.KEYCODE_0,
            KeyEvent.KEYCODE_1,
            KeyEvent.KEYCODE_2,
            KeyEvent.KEYCODE_3,
            KeyEvent.KEYCODE_4,
            KeyEvent.KEYCODE_5,
            KeyEvent.KEYCODE_6,
            KeyEvent.KEYCODE_7,
            KeyEvent.KEYCODE_8,
            KeyEvent.KEYCODE_9 -> {
                Log.d(TAG, "Number key ${event.keyCode - KeyEvent.KEYCODE_0} intercepted")
                // 调用原生的dispatchKeyEvent让应用内部处理，但通过返回true阻止传递给电视系统
                val handled = super.dispatchKeyEvent(event)
                return true // 始终返回true，阻止事件传递给电视系统
            }
            // 处理一些Sony电视可能使用的其他按键码
            166, 167 -> { // KEYCODE_PROG_RED, KEYCODE_PROG_GREEN 有时用于频道切换
                if (event.action == KeyEvent.ACTION_DOWN) {
                    if (event.keyCode == 166) { // 红色按键可能映射为频道上
                        Log.d(TAG, "Prog Red key (Channel Up) intercepted")
                        handleChannelUp()
                    } else { // 绿色按键可能映射为频道下  
                        Log.d(TAG, "Prog Green key (Channel Down) intercepted")
                        handleChannelDown()
                    }
                }
                return true
            }
        }
        return super.dispatchKeyEvent(event)
    }
    
    /**
     * 处理频道上切换
     */
    private fun handleChannelUp() {
        Log.d(TAG, "Processing Channel Up")
        // 发送广播通知应用其他组件进行频道切换
        sendBroadcast(Intent("top.yogiczy.mytv.tv.CHANNEL_UP"))
        
        // 也可以通过其他方式触发频道切换，比如模拟按键
        // 这里模拟发送DPAD_UP按键，让现有的按键处理逻辑来处理
        simulateKeyPress(KeyEvent.KEYCODE_DPAD_UP)
    }
    
    /**
     * 处理频道下切换
     */
    private fun handleChannelDown() {
        Log.d(TAG, "Processing Channel Down")
        // 发送广播通知应用其他组件进行频道切换
        sendBroadcast(Intent("top.yogiczy.mytv.tv.CHANNEL_DOWN"))
        
        // 模拟发送DPAD_DOWN按键，让现有的按键处理逻辑来处理
        simulateKeyPress(KeyEvent.KEYCODE_DPAD_DOWN)
    }
    
    /**
     * 模拟按键事件，让应用的现有按键处理逻辑来处理
     */
    private fun simulateKeyPress(keyCode: Int) {
        try {
            val downEvent = KeyEvent(KeyEvent.ACTION_DOWN, keyCode)
            val upEvent = KeyEvent(KeyEvent.ACTION_UP, keyCode)
            
            // 发送给父类处理，避免无限循环
            super.dispatchKeyEvent(downEvent)
            super.dispatchKeyEvent(upEvent)
        } catch (e: Exception) {
            Log.e(TAG, "Error simulating key press: ${e.message}")
        }
    }

    override fun onDestroy() {
        HttpServer.stopService(applicationContext)
        super.onDestroy()
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && Configs.appPipEnable) {
            val aspectRatio = Rational(16, 9)
            val params = PictureInPictureParams.Builder().setAspectRatio(aspectRatio).build()
            enterPictureInPictureMode(params)
        }
    }
}
