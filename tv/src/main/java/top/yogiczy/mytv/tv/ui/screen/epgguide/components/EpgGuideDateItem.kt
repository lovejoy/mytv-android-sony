package top.yogiczy.mytv.tv.ui.screen.epgguide.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents

@Composable
fun EpgGuideDateItem(
    modifier: Modifier = Modifier,
    text: String = "",
    isSelected: Boolean = false,
    onSelected: () -> Unit = {},
) {
    var isFocused by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .onFocusChanged { isFocused = it.isFocused || it.hasFocus }
            .handleKeyEvents(onSelect = onSelected),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary 
                           else if (isFocused) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f) 
                           else Color.Transparent,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary 
                          else MaterialTheme.colorScheme.onSurface,
        ),
        onClick = {},
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
        )
    }
}

@Composable
fun EpgGuideTimeSlotItem(
    modifier: Modifier = Modifier,
    text: String = "",
    isSelected: Boolean = false,
    onSelected: () -> Unit = {},
) {
    var isFocused by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .onFocusChanged { isFocused = it.isFocused || it.hasFocus }
            .handleKeyEvents(onSelect = onSelected),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.secondary 
                           else if (isFocused) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f) 
                           else Color.Transparent,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onSecondary 
                          else MaterialTheme.colorScheme.onSurface,
        ),
        onClick = {},
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            text = text,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
        )
    }
}

@Preview
@Composable
private fun EpgGuideDateItemPreview() {
    MyTvTheme {
        EpgGuideDateItem(
            text = "今天",
            isSelected = true,
        )
    }
}

@Preview
@Composable
private fun EpgGuideTimeSlotItemPreview() {
    MyTvTheme {
        EpgGuideTimeSlotItem(
            text = "12:00",
            isSelected = false,
        )
    }
}