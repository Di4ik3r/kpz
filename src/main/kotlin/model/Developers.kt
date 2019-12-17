package model

import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.*
import model.Games.bindTo
import model.Games.primaryKey
import java.math.BigDecimal
import java.time.LocalDate


interface Developer : Entity<Developer> {
    val id: Int
    var name: String
    var role: Int
}

object Developers : Table<Developer>("developers") {

    val id by int("id").primaryKey().bindTo { it.id }
    val name by varchar("name").bindTo { it.name }
    val role by int("role").bindTo { it.role }
}