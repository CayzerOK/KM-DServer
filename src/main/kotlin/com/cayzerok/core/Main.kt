package com.cayzerok.core

import com.cayzerok.game.layerList
import com.cayzerok.game.playerList
import com.cayzerok.io.getInput
import com.cayzerok.io.removePlayer
import com.cayzerok.io.sendKeyFrame
import com.cayzerok.io.sendOutput
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.aSocket
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.InetSocketAddress
import java.time.LocalTime

@KtorExperimentalAPI
fun main() = runBlocking {
    val server = aSocket(ActorSelectorManager(Dispatchers.IO)).udp().bind(InetSocketAddress(9090))
    println("Server started at: ${server.localAddress}")
    
    layerList.forEach { it.loadWorld() }

    val input = launch(Dispatchers.IO) { getInput(server) }
    val output = launch(Dispatchers.IO) { sendOutput(server) }
    val checkUsers = launch(Dispatchers.IO) {
        while (true) {
            delay( 5000)
            playerList.filter { it.inUse }.forEach { player ->
                if (LocalTime.now().second - player.lastCall!! >=3){
                   removePlayer(player)
                } else sendKeyFrame(player.uuid)
            }
        }
    }
    val game = launch{ mainLoop()}
    checkUsers.join()
    input.join()
    game.join()
    output.join()
}