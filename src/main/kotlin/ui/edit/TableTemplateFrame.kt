package ui.edit

import model.Game
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Font
import javax.swing.*



abstract class TableTemplateFrame : JFrame() {

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

open class TableTemplatePanel : JPanel() {

    val panelTop = JPanel()
    val textfieldTitle = JTextField()
    val panelTopLeft = JPanel()

    val panelBottom = JPanel()
    val buttonUpdate = JButton("update")
    val buttonAdd = JButton("add")
    val buttonRemove = JButton("remove")
    val buttonPick = JButton("pick")
    val textfieldSearch = JTextField(15)

    val panelCenter = JPanel()

    val panelCenterLeft = JPanel()
    val panelCenterRight = JPanel()

    init {
        this.layout = BorderLayout()
        this.add(panelTop, BorderLayout.NORTH)
        this.add(panelCenter, BorderLayout.CENTER)

        this.panelTop.layout = BorderLayout()

        this.panelTopLeft.layout = FlowLayout()
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
    }
}