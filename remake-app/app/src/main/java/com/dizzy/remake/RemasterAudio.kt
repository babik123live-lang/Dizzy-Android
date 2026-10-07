package com.dizzy.remake

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlin.math.PI
import kotlin.math.sin

/**
 * Native deterministic audio clock for the remake.
 * 48 kHz stereo PCM; game events enqueue short cues without changing game speed.
 */
class RemasterAudio {
    private val rate = 48_000
    private var track: AudioTrack? = null
    @Volatile private var jumpFrames = 0

    fun start() {
        if (track != null) return
        val min = AudioTrack.getMinBufferSize(rate, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT)
        try {
        track = AudioTrack.Builder()
            .setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
            .setAudioFormat(AudioFormat.Builder().setSampleRate(rate).setChannelMask(AudioFormat.CHANNEL_OUT_STEREO).setEncoding(AudioFormat.ENCODING_PCM_16BIT).build())
            .setBufferSizeInBytes((min * 2).coerceAtLeast(8192))
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build().also { it.play() }
        } catch (_: UnsupportedOperationException) {
            track = null
            return
        } catch (_: IllegalArgumentException) {
            track = null
            return
        } catch (_: IllegalStateException) {
            track = null
            return
        }
        Thread({ pump() }, "DizzyAudio").apply { isDaemon = true; start() }
    }

    fun jump() { jumpFrames = rate / 9 }

    private fun pump() {
        val buf = ShortArray(2048)
        var phase = 0L
        while (track != null) {
            for (i in buf.indices step 2) {
                var s = 0.0
                if (jumpFrames > 0) {
                    val f = 330.0 + (1.0 - jumpFrames.toDouble() / (rate / 9)) * 250.0
                    s = sin(2.0 * PI * f * phase / rate) * 0.12
                    jumpFrames--
                }
                val v = (s * Short.MAX_VALUE).toInt().toShort()
                buf[i] = v; buf[i + 1] = v; phase++
            }
            track?.write(buf, 0, buf.size, AudioTrack.WRITE_BLOCKING)
        }
    }

    fun stop() {
        val old = track
        track = null
        old?.pause(); old?.flush(); old?.release()
    }
}
