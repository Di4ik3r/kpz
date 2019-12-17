package model

import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.int
import model.GameDevelopers.bindTo
import model.GameDevelopers.primaryKey


interface GameGenre : Entity<GameGenre> {
    val id: Int
    val game: Int
    val genre: Int
}

object GameGenres : Table<GameGenre>("game_genres") {

    val id by int("id").primaryKey().bindTo { it.id }
    val game by int("game_id").bindTo { it.game }
    val genre by int("genre_id").bindTo { it.genre }
}