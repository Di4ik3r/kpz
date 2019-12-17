

package model

import io.requery.Entity


class Game(id: Int, name: String, price: Float) {

    var id: Int? = null
    var name: String? = null
    var price: Float? = null
    var genres: Genres = Genres()
    var developers: MutableList<Developer> = mutableListOf()

    init {
        this.id = id
        this.name = name
        this.price = price
    }
}