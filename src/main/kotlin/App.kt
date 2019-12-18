

import ui.login.LoginFrame

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

