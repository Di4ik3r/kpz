package database

import model.Game
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.util.*


class Database {

    private val userName: String = "root"
    private val userPass: String = "root"
    private val dbms: String = "mysql"
    private val serverName: String = "localhost"
    private val port = 3306

    private val databaseName: String = "l7"

    var connection: Connection? = null

    init {
        val connectionProps = Properties()
        connectionProps["user"] = this.userName
        connectionProps["password"] = this.userPass
        this.connection = DriverManager.getConnection(
            "jdbc:${this.dbms}://${this.serverName}:${this.port.toString()}/${this.databaseName}",
            connectionProps
        )
        println("Connected to database")
    }

    fun insertInGames(game: Game) {
        val sql = "INSERT INTO games (name, price) VALUES (?, ?)"

        val statement: PreparedStatement = this.connection!!.prepareStatement(sql)
        statement.setString(1, game.name)
        statement.setFloat(2, game.price!!)

        val rowsInserted = statement.executeUpdate()
        if (rowsInserted > 0) {
            println("A new user was inserted successfully!")
        }
    }

    fun getAllFromGames() {
        val input = "SELECT * FROM games"
        val statement = this.connection?.prepareStatement(input)
        val result = statement?.executeQuery()
        while(result?.next()!!) {
            val id = result.getInt(1)
            val name = result.getString(2)
            val price = result.getFloat(3)
            println("$id: $name = $price")
        }
    }


//    @Throws(SQLException::class)
//    fun getConnection(): Connection? {
////        this.connection: Connection? = null
//        val connectionProps = Properties()
//        connectionProps.put("user", this.userName)
//        connectionProps.put("password", this.userPass)
//        conn = DriverManager.getConnection(
//            "jdbc:${this.dbms}://${this.serverName}:${this.port.toString()}/${this.databaseName}"
//            ,
////            connectionProps
//            userName, userPass
//        )
//        println("Connected to database")
//        return conn
//    }
}