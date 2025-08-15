package top.yogiczy.mytv.tv.ui.screensold.videoplayercore

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.Text
import top.yogiczy.mytv.tv.ui.material.Drawer
import top.yogiczy.mytv.tv.ui.material.DrawerPosition
import top.yogiczy.mytv.tv.ui.screensold.components.rememberScreenAutoCloseState
import top.yogiczy.mytv.tv.ui.screensold.videoplayercore.components.VideoPlayerCoreItemList
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.tooling.PreviewWithLayoutGrids
import top.yogiczy.mytv.tv.ui.utils.Configs
import top.yogiczy.mytv.tv.ui.utils.backHandler
import top.yogiczy.mytv.tv.ui.utils.gridColumns

@Composable
fun VideoPlayerCoreScreen(
    modifier: Modifier = Modifier,
    currentCoreProvider: () -> Configs.VideoPlayerCore = { Configs.VideoPlayerCore.MEDIA3 },
    onCoreChanged: (Configs.VideoPlayerCore) -> Unit = {},
    onClose: () -> Unit = {},
) {
    val screenAutoCloseState = rememberScreenAutoCloseState(onTimeout = onClose)

    Drawer(
        modifier = modifier.backHandler { onClose() },
        onDismissRequest = onClose,
        position = DrawerPosition.End,
        header = { Text("播放器") },
    ) {
        VideoPlayerCoreItemList(
            modifier = Modifier.width(4.5f.gridColumns()),
            currentCoreProvider = currentCoreProvider,
            onSelected = onCoreChanged,
            onUserAction = { screenAutoCloseState.active() },
        )
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun VideoPlayerCoreScreenPreview() {
    MyTvTheme {
        PreviewWithLayoutGrids {
            VideoPlayerCoreScreen(
                currentCoreProvider = { Configs.VideoPlayerCore.MEDIA3 },
            )
        }
    }
}