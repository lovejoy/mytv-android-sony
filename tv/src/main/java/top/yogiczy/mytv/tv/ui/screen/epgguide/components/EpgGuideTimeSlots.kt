package top.yogiczy.mytv.tv.ui.screen.epgguide.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import top.yogiczy.mytv.tv.R
import top.yogiczy.mytv.tv.ui.material.LazyRow
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun EpgGuideTimeSlots(
    modifier: Modifier = Modifier,
    selectedDateProvider: () -> Long = { System.currentTimeMillis() },
    selectedTimeSlotProvider: () -> Int = { 0 },
    onDateSelected: (Long) -> Unit = {},
    onTimeSlotSelected: (Int) -> Unit = {},
) {
    val childPadding = rememberChildPadding()
    val dateFormat = SimpleDateFormat("MM-dd E", Locale.getDefault())
    
    val dates = remember {
        (0..6).map { dayOffset ->
            Calendar.getInstance().apply {
                add(Calendar.DAY_OF_MONTH, dayOffset)
            }.timeInMillis
        }
    }
    
    // 默认选择今天
    if (selectedDateProvider() == 0L) {
        onDateSelected(dates[0])
    }
    
    val timeSlotHours = remember { listOf(0, 6, 12, 18) }
    val timeSlots = remember {
        timeSlotHours.map { hour ->
            String.format("%02d:00", hour)
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            modifier = Modifier.padding(horizontal = childPadding.start),
            text = "节目指南",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = childPadding.start),
        ) { runtime ->
            dates.forEachIndexed { index, date ->
                item {
                    EpgGuideDateItem(
                        modifier = if (index == 0) Modifier.focusRequester(runtime.firstItemFocusRequester) else Modifier,
                        text = if (index == 0) stringResource(R.string.ui_epg_today) else dateFormat.format(date),
                        isSelected = selectedDateProvider() == date,
                        onSelected = { onDateSelected(date) },
                    )
                }
            }
        }

    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun EpgGuideTimeSlotsPreview() {
    MyTvTheme {
        AppScreen {
            EpgGuideTimeSlots(
                modifier = Modifier.padding(vertical = 20.dp),
            )
        }
    }
}