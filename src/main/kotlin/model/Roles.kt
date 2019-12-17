package model

import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.*
import model.Games.bindTo
import model.Games.primaryKey
import java.math.BigDecimal
import java.time.LocalDate


interface Role : Entity<Role> {
    val id: Int
    var name: String
}

object Roles : Table<Role>("roles") {

    val id by int("id").primaryKey().bindTo { it.id }
    val name by varchar("name").bindTo { it.name }
}