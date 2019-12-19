package ui.edit.panel

import me.liuwj.ktorm.dsl.*
import me.liuwj.ktorm.entity.asSequence
import me.liuwj.ktorm.entity.first
import me.liuwj.ktorm.entity.forEach
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


class EditDevelopersPanel(val editFrame: EditFrame) : TableTemplatePanel<Developers>(editFrame) {

    var currentDeveloper = Developers.select().map { Developers.createEntity(it) }.first()
    val frameDeveloperPick = TablePickFrame<Developers>(frameContext)

    val comboboxRole = JComboBox<Role>()

    init {
        println(currentDeveloper)

        this.refresh(true)

        this.panelCenterCenter.add(Box.createRigidArea(Dimension(0, 100)))

        val panelFields = JPanel(); panelFields.layout = FlowLayout()
        panelFields.add(JLabel("Price: "))
        panelFields.add(comboboxRole)
        this.panelCenterCenter.add(panelFields)


        val panelDate = JPanel(); panelDate.layout = FlowLayout()
        this.panelCenterCenter.add(panelDate)
        this.panelCenterCenter.add(Box.createRigidArea(Dimension(0, 100)))

        val panelButtonDeveloper = JPanel(); panelButtonDeveloper.layout = FlowLayout()
        this.panelCenterLeft.add(panelButtonDeveloper, BorderLayout.SOUTH)
        val panelRemoveDeveloper = JPanel(); panelRemoveDeveloper.layout = FlowLayout()


        // ******************************************************* BUTTON SEARCH
        this.textfieldSearch.addKeyListener(SearchKeyAdapter())

        // ******************************************************* BUTTON REMOVE
        this.buttonRemove.addActionListener {
            GameDevelopers.delete { it.developer eq currentDeveloper.id }
            Developers.delete { it.id eq currentDeveloper.id }
            if(Developers.count() < 1) {
                Developers.insert {
                    it.name to "developer"
                    it.role to Roles.asSequence().first().id
                }
            }

            currentDeveloper = Developers.select()
                .map { Developers.createEntity(it)}
                .asSequence()
                .last()

            refresh()
        }


        // ******************************************************* BUTTON PICK
        this.frameDeveloperPick.buttonPick.addActionListener {
            if(frameDeveloperPick.itemsList.selectedIndex < 0) {
                JOptionPane.showMessageDialog(
                    frameDeveloperPick,
                    "There is must be selected item."
                )
                return@addActionListener
            }
            this.currentDeveloper = this.frameDeveloperPick.itemsList.selectedValue as Developer

            this.frameDeveloperPick.isVisible = false
            this.frameContext.isVisible = true

            refresh()
        }

        this.frameDeveloperPick.addWindowListener(DeveloperChooseListener())

        this.buttonPick.addActionListener {
            this.frameDeveloperPick.isVisible = true
            frameContext.isVisible = false
        }


        // ******************************************************* BUTTON UPDATE
        this.buttonUpdate.addActionListener {
            try {
                Developers.update {
                    it.name to textfieldTitle.text
                    it.role to (comboboxRole.selectedItem as Role).id

                    where {
                        it.id eq currentDeveloper.id
                    }
                }

                currentDeveloper = Developers.select()
                    .map { Developers.createEntity(it) }
                    .asSequence()
                    .first {
                        it.id == currentDeveloper.id
                    }

                this.refresh()
            } catch(ex: Exception) {
                ex.printStackTrace()
                JOptionPane.showMessageDialog(
                    frameDeveloperPick,
                    "Fields has incorrect values"
                )
            }
        }


        // ******************************************************* BUTTON ADD
        this.buttonAdd.addActionListener {
            Developers.insert {
                it.name to "developer"
                it.role to Roles.asSequence().first().id
            }

            val addedDeveloper = Developers.select().map { Developers.createEntity(it) }.last()

            println(addedDeveloper)
            this.currentDeveloper = addedDeveloper
            this.refresh()
        }
    }

    fun refresh(roles: Boolean = false) {
        if(roles) {
            val roles = Roles.asSequence()
            val model = DefaultComboBoxModel<Role>()
            roles.forEach {
                model.addElement(it)
            }
            this.comboboxRole.model = model
        }

        this.textfieldTitle.text = currentDeveloper.name
        val role = Roles.select()
            .where { Roles.id eq currentDeveloper.role }
            .map { Roles.createEntity(it) }
            .asSequence()
            .first()

        this.comboboxRole.model.selectedItem = role

//        this.textfieldPrice.text = currentDeveloper.price.toString()

    }


    inner class SearchKeyAdapter() : KeyAdapter() {

        override fun keyPressed(e: KeyEvent?) {
            super.keyPressed(e)
            if(e?.keyCode == KeyEvent.VK_ENTER) {
                val input = textfieldSearch.text
                try {
                    val id = Integer.parseInt(input)

                    val developers = Developers.select()
                        .where { Developers.id eq id }
                        .map { Developers.createEntity(it) }
                        .asSequence()

                    if(developers.count() > 0) {
                        currentDeveloper = developers.first()
                        refresh()
                    } else {
                        JOptionPane.showMessageDialog(
                            null,
                            "Not found"
                        )
                    }

                } catch(ex: Exception) {
                    val developers = Developers.select()
                        .where { Developers.name like input }
                        .map { Developers.createEntity(it) }
                        .asSequence()

                    if(developers.count() > 0) {
                        currentDeveloper = developers.first()
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



    inner class DeveloperChooseListener : WindowListener {

        override fun windowActivated(e: WindowEvent?) {
            val developers = Developers.select()
                .map { Developers.createEntity(it) }
                .asSequence()
            val model = DefaultListModel<Any>()
            developers.forEach {
                model.addElement(it)
            }

            frameDeveloperPick.itemsList.model = model
            frameDeveloperPick.itemsList.selectedIndex = frameDeveloperPick.itemsList.model.size - 1
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