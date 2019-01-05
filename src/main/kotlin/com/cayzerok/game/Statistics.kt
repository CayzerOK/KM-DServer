package com.cayzerok.game

object Statistics {
    private var lastFrame: Long = 0
    private var thisFrame: Long = 0
    var frameTime: Long = 0
    private var millis: Long = 0
    private var frames: Int = 0
    var frameRate: Int = 0

    fun readFrameRate() {
        lastFrame = thisFrame
        while (thisFrame< lastFrame+8000)
            thisFrame = System.nanoTime()/100
        frameTime = thisFrame - lastFrame
        millis += frameTime
        frames++
        if (millis >= 50000000) {
            millis = 0
            frameRate = frames/5
            frames = 0
//            println("FPS: ${frameRate/10}")
//            println("FrameTime: ${frameTime/1000}")
        }
    }
}


class Timer(val pointRate:Int) {
    var points = 0
    var lastFrame: Float = 0f
    var thisFrame: Float = 0f
    var frameTime: Float = 0f
    var milis: Float = 0f
    private var frames: Int = 0
    var frameRate: Int = 0

    fun calculate() {
        lastFrame = thisFrame
        thisFrame = System.nanoTime() * 0.000001.toFloat()
        frameTime = thisFrame - lastFrame
        milis += frameTime
        frames++
        if (milis >= pointRate) {
            points ++
            milis = 0.0f
            frameRate = frames
            frames = 0
        }
    }
    fun `break`() {
        points = 0
        lastFrame = 0f
        thisFrame = 0f
        frameTime = 0f
        frameRate = 0
        milis = 0f
        frames = 0
    }
}