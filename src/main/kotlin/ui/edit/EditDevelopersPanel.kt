package ui.edit

import me.liuwj.ktorm.dsl.*
import model.*
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import java.math.BigDecimal
import java.time.LocalDate
import javax.swing.*


class EditDevelopersPanel(val editFrame: EditFrame) : TableTemplatePanel<Games>(editFrame) {


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