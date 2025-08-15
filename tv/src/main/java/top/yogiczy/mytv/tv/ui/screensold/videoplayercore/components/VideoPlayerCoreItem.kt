package top.yogiczy.mytv.tv.ui.screensold.videoplayercore.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Icon
import androidx.tv.material3.ListItem
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.RadioButton
import androidx.tv.material3.Text
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.Configs
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents
import top.yogiczy.mytv.tv.ui.utils.ifElse
import top.yogiczy.mytv.tv.ui.utils.focusOnLaunchedSaveable

@Composable
fun VideoPlayerCoreItem(
    modifier: Modifier = Modifier,
    core: Configs.VideoPlayerCore = Configs.VideoPlayerCore.MEDIA3,
    isSelected: Boolean = false,
    onSelected: () -> Unit = {},
) {
    ListItem(
        modifier = modifier
            .ifElse(isSelected, Modifier.focusOnLaunchedSaveable())
            .handleKeyEvents(onSelect = onSelected),
        selected = false,
        onClick = {},
        headlineContent = { Text(core.label) },
        trailingContent = {
            RadioButton(selected = isSelected, onClick = {})
        },
    )
}

@Preview
@Composable
private fun VideoPlayerCoreItemPreview() {
    MyTvTheme {
        VideoPlayerCoreItem(
            core = Configs.VideoPlayerCore.MEDIA3,
            isSelected = true,
        )
    }
}