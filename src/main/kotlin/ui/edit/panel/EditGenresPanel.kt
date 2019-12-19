package ui.edit.panel


import me.liuwj.ktorm.dsl.*
import model.*
import ui.edit.EditFrame
import ui.edit.TablePickFrame
import ui.edit.TableTemplatePanel
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import javax.swing.*


class EditGenresPanel(val editFrame: EditFrame) : TableTemplatePanel<Genre>(editFrame) {

    var currentGenre = Genres.select().map { Genres.createEntity(it) }.first()
    val frameGenrePick = TablePickFrame<Genres>(frameContext)

    init {
        println(currentGenre)

        this.refresh()

        this.panelCenterCenter.add(Box.createRigidArea(Dimension(0, 100)))

        val panelFields = JPanel(); panelFields.layout = FlowLayout()
        this.panelCenterCenter.add(panelFields)


        val panelDate = JPanel(); panelDate.layout = FlowLayout()
        this.panelCenterCenter.add(panelDate)
        this.panelCenterCenter.add(Box.createRigidArea(Dimension(0, 100)))

        val panelButtonGenre = JPanel(); panelButtonGenre.layout = FlowLayout()
        this.panelCenterLeft.add(panelButtonGenre, BorderLayout.SOUTH)
        val panelRemoveGenre = JPanel(); panelRemoveGenre.layout = FlowLayout()


        // ******************************************************* BUTTON SEARCH
        this.textfieldSearch.addKeyListener(SearchKeyAdapter())

        // ******************************************************* BUTTON REMOVE
        this.buttonRemove.addActionListener {
            GameGenres.delete { it.genre eq currentGenre.id }
            Genres.delete { it.id eq currentGenre.id }
            if(Genres.count() < 1) {
                Genres.insert {
                    it.name to "Genre"
                }
            }

            currentGenre = Genres.select()
                .map { Genres.createEntity(it)}
                .asSequence()
                .last()

            refresh()
        }


        // ******************************************************* BUTTON PICK
        this.frameGenrePick.buttonPick.addActionListener {
            if(frameGenrePick.itemsList.selectedIndex < 0) {
                JOptionPane.showMessageDialog(
                    frameGenrePick,
                    "There is must be selected item."
                )
                return@addActionListener
            }
            this.currentGenre = this.frameGenrePick.itemsList.selectedValue as Genre

            this.frameGenrePick.isVisible = false
            this.frameContext.isVisible = true

            refresh()
        }

        this.frameGenrePick.addWindowListener(GenreChooseListener())

        this.buttonPick.addActionListener {
            this.frameGenrePick.isVisible = true
            frameContext.isVisible = false
        }


        // ******************************************************* BUTTON UPDATE
        this.buttonUpdate.addActionListener {
            try {
                Genres.update {
                    it.name to textfieldTitle.text

                    where {
                        it.id eq currentGenre.id
                    }
                }

                currentGenre = Genres.select()
                    .map { Genres.createEntity(it) }
                    .asSequence()
                    .first {
                        it.id == currentGenre.id
                    }

                this.refresh()
            } catch(ex: Exception) {
                ex.printStackTrace()
                JOptionPane.showMessageDialog(
                    frameGenrePick,
                    "Fields has incorrect values"
                )
            }
        }


        // ******************************************************* BUTTON ADD
        this.buttonAdd.addActionListener {
            Genres.insert {
                it.name to "Genre"
            }

            val addedGenre = Genres.select().map { Genres.createEntity(it) }.last()

            println(addedGenre)
            this.currentGenre = addedGenre
            this.refresh()
        }
    }

    fun refresh() {
        this.textfieldTitle.text = currentGenre.name
    }


    inner class SearchKeyAdapter() : KeyAdapter() {

        override fun keyPressed(e: KeyEvent?) {
            super.keyPressed(e)
            if(e?.keyCode == KeyEvent.VK_ENTER) {
                val input = textfieldSearch.text
                try {
                    val id = Integer.parseInt(input)

                    val Genres = Genres.select()
                        .where { Genres.id eq id }
                        .map { Genres.createEntity(it) }
                        .asSequence()

                    if(Genres.count() > 0) {
                        currentGenre = Genres.first()
                        refresh()
                    } else {
                        JOptionPane.showMessageDialog(
                            null,
                            "Not found"
                        )
                    }

                } catch(ex: Exception) {
                    val Genres = Genres.select()
                        .where { Genres.name like input }
                        .map { Genres.createEntity(it) }
                        .asSequence()

                    if(Genres.count() > 0) {
                        currentGenre = Genres.first()
                        refresh()
                    } else {
                        JOptionPane.showMessageDialog(
                            null,
                            "Not found"
                        )
                    }
                }

                textfieldSearch.text = ""
            }
        }
    }



    inner class GenreChooseListener : WindowListener {

        override fun windowActivated(e: WindowEvent?) {
            val Genres = Genres.select()
                .map { Genres.createEntity(it) }
                .asSequence()
            val model = DefaultListModel<Any>()
            Genres.forEach {
                model.addElement(it)
            }

            frameGenrePick.itemsList.model = model
            frameGenrePick.itemsList.selectedIndex = frameGenrePick.itemsList.model.size - 1
        }

        override fun windowClosing(e: WindowEvent?) {
            frameContext.isVisible = true
        }

        override fun windowClosed(e: WindowEvent?) {}
        override fun windowDeactivated(e: WindowEvent?) {}
        override fun windowDeiconified(e: WindowEvent?) {}
        override fun windowIconified(e: WindowEvent?) {}
        override fun windowOpened(e: WindowEvent?) {}
    }
}