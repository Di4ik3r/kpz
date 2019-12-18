package ui.edit

import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Font
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import javax.swing.*


abstract class TableTemplateFrame(val frameContext: JFrame) : JFrame() {

//    val mainPanel = TableTemplatePanel()

    init {
        initComponents()
    }

    private fun initComponents() {
//        contentPane.add(mainPanel)

        preferredSize = Dimension(650, 450)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        pack()

        setLocationRelativeTo(null)
    }
}

open class TableTemplatePanel<T>(val frameContext: EditFrame) : JPanel() {
    val panelTop = JPanel()
    val textfieldTitle = JTextField(15)

    val panelTopLeft = JPanel()
    val buttonGames = JButton("games")
    val buttonDevelopers = JButton("developers")
    val buttonGenres = JButton("genres")
    val buttonRoles = JButton("roles")

    val panelBottom = JPanel()
    val buttonUpdate = JButton("update")
    val buttonAdd = JButton("add")
    val buttonRemove = JButton("remove")
    val buttonPick = JButton("pick")
    val textfieldSearch = JTextField(15)

    val panelCenter = JPanel()
    val panelCenterCenter = JPanel()

    val panelCenterLeft = JPanel()
    val panelCenterRight = JPanel()

    val panelTopRight = JPanel()
    val buttonLogout = JButton("logout")

    init {
        this.layout = BorderLayout()
        this.add(panelTop, BorderLayout.NORTH)

//        panelCenter.layout = BoxLayout(panelCenter, BoxLayout.PAGE_AXIS)
        panelCenter.layout = BorderLayout()
        panelCenterCenter.layout = BoxLayout(panelCenterCenter, BoxLayout.PAGE_AXIS)
        panelCenter.add(panelCenterCenter, BorderLayout.CENTER)
        panelCenter.add(panelCenterLeft, BorderLayout.WEST)
        this.panelCenterLeft.layout = BorderLayout()
        panelCenter.add(panelCenterRight, BorderLayout.EAST)
        this.panelCenterRight.layout = BorderLayout()
        this.add(panelCenter, BorderLayout.CENTER)

        this.panelTop.layout = BorderLayout()

        this.panelTopLeft.layout = FlowLayout()
        this.panelTopLeft.add(buttonGames)
        this.panelTopLeft.add(buttonDevelopers)
        this.panelTopLeft.add(buttonGenres)
        this.panelTopLeft.add(buttonRoles)
        this.panelTop.add(panelTopLeft, BorderLayout.WEST)

        this.panelBottom.layout = BoxLayout(this.panelBottom, BoxLayout.PAGE_AXIS)
        val panelBottomTop = JPanel()
        panelBottomTop.layout = FlowLayout()
        panelBottomTop.add(this.buttonUpdate)
        val panelBottomMiddle = JPanel()
        panelBottomMiddle.layout = FlowLayout()
        val panelBottomDown = JPanel()
        panelBottomDown.layout = FlowLayout()
        this.panelBottom.add(panelBottomTop)
        this.panelBottom.add(panelBottomMiddle)
        this.panelBottom.add(panelBottomDown)
        panelBottomMiddle.add(buttonAdd)
        panelBottomMiddle.add(buttonRemove)
        panelBottomDown.add(buttonPick)
        panelBottomDown.add(textfieldSearch)
        this.add(panelBottom, BorderLayout.SOUTH)
//        this.panelTop.add(panelTopRight, BorderLayout.EAST)

        val panelTopCenter = JPanel()
        panelTopCenter.layout = FlowLayout()
        this.textfieldTitle.font = Font("default", 1, 26)
        panelTopCenter.add(textfieldTitle)
        this.panelTop.add(panelTopCenter, BorderLayout.CENTER)

        this.panelTopRight.add(buttonLogout)
        this.panelTop.add(panelTopRight, BorderLayout.EAST)


        this.buttonGenres.addActionListener {
            this.frameContext.openGenres()
        }
        this.buttonRoles.addActionListener {
            this.frameContext.openRoles()
        }
        this.buttonGames.addActionListener {
            this.frameContext.openGames()
        }
        this.buttonDevelopers.addActionListener {
            this.frameContext.openDevelopers()
        }

        this.buttonLogout.addActionListener {
            this.frameContext.isVisible = false
            this.frameContext.loginFrame.isVisible = true
        }

    }
}

open class TablePickFrame<T>(val frameContext: JFrame) : JFrame() {

    val itemsList = JList<Any>()
    val buttonPick = JButton("pick")

    init {
        initComponents()

//        this.addWindowListener(TableChooseListener())
    }

    private fun initComponents() {
        val panel = JPanel()
        panel.layout = BorderLayout()

        this.itemsList.selectionMode = ListSelectionModel.SINGLE_SELECTION
        this.itemsList.layoutOrientation = JList.VERTICAL
        val scrollPane = JScrollPane(itemsList)

        panel.add(scrollPane, BorderLayout.CENTER)

        val buttonPanel = JPanel()
        buttonPanel.layout = FlowLayout()
        buttonPanel.add(buttonPick)

        panel.add(buttonPanel, BorderLayout.SOUTH)

        contentPane.add(panel)

        preferredSize = Dimension(600, 300)
        defaultCloseOperation = JFrame.HIDE_ON_CLOSE

        pack()

        setLocationRelativeTo(null)
    }
}