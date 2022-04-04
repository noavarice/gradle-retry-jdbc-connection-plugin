package com.github.noavarice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.BuildTask;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link RetryJdbcConnectionPlugin}.
 *
 * @author noavarice
 * @since 1.0.0
 */
@DisplayName("Tests for JDBC connection retrying plugin")
class RetryJdbcConnectionPluginTest {

  @Test
  @DisplayName("Test retrying without running database")
  void testWithoutRunningDatabase() {
    final File projectDir = createProjectDir("/test-project.gradle.kts");
    final BuildResult buildResult = GradleRunner
        .create()
        .withPluginClasspath()
        .withProjectDir(projectDir)
        .withArguments(":retryJdbcConnectionMain")
        .forwardOutput()
        .buildAndFail();

    final BuildTask task = buildResult.task(":retryJdbcConnectionMain");
    assertNotNull(task);
    assertEquals(TaskOutcome.FAILED, task.getOutcome());
  }

  private File createProjectDir(final String projectBuildscriptResourcePath) {
    try (final InputStream in = getClass().getResourceAsStream(projectBuildscriptResourcePath)) {
      Objects.requireNonNull(in);
      final Path projectDirPath = Files.createTempDirectory("retry-jdbc-connection-plugin-test");
      Files.copy(in, projectDirPath.resolve("build.gradle.kts"));
      return projectDirPath.toFile();
    } catch (final IOException e) {
      throw new IllegalStateException(e);
    }
  }
}
