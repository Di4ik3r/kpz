package model

import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar
import model.Roles.bindTo
import model.Roles.primaryKey

interface Admin : Entity<Admin> {
    val id: Int
    var name: String
    var password: String
}

object Admins : Table<Admin>("admins") {

    val id by int("id").primaryKey().bindTo { it.id }
    val name by varchar("name").bindTo { it.name }
    val password by varchar("password").bindTo { it.password }
}