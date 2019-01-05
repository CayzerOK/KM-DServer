package com.cayzerok.game

import org.joml.Vector2f
import org.joml.Vector3f
import kotlin.math.cos
import kotlin.math.sin


class AABB(var center:Vector2f, private val hExtend:Vector2f) {

    fun isIntersecting(box2:AABB):Boolean {
        val distance = box2.center.sub(center, Vector2f())
        distance.x=Math.abs(distance.x)
        distance.y=Math.abs(distance.y)

        distance.sub(hExtend.add(box2.hExtend, Vector2f()))

        return (distance.x < 0 && distance.y < 0)
    }
}

private var index = 0
fun getBBoxes(position: Vector3f): Array<AABB?> {
    val boxes = Array<AABB?>(25*layerList.size) {null}
    index = 0
    layerList.forEach { it ->
        for (x in (-position.x / (World.scale * 2) + 0.5).toInt() - 1..(-position.x / (World.scale * 2) + 0.5).toInt() + 1)
            for (y in (position.y / (World.scale * 2) + 0.5).toInt() - 1..(position.y / (World.scale * 2) + 0.5).toInt() + 1) {
                val thisTile = it.getTile(x, y)
                val thisAngle = it.getAngle(x, y)
                if (thisTile != null)
                    if (thisTile.hExtend != null) {

                        val center = Vector2f(-x * (World.scale * 2), y * (World.scale * 2))
                        val bCenter = Vector2f(rotateVec2f(thisTile.center!!, thisAngle!!)).mul(World.scale/2)

                        if (thisAngle != 90.0 && thisAngle != 270.0) {
                            boxes[index] = AABB(Vector2f(
                                center.x+bCenter.x,
                                center.y+bCenter.y),
                                Vector2f(thisTile.hExtend))
                        } else {
                            boxes[index] = AABB(Vector2f(
                                center.x+bCenter.x,
                                center.y+bCenter.y),
                                Vector2f(thisTile.hExtend.y, thisTile.hExtend.x))
                        }
                        index++
                    }
            }
    }
    return boxes
}


fun rotateVec2f(vec:Vector2f, angle:Double): Vector2f {
    val radians = Math.toRadians(angle)
    val sin = sin(radians)
    val cos = cos(radians)

    return Vector2f((cos * vec.x - sin * vec.y).toFloat(), (sin * vec.x + cos * vec.y).toFloat())
}