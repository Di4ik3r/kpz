package ui.login

import me.liuwj.ktorm.database.Database
import ui.edit.EditFrame
import ui.view.ViewFrame
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Font
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.*

class LoginFrame : JFrame() {

    val mainPanel = LoginPanel(this)

//    val viewFrame = ViewFrame()
//    val editFrame = EditFrame()

    init {
        initComponents()
    }

    private fun open() {
        try {
            val name = this.mainPanel.textfieldName.text
            val pass = this.mainPanel.textfieldPass.text
            Database.connect("jdbc:mysql://localhost:3306/kpz", user = name, password = pass, driver = "com.mysql.cj.jdbc.Driver")
            when(name) {
                "root" ->  {
                    val editFrame = EditFrame(this)
                    editFrame.isVisible = true
                    this.isVisible = false
                }
                "view" -> {
                    val viewFrame = ViewFrame()
                    viewFrame.isVisible = true
                    this.isVisible = false
                }
            }
        } catch(ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun initComponents() {
        contentPane.add(mainPanel)

        preferredSize = Dimension(350, 200)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        pack()

        setLocationRelativeTo(null)
    }

    class LoginPanel(context: LoginFrame) : JPanel() {

        val labelTitle = JLabel("Yurchenko")

        val textfieldName = JTextField(20);
        val textfieldPass = JPasswordField(20);

        val buttonLogin = JButton("Login")

        init {
            this.layout = BoxLayout(this, BoxLayout.PAGE_AXIS)

            val panelTitle = JPanel()
            panelTitle.layout = FlowLayout()
            panelTitle.alignmentX = JComponent.CENTER_ALIGNMENT
            labelTitle.font = Font("default", 1, 24)
            panelTitle.add(labelTitle)
            this.add(panelTitle, BorderLayout.NORTH)

            val centerPanel = JPanel()
            centerPanel.alignmentX = JComponent.CENTER_ALIGNMENT
            centerPanel.layout = FlowLayout()
            centerPanel.add(textfieldName, BorderLayout.NORTH)
            centerPanel.add(textfieldPass, BorderLayout.SOUTH)
            this.add(centerPanel)

            textfieldName.alignmentX = JComponent.CENTER_ALIGNMENT
            textfieldPass.alignmentX = JComponent.CENTER_ALIGNMENT
            buttonLogin.alignmentX = JComponent.CENTER_ALIGNMENT

            val panelBottom = JPanel()
            panelBottom.alignmentX = JComponent.CENTER_ALIGNMENT
            panelBottom.layout = FlowLayout()
            panelBottom.add(buttonLogin)
            this.add(panelBottom)


            textfieldName.addKeyListener(LoginKeyAdapter(context))
            textfieldPass.addKeyListener(LoginKeyAdapter(context))

            buttonLogin.addActionListener {
                context.open()
//                val admins = Admins.select()
//                    .whereWithConditions {
//                        if(textfieldName.text != null)
//                            it += Admins.name eq textfieldName.text
//                        if(textfieldPass.text != null)
//                            it += Admins.password eq textfieldPass.text
//                    }
//                    .asSequence()
//                if(admins.count() > 0) {
//                    println("elkal;sdk;asd")
//                } else {
//                    println("lox")
//                }

//                try {
//                    Database.connect("jdbc:mysql://localhost:3306/kpz", user = textfieldName.text, password = textfieldPass.text, driver = "com.mysql.cj.jdbc.Driver")
//                    println("logen")
//                } catch (ex: Exception) {
//                    println("lox")
//                }
            }
        }

        class LoginKeyAdapter(context: LoginFrame) : KeyAdapter() {

            val context = context

            override fun keyPressed(e: KeyEvent?) {
                super.keyPressed(e)
                if(e?.keyCode == KeyEvent.VK_ENTER)
                    context.open()
            }
        }
    }
}