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
            // USB遥控器可能使用的媒体控制键
            KeyEvent.KEYCODE_MEDIA_NEXT -> {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    Log.d(TAG, "Media Next key (Channel Down) intercepted")
                    handleChannelUp()
                }
                return true
            }
            KeyEvent.KEYCODE_MEDIA_PREVIOUS -> {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    Log.d(TAG, "Media Previous key (Channel Up) intercepted")
                    handleChannelDown()
                }
                return true
            }
            // 翻页键
            KeyEvent.KEYCODE_PAGE_UP -> {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    Log.d(TAG, "Page Up key (Channel Up) intercepted")
                    handleChannelUp()
                }
                return true
            }
            KeyEvent.KEYCODE_PAGE_DOWN -> {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    Log.d(TAG, "Page Down key (Channel Down) intercepted")
                    handleChannelDown()
                }
                return true
            }
            // 数字键盘的加减号
            KeyEvent.KEYCODE_NUMPAD_ADD -> {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    Log.d(TAG, "Numpad Add key (Channel Up) intercepted")
                    handleChannelUp()
                }
                return true
            }
            KeyEvent.KEYCODE_NUMPAD_SUBTRACT -> {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    Log.d(TAG, "Numpad Subtract key (Channel Down) intercepted")
                    handleChannelDown()
                }
                return true
            }
            // 通用加减号
            KeyEvent.KEYCODE_PLUS -> {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    Log.d(TAG, "Plus key (Channel Up) intercepted")
                    handleChannelUp()
                }
                return true
            }
            KeyEvent.KEYCODE_MINUS -> {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    Log.d(TAG, "Minus key (Channel Down) intercepted")
                    handleChannelDown()
                }
                return true
            }
            // EPG节目指南按键
            KeyEvent.KEYCODE_GUIDE -> {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    Log.d(TAG, "Guide key intercepted")
                    handleGuideKey()
                }
                return true // 拦截事件，不传递给系统
            }
            // 回看按键
            KeyEvent.KEYCODE_LAST_CHANNEL -> {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    Log.d(TAG, "Last Channel key intercepted")
                    handleLastChannelKey()
                }
                return true // 拦截事件，不传递给系统
            }
            // F2键 - Dashboard主页
            KeyEvent.KEYCODE_F2 -> {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    Log.d(TAG, "F2 key intercepted")
                    handleF2Key()
                }
                return true // 拦截事件，不传递给系统
            }
            // 调试模式：记录所有未处理的按键，帮助识别USB遥控器的keycode
            else -> {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    Log.d(TAG, "Unknown key intercepted: keyCode=${event.keyCode}, scanCode=${event.scanCode}, displayLabel='${event.displayLabel}', keyCharacterMap=${event.keyCharacterMap}")
                    // 如果是未知的按键，可以在这里添加临时处理逻辑
                    // 例如检查scanCode或其他属性来识别特定的USB遥控器
                }
            }
        }
        return super.dispatchKeyEvent(event)
    }
    
    /**
     * 额外的按键处理，用于处理系统级按键
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_LAST_CHANNEL -> {
                Log.d(TAG, "Last Channel key intercepted in onKeyDown")
                handleLastChannelKey()
                return true
            }
            KeyEvent.KEYCODE_F2 -> {
                Log.d(TAG, "F2 key intercepted in onKeyDown")
                handleF2Key()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
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
     * 处理EPG指南按键
     */
    private fun handleGuideKey() {
        Log.d(TAG, "Processing Guide key")
        // 直接模拟GUIDE按键传递给UI层
        simulateKeyPress(KeyEvent.KEYCODE_GUIDE)
    }
    
    /**
     * 处理回看按键
     */
    private fun handleLastChannelKey() {
        Log.d(TAG, "Processing Last Channel key")
        // 发送广播通知应用切换EPG页面
        sendBroadcast(Intent("top.yogiczy.mytv.tv.TOGGLE_EPG_GUIDE_PAGE"))
    }
    
    /**
     * 处理音轨按键
     */
    private fun handleAudioTrackKey() {
        Log.d(TAG, "Processing Audio Track key")
        // 发送广播通知应用切换音轨页面
        sendBroadcast(Intent("top.yogiczy.mytv.tv.TOGGLE_AUDIO_TRACKS"))
    }
    
    /**
     * 处理F2按键 - 跳转到Dashboard主页
     */
    private fun handleF2Key() {
        Log.d(TAG, "Processing F2 key")
        // 发送广播通知应用跳转到主页
        sendBroadcast(Intent("top.yogiczy.mytv.tv.SHOW_DASHBOARD"))
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
