package com.cayzerok.io

import com.cayzerok.game.Player
import com.cayzerok.game.playerList
import org.joml.Vector3f
import java.net.SocketAddress
import java.time.LocalTime
import java.util.*

data class UserData(
    val uuid:UUID,
    val moveKeys:BooleanArray,
    val mustShoot:Boolean,
    val angle:Float
)

suspend fun addUser(address:SocketAddress) {
    val newUser = playerList.first { !it.inUse }
    newUser.inUse = true
    newUser.address = address
    newUser.lastCall = LocalTime.now().second
    newUser.position = Vector3f(-200f, 100f, 0f)
    println("User ${playerList.find { it == newUser }!!.uuid} connected")
    val uuidBytes = byteArrayOf(0)+getBytesFromUUID(newUser.uuid)
    var uuid = byteArrayOf()
    playerList.filter { it.inUse }.forEach {
        if (it.uuid != newUser.uuid) {
            outChannel.send(Cell(it.address!!, uuidBytes))
            uuid += getBytesFromUUID(it.uuid)
        }
    }
    outChannel.send(Cell(newUser.address!!,uuidBytes+uuid))
}

suspend fun removePlayer(nullPlayer: Player) {
    playerList.filter { it.inUse }.forEach {
        outChannel.send(Cell(it.address!!,byteArrayOf(5)+getBytesFromUUID(nullPlayer.uuid)))
    }
    println("User ${nullPlayer.uuid} disconnected")
    nullPlayer.inUse = false
    nullPlayer.setPos(Vector3f(-1000f,-1000f,0f))
    nullPlayer.HP = 100
    nullPlayer.mustShoot = false
    nullPlayer.angle = 0f
    nullPlayer.moveKeys = booleanArrayOf(false,false,false,false)
    nullPlayer.uuid = UUID.randomUUID()
    nullPlayer.address = null
}

suspend fun genericFrame(bytes:ByteArray, client:SocketAddress) {
    val data = deserializePlayerData(bytes)
    val player = playerList.first { it.address == client && it.uuid == data.uuid}
    player.mustShoot = data.mustShoot
    player.angle = data.angle
    player.moveKeys = data.moveKeys
    player.lastCall = LocalTime.now().second

    var answerBytes = byteArrayOf(1)
    playerList.filter{it.inUse}.forEach {it ->
        answerBytes += serializePlayerData(it)
    }
    outChannel.send(Cell(player.address!!, answerBytes))
}

suspend fun sendKeyFrame(uuid: UUID) {
    var keyBytes = byteArrayOf(2.toByte())
    playerList.filter { it.inUse }.forEach {
        keyBytes += getBytesFromUUID(it.uuid)
        keyBytes += getByteArray(it.position.x)
        keyBytes += getByteArray(it.position.y)
        keyBytes += getByteArray(it.position.z)
    }
    outChannel.send(Cell(playerList.first{it.uuid==uuid}.address!!,keyBytes))
}

