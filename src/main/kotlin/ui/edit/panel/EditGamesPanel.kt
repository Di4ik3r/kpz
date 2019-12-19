package ui.edit.panel

import me.liuwj.ktorm.dsl.*
import model.*
import ui.edit.EditFrame
import ui.edit.TablePickFrame
import ui.edit.TableTemplatePanel
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import java.math.BigDecimal
import java.time.LocalDate
import javax.swing.*


class EditGamesPanel(val editFrame: EditFrame) : TableTemplatePanel<Games>(editFrame) {

    var currentGame = Games.select().map { Games.createEntity(it) }.first()
    val frameGamePick = TablePickFrame<Games>(frameContext)

    val textfieldPrice = JTextField(5)

    val spinnerDateDay = JSpinner(SpinnerNumberModel(1,1,28,1))
    val spinnerDateMonth = JSpinner(SpinnerNumberModel(1,1,12,1))
    val spinnerDateYear = JSpinner(SpinnerNumberModel(2000,2000 - 50,2019,1))

    val listDevelopers = JList<Developer>()
    val buttonAddDeveloper = JButton("Add developer")
    val buttonRemoveDeveloper = JButton("Remove developer")

    val listGenres = JList<Genre>()
    val buttonAddGenre = JButton("Add genre")
    val buttonRemoveGenre = JButton("Remove genre")

    init {
        println(currentGame)

        this.refresh(fields = true, developers = true, genres = true)

        this.panelCenterCenter.add(Box.createRigidArea(Dimension(0, 100)))

        val panelPrice = JPanel(); panelPrice.layout = FlowLayout()
        panelPrice.add(JLabel("Price: "))
        panelPrice.add(textfieldPrice)
        this.panelCenterCenter.add(panelPrice)


        val panelDate = JPanel(); panelDate.layout = FlowLayout()
        panelDate.add(JLabel("Date: "))
        panelDate.add(spinnerDateDay)
        panelDate.add(JLabel("."))
        panelDate.add(spinnerDateMonth)
        panelDate.add(JLabel("."))
        panelDate.add(spinnerDateYear)
        this.panelCenterCenter.add(panelDate)
        this.panelCenterCenter.add(Box.createRigidArea(Dimension(0, 100)))

        this.panelCenterLeft.add(JScrollPane(this.listDevelopers), BorderLayout.CENTER)
        val panelButtonDeveloper = JPanel(); panelButtonDeveloper.layout = FlowLayout()
        panelButtonDeveloper.add(buttonAddDeveloper)
        panelButtonDeveloper.add(buttonRemoveDeveloper)
        this.panelCenterLeft.add(panelButtonDeveloper, BorderLayout.SOUTH)
        val panelRemoveDeveloper = JPanel(); panelRemoveDeveloper.layout = FlowLayout()

        this.panelCenterRight.add(JScrollPane(this.listGenres), BorderLayout.CENTER)
        val panelAddGenre = JPanel(); panelAddGenre.layout = FlowLayout()
        panelAddGenre.add(buttonAddGenre)
        panelAddGenre.add(buttonRemoveGenre)
        this.panelCenterRight.add(panelAddGenre, BorderLayout.SOUTH)
        val panelRemoveGenre = JPanel(); panelRemoveGenre.layout = FlowLayout()


        // ******************************************************* TEXTFIELD SEARCH
        this.textfieldSearch.addKeyListener(SearchKeyAdapter())

        // ******************************************************* BUTTON REMOVE GENRE
        this.buttonRemoveGenre.addActionListener {
            try {
                val selectedGenre = listGenres.selectedValue as Genre
                GameGenres.select()
                    .where {
                        (GameGenres.game eq currentGame.id) and (GameGenres.genre eq selectedGenre.id)
                    }
                    .map { GameGenres.createEntity(it) }
                    .first()
                    .delete()
                refresh(genres = true)
            } catch(ex: Exception) {
                JOptionPane.showMessageDialog(
                    frameGamePick,
                    "There is must be selected genre."
                )
            }

        }

        // ******************************************************* BUTTON ADD GENRE
        this.buttonAddGenre.addActionListener {
            var genresNotIn = GameGenres
                .innerJoin(Games, on = GameGenres.game eq currentGame.id)
                .rightJoin(Genres, on = GameGenres.genre eq Genres.id)
                .select()
                .where { GameGenres.game.isNull() }
                .map { Genres.createEntity(it) }
                .asSequence()

            if(genresNotIn.count() < 1) {
                JOptionPane.showMessageDialog(
                    frameGamePick,
                    "There are no developers left."
                )
            }

            val choices: Array<Genre> = Array<Genre>(genresNotIn.count()) { indexer ->
                genresNotIn.elementAt(indexer)
            }

            val input = JOptionPane.showInputDialog(null,
                "Choose genre",
                "Genre chooser",
                JOptionPane.QUESTION_MESSAGE,
                null,
                choices,
                choices[0])

            if(input != null)  {
                GameGenres.insert {
                    it.game to currentGame.id
                    it.genre to (input as Genre).id
                }
                refresh(genres = true)
            }
        }



        // ******************************************************* BUTTON REMOVE DEVELOPER
        this.buttonRemoveDeveloper.addActionListener {
            try {
                val selectedDeveloper = listDevelopers.selectedValue as Developer
                GameDevelopers.select()
                    .where {
                        (GameDevelopers.game eq currentGame.id) and (GameDevelopers.developer eq selectedDeveloper.id)
                    }
                    .map { GameDevelopers.createEntity(it) }
                    .first()
                    .delete()
                refresh(developers = true)
            } catch(ex: Exception) {
                JOptionPane.showMessageDialog(
                    frameGamePick,
                    "There is must be selected developer."
                )
            }

        }

        // ******************************************************* BUTTON ADD DEVELOPER
        this.buttonAddDeveloper.addActionListener {
//            var developersNotIn = GameDevelopers
//                .rightJoin(Developers, on = GameDevelopers.developer eq Developers.id)
//                .leftJoin(Games, on = GameDevelopers.game eq Games.id)
//                .select(Developers.id, Developers.name, Developers.role, GameDevelopers.game)
//                .where { (GameDevelopers.game notEq currentGame.id) or (GameDevelopers.game.isNull()) }
//                .map { Developers.createEntity(it) }
//                .asSequence()
            var developersNotIn = GameDevelopers
                .innerJoin(Games, on = GameDevelopers.game eq currentGame.id)
                .rightJoin(Developers, on = GameDevelopers.developer eq Developers.id)
                .select()
                .where { GameDevelopers.game.isNull() }
                .map { Developers.createEntity(it) }
                .asSequence()

            if(developersNotIn.count() < 1) {
                JOptionPane.showMessageDialog(
                    frameGamePick,
                    "There are no developers left."
                )
            }

            val choices: Array<Developer> = Array<Developer>(developersNotIn.count()) {indexer ->
                developersNotIn.elementAt(indexer)
            }

            val input = JOptionPane.showInputDialog(null,
                "Choose developer",
                "Developer chooser",
                JOptionPane.QUESTION_MESSAGE,
                null,
                choices,
                choices[0])

            if(input != null)  {
                GameDevelopers.insert {
                    it.game to currentGame.id
                    it.developer to (input as Developer).id
                }
                refresh(developers = true)
            }
        }

        // ******************************************************* BUTTON REMOVE
        this.buttonRemove.addActionListener {
            GameDevelopers.delete { it.game eq currentGame.id }
            GameGenres.delete {it.game eq currentGame.id }
            Games.delete { it.id eq currentGame.id }
            if(Games.count() < 1) {
                Games.insert {
                    it.name to "game"
                    it.price to BigDecimal.ZERO
                    it.date to LocalDate.now()
                }
            }

            currentGame = Games.select()
                .map { Games.createEntity(it)}
                .asSequence()
                .last()

            refresh(fields = true, developers = true, genres = true)
        }


        // ******************************************************* BUTTON PICK
        this.frameGamePick.buttonPick.addActionListener {
            if(frameGamePick.itemsList.selectedIndex < 0) {
                JOptionPane.showMessageDialog(
                    frameGamePick,
                    "There is must be selected item."
                )
                return@addActionListener
            }
            this.currentGame = this.frameGamePick.itemsList.selectedValue as Game

            this.frameGamePick.isVisible = false
            this.frameContext.isVisible = true

            refresh(fields = true, developers = true, genres = true)
        }

        this.frameGamePick.addWindowListener(GameChooseListener())
        this.buttonPick.addActionListener {
            this.frameGamePick.isVisible = true
            frameContext.isVisible = false
        }


        // ******************************************************* BUTTON UPDATE
        this.buttonUpdate.addActionListener {
            try {
                Games.update {
                    it.name to textfieldTitle.text
                    it.price to BigDecimal.valueOf(textfieldPrice.text.toDouble())
                    it.date to LocalDate.of(
                        spinnerDateYear.value as Int,
                        spinnerDateMonth.value as Int,
                        spinnerDateDay.value as Int)

                    where {
                        it.id eq currentGame.id
                    }
                }

                currentGame = Games.select()
                    .map { Games.createEntity(it) }
                    .asSequence()
                    .first {
                        it.id == currentGame.id
                    }

                this.refresh(fields = true, developers = true, genres = true)
            } catch(ex: Exception) {
                JOptionPane.showMessageDialog(
                    frameGamePick,
                    "Fields has incorrect values"
                )
            }
        }


        // ******************************************************* BUTTON ADD
        this.buttonAdd.addActionListener {
            Games.insert {
                it.name to "game"
                it.price to BigDecimal.ZERO
                it.date to LocalDate.now()
            }

            val addedGame = Games.select().map { Games.createEntity(it) }.last()

            println(addedGame)
            this.currentGame = addedGame
            this.refresh(fields = true, developers = true, genres = true)
        }
    }

    fun refresh(fields: Boolean = false, developers: Boolean = false, genres: Boolean = false) {
        if(fields) {
            this.textfieldTitle.text = currentGame.name
            this.textfieldPrice.text = currentGame.price.toString()

            this.spinnerDateDay.value = currentGame.date.dayOfMonth
            this.spinnerDateMonth.value = currentGame.date.monthValue
            this.spinnerDateYear.value = currentGame.date.year
        }
        if(developers) {
            val developers = GameDevelopers
                .innerJoin(Games, on = GameDevelopers.game eq Games.id)
                .innerJoin(Developers, on = GameDevelopers.developer eq Developers.id)
                .select()
                .where { Games.id eq currentGame.id }
                .map { Developers.createEntity(it) }
                .asSequence()

            val model = DefaultListModel<Developer>()
            developers.forEach {
                model.addElement(it)
            }

            listDevelopers.model = model
            listDevelopers.selectedIndex = frameGamePick.itemsList.model.size - 1
        }
        if(genres) {
            val genres = GameGenres
                .innerJoin(Games, on = GameGenres.game eq Games.id)
                .innerJoin(Genres, on = GameGenres.genre eq Genres.id)
                .select()
                .where { Games.id eq currentGame.id }
                .map { Genres.createEntity(it) }
                .asSequence()

            val model = DefaultListModel<Genre>()
            genres.forEach {
                model.addElement(it)
            }

            listGenres.model = model
            listGenres.selectedIndex = frameGamePick.itemsList.model.size - 1
        }
    }



    inner class SearchKeyAdapter() : KeyAdapter() {

        override fun keyPressed(e: KeyEvent?) {
            super.keyPressed(e)
            if(e?.keyCode == KeyEvent.VK_ENTER) {
                val input = textfieldSearch.text
                try {
                    val id = Integer.parseInt(input)

                    val games = Games.select()
                        .where { Games.id eq id }
                        .map { Games.createEntity(it) }
                        .asSequence()

                    if(games.count() > 0) {
                        currentGame = games.first()
                        refresh(true, true, true)
                    } else {
                        JOptionPane.showMessageDialog(
                            null,
                            "Not found"
                        )
                    }

                } catch(ex: Exception) {
                    val games = Games.select()
                        .where { Games.name like input }
                        .map { Games.createEntity(it) }
                        .asSequence()

                    if(games.count() > 0) {
                        currentGame = games.first()
                        refresh(true, true, true)
                    } else {
                        JOptionPane.showMessageDialog(
                            null,
                            "Not found"
                        )
                    }
                }

                textfieldSearch.text = ""
            }
        }
    }


    inner class GameChooseListener : WindowListener {

        override fun windowActivated(e: WindowEvent?) {
            val games = Games.select()
                .map { Games.createEntity(it) }
                .asSequence()
            val model = DefaultListModel<Any>()
            games.forEach {
                model.addElement(it)
            }

            frameGamePick.itemsList.model = model
            frameGamePick.itemsList.selectedIndex = frameGamePick.itemsList.model.size - 1
        }

        override fun windowClosing(e: WindowEvent?) {
            frameContext.isVisible = true
        }

        override fun windowClosed(e: WindowEvent?) {}
        override fun windowDeactivated(e: WindowEvent?) {}
        override fun windowDeiconified(e: WindowEvent?) {}
        override fun windowIconified(e: WindowEvent?) {}
        override fun windowOpened(e: WindowEvent?) {}
    }
}
