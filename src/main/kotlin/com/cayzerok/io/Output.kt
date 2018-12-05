package com.cayzerok.io

import com.cayzerok.core.Cell
import com.cayzerok.core.gson
import com.cayzerok.core.shouldClose
import com.cayzerok.game.users
import io.ktor.network.sockets.BoundDatagramSocket
import io.ktor.network.sockets.Datagram
import kotlinx.coroutines.channels.Channel
import kotlinx.io.core.BytePacketBuilder
import kotlinx.io.core.writeFully

val packet = BytePacketBuilder()
val outChannel = Channel<Cell>()

suspend fun sendOutput(server: BoundDatagramSocket) {
    println("SEND")
    while (!shouldClose) {
        val outData = outChannel.receive()
        val output = server.outgoing
        val outJSON = gson.toJson(outData)
        packet.writeFully(outJSON.toByteArray())
        output.send(Datagram(packet.build(), users.find { it.UUID == outData.user }!!.address!!))
        println("SENDED")
    }
}