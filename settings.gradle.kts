rootProject.name = "gradle-retry-jdbc-connection-plugin"

dependencyResolutionManagement {
  versionCatalogs {
    create("libs") {
      version("jupiter", "5.8.2")
      library("jupiter-api", "org.junit.jupiter", "junit-jupiter-api").versionRef("jupiter")
      library("jupiter-engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef("jupiter")
    }
  }
}
