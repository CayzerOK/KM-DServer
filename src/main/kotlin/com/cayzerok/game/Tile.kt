package com.cayzerok.game

import org.joml.Vector2f

val tiles = Array<Tile?>(255) {null}

class Tile(var id:Int,val center:Vector2f? = null, val hExtend:Vector2f? = null) {
    init {
        if (tiles[id] != null) {
            throw IllegalStateException("Tile id [$id] is already used.")
        } else {
            tiles[id] = this
        }
    }
}


object TileList{
    val stone0 = Tile(1)
    val stone1 = Tile(2)
    val stone2 = Tile(3)
    val stone3 = Tile(4)
    val grass0 = Tile(5)
    val grass1 = Tile(6)
    val grass2 = Tile(7)
    val grass3 = Tile(8)
    val sand0 = Tile(9)
    val sand1 = Tile(10)
    val sand2 = Tile(11)
    val sand3 = Tile(12)
    val waterTile = Tile(13)
    val aim = Tile(14)
    val waypoint = Tile(15)
    val intWall0 = Tile(16, Vector2f(1.6f,0f), Vector2f(World.scale/5,World.scale))
    val intWall1 = Tile(17, Vector2f(1.6f, 1.6f), Vector2f(World.scale/5,World.scale/5))
}

