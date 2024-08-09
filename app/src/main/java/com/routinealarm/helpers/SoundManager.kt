package com.routinealarm.helpers

import android.content.Context
import android.media.MediaPlayer
import com.routinealarm.R.raw

object SoundManager {

    private var soundsLibrary =  LinkedHashMap<String, MediaPlayer>()

    fun init(context: Context) {
        soundsLibrary["bell"] = MediaPlayer.create(context, raw.bell)
        soundsLibrary["blip"] = MediaPlayer.create(context, raw.blip)
        soundsLibrary["chime"] = MediaPlayer.create(context, raw.chime)
        soundsLibrary["drum"] = MediaPlayer.create(context, raw.drum)
        soundsLibrary["gong"] = MediaPlayer.create(context, raw.gong)
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