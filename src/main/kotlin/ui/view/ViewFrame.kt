

package ui.view

import ui.view.panel.ViewMainPanel
import java.awt.CardLayout
import java.awt.Dimension
import java.awt.FlowLayout
import javax.swing.JFrame
import javax.swing.JPanel



class ViewFrame : JFrame() {

    val mainPanel = ViewMainPanel()


    init {
        initComponents()
    }

    private fun initComponents() {
        contentPane.add(mainPanel)

        preferredSize = Dimension(900, 450)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        pack()

        setLocationRelativeTo(null)
    }

}