

package ui.view

import me.liuwj.ktorm.database.Database
import ui.login.LoginFrame
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.JPanel



class ViewFrame : JFrame() {

    val mainPanel = JPanel()

    init {
        initComponents()
    }

    private fun initComponents() {
        contentPane.add(mainPanel)

        preferredSize = Dimension(550, 350)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        pack()

        setLocationRelativeTo(null)
    }

}