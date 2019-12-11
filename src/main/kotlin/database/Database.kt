package database

import java.sql.Connection
import java.sql.DriverManager

import java.sql.SQLException
import java.util.*


class Database {

    private val userName: String = "root"
    private val userPass: String = "root"
    private val dbms: String = "mysql"
    private val serverName: String = "localhost"
    private val port = 3306

    private val databaseName: String = "l7"

    private var connection: Connection? = null

    init {
        val connectionProps = Properties()
        connectionProps.put("user", this.userName)
        connectionProps.put("password", this.userPass)
        this.connection = DriverManager.getConnection(
            "jdbc:${this.dbms}://${this.serverName}:${this.port.toString()}/${this.databaseName}"
            ,
//            connectionProps
            userName, userPass
        )
        println("Connected to database")
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