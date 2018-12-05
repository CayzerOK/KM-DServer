
import com.cayzerok.core.gson
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.Datagram
import io.ktor.network.sockets.aSocket
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.io.core.BytePacketBuilder
import kotlinx.io.core.writeFully
import java.net.InetSocketAddress
import java.util.*

data class Cell(
    val user: UUID,
    var content:String)

val packet = BytePacketBuilder()

@KtorExperimentalAPI
fun main(args: Array<String>) {
    runBlocking {
        val myUUID = UUID.randomUUID()
        val socket = aSocket(ActorSelectorManager(Dispatchers.IO)).udp().connect(InetSocketAddress("127.0.0.1", 8080))
        while (true) {
            val output = socket.outgoing
            var text = readLine()
            if (text == "" || text == null) text = "TKOTKJOTKOTJO"
            val newCell = Cell(myUUID, text)
            val jsonData = gson.toJson(newCell)
            packet.writeFully(jsonData.toByteArray())
            output.send(Datagram(packet.build(), InetSocketAddress("localhost", 8080)))
            val input = socket.incoming.receive().packet
            //println(gson.fromJson(io.readText(),Cell::class.java).content)
            println(input.readText())
        }
    }
}