package com.cayzerok.game

import com.google.gson.Gson
import java.io.File
import java.io.FileNotFoundException

val layerList = listOf(
    Layer("main1"),
    Layer("main2"),
    Layer("main3"),
    Layer("main4")
)
val gson = Gson()
object World{
    const val width = 64
    const val height = 64
    var scale = 72f

    val w = -World.width*World.scale*2
    val h = World.height*World.scale*2
}

class Layer(private val name:String){

    private var sheet:Array<Int> = Array(World.width*World.height) {0}
    private var angleSheet:Array<Double?> =  Array(World.width*World.height) {0.0}


    fun getAngle(x:Int,y:Int): Double? {
        return try {
            angleSheet[x+y*World.width]
        } catch (e:ArrayIndexOutOfBoundsException) {
            null
        }
    }

    fun getTile(x:Int,y:Int): Tile? {
        return try {
            tiles[sheet[x+y*World.width]]
        } catch (e:ArrayIndexOutOfBoundsException) {
            null
        }
    }


    //val path = File(MainWindow::class.java.protectionDomain.codeSource.location.toURI()).path.dropLast(14)
    val path = "./src/main/resources/"

    fun loadWorld() {
        try {
            sheet = gson.fromJson(File(path+"levels/"+name+".lvl").readText(), Array<Int>::class.java)
            angleSheet = gson.fromJson(File(path+"levels/"+name+".ang").readText(),Array<Double?>::class.java)
            println(angleSheet[0])
        } catch (e: FileNotFoundException){}

    }
    fun saveWorld() {
        val lvlString = gson.toJson(sheet)
        val angleString = gson.toJson(angleSheet)
        File(path+"levels/"+name+".lvl").writeText(lvlString)
        File(path+"levels/"+name+".ang").writeText(angleString)
    }
}