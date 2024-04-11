/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package dev.conn.breathe.presentation

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.ambient.AmbientLifecycleObserver
import androidx.wear.compose.foundation.CurvedDirection
import androidx.wear.compose.foundation.CurvedLayout
import androidx.wear.compose.foundation.CurvedTextStyle
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.curvedText
import dev.conn.breathe.BuildConfig
import dev.conn.breathe.presentation.theme.BreatheTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

//        val ambientCallback = object : AmbientLifecycleObserver.AmbientLifecycleCallback {
//            override fun onEnterAmbient(ambientDetails: AmbientLifecycleObserver.AmbientDetails) {
//            }
//        }
//
//        val observer = AmbientLifecycleObserver(this, ambientCallback)
//        lifecycle.addObserver(observer)
//
//        ViewModelProvider(this).get(MainViewModel::class.java)

        setContent {
            WearApp()
        }
    }
}

@Composable
fun WearApp() {

    BreatheTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            TimeText()
            Timer()

            CurvedLayout(anchor = 90.0f) {
                curvedText(
                    text = "Version: ${BuildConfig.VERSION_CODE}",
                    angularDirection = CurvedDirection.Angular.Reversed,
                    style = CurvedTextStyle(
                        color = Color.Gray
                    )
                )
            }
        }
    }
}

@Composable
fun Timer(mainViewModel: MainViewModel = viewModel()) {
    val isTimerRunning by mainViewModel.runningState.collectAsState()
    val timerState by mainViewModel.timerState.collectAsState()

    val text = if (isTimerRunning) {
        val time = timerState.count
        if (time == 0) {
            4
        } else {
            time
        }.toString()
    } else {
        "Ready?"
    }

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (isTimerRunning) {
                    mainViewModel.stopTimer()
                } else {
                    mainViewModel.startTimer()
                }
            },
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = text,
    )
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp()
}