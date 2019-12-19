package ui.view.panel

import me.liuwj.ktorm.dsl.*
import me.liuwj.ktorm.entity.asSequence
import me.liuwj.ktorm.entity.count
import me.liuwj.ktorm.entity.elementAt
import me.liuwj.ktorm.entity.forEach
import model.*
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.xwpf.usermodel.XWPFDocument
import ui.view.ViewFrame
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.io.File
import java.io.FileOutputStream
import java.math.BigDecimal
import java.time.LocalDate
import javax.swing.*
import javax.swing.event.ListSelectionEvent
import javax.swing.table.DefaultTableColumnModel
import javax.swing.table.DefaultTableModel


class ViewMainPanel(val frameContext: ViewFrame) : JPanel() {

    val tableGames: JTable
    val tableGamesDevelopers: JTable
    val tableGamesGenres: JTable

    private val columnsGames = arrayOf("id", "name", "price", "date")
    private val columnsGamesDevelopers = arrayOf("id", "name", "role")
    private val columnsGamesGenres = arrayOf("id", "name")

    private val buttonToWord = JButton("export to word")
    private val buttonToExcel = JButton("export to excel")
    private val buttonLogout = JButton("logout")

    init {
        this.layout = BoxLayout(this, BoxLayout.LINE_AXIS)

        tableGamesGenres = JTable()
        tableGamesDevelopers = JTable()


        val games = Games.asSequence()
        val arrayGames = Array(games.count()) { i->
            arrayOf(games.elementAt(i).id, games.elementAt(i).name, games.elementAt(i).price, games.elementAt(i).date)
        }
        val modelGames = DefaultTableModel(arrayGames, columnsGames)
        tableGames = JTable(modelGames)
        tableGames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)

        val selectionModel = tableGames.selectionModel
        selectionModel.addListSelectionListener(GamesListSelectionListener())


        val panelGames = JPanel(); panelGames.layout = BorderLayout()
        panelGames.add(JScrollPane(tableGames), BorderLayout.CENTER)
        val panelGamesButton = JPanel(); panelGamesButton.layout = FlowLayout()
        panelGamesButton.add(buttonToExcel)
        panelGamesButton.add(buttonToWord)
        panelGames.add(panelGamesButton, BorderLayout.SOUTH)
        this.add(panelGames)
        this.add(JScrollPane(tableGamesDevelopers))
        val panelGenres = JPanel(); panelGenres.layout = BorderLayout()
        panelGenres.add(JScrollPane(tableGamesGenres), BorderLayout.CENTER)
        panelGenres.add(buttonLogout, BorderLayout.SOUTH)
        this.add(panelGenres)


        this.buttonLogout.addActionListener {
            this.frameContext.isVisible = false
            this.frameContext.loginFrame.isVisible = true
        }

        this.buttonToWord.addActionListener {
            val document = XWPFDocument()
            val out = FileOutputStream(File("games.docx"))

            val table = document.createTable()
            var tableRowOne = table.getRow(0);
            tableRowOne.getCell(0).setText("id");
            tableRowOne.addNewTableCell().setText("name");
            tableRowOne.addNewTableCell().setText("price");
            tableRowOne.addNewTableCell().setText("date");

            val games = Games.asSequence()
            var indexer = 0
            games.forEach {
                val row = table.createRow()
                row.getCell(0).text = it.id.toString()
                row.getCell(1).text = it.name
                row.getCell(2).text = it.price.toString()
                row.getCell(3).text = it.date.toString()
            }

            document.write(out)
            out.close()
        }

        this.buttonToExcel.addActionListener {
            val workbook: Workbook = XSSFWorkbook()
            val createHelper = workbook.creationHelper

            val sheet: Sheet = workbook.createSheet("Employee")

            val headerFont: Font = workbook.createFont()
            headerFont.setBold(true)
            headerFont.setFontHeightInPoints(14.toShort())
            headerFont.setColor(IndexedColors.RED.getIndex())

            val headerCellStyle = workbook.createCellStyle()
            headerCellStyle.setFont(headerFont)

            val headerRow: Row = sheet.createRow(0)

            for (i in 0 until columnsGames.count()) {
                val cell: Cell = headerRow.createCell(i)
                cell.setCellValue(columnsGames[i])
                cell.setCellStyle(headerCellStyle)
            }

            val dateCellStyle = workbook.createCellStyle()
            dateCellStyle.dataFormat = createHelper.createDataFormat().getFormat("dd-MM-yyyy")

            var rowNum = 1
            var games = Games.asSequence()
            for (game in games) {
                val row: Row = sheet.createRow(rowNum++)
                row.createCell(0)
                    .setCellValue(game.id.toString())
                row.createCell(1)
                    .setCellValue(game.name)
                val dateOfBirthCell: Cell = row.createCell(2)
                dateOfBirthCell.setCellValue(game.date)
                dateOfBirthCell.setCellStyle(dateCellStyle)
                row.createCell(3)
                    .setCellValue(game.price.toString())
            }
            for (i in 0 until columnsGames.count()) {
                sheet.autoSizeColumn(i)
            }

            rowNum += 2
            var column = 0
            val removedGames = RemovedGames.asSequence()
            for (game in removedGames) {
                val row: Row = sheet.createRow(rowNum++)
                row.createCell(column)
                    .setCellValue(game.id.toString())
                row.createCell(column + 1)
                    .setCellValue(game.name)
                val dateOfBirthCell: Cell = row.createCell(column + 2)
                dateOfBirthCell.setCellValue(game.date)
                dateOfBirthCell.setCellStyle(dateCellStyle)
                row.createCell(column + 3)
                    .setCellValue(game.price.toString())
            }
            for (i in 0 until columnsGames.count()) {
                sheet.autoSizeColumn(i)
            }

            val fileOut = FileOutputStream("games.xlsx")
            workbook.write(fileOut)
            fileOut.close()

            workbook.close()
        }
    }


    inner class GamesListSelectionListener: DefaultTableColumnModel() {

        var previousItem = tableGames.selectedRow
        override fun fireColumnSelectionChanged(e: ListSelectionEvent?) {
            super.fireColumnSelectionChanged(e)
            val row = tableGames.selectedRow

            if(row != previousItem) {
                val id = Integer.parseInt(tableGames.getValueAt(row, 0).toString())
                val name = tableGames.getValueAt(row, 1).toString()
                val price = BigDecimal.valueOf(tableGames.getValueAt(row, 2).toString().toDouble())
                val date = tableGames.getValueAt(row, 3) as LocalDate

                val game = Games.select()
                    .where {
                        (Games.id eq id) and (Games.name eq name) and (Games.price eq price) and(Games.date eq date)
                    }
                    .map { Games.createEntity(it) }
                    .asSequence()
                    .first()

                val developers = GameDevelopers
                    .innerJoin(Games, on = GameDevelopers.game eq Games.id)
                    .innerJoin(Developers, on = GameDevelopers.developer eq Developers.id)
                    .innerJoin(Roles, on = Developers.role eq Roles.id)
                    .select()
                    .where {
                        Games.id eq game.id
                    }


                var arrayDevelopers = Array(developers.rowSet.size()) { Array(3) {""} }
                var indexer = 0
                for(row in developers.rowSet) {
                    arrayDevelopers[indexer++] = arrayOf(
                        row[Developers.id].toString(),
                        row[Developers.name].toString(),
                        row[Roles.name].toString())
                }

                val modelGamesDevelopers = DefaultTableModel(arrayDevelopers, columnsGamesDevelopers)
                tableGamesDevelopers.model = modelGamesDevelopers







                val genres = GameGenres
                    .innerJoin(Games, on = GameGenres.game eq Games.id)
                    .innerJoin(Genres, on = GameGenres.genre eq Genres.id)
                    .select()
                    .where {
                        Games.id eq game.id
                    }


                var arrayGenres = Array(genres.rowSet.size()) { Array(2) {""} }
                indexer = 0
                for(row in genres.rowSet) {
                    arrayGenres[indexer++] = arrayOf(
                        row[Genres.id].toString(),
                        row[Genres.name].toString())
                }

                val modelGamesGenres = DefaultTableModel(arrayGenres, columnsGamesGenres)
                tableGamesGenres.model = modelGamesGenres
            }
            previousItem = row
        }
    }
}