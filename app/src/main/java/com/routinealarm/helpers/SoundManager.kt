package com.routinealarm.helpers

import android.content.Context
import android.media.MediaPlayer
import com.routinealarm.GlobalData.Companion.appContext
import com.routinealarm.R
import com.routinealarm.R.raw

var soundList = mutableMapOf(
    appContext.getString(R.string.alarm) to raw.alarm,
    appContext.getString(R.string.alien) to raw.alien,
    appContext.getString(R.string.arp) to raw.arp,
    appContext.getString(R.string.blip) to raw.blip,
    appContext.getString(R.string.bell) to raw.bell,
    appContext.getString(R.string.busy) to raw.busy,
    appContext.getString(R.string.chord) to raw.chord,
    appContext.getString(R.string.dtmf) to raw.dtmf,
    appContext.getString(R.string.gliss) to raw.gliss,
    appContext.getString(R.string.gong) to raw.gong,
    appContext.getString(R.string.pad) to raw.pad,
    appContext.getString(R.string.space) to raw.space,
    appContext.getString(R.string.tune)  to raw.tune,
)

object SoundManager {

    private var soundsLibrary =  LinkedHashMap<String, MediaPlayer>()

    fun init(context: Context) {
        soundList.forEach{ (key, value) ->
            soundsLibrary[key] = MediaPlayer.create(context, value)
        }
    }

    fun play(sound : String, reps : Int) {
        var numReps = reps-1
        soundsLibrary[sound]?.setOnCompletionListener {
            if(numReps-->0) {
                soundsLibrary[sound]?.seekTo(0)
                soundsLibrary[sound]?.start()
            }
        }
        soundsLibrary[sound]?.start() // First time trigger
    }
}