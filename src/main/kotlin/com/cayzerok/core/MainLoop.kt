package com.cayzerok.core

import com.cayzerok.game.*
import org.joml.Vector3f

fun mainLoop() {
    while (true) {
        Statistics.readFrameRate()
        playerList.filter { it.inUse }.forEach {
            if (it.moveKeys[0]) it.move(0f,-30f,0f)
            if (it.moveKeys[1]) it.move(30f,0f,0f)

            if (it.moveKeys[2]) it.move(0f,30f,0f)
            if (it.moveKeys[3]) it.move(-30f,0f,0f)
        }
        playerList.forEach { player ->
            player.bulletArray.forEach {
                if (getLength(it.coords, player.position)>World.scale/2)
                    it.avalible = true
                if (!it.avalible)
                    it.avalible = Bullet.renderIt(it)
            }
        }
        playerList.forEach { it.gun.reloadGun(it.angle) }
        shoot()
    }
}

fun getLength(tar1: Vector3f, tar2: Vector3f): Float {
    val vec = tar1.sub(tar2, Vector3f())
    return vec.length()/ World.scale*2
}