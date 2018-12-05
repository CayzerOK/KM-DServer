package com.cayzerok.core

import com.cayzerok.game.users
import com.cayzerok.io.getInput
import com.cayzerok.io.sendOutput
import com.google.gson.Gson
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.aSocket
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.*
import java.net.InetSocketAddress
import java.util.*

val shouldClose = false
val gson = Gson()

data class Cell(
    val user:UUID,
    var content:Any)

class MainArgs(parser: ArgParser) {
    val newAddress by parser.storing("-a", "--address",
        help = "set IP-address").default("localhost")
    val newPort by parser.storing("-p", "--inputport",
        help = "set port").default { "8080" }
}

@ObsoleteCoroutinesApi
@KtorExperimentalAPI
fun main(args: Array<String>) = runBlocking {
    var address:String
    var port:Int
        ArgParser(args).parseInto(::MainArgs).run {
            address = newAddress
            port = newPort.toInt()
        }
    val server = aSocket(ActorSelectorManager(Dispatchers.IO)).udp().bind(InetSocketAddress(address, port ))
    println("Server started at: ${server.localAddress}")
    val input = launch { getInput(server) }
    val output = launch { sendOutput(server) }
    val checkUsers = launch{
        while (!shouldClose) {
            delay( 5000)
            users.forEach {
                if (it.UUID != null) {
                    try {
                        withTimeout(3000) {
                            var waiting = true
                            while (waiting) {
                                val answer = server.incoming.receive()
                                if (answer.address == it.address)
                                   waiting = false
                            }
                        }
                    } catch (e:TimeoutCancellationException) {
                        println("User ${it.UUID} disconnected")
                        it.UUID = null
                        it.address = null
                    }
                }
            }
        }
    }
    checkUsers.join()
    input.join()
    output.join()
}