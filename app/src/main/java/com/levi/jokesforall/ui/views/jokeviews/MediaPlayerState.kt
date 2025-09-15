package com.levi.jokesforall.ui.views.jokeviews

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.io.IOException

@Composable
fun rememberMediaPlayerState(
    context: Context,
    mediaResourceId: Int
): MediaPlayerState {
    return remember { MediaPlayerState(context, mediaResourceId) }
}

enum class PlayerState {
    UNPREPARED,
    PLAYING,
    PAUSED,
    ERROR
}

@Stable
class MediaPlayerState(
    private val context: Context,
    private val mediaResourceId: Int
) : DefaultLifecycleObserver {
    private var mediaPlayer: MediaPlayer? = null
    private var currentPlayerState: PlayerState = PlayerState.UNPREPARED
    private var enabled = false

    override fun onStart(owner: LifecycleOwner) {
        if (enabled) startOrResumePlayback()
    }

    override fun onStop(owner: LifecycleOwner) {
        pause()
    }

    fun startOrResumePlayback() {
        if (currentPlayerState == PlayerState.PAUSED) {
            mediaPlayer?.start()
            currentPlayerState = PlayerState.PLAYING
        } else if (currentPlayerState == PlayerState.UNPREPARED) {
            prepareAndStart()
        }
    }

    private fun prepareAndStart() {
        mediaPlayer = MediaPlayer().apply {
            setOnPreparedListener {
                isLooping = true
                enabled = true
                currentPlayerState = PlayerState.PLAYING
                start()
            }
            setOnErrorListener { _, what, extra ->
                Log.e(
                    TAG,
                    "MediaPlayer Error during preparation: what=$what, extra=$extra"
                )
                currentPlayerState = PlayerState.UNPREPARED
                true
            }
            try {
                val afd = context.resources.openRawResourceFd(mediaResourceId)
                    ?: throw IOException("Resource not found")
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                afd.close()
                prepareAsync()
            } catch (e: Exception) {
                Log.e(
                    TAG,
                    "Error setting data source or preparing async",
                    e
                )
                clearMediaPlayer()
                currentPlayerState = PlayerState.ERROR
            }
        }
    }

    fun pause() {
        if (currentPlayerState == PlayerState.PLAYING) {
            mediaPlayer?.pause()
            currentPlayerState = PlayerState.PAUSED
        }
    }

    fun pauseAndReset() {
        if (currentPlayerState == PlayerState.PLAYING) {
            mediaPlayer?.pause()
            mediaPlayer?.seekTo(0)
            currentPlayerState = PlayerState.PAUSED
        }
    }

    fun mute() {
        mediaPlayer?.setVolume(0f, 0f)
    }

    fun unMute() {
        mediaPlayer?.setVolume(1f, 1f)
    }

    fun setEnabled(isEnabled: Boolean) {
        enabled = isEnabled
    }

    fun clearMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    companion object {
        private const val TAG = "MediaPlayerState"
    }
}
