package com.routinealarm.helpers

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import com.routinealarm.GlobalData.Companion.appContext
import com.routinealarm.R
import com.routinealarm.R.raw

object SoundManager {

    private var soundsLibrary =  LinkedHashMap<String, MediaPlayer>()

    fun init(context: Context) {
        soundsLibrary[appContext.getString(R.string.bell)] = MediaPlayer.create(context, raw.bell)
        soundsLibrary[appContext.getString(R.string.blip)] = MediaPlayer.create(context, raw.blip)
        soundsLibrary[appContext.getString(R.string.chime)] = MediaPlayer.create(context, raw.chime)
        soundsLibrary[appContext.getString(R.string.drum)] = MediaPlayer.create(context, raw.drum)
        soundsLibrary[appContext.getString(R.string.gong)] = MediaPlayer.create(context, raw.gong)
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