package top.yogiczy.mytv.tv.ui.screensold.videoplayer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.LocalTextStyle
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import top.yogiczy.mytv.core.util.utils.humanizeBitrate
import top.yogiczy.mytv.tv.R
import top.yogiczy.mytv.tv.ui.screensold.videoplayer.player.VideoPlayer
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme

@Composable
fun VideoPlayerMetadata(
    modifier: Modifier = Modifier,
    metadataProvider: () -> VideoPlayer.Metadata = { VideoPlayer.Metadata() },
) {
    val metadata = metadataProvider()

    CompositionLocalProvider(
        LocalTextStyle provides MaterialTheme.typography.bodySmall,
        LocalContentColor provides MaterialTheme.colorScheme.onSurface,
    ) {
        Column(
            modifier = modifier
                .background(
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                    MaterialTheme.shapes.medium,
                )
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            metadata.video?.let { nnVideo ->
                Column {
                    Text(stringResource(R.string.ui_video_metadata_video), style = MaterialTheme.typography.titleMedium)
                    Column(modifier = Modifier.padding(start = 10.dp)) {
                        nnVideo.width?.let { nnWidth -> Text("${stringResource(R.string.ui_video_metadata_resolution)}: ${nnWidth}x${nnVideo.height}") }
                        nnVideo.color?.let { nnColor -> Text("${stringResource(R.string.ui_video_metadata_color_space)}: $nnColor") }
                        nnVideo.frameRate?.let { nnFrameRate -> Text("${stringResource(R.string.ui_video_metadata_frame_rate)}: $nnFrameRate") }
                        nnVideo.bitrate?.let { nnBitrate -> Text("${stringResource(R.string.ui_video_metadata_bitrate)}: ${nnBitrate.humanizeBitrate()}") }
                        nnVideo.mimeType?.let { nnMimeType -> Text("${stringResource(R.string.ui_video_metadata_encoding)}: $nnMimeType") }
                        nnVideo.decoder?.let { nnDecoder -> Text("${stringResource(R.string.ui_video_metadata_decoder)}: $nnDecoder") }
                    }
                }
            }

            metadata.audio?.let { nnAudio ->
                Column {
                    Text(stringResource(R.string.ui_video_metadata_audio), style = MaterialTheme.typography.titleMedium)
                    Column(modifier = Modifier.padding(start = 10.dp)) {
                        nnAudio.channels?.let { nnChannels -> Text("${stringResource(R.string.ui_video_metadata_channels)}: $nnChannels") }
                        nnAudio.sampleRate?.let { nnSampleRate -> Text("${stringResource(R.string.ui_video_metadata_sample_rate)}: $nnSampleRate Hz") }
                        nnAudio.bitrate?.let { nnBitrate -> Text("${stringResource(R.string.ui_video_metadata_bit_rate)}: ${nnBitrate.humanizeBitrate()}") }
                        nnAudio.mimeType?.let { nnMimeType -> Text("${stringResource(R.string.ui_video_metadata_encoding)}: $nnMimeType") }
                        nnAudio.decoder?.let { nnDecoder -> Text("${stringResource(R.string.ui_video_metadata_decoder)}: $nnDecoder") }
                    }
                }
            }
            metadata.subtitleTracks?.let { nnSubtitleTracks ->
                Column {
                    Text(stringResource(R.string.ui_video_metadata_subtitle_tracks), style = MaterialTheme.typography.titleMedium)
                    Column(modifier = Modifier.padding(start = 10.dp)) {
                        Text("${stringResource(R.string.ui_video_metadata_subtitle_count)}: ${nnSubtitleTracks.size}")
                        nnSubtitleTracks.forEach { nnSubtitle ->
                            nnSubtitle.language?.let { language -> Text("${stringResource(R.string.ui_video_metadata_language)}: $language") }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun VideoMetadataPreview() {
    MyTvTheme {
        VideoPlayerMetadata(
            metadataProvider = {
                VideoPlayer.Metadata(
                    video = VideoPlayer.Metadata.Video(
                        width = 1920,
                        height = 1080,
                        color = "BT2020/Limited range/HLG/8/8",
                        bitrate = 10605096,
                        mimeType = "video/hevc",
                        decoder = "c2.goldfish.h264.decoder",
                    ),

                    audio = VideoPlayer.Metadata.Audio(
                        channels = 2,
                        sampleRate = 32000,
                        bitrate = 256 * 1024,
                        mimeType = "audio/mp4a-latm",
                    ),
                )
            }
        )
    }
}