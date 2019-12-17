

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.*
import me.liuwj.ktorm.entity.EntitySequence
import me.liuwj.ktorm.entity.asSequence
import me.liuwj.ktorm.entity.forEach
import model.*
import ui.main.LoginFrame

fun main() {

//    GameDevelopers
//        .innerJoin(Developers, on = GameDevelopers.developer eq Developers.id)
//        .innerJoin(Games, on = GameDevelopers.game eq Games.id)
//        .select().asSequence().forEach {
//            println("${it[Games.name]}\t${it[Developers.name]}")
//        }

    val frame = LoginFrame()
    frame.isVisible = true

}

