package com.cayzerok.io

import com.cayzerok.game.Player
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.util.*

data class Cell(
    val address: SocketAddress,
    val data:ByteArray
)

fun serializePlayerData(player: Player) : ByteArray {
    val data = ByteArray(26)
    val uuidBytes = getBytesFromUUID(player.uuid)
    uuidBytes.forEachIndexed { index, byte ->
        data[index] = byte }
    player.moveKeys.forEachIndexed{ index, it ->
        data[index+16] = (if (it) 1 else 0).toByte() }
    data[20] = (player.gun.cells).toByte()
    getByteArray(player.angle).forEachIndexed { index, byte ->
        data[index+21] = byte }
    data[25] = player.HP.toByte()
    return data
}

fun deserializePlayerData(data: ByteArray) : UserData {
    val uuidBytes = data.copyOfRange(0,16)
    val uuid = getUUIDFromBytes(uuidBytes)
    val moveKeys = BooleanArray(4)
    for (index in 0..3) {
        moveKeys[index] = (data[index+16] == 1.toByte())
    }
    val mustShoot = (data[20] == 1.toByte())
    val angle = getFloat(data.copyOfRange(21, 25))
    return UserData(uuid, moveKeys, mustShoot, angle)
}

fun getByteArray(value: Float): ByteArray {
    return ByteBuffer.allocate(4).putFloat(value).array()
}

fun getFloat(value:ByteArray) : Float {
    return ByteBuffer.wrap(value).float
}

fun getBytesFromUUID(uuid: UUID): ByteArray {
    val bb = ByteBuffer.wrap(ByteArray(16))
    bb.putLong(uuid.mostSignificantBits)
    bb.putLong(uuid.leastSignificantBits)
    bb.flip()
    return bb.array()
}

fun getUUIDFromBytes(bytes: ByteArray): UUID {
    val byteBuffer = ByteBuffer.wrap(bytes)
    val high = byteBuffer.long
    val low = byteBuffer.long

    return UUID(high, low)
}