package ui.edit

import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.JPanel


class EditFrame : JFrame() {

    val panelGames = EditGamesPanel()
    val panelCurrent = panelGames


    init {
        initComponents()
    }

    private fun initComponents() {
        contentPane.add(panelCurrent)

        preferredSize = Dimension(650, 450)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        pack()

        setLocationRelativeTo(null)
    }
}