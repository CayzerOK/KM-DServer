package com.cayzerok.game

import org.joml.Vector3f
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

val random = Random(10)

class Gun {
    var y = 0f
    var x = 0f
    val reloadTimer = Timer(250)
    val gunTimer = Timer(100)
    var lastpoints = -1
    var cells = 30
    var reload = false
    fun reloadGun(angle:Float) {
        y = 0.92f * sin(angle)
        x = 0.92f * cos(angle)
        if (reload) {
            reloadTimer.calculate()
            if (reloadTimer.points>=8) {
                cells = 30
                reload = false
                reloadTimer.`break`()
            }
        }
    }
}

fun shoot() {
    playerList.forEach { player ->
        if (player.mustShoot && player.HP>0) {
            player.gun.gunTimer.calculate()
            if (player.gun.lastpoints != player.gun.gunTimer.points && !player.gun.reload) {
                val bullet = player.bulletArray.firstOrNull { it.avalible }
                if (bullet != null) {
                    bullet.avalible = false
                    bullet.angle = (player.angle - Math.toRadians(90.0 + random.nextDouble(-1.0, 1.0))).toFloat()
                    bullet.coords = Vector3f(player.position.x + player.gun.y * World.scale * 2f, player.position.y - player.gun.x * World.scale * 2f, 0f)
                    player.gun.lastpoints = player.gun.gunTimer.points
                    player.gun.cells--
                    if (player.gun.cells < 1) {
                        player.gun.reload = true
                    }
                }
            }
        } else {
            player.gun.lastpoints = -1
            player.gun.gunTimer.`break`()
        }
    }
}