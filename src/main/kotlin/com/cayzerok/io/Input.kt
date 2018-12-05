package com.cayzerok.io

import com.cayzerok.core.Cell
import com.cayzerok.core.gson
import com.cayzerok.core.shouldClose
import com.cayzerok.game.User
import com.cayzerok.game.users
import io.ktor.network.sockets.BoundDatagramSocket
import io.ktor.network.sockets.Datagram
import kotlinx.coroutines.channels.Channel

val inChannel = Channel<Datagram>()

suspend fun getInput(server:BoundDatagramSocket) {
    while (!shouldClose) {
        val input = server.incoming.receive()
        val inCell = gson.fromJson(input.packet.readText(), Cell::class.java)
        if (!users.contains(User(inCell.user,input.address))){

            val thisUser = users.findLast { it.UUID == inCell.user || it.address == input.address }
            if (thisUser == null) {

                val newUser = users.first { it.UUID == null}
                newUser.UUID = inCell.user
                newUser.address  = input.address
                println("New user: ${users.find { it == newUser }}")

            }else{
                thisUser.UUID = inCell.user
                thisUser.address = input.address
                println("User data corrected")
            }
        }
        outChannel.send(inCell)
    }
}