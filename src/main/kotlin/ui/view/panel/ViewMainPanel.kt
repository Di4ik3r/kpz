package ui.view.panel

import javafx.beans.binding.Bindings.select
import me.liuwj.ktorm.dsl.*
import me.liuwj.ktorm.entity.asSequence
import me.liuwj.ktorm.entity.count
import me.liuwj.ktorm.entity.elementAt
import model.*
import java.awt.BorderLayout
import java.math.BigDecimal
import java.time.LocalDate
import javax.swing.*
import javax.swing.event.ListSelectionEvent
import javax.swing.table.DefaultTableColumnModel
import javax.swing.table.DefaultTableModel

class ViewMainPanel : JPanel() {

    val tableGames: JTable
    val tableGamesDevelopers: JTable
    val tableGamesGenres: JTable

    private val columnsGames = arrayOf("id", "name", "price", "date")
    private val columnsGamesDevelopers = arrayOf("id", "name", "role")
    private val columnsGamesGenres = arrayOf("id", "name")

    init {
        this.layout = BoxLayout(this, BoxLayout.LINE_AXIS)

        tableGamesGenres = JTable()
        tableGamesDevelopers = JTable()


        val games = Games.asSequence()
        val arrayGames = Array(games.count()) { i->
            arrayOf(games.elementAt(i).id, games.elementAt(i).name, games.elementAt(i).price, games.elementAt(i).date)
        }
        val modelGames = DefaultTableModel(arrayGames, columnsGames)
        tableGames = JTable(modelGames)
        tableGames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)

        val selectionModel = tableGames.selectionModel
        selectionModel.addListSelectionListener(GamesListSelectionListener())


        val panelGames = JPanel(); panelGames.layout = BorderLayout()
        panelGames.add(JScrollPane(tableGames), BorderLayout.CENTER)
        this.add(panelGames)
        this.add(JScrollPane(tableGamesDevelopers))
        this.add(JScrollPane(tableGamesGenres))
    }

    fun refreshTable() {

    }

    inner class GamesListSelectionListener: DefaultTableColumnModel() {

        var previousItem = tableGames.selectedRow
        override fun fireColumnSelectionChanged(e: ListSelectionEvent?) {
            super.fireColumnSelectionChanged(e)
            val row = tableGames.selectedRow

            if(row != previousItem) {
                val id = Integer.parseInt(tableGames.getValueAt(row, 0).toString())
                val name = tableGames.getValueAt(row, 1).toString()
                val price = BigDecimal.valueOf(tableGames.getValueAt(row, 2).toString().toDouble())
                val date = tableGames.getValueAt(row, 3) as LocalDate

                val game = Games.select()
                    .where {
                        (Games.id eq id) and (Games.name eq name) and (Games.price eq price) and(Games.date eq date)
                    }
                    .map { Games.createEntity(it) }
                    .asSequence()
                    .first()

                val developers = GameDevelopers
                    .innerJoin(Games, on = GameDevelopers.game eq Games.id)
                    .innerJoin(Developers, on = GameDevelopers.developer eq Developers.id)
                    .innerJoin(Roles, on = Developers.role eq Roles.id)
                    .select()
                    .where {
                        Games.id eq game.id
                    }


                var arrayDevelopers = Array(developers.rowSet.size()) { Array(3) {""} }
                var indexer = 0
                for(row in developers.rowSet) {
                    arrayDevelopers[indexer++] = arrayOf(
                        row[Developers.id].toString(),
                        row[Developers.name].toString(),
                        row[Roles.name].toString())
                }

                val modelGamesDevelopers = DefaultTableModel(arrayDevelopers, columnsGamesDevelopers)
                tableGamesDevelopers.model = modelGamesDevelopers







                val genres = GameGenres
                    .innerJoin(Games, on = GameGenres.game eq Games.id)
                    .innerJoin(Genres, on = GameGenres.genre eq Genres.id)
                    .select()
                    .where {
                        Games.id eq game.id
                    }


                var arrayGenres = Array(genres.rowSet.size()) { Array(2) {""} }
                indexer = 0
                for(row in genres.rowSet) {
                    arrayGenres[indexer++] = arrayOf(
                        row[Genres.id].toString(),
                        row[Genres.name].toString())
                }

                val modelGamesGenres = DefaultTableModel(arrayGenres, columnsGamesGenres)
                tableGamesGenres.model = modelGamesGenres
            }
            previousItem = row
        }
    }
}