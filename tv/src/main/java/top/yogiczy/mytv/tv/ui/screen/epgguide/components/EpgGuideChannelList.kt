package top.yogiczy.mytv.tv.ui.screen.epgguide.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList.Companion.channelList
import top.yogiczy.mytv.core.data.entities.epg.EpgList
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents
import top.yogiczy.mytv.tv.ui.utils.focusOnLaunched

@Composable
fun EpgGuideChannelList(
    modifier: Modifier = Modifier,
    channelGroupListProvider: () -> ChannelGroupList = { ChannelGroupList() },
    epgListProvider: () -> EpgList = { EpgList() },
    selectedDateProvider: () -> Long = { System.currentTimeMillis() },
    onChannelSelected: (Channel) -> Unit = {},
    updateTopBarVisibility: (() -> Unit)? = null,
) {
    val childPadding = rememberChildPadding()
    val listState = rememberLazyListState()
    var focusedChannelIndex by remember { mutableIntStateOf(0) }
    
    val channelList = channelGroupListProvider().channelList
    val epgList = epgListProvider()

    LaunchedEffect(focusedChannelIndex) {
        listState.animateScrollToItem(focusedChannelIndex)
    }

    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = PaddingValues(
            start = childPadding.start,
            end = childPadding.end,
            bottom = childPadding.bottom,
        ),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        itemsIndexed(
            items = channelList,
            key = { _, channel -> "${channel.name}-${channel.lineList.hashCode()}" },
        ) { index, channel ->
            EpgGuideChannelItem(
                modifier = Modifier
                    .let { m ->
                        if (index == 0) m.focusOnLaunched()
                        else m
                    }
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused || focusState.hasFocus) {
                            focusedChannelIndex = index
                            updateTopBarVisibility?.invoke()
                        }
                    }
                    .handleKeyEvents(
                        onSelect = { onChannelSelected(channel) },
                        onLongSelect = { onChannelSelected(channel) },
                    ),
                channel = channel,
            )
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun EpgGuideChannelListPreview() {
    MyTvTheme {
        AppScreen {
            EpgGuideChannelList(
                channelGroupListProvider = { ChannelGroupList.EXAMPLE },
                epgListProvider = { EpgList.example(ChannelGroupList.EXAMPLE.channelList) },
            )
        }
    }
}