plugins {
  `java-gradle-plugin`
  idea
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

gradlePlugin {
  plugins {
    create("retryJdbcConnectionPlugin") {
      id = "com.github.noavarice.retry-jdbc-connection-plugin"
      implementationClass = "com.github.noavarice.RetryJdbcConnectionPlugin"
    }
  }
}

group = "com.github.noavarice"
version = "0.1.0"

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(libs.jupiter.api)
  testRuntimeOnly(libs.jupiter.engine)
}

tasks.getByName<Test>("test") {
  useJUnitPlatform()
}
