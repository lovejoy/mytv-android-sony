package top.yogiczy.mytv.tv.ui.screen.epgguide.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList.Companion.channelList
import top.yogiczy.mytv.core.data.entities.epg.EpgList
import top.yogiczy.mytv.core.data.entities.epg.EpgList.Companion.match
import top.yogiczy.mytv.core.data.entities.epg.EpgProgramme
import top.yogiczy.mytv.tv.R
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.focusOnLaunched
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun EpgGuideCombinedGrid(
    modifier: Modifier = Modifier,
    channelGroupListProvider: () -> ChannelGroupList = { ChannelGroupList() },
    epgListProvider: () -> EpgList = { EpgList() },
    selectedDateProvider: () -> Long = { System.currentTimeMillis() },
    selectedTimeSlotProvider: () -> Int = { 0 },
    onChannelSelected: (Channel) -> Unit = {},
    onTimeSlotSelected: (Int) -> Unit = {},
    updateTopBarVisibility: (() -> Unit)? = null,
) {
    val childPadding = rememberChildPadding()
    val listState = rememberLazyListState()
    var focusedChannelIndex by remember { mutableIntStateOf(0) }
    
    val channelList = channelGroupListProvider().channelList
    val epgList = epgListProvider()
    val selectedDate = selectedDateProvider()

    LaunchedEffect(focusedChannelIndex) {
        listState.animateScrollToItem(focusedChannelIndex)
    }

    val timeSlots = remember(selectedDate) {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = selectedDate
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        
        // 按半小时分段，一天48个时间段
        (0..47).map { halfHour ->
            val startTime = calendar.timeInMillis + halfHour * 30 * 60 * 1000L
            val endTime = startTime + 30 * 60 * 1000L
            startTime to endTime
        }
    }

    Column(
        modifier = modifier,
    ) {
        // 时间标头
        TimeHeader(
            selectedTimeSlot = selectedTimeSlotProvider(),
            timeSlots = timeSlots,
            startPadding = 16.dp,
            onTimeSlotSelected = onTimeSlotSelected,
        )

        // 频道和节目行
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(
                bottom = childPadding.bottom,
            ),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            itemsIndexed(
                items = channelList,
                key = { _, channel -> "${channel.name}-combined" },
            ) { index, channel ->
                val channelEpg = epgList.match(channel)
                
                CombinedChannelRow(
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
                        },
                    channel = channel,
                    timeSlots = timeSlots,
                    programs = channelEpg?.programmeList ?: emptyList(),
                    onChannelSelected = onChannelSelected,
                    startPadding = 16.dp,
                )
            }
        }
    }
}

@Composable
private fun TimeHeader(
    selectedTimeSlot: Int,
    timeSlots: List<Pair<Long, Long>>,
    startPadding: Dp,
    onTimeSlotSelected: (Int) -> Unit = {},
) {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
    ) {
        // 频道名称占位空间 - 缩小宽度
        Box(
            modifier = Modifier
                .width(120.dp)
                .padding(start = startPadding)
        )
        
        // 时间段标头 - 可滑动
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            itemsIndexed(timeSlots) { index, (startTime, _) ->
                val halfHourIndex = index // 半小时段索引
                Surface(
                    modifier = Modifier
                        .width(80.dp)
                        .handleKeyEvents(onSelect = { onTimeSlotSelected(halfHourIndex) }),
                    colors = ClickableSurfaceDefaults.colors(
                        containerColor = if (halfHourIndex == selectedTimeSlot) 
                            MaterialTheme.colorScheme.primary 
                        else MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(4.dp)),
                    onClick = { onTimeSlotSelected(halfHourIndex) },
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                        text = timeFormat.format(startTime),
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        }
    }
}

@Composable
private fun CombinedChannelRow(
    modifier: Modifier = Modifier,
    channel: Channel,
    timeSlots: List<Pair<Long, Long>>,
    programs: List<EpgProgramme>,
    onChannelSelected: (Channel) -> Unit = {},
    startPadding: Dp,
) {
    Row(
        modifier = modifier.height(60.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        // 频道名称 - 固定在左边，缩小宽度
        ChannelNameBlock(
            modifier = Modifier
                .width(120.dp)
                .height(60.dp)
                .padding(start = startPadding)
                .handleKeyEvents(onSelect = { onChannelSelected(channel) }),
            channel = channel,
        )
        
        // 节目时间段 - 可滑动部分
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            itemsIndexed(timeSlots) { index, (startTime, endTime) ->
                val program = programs.find { 
                    it.startAt < endTime && it.endAt > startTime 
                }
                
                ProgramBlock(
                    modifier = Modifier
                        .width(80.dp)
                        .height(60.dp)
                        .handleKeyEvents(onSelect = { onChannelSelected(channel) }),
                    program = program,
                    timeSlot = startTime to endTime,
                )
            }
        }
    }
}

@Composable
private fun ChannelNameBlock(
    modifier: Modifier = Modifier,
    channel: Channel,
) {
    var isFocused by remember { mutableStateOf(false) }
    
    Surface(
        modifier = modifier
            .onFocusChanged { isFocused = it.isFocused || it.hasFocus },
        colors = ClickableSurfaceDefaults.colors(
            containerColor = if (isFocused) MaterialTheme.colorScheme.surfaceVariant 
                           else MaterialTheme.colorScheme.surface,
        ),
        shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(6.dp)),
        onClick = {},
    ) {
        Box(
            modifier = Modifier.padding(12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            // 频道名称，居左对齐，给底部留出空间
            Text(
                modifier = Modifier.padding(
                    bottom = if (channel.index >= 0) 20.dp else 0.dp,
                ),
                text = channel.name,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            // 频道序号显示在左下角，再往下移动一点
            if (channel.index >= 0) {
                Text(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 2.dp),
                    text = channel.no,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

@Composable
private fun ProgramBlock(
    modifier: Modifier = Modifier,
    program: EpgProgramme?,
    timeSlot: Pair<Long, Long>,
) {
    var isFocused by remember { mutableStateOf(false) }
    
    Surface(
        modifier = modifier
            .onFocusChanged { isFocused = it.isFocused || it.hasFocus },
        colors = ClickableSurfaceDefaults.colors(
            containerColor = if (isFocused) MaterialTheme.colorScheme.primaryContainer 
                           else MaterialTheme.colorScheme.surface,
        ),
        shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(6.dp)),
        onClick = {},
    ) {
        Box(
            modifier = Modifier.padding(8.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = program?.title ?: stringResource(R.string.ui_no_program),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = if (program != null) MaterialTheme.colorScheme.onSurface 
                       else MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun EpgGuideCombinedGridPreview() {
    MyTvTheme {
        AppScreen {
            EpgGuideCombinedGrid(
                channelGroupListProvider = { ChannelGroupList.EXAMPLE },
                epgListProvider = { EpgList.example(ChannelGroupList.EXAMPLE.channelList) },
            )
        }
    }
}