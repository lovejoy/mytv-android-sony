package top.yogiczy.mytv.tv.ui.screensold.videoplayercore.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import top.yogiczy.mytv.tv.ui.material.LazyColumn
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.Configs
import top.yogiczy.mytv.tv.ui.utils.focusOnLaunched

@Composable
fun VideoPlayerCoreItemList(
    modifier: Modifier = Modifier,
    currentCoreProvider: () -> Configs.VideoPlayerCore = { Configs.VideoPlayerCore.MEDIA3 },
    onSelected: (Configs.VideoPlayerCore) -> Unit = {},
    onUserAction: () -> Unit = {},
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }.distinctUntilChanged()
            .collect { onUserAction() }
    }

    LazyColumn(
        modifier = modifier,
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp),
    ) { runtime ->
        Configs.VideoPlayerCore.entries.forEachIndexed { index, core ->
            item {
                VideoPlayerCoreItem(
                    modifier = if (index == 0) Modifier.focusOnLaunched() else Modifier,
                    core = core,
                    isSelected = core == currentCoreProvider(),
                    onSelected = { onSelected(core) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun VideoPlayerCoreItemListPreview() {
    MyTvTheme {
        VideoPlayerCoreItemList(
            currentCoreProvider = { Configs.VideoPlayerCore.MEDIA3 },
        )
    }
}