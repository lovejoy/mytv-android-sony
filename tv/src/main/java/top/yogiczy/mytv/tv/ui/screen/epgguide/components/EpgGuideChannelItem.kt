package top.yogiczy.mytv.tv.ui.screen.epgguide.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.core.data.entities.epg.EpgList
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme

@Composable
fun EpgGuideChannelItem(
    modifier: Modifier = Modifier,
    channel: Channel = Channel.EMPTY,
) {
    var isFocused by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { isFocused = it.isFocused || it.hasFocus },
        colors = ClickableSurfaceDefaults.colors(
            containerColor = if (isFocused) MaterialTheme.colorScheme.surfaceVariant 
                           else MaterialTheme.colorScheme.surface,
        ),
        shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(8.dp)),
        onClick = {},
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = channel.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            if (channel.index >= 0) {
                Text(
                    text = channel.no,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Preview
@Composable
private fun EpgGuideChannelItemPreview() {
    MyTvTheme {
        EpgGuideChannelItem(
            channel = Channel.EXAMPLE,
        )
    }
}