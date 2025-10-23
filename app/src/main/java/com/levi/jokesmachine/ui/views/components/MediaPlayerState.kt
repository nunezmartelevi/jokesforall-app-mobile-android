package com.levi.jokesmachine.ui.views.components

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.io.IOException

/**
 * A Composable side-effect that manages the volume of a [MediaPlayerState] instance.
 * It observes the [isSoundOn] boolean and mutes or unmutes the media player accordingly.
 * This is useful for toggling sound on/off in the UI.
 *
 * @param isSoundOn A boolean indicating whether the sound should be on (`true`) or off (`false`).
 * @param mediaPlayerState The [MediaPlayerState] instance to control.
 */
@Composable
fun MediaPlayerVolumeEffect(
    isSoundOn: Boolean,
    mediaPlayerState: MediaPlayerState
) {
    LaunchedEffect(isSoundOn) {
        if (isSoundOn) {
            mediaPlayerState.unMute()
        } else {
            mediaPlayerState.mute()
        }
    }
}

/**
 * Creates and remembers a [MediaPlayerState] instance. This function is lifecycle-aware when
 * the returned [MediaPlayerState] is observed by a [LifecycleOwner].
 *
 * This composable uses `remember` to ensure that the same `MediaPlayerState` instance is
 * retained across recompositions.
 *
 * @param context The application context, used for accessing resources and creating the `MediaPlayer`.
 * @param mediaResourceId The raw resource ID of the media file to be played.
 * @return A remembered instance of [MediaPlayerState].
 */
@Composable
fun rememberMediaPlayerState(
    context: Context,
    mediaResourceId: Int
): MediaPlayerState {
    return remember { MediaPlayerState(context, mediaResourceId) }
}

enum class PlayerState {
    UNPREPARED,
    PREPARED,
    ERROR
}

/**
 * A state holder for managing a [MediaPlayer] instance, designed to be used within
 * Jetpack Compose. This class encapsulates the player's state and behavior,
 * and it observes the lifecycle of a [LifecycleOwner] to automatically manage
 * the player's resources.
 *
 * It prepares the player in `onStart` and releases it in `onStop` to prevent
 * resource leaks and handle background/foreground transitions gracefully.
 *
 * This class is marked as `@Stable` to ensure that Compose can make optimization
 * decisions, as its public properties and methods will not change in a way that
 * would affect UI recomposition unpredictably.
 *
 * Example usage with a Composable:
 * ```
 * val context = LocalContext.current
 * val lifecycleOwner = LocalLifecycleOwner.current
 * val mediaPlayerState = rememberMediaPlayerState(context, R.raw.my_sound)
 *
 * DisposableEffect(lifecycleOwner) {
 *     lifecycleOwner.lifecycle.addObserver(mediaPlayerState)
 *     onDispose {
 *         lifecycleOwner.lifecycle.removeObserver(mediaPlayerState)
 *         mediaPlayerState.clearMediaPlayer()
 *     }
 * }
 * ```
 *
 * @param context The application context, used for accessing resources.
 * @param mediaResourceId The raw resource ID of the media file to be played.
 */
@Stable
class MediaPlayerState(
    private val context: Context,
    private val mediaResourceId: Int
) : DefaultLifecycleObserver {
    private var mediaPlayer: MediaPlayer? = null
    private var currentPlayerState: PlayerState = PlayerState.UNPREPARED
    private var resumingOnStartEnabled = false
    private var loopingEnabled = false
    private var currentPlaybackPosition = 0
    private var isMuted = false


    override fun onStart(owner: LifecycleOwner) {
        prepare(shouldStartPlaybackAfterPrepared = resumingOnStartEnabled)
    }

    override fun onStop(owner: LifecycleOwner) {
        currentPlaybackPosition = mediaPlayer?.currentPosition ?: 0
        clearMediaPlayer()
    }

    fun prepare(shouldStartPlaybackAfterPrepared: Boolean = false) {
        if (currentPlayerState != PlayerState.UNPREPARED) return

        mediaPlayer = MediaPlayer().apply {
            setOnPreparedListener {
                currentPlayerState = PlayerState.PREPARED
                if (isMuted) mute()
                if (shouldStartPlaybackAfterPrepared) startOrResumePlayback()
            }
            setOnErrorListener { _, what, extra ->
                Log.d(
                    TAG,
                    "MediaPlayer Error during preparation: what=$what, extra=$extra"
                )
                true
            }

            try {
                val afd = context.resources.openRawResourceFd(mediaResourceId)
                    ?: throw IOException("Resource not found for ID: $mediaResourceId")
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                afd.close()
                prepareAsync()
            } catch (e: Exception) {
                Log.e(
                    TAG,
                    "Error setting data source or preparing async",
                    e
                )
                currentPlayerState = PlayerState.ERROR
            }
        }
    }

    fun startOrResumePlayback() {
        mediaPlayer?.let { mp ->
            if (currentPlayerState == PlayerState.PREPARED && !mp.isPlaying) {
                mp.isLooping = loopingEnabled
                mp.seekTo(currentPlaybackPosition)
                mp.start()
            }
        }
    }

    fun pausePlayback() {
        mediaPlayer?.let { mp ->
            if (mp.isPlaying) {
                mp.pause()
            }
        }
    }

    fun resetPlaybackPosition() {
        currentPlaybackPosition = 0
    }

    fun mute() {
        mediaPlayer?.setVolume(0f, 0f)
        isMuted = true
    }

    fun unMute() {
        mediaPlayer?.setVolume(1f, 1f)
        isMuted = false
    }

    fun setResumingOnStartEnabled(isEnabled: Boolean) {
        resumingOnStartEnabled = isEnabled
    }

    fun setLoopingEnabled(isEnabled: Boolean) {
        loopingEnabled = isEnabled
    }

    fun clearMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
        currentPlayerState = PlayerState.UNPREPARED
        Log.d(TAG, "MediaPlayer cleared")
    }

    companion object {
        private const val TAG = "MediaPlayerState"
    }
}
