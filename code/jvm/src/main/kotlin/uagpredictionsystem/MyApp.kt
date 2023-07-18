package uagpredictionsystem

import org.jdbi.v3.core.Jdbi
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import uagpredictionsystem.repository.jdbi.configure


@Configuration
@SpringBootApplication
class MyApp {

    @Bean

    fun jdbi(): Jdbi {
        val dbHost = System.getenv("DB_HOST") ?: throw IllegalStateException("DB_HOST environment variable not set")
        val dbPort = System.getenv("DB_PORT") ?: throw IllegalStateException("DB_PORT environment variable not set")
        val dbName = System.getenv("DB_NAME") ?: throw IllegalStateException("DB_NAME environment variable not set")
        val dbUser = System.getenv("DB_USER") ?: throw IllegalStateException("DB_USER environment variable not set")
        val dbPassword = System.getenv("DB_PASSWORD") ?: throw IllegalStateException("DB_PASSWORD environment variable not set")

        return Jdbi.create(
            PGSimpleDataSource().apply {
                setURL("jdbc:postgresql://$dbHost:$dbPort/$dbName")
                user = dbUser
                password = dbPassword
            }
        ).configure()
    }

}

    fun main(args: Array<String>) {
        runApplication<MyApp>(*args)
    }
