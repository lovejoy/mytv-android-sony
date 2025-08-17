package top.yogiczy.mytv.tv.ui.screensold.videoplayer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import android.content.Context
import top.yogiczy.mytv.tv.R
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme

@Composable
fun VideoPlayerError(
    modifier: Modifier = Modifier,
    errorProvider: () -> String? = { null },
) {
    val error = errorProvider() ?: return
    val context = LocalContext.current

    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                shape = MaterialTheme.shapes.medium,
            )
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = context.getString(R.string.ui_video_playback_failed),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.error,
        )

        Text(
            text = error,
            style = MaterialTheme.typography.bodyMedium,
            color = LocalContentColor.current.copy(alpha = 0.8f),
        )

        getErrorCodeDesc(context, error)?.let { nnDesc ->
            Text(
                text = nnDesc,
                style = MaterialTheme.typography.bodyMedium,
                color = LocalContentColor.current.copy(alpha = 0.8f),
            )
        }
    }
}

private fun getErrorCodeDesc(context: Context, error: String): String? {
    return when (error.substringBefore("(")) {
        "ERROR_UNSUPPORTED_TYPE" -> context.getString(R.string.ui_video_error_unsupported_type)
        "ERROR_LOAD_TIMEOUT" -> context.getString(R.string.ui_video_error_load_timeout)
        "MEDIA3_ERROR_UNSPECIFIED" -> context.getString(R.string.ui_video_error_unspecified)
        "MEDIA3_ERROR_REMOTE_ERROR" -> context.getString(R.string.ui_video_error_remote_error)
        "MEDIA3_ERROR_BEHIND_LIVE_WINDOW" -> context.getString(R.string.ui_video_error_behind_live_window)
        "MEDIA3_ERROR_TIMEOUT" -> context.getString(R.string.ui_video_error_timeout)
        "MEDIA3_ERROR_IO_UNSPECIFIED" -> context.getString(R.string.ui_video_error_io_unspecified)
        "MEDIA3_ERROR_IO_NETWORK_CONNECTION_FAILED" -> context.getString(R.string.ui_video_error_network_failed)
        "MEDIA3_ERROR_IO_NETWORK_CONNECTION_TIMEOUT" -> context.getString(R.string.ui_video_error_network_timeout)
        "MEDIA3_ERROR_IO_INVALID_HTTP_CONTENT_TYPE" -> context.getString(R.string.ui_video_error_invalid_http_content_type)
        "MEDIA3_ERROR_IO_BAD_HTTP_STATUS" -> context.getString(R.string.ui_video_error_bad_http_status)
        "MEDIA3_ERROR_IO_FILE_NOT_FOUND" -> context.getString(R.string.ui_video_error_file_not_found)
        "MEDIA3_ERROR_IO_NO_PERMISSION" -> context.getString(R.string.ui_video_error_no_permission)
        "MEDIA3_ERROR_IO_CLEARTEXT_NOT_PERMITTED" -> context.getString(R.string.ui_video_error_cleartext_not_permitted)
        "MEDIA3_ERROR_IO_READ_POSITION_OUT_OF_RANGE" -> context.getString(R.string.ui_video_error_read_position_out_of_range)
        "MEDIA3_ERROR_PARSING_CONTAINER_MALFORMED" -> context.getString(R.string.ui_video_error_container_malformed)
        "MEDIA3_ERROR_PARSING_MANIFEST_MALFORMED" -> context.getString(R.string.ui_video_error_manifest_malformed)
        "MEDIA3_ERROR_PARSING_CONTAINER_UNSUPPORTED" -> context.getString(R.string.ui_video_error_container_unsupported)
        "MEDIA3_ERROR_PARSING_MANIFEST_UNSUPPORTED" -> context.getString(R.string.ui_video_error_manifest_unsupported)
        "MEDIA3_ERROR_DECODER_INIT_FAILED" -> context.getString(R.string.ui_video_error_decoder_init_failed)
        "MEDIA3_ERROR_DECODER_QUERY_FAILED" -> context.getString(R.string.ui_video_error_decoder_query_failed)
        "MEDIA3_ERROR_DECODING_FAILED" -> context.getString(R.string.ui_video_error_decoding_failed)
        "MEDIA3_ERROR_DECODING_FORMAT_EXCEEDS_CAPABILITIES" -> context.getString(R.string.ui_video_error_decoding_format_exceeds_capabilities)
        "MEDIA3_ERROR_DECODING_FORMAT_UNSUPPORTED" -> context.getString(R.string.ui_video_error_decoding_unsupported)
        "MEDIA3_ERROR_DECODING_RESOURCES_RECLAIMED" -> context.getString(R.string.ui_video_error_decoding_resources_reclaimed)
        "MEDIA3_ERROR_AUDIO_TRACK_INIT_FAILED" -> context.getString(R.string.ui_video_error_audio_track_init_failed)
        "MEDIA3_ERROR_AUDIO_TRACK_WRITE_FAILED" -> context.getString(R.string.ui_video_error_audio_track_write_failed)
        "MEDIA3_ERROR_AUDIO_TRACK_OFFLOAD_WRITE_FAILED" -> context.getString(R.string.ui_video_error_audio_track_offload_write_failed)
        "MEDIA3_ERROR_AUDIO_TRACK_OFFLOAD_INIT_FAILED" -> context.getString(R.string.ui_video_error_audio_track_offload_init_failed)
        "MEDIA3_ERROR_VIDEO_FRAME_PROCESSOR_INIT_FAILED" -> context.getString(R.string.ui_video_error_video_frame_processor_init_failed)
        "MEDIA3_ERROR_VIDEO_FRAME_PROCESSING_FAILED" -> context.getString(R.string.ui_video_error_video_frame_processing_failed)
        "MEDIA3_ERROR_FAILED_RUNTIME_CHECK" -> context.getString(R.string.ui_video_error_failed_runtime_check)
        "MEDIA3_ERROR_DRM_UNSPECIFIED" -> context.getString(R.string.ui_video_error_drm_unspecified)
        "MEDIA3_ERROR_DRM_LICENSE_ACQUISITION_FAILED" -> context.getString(R.string.ui_video_error_drm_license_acquisition_failed)
        "MEDIA3_ERROR_DRM_DISALLOWED_OPERATION" -> context.getString(R.string.ui_video_error_drm_disallowed_operation)
        "MEDIA3_ERROR_DRM_SYSTEM_ERROR" -> context.getString(R.string.ui_video_error_drm_system_error)
        "MEDIA3_ERROR_DRM_DEVICE_REVOKED" -> context.getString(R.string.ui_video_error_drm_device_revoked)
        "MEDIA3_ERROR_DRM_LICENSE_EXPIRED" -> context.getString(R.string.ui_video_error_drm_license_expired)
        else -> error
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun VideoPlayerErrorPreview() {
    MyTvTheme {
        VideoPlayerError(
            errorProvider = { "MEDIA3_ERROR_IO_NETWORK_CONNECTION_FAILED(2001)" }
        )
    }
}