package ui.edit

import ui.login.LoginFrame
import java.awt.CardLayout
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel


class EditFrame(val loginFrame: LoginFrame) : JFrame() {

    val panelGames = EditGamesPanel(this)
    val panelDevelopers = EditDevelopersPanel(this)
    val panelRoles = EditRolesPanel(this)
    val panelGenres = EditGenresPanel(this)

    val panelCard = JPanel()


    init {
        initComponents()
    }

    fun openGenres() {
        val cardLayout = panelCard.layout as CardLayout
        cardLayout.show(panelCard, "genres")
    }

    fun openRoles() {
        val cardLayout = panelCard.layout as CardLayout
        cardLayout.show(panelCard, "roles")
    }

    fun openGames() {
        panelGames.refresh(true, true, true)
        val cardLayout = panelCard.layout as CardLayout
        cardLayout.show(panelCard, "games")
    }

    fun openDevelopers() {
        panelDevelopers.refresh(roles = true)
        val cardLayout = panelCard.layout as CardLayout
        cardLayout.show(panelCard, "developers")
    }

    private fun initComponents() {
        panelCard.layout = CardLayout()
        panelCard.add(panelGames, "games")
        panelCard.add(panelDevelopers, "developers")
        panelCard.add(panelRoles, "roles")
        panelCard.add(panelGenres, "genres")

        contentPane.add(panelCard)

        preferredSize = Dimension(900, 450)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        pack()

        setLocationRelativeTo(null)
    }
}