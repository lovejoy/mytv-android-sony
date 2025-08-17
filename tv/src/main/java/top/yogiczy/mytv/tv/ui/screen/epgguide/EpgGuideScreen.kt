package top.yogiczy.mytv.tv.ui.screen.epgguide

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import java.util.Calendar
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import top.yogiczy.mytv.tv.R
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList.Companion.channelList
import top.yogiczy.mytv.core.data.entities.epg.EpgList
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.screen.epgguide.components.EpgGuideCombinedGrid
import top.yogiczy.mytv.tv.ui.screen.epgguide.components.EpgGuideTimeSlots
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.gridColumns

@Composable
fun EpgGuideScreen(
    modifier: Modifier = Modifier,
    channelGroupListProvider: () -> ChannelGroupList = { ChannelGroupList() },
    epgListProvider: () -> EpgList = { EpgList() },
    onChannelSelected: (Channel) -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    var selectedDate by rememberSaveable { mutableStateOf(System.currentTimeMillis()) }
    
    // 根据当前时间设置默认时间段（半小时段）
    val currentTimeSlot = remember {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)
        
        // 计算当前是第几个半小时段（0-47）
        currentHour * 2 + if (currentMinute >= 30) 1 else 0
    }
    
    var selectedTimeSlot by rememberSaveable { mutableIntStateOf(currentTimeSlot) }

    AppScreen(
        modifier = modifier,
        header = { Text(stringResource(R.string.ui_epg_guide)) },
        canBack = true,
        enableTopBarHidden = true,
        onBackPressed = onBackPressed,
    ) { updateTopBarVisibility ->
        Column(
            modifier = Modifier.padding(top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            EpgGuideTimeSlots(
                selectedDateProvider = { selectedDate },
                selectedTimeSlotProvider = { selectedTimeSlot },
                onDateSelected = { selectedDate = it },
                onTimeSlotSelected = { selectedTimeSlot = it },
            )

            EpgGuideCombinedGrid(
                modifier = Modifier.fillMaxHeight(),
                channelGroupListProvider = channelGroupListProvider,
                epgListProvider = epgListProvider,
                selectedDateProvider = { selectedDate },
                selectedTimeSlotProvider = { selectedTimeSlot },
                onChannelSelected = onChannelSelected,
                onTimeSlotSelected = { selectedTimeSlot = it },
                updateTopBarVisibility = { updateTopBarVisibility(false) },
            )
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun EpgGuideScreenPreview() {
    MyTvTheme {
        EpgGuideScreen(
            channelGroupListProvider = { ChannelGroupList.EXAMPLE },
            epgListProvider = { EpgList.example(ChannelGroupList.EXAMPLE.channelList) },
        )
    }
}