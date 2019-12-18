package ui.edit

import ui.login.LoginFrame
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.JPanel


class EditFrame(val loginFrame: LoginFrame) : JFrame() {

    val panelGames = EditGamesPanel(this)
    val panelDevelopers = EditDevelopersPanel(this)
    var panelCurrent: JPanel = panelGames


    init {
        initComponents()
    }

    fun openGames() {
        this.panelCurrent = panelGames

        contentPane.removeAll()
        contentPane.add(panelCurrent)

        this.repaint()
    }

    fun openDevelopers() {
        this.panelCurrent = panelDevelopers

        contentPane.removeAll()
        contentPane.add(panelCurrent)

        this.repaint()
    }

    private fun initComponents() {
        contentPane.add(panelCurrent)

        preferredSize = Dimension(900, 450)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        pack()

        setLocationRelativeTo(null)
    }
}