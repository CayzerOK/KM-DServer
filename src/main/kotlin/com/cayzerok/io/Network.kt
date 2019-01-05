package com.cayzerok.io

import com.cayzerok.game.playerList
import io.ktor.network.sockets.BoundDatagramSocket
import io.ktor.network.sockets.Datagram
import kotlinx.coroutines.channels.Channel
import kotlinx.io.core.BytePacketBuilder
import kotlinx.io.core.readBytes
import kotlinx.io.core.writeFully

val packet = BytePacketBuilder()
val outChannel = Channel<Cell>()

suspend fun getInput(server: BoundDatagramSocket) {
    while (true) {
        val input = server.incoming.receive()
        val inputBytes = input.packet.readBytes()
        when(inputBytes[0]) {
            0.toByte() -> addUser(input.address)
            1.toByte() -> genericFrame(inputBytes.copyOfRange(1, inputBytes.lastIndex+1), input.address)
            2.toByte() -> {} //key
            3.toByte() -> {} //damage
            4.toByte() -> playerList.first { it.address == input.address }.gun.reload = true
            5.toByte() -> removePlayer(playerList.first{it.address == input.address})
        }
    }
}

suspend fun sendOutput(server: BoundDatagramSocket) {
    while (true) {
        val outData = outChannel.receive()
        val output = server.outgoing
        packet.writeFully(outData.data)
        output.send(Datagram(packet.build(), outData.address))
    }
}