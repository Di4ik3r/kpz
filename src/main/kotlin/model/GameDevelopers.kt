package model

import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.*
import model.Games.bindTo
import model.Games.primaryKey
import java.math.BigDecimal
import java.time.LocalDate

interface GameDeveloper : Entity<GameDeveloper> {
    val id: Int
    val game: Int
    val developer: Int
}

object GameDevelopers : Table<GameDeveloper>("game_developers") {

    val id by int("id").primaryKey().bindTo { it.id }
    val game by int("game_id").bindTo { it.game }
    val developer by int("developer_id").bindTo { it.developer }
}