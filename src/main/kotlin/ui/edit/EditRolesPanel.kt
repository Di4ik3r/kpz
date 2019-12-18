package ui.edit


import me.liuwj.ktorm.dsl.*
import model.*
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import javax.swing.*


class EditRolesPanel(val editFrame: EditFrame) : TableTemplatePanel<Roles>(editFrame) {

    var currentRole = Roles.select().map { Roles.createEntity(it) }.first()
    val frameRolePick = TablePickFrame<Roles>(frameContext)

    init {
        println(currentRole)

        this.refresh()

        this.panelCenterCenter.add(Box.createRigidArea(Dimension(0, 100)))

        val panelFields = JPanel(); panelFields.layout = FlowLayout()
        this.panelCenterCenter.add(panelFields)


        val panelDate = JPanel(); panelDate.layout = FlowLayout()
        this.panelCenterCenter.add(panelDate)
        this.panelCenterCenter.add(Box.createRigidArea(Dimension(0, 100)))

        val panelButton = JPanel(); panelButton.layout = FlowLayout()
        this.panelCenterLeft.add(panelButton, BorderLayout.SOUTH)


        // ******************************************************* BUTTON SEARCH
        this.textfieldSearch.addKeyListener(SearchKeyAdapter())

        // ******************************************************* BUTTON REMOVE
        this.buttonRemove.addActionListener {
            Roles.delete { it.id eq currentRole.id }
            if(Roles.count() < 1) {
                Roles.insert {
                    it.name to "role"
                }
            }

            currentRole = Roles.select()
                .map { Roles.createEntity(it)}
                .asSequence()
                .last()

            refresh()
        }


        // ******************************************************* BUTTON PICK
        this.frameRolePick.buttonPick.addActionListener {
            if(frameRolePick.itemsList.selectedIndex < 0) {
                JOptionPane.showMessageDialog(
                    frameRolePick,
                    "There is must be selected item."
                )
                return@addActionListener
            }
            this.currentRole = this.frameRolePick.itemsList.selectedValue as Role

            this.frameRolePick.isVisible = false
            this.frameContext.isVisible = true

            refresh()
        }

        this.frameRolePick.addWindowListener(RoleChooseListener())

        this.buttonPick.addActionListener {
            this.frameRolePick.isVisible = true
            frameContext.isVisible = false
        }


        // ******************************************************* BUTTON UPDATE
        this.buttonUpdate.addActionListener {
            try {
                Roles.update {
                    it.name to textfieldTitle.text

                    where {
                        it.id eq currentRole.id
                    }
                }

                currentRole = Roles.select()
                    .map { Roles.createEntity(it) }
                    .asSequence()
                    .first {
                        it.id == currentRole.id
                    }

                this.refresh()
            } catch(ex: Exception) {
                ex.printStackTrace()
                JOptionPane.showMessageDialog(
                    frameRolePick,
                    "Fields has incorrect values"
                )
            }
        }


        // ******************************************************* BUTTON ADD
        this.buttonAdd.addActionListener {
            Roles.insert {
                it.name to "role"
            }

            val addedRole = Roles.select().map { Roles.createEntity(it) }.last()

            println(addedRole)
            this.currentRole = addedRole
            this.refresh()
        }
    }

    fun refresh() {
        this.textfieldTitle.text = currentRole.name
    }


    inner class SearchKeyAdapter() : KeyAdapter() {

        override fun keyPressed(e: KeyEvent?) {
            super.keyPressed(e)
            if(e?.keyCode == KeyEvent.VK_ENTER) {
                val input = textfieldSearch.text
                try {
                    val id = Integer.parseInt(input)

                    val roles = Roles.select()
                        .where { Roles.id eq id }
                        .map { Roles.createEntity(it) }
                        .asSequence()

                    if(roles.count() > 0) {
                        currentRole = roles.first()
                        refresh()
                    } else {
                        JOptionPane.showMessageDialog(
                            null,
                            "Not found"
                        )
                    }

                } catch(ex: Exception) {
                    val roles = Roles.select()
                        .where { Roles.name like input }
                        .map { Roles.createEntity(it) }
                        .asSequence()

                    if(roles.count() > 0) {
                        currentRole = roles.first()
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



    inner class RoleChooseListener : WindowListener {

        override fun windowActivated(e: WindowEvent?) {
            val roles = Roles.select()
                .map { Roles.createEntity(it) }
                .asSequence()
            val model = DefaultListModel<Any>()
            roles.forEach {
                model.addElement(it)
            }

            frameRolePick.itemsList.model = model
            frameRolePick.itemsList.selectedIndex = frameRolePick.itemsList.model.size - 1
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