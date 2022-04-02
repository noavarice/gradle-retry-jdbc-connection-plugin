import java.time.Duration

plugins {
  java
  id("com.github.chy.retry-jdbc-connection-plugin")
}

retryJdbcConnection {
  create("main") {
    connection.url.set(jooqDatabaseUrl)
    connection.username.set(jooqDatabaseUser)
    connection.password.set(jooqDatabasePassword)

    retry.initialDelay.set(Duration.ofSeconds(2))
  }
}
