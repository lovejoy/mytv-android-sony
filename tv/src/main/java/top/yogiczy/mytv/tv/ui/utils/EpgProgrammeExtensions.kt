package top.yogiczy.mytv.tv.ui.utils

import android.content.Context
import top.yogiczy.mytv.core.data.entities.epg.EpgProgramme
import top.yogiczy.mytv.tv.R
import java.util.Calendar

/**
 * 获取本地化的示例节目
 */
fun EpgProgramme.Companion.getLocalizedExample(context: Context): EpgProgramme {
    return EpgProgramme(
        startAt = System.currentTimeMillis() - 3600 * 1000,
        endAt = System.currentTimeMillis() + 3600 * 1000,
        title = context.getString(R.string.ui_program_title),
    )
}

/**
 * 获取本地化的空节目
 */
fun EpgProgramme.Companion.getLocalizedEmpty(context: Context): EpgProgramme {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return EpgProgramme(
        startAt = calendar.timeInMillis,
        endAt = calendar.timeInMillis + (24 * 3600 - 1) * 1000,
        title = context.getString(R.string.ui_excellent_program),
    )
}
