package top.yogiczy.mytv.tv.ui.screen.settings.categories

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.Switch
import androidx.tv.material3.Text
import top.yogiczy.mytv.tv.ui.screen.settings.SettingsViewModel
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsCategoryScreen
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsListItem
import top.yogiczy.mytv.tv.ui.screen.settings.settingsVM
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme

@Composable
fun SettingsUpdateScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = settingsVM,
    toUpdateChannelScreen: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    SettingsCategoryScreen(
        modifier = modifier,
        header = { Text("设置 / 系统") },
        onBackPressed = onBackPressed,
    ) { firstItemFocusRequester ->
        item {
            val channel = settingsViewModel.updateChannel

            SettingsListItem(
                modifier = Modifier.focusRequester(firstItemFocusRequester),
                headlineContent = "更新通道",
                trailingContent = when (channel) {
                    "stable" -> "稳定版"
                    "beta" -> "测试版"
                    "dev" -> "开发版"
                    else -> channel
                },
                onSelect = toUpdateChannelScreen,
                link = true,
            )
        }

        item {
            val autoCheck = settingsViewModel.updateAutoCheck

            SettingsListItem(
                headlineContent = "启动时自动检查更新",
                supportingContent = if (autoCheck) "程序启动时会自动检查更新"
                else "程序启动时不检查更新",
                trailingContent = {
                    Switch(autoCheck, null)
                },
                onSelect = {
                    settingsViewModel.updateAutoCheck = !autoCheck
                },
            )
        }

        item {
            val forceRemind = settingsViewModel.updateForceRemind

            SettingsListItem(
                headlineContent = "更新强提醒",
                supportingContent = if (forceRemind) "检测到新版本时会全屏提醒"
                else "检测到新版本时仅消息提示",
                trailingContent = {
                    Switch(forceRemind, null)
                },
                onSelect = {
                    settingsViewModel.updateForceRemind = !forceRemind
                },
            )
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun SettingsUpdateScreenPreview() {
    MyTvTheme {
        SettingsUpdateScreen()
    }
}