package top.yogiczy.mytv.tv.ui.screen.settings.subcategories

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.Text
import top.yogiczy.mytv.tv.R
import top.yogiczy.mytv.tv.ui.screen.settings.SettingsViewModel
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsCategoryScreen
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsListItem
import top.yogiczy.mytv.tv.ui.screen.settings.settingsVM
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.LanguageHelper

@Composable
fun SettingsLanguageScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = settingsVM,
    onBackPressed: () -> Unit = {},
) {
    val context = LocalContext.current
    val languageOptions = listOf(
        "" to stringResource(R.string.ui_language_follow_system),
        "zh" to stringResource(R.string.ui_language_chinese),
        "en" to stringResource(R.string.ui_language_english)
    )

    SettingsCategoryScreen(
        modifier = modifier,
        header = { Text("${stringResource(R.string.ui_dashboard_module_settings)} / ${stringResource(R.string.ui_language_setting)}") },
        onBackPressed = onBackPressed,
    ) { firstItemFocusRequester ->
        languageOptions.forEachIndexed { index, (code, name) ->
            item {
                SettingsListItem(
                    modifier = if (index == 0) Modifier.focusRequester(firstItemFocusRequester) else Modifier,
                    headlineContent = name,
                    trailingContent = {
                        if (settingsViewModel.appLanguage == code) {
                            androidx.tv.material3.Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null
                            )
                        }
                    },
                    onSelect = {
                        if (settingsViewModel.appLanguage != code) {
                            settingsViewModel.appLanguage = code
                            // 应用新语言并重启Activity
                            if (context is androidx.activity.ComponentActivity) {
                                LanguageHelper.applyLanguageAndRestart(context, code)
                            }
                        }
                    },
                )
            }
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun SettingsLanguageScreenPreview() {
    MyTvTheme {
        SettingsLanguageScreen()
    }
}
