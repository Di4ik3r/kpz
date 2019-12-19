package model

import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.*
import model.RemovedGames.bindTo
import model.RemovedGames.primaryKey
import java.math.BigDecimal
import java.time.LocalDate

interface RemovedGame : Entity<RemovedGame> {
    val id: Int
    var name: String
    var price: BigDecimal
    var date: LocalDate
}

object RemovedGames : Table<RemovedGame>("removed_games") {

    val id by int("id").primaryKey().bindTo { it.id }
    val name by varchar("name").bindTo { it.name }
    val price by decimal("price").bindTo { it.price }
    val date by date("date").bindTo { it.date }
}