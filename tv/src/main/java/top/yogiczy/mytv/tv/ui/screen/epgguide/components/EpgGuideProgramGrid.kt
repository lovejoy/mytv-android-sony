package top.yogiczy.mytv.tv.ui.screen.epgguide.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun EpgGuideProgramGrid(
    modifier: Modifier = Modifier,
    channelGroupListProvider: () -> ChannelGroupList = { ChannelGroupList() },
    epgListProvider: () -> EpgList = { EpgList() },
    selectedDateProvider: () -> Long = { System.currentTimeMillis() },
    selectedTimeSlotProvider: () -> Int = { 0 },
    onChannelSelected: (Channel) -> Unit = {},
    updateTopBarVisibility: (() -> Unit)? = null,
) {
    val childPadding = rememberChildPadding()
    val channelListState = rememberLazyListState()
    var focusedChannelIndex by remember { mutableIntStateOf(0) }
    
    val channelList = channelGroupListProvider().channelList
    val epgList = epgListProvider()
    val selectedDate = selectedDateProvider()
    val selectedTimeSlot = selectedTimeSlotProvider()

    LaunchedEffect(focusedChannelIndex) {
        channelListState.animateScrollToItem(focusedChannelIndex)
    }

    val timeSlots = remember {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = selectedDate
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        
        listOf(0, 6, 12, 18).map { hour ->
            val startTime = calendar.timeInMillis + hour * 3600 * 1000L
            val endTime = startTime + 6 * 3600 * 1000L // 6小时段
            startTime to endTime
        }
    }

    Column(
        modifier = modifier,
    ) {
        TimeHeader(
            selectedTimeSlot = selectedTimeSlot,
            timeSlots = timeSlots,
        )

        LazyColumn(
            state = channelListState,
            contentPadding = PaddingValues(
                end = childPadding.end,
                bottom = childPadding.bottom,
            ),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            itemsIndexed(
                items = channelList,
                key = { _, channel -> "${channel.name}-programs" },
            ) { index, channel ->
                val channelEpg = epgList.match(channel)
                
                ProgramRow(
                    modifier = Modifier
                        .fillMaxWidth()
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
                )
            }
        }
    }
}

@Composable
private fun TimeHeader(
    selectedTimeSlot: Int,
    timeSlots: List<Pair<Long, Long>>,
) {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val timeSlotHours = listOf(0, 6, 12, 18)
    
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        contentPadding = PaddingValues(bottom = 8.dp),
    ) {
        itemsIndexed(timeSlots) { index, (startTime, _) ->
            Surface(
                colors = ClickableSurfaceDefaults.colors(
                    containerColor = if (timeSlotHours[index] == selectedTimeSlot) 
                        MaterialTheme.colorScheme.primary 
                    else MaterialTheme.colorScheme.surfaceVariant,
                ),
                shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(4.dp)),
                onClick = {},
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    text = timeFormat.format(startTime),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

@Composable
private fun ProgramRow(
    modifier: Modifier = Modifier,
    channel: Channel,
    timeSlots: List<Pair<Long, Long>>,
    programs: List<EpgProgramme>,
    onChannelSelected: (Channel) -> Unit = {},
) {
    LazyRow(
        modifier = modifier.height(60.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        itemsIndexed(timeSlots) { index, (startTime, endTime) ->
            val program = programs.find { 
                it.startAt < endTime && it.endAt > startTime 
            }
            
            ProgramBlock(
                modifier = Modifier
                    .width(180.dp)
                    .fillMaxHeight()
                    .handleKeyEvents(onSelect = { onChannelSelected(channel) }),
                program = program,
                timeSlot = startTime to endTime,
            )
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
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    
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
            Column {
                if (program != null) {
                    Text(
                        text = program.title,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = "${timeFormat.format(program.startAt)}-${timeFormat.format(program.endAt)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    Text(
                        text = "暂无节目",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = "${timeFormat.format(timeSlot.first)}-${timeFormat.format(timeSlot.second)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun EpgGuideProgramGridPreview() {
    MyTvTheme {
        AppScreen {
            EpgGuideProgramGrid(
                channelGroupListProvider = { ChannelGroupList.EXAMPLE },
                epgListProvider = { EpgList.example(ChannelGroupList.EXAMPLE.channelList) },
            )
        }
    }
}