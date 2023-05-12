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
        return Jdbi.create(
            PGSimpleDataSource().apply {
                setURL("jdbc:postgresql://localhost:5432/postgres")
                user = "postgres"
                password = "joaopedro123"
            }
        ).configure()
    }
}

    fun main(args: Array<String>) {
        runApplication<MyApp>(*args)
    }
