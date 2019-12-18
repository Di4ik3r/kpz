package ui.edit

import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.insert
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.update
import model.Game
import model.Games
import java.math.BigDecimal
import java.time.LocalDate
import javax.swing.AbstractListModel
import javax.swing.ComboBoxModel


class EditGamesPanel : TableTemplatePanel() {

    var currentGame = Games.select().map { Games.createEntity(it) }.first()

    init {
        println(currentGame)

        this.refresh()

        this.buttonUpdate.addActionListener {
            Games.update {
                it.name to textfieldTitle.text

                where {
                    it.id eq currentGame.id
                }
            }
            this.refresh()
        }

        this.buttonAdd.addActionListener {
            Games.insert {
                it.name to "game"
                it.price to BigDecimal.ZERO
                it.date to LocalDate.now()
            }

            val addedGame = Games.select().map { Games.createEntity(it) }.last()

            println(addedGame)
            this.currentGame = addedGame
            this.refresh()
        }

    }

    private fun refresh() {
        this.textfieldTitle.text = currentGame.name
    }
}



//internal class GamesComboBoxModel : AbstractListModel<Game>(), ComboBoxModel<Game> {
//    var array = Games.select(Games.id, Games.name).map { Games.createEntity(it) }
//
//    var selection: Game? = array.first()
//    override fun getElementAt(index: Int): Game {
//        return array[index]
//    }
//
//    override fun getSize(): Int {
//        return array.size
//    }
//
//    override fun setSelectedItem(anItem: Any) {
//        selection = anItem as Game // to select and register an
//    } // item from the pull-down list
//
//    // Methods implemented from the interface ComboBoxModel
//    override fun getSelectedItem(): Game {
//        return selection!! // to add the selection to the combo box
//    }
//}