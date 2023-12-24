package com.example.mymusic.exoplayer

import android.media.session.PlaybackState

inline val PlaybackState.isPrepared
    get() = state == PlaybackState.STATE_BUFFERING ||
            state == PlaybackState.STATE_PAUSED ||
            state == PlaybackState.STATE_PLAYING