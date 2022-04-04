import java.time.Duration

plugins {
  java
  id("com.github.noavarice.retry-jdbc-connection-plugin")
}

retryJdbcConnection {
  create("main") {
    connection.url.set("jdbc:h2://localhost:5423/db")
    connection.username.set("user")
    connection.password.set("changeit")

    retry.initialDelay.set(Duration.ofSeconds(2))
    retry.interval.set(Duration.ofSeconds(1))
  }
}
