

package model

import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.*
import java.math.BigDecimal
import java.sql.Date
import java.time.LocalDate
import java.util.*


//
//class Game(id: Int, name: String, price: Float) {
//
//    var id: Int? = null
//    var name: String? = null
//    var price: Float? = null
//    var genres: Genres = Genres()
//    var developers: MutableList<Developer> = mutableListOf()
//
//    init {
//        this.id = id
//        this.name = name
//        this.price = price
//    }
//}

interface Game : Entity<Game> {
    val id: Int
    var name: String
    var price: BigDecimal
    var date: LocalDate
}

object Games : Table<Game>("games") {

    val id by int("id").primaryKey().bindTo { it.id }
    val name by varchar("name").bindTo { it.name }
    val price by decimal("price").bindTo { it.price }
    val date by date("date").bindTo { it.date }
}