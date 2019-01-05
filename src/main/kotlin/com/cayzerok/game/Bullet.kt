package com.cayzerok.game

import org.joml.Vector2f
import org.joml.Vector3f
import kotlin.math.cos
import kotlin.math.sin


data class BulletNote(
        var coords:Vector3f,
        var angle:Float,
        var avalible:Boolean
)

object Bullet {
    var bAABB = AABB(Vector2f(), Vector2f())

    fun renderIt(bullet: BulletNote): Boolean {
        val y = 400 * sin(bullet.angle)
        val x = 400 * cos(bullet.angle)
        val boxes = getBBoxes(bullet.coords)
        bAABB = AABB(Vector2f(bullet.coords.x, bullet.coords.y), Vector2f(0.06f * World.scale, 0.06f * World.scale))

        bullet.coords.add(stableFloat(x), stableFloat(y), stableFloat(0f))
        try {
            boxes.forEach {
                if (it != null)
                    if (bAABB.isIntersecting(it)) {
                        return true
                    }
            }
            playerList.forEach { player ->
                if (bAABB.isIntersecting(player.playerAABB)) {
                    player.HP -= 10
                    if (player.HP<1) {
                        player.setPos(Vector3f(-300f, -300f, 0f))
                    }
                    return true
                }
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
        }
        return false
    }
}