package ui.main

import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.JPanel

class MainFrame : JFrame() {

    val mainPanel: JPanel = JPanel()

    init {
        initComponents()
    }

    private fun initComponents() {
        contentPane.add(mainPanel)

        preferredSize = Dimension(780, 220)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        pack()
    }
}