package com.cayzerok.game


import com.cayzerok.game.World.h
import com.cayzerok.game.World.w
import org.joml.Vector2f
import org.joml.Vector3f
import java.net.SocketAddress
import java.util.*

val playerList = Array(8){ Player(UUID.randomUUID()) }

fun stableFloat(number:Float): Float {
    return number*Statistics.frameTime/1000000
}

class Player(var uuid: UUID, var address: SocketAddress? = null) {
    var inUse = false
    val gun = Gun()
    var mustShoot = false
    var position = Vector3f(-300f, -300f, 0f)
    var lastCall:Int? = null
    var moveKeys = BooleanArray(4)
    var HP = 100
    var bulletArray = Array(30) { BulletNote(Vector3f(),0f, true) }
    var angle = 0f
    var playerAABB = AABB(Vector2f(position.x,position.y), Vector2f(World.scale*2/3,World.scale*2/3))

    fun setPos(pos:Vector3f) {
        position = pos
        playerAABB.center = Vector2f(position.x,position.y)
    }

    fun move(x: Float, y: Float, z: Float) {
        if (HP > 0) {
            position.add(stableFloat(x), stableFloat(y), stableFloat(z))
            if (position.x > World.scale + 0.5f)
                position.x = World.scale + 0.5f
            if (position.x < w + World.scale - 0.5f)
                position.x = w + World.scale - 0.5f

            if (position.y < -World.scale + 0.5f)
                position.y = -World.scale + 0.5f
            if (position.y > h - World.scale - 0.5f)
                position.y = h - World.scale - 0.5f
            val boxes = getBBoxes(position)
            playerAABB.center = Vector2f(position.x, position.y)
            try {
                boxes.forEach {
                    if (it != null)
                        if (playerAABB.isIntersecting(it)) {
                            position.add(-stableFloat(x), -stableFloat(y), -stableFloat(z))
                            playerAABB.center = Vector2f(position.x, position.y)
                        }
                }
                playerList.forEach {
                    if (this != it) {
                        if (playerAABB.isIntersecting(it.playerAABB)) {
                            position.add(-stableFloat(x), -stableFloat(y), -stableFloat(z))
                            playerAABB.center = Vector2f(position.x, position.y)
                        }
                    }
                }

            } catch (e: Throwable) {
                println("AABB ERROR")
            }
        }
    }
}