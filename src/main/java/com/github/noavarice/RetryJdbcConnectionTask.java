package com.github.noavarice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.util.function.BooleanSupplier;
import javax.inject.Inject;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.FileCollection;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.TaskAction;

/**
 * {@link DefaultTask} that retries JDBC properties until it's successful.
 *
 * @author noavarice
 * @since 1.0.0
 */
public abstract class RetryJdbcConnectionTask extends DefaultTask {

  private final RetryJdbcConnectionPluginExtension config;

  private final FileCollection driverClasspath;

  @Inject
  public RetryJdbcConnectionTask(
      final ObjectFactory objects,
      final RetryJdbcConnectionPluginExtension config,
      final FileCollection driverClasspath
  ) {
    this.config = config;
    this.driverClasspath = objects.fileCollection().from(driverClasspath);
  }

  @TaskAction
  public void retryConnection() throws InterruptedException {
    validateInputs();

    final var connectionProperties = config.getConnection();
    getLogger().quiet("Connecting to {} as {}", connectionProperties.getUrl(), connectionProperties.getUsername());

    final var retryProperties = config.getRetry();
    retrying(() -> tryConnect(connectionProperties), retryProperties);
  }

  private void validateInputs() {
    final RetryConfig retry = config.getRetry();
    final int retryCount = retry.getCount().get();
    if (retryCount <= 0) {
      throw new IllegalArgumentException("Retry count must be positive" + retryCount);
    }

    final Duration retryInitialDelay = retry.getInitialDelay().get();
    if (retryInitialDelay.isNegative()) {
      throw new IllegalArgumentException(
          "Initial retry interval must be positive or zero" + retryInitialDelay
      );
    }

    final Duration retryInterval = retry.getInterval().get();
    if (retryInterval.isNegative() || retryInterval.isZero()) {
      throw new IllegalArgumentException("Retry interval must be positive" + retryInterval);
    }
  }

  private boolean tryConnect(final ConnectionConfig properties) {
    // specifically using ignored variable in try-with-resources
    // to automatically close properties if created
    try (final Connection ignored = createConnection(properties)) {
      getLogger().quiet("Connected successfully");
      return true;
    } catch (final SQLException e) {
      getLogger().warn("Failed to connect", e);
      return false;
    }
  }

  private Connection createConnection(final ConnectionConfig properties) throws SQLException {
    return DriverManager.getConnection(
        properties.getUrl().get(),
        properties.getUsername().get(),
        properties.getPassword().get()
    );
  }

  private void retrying(
      final BooleanSupplier runnable,
      final RetryConfig properties
  ) throws InterruptedException {

    // delaying initially if required
    final Duration initialDelay = properties.getInitialDelay().get();
    if (!initialDelay.isZero()) {
      getLogger().quiet("Initial delay: {}", initialDelay);
      Thread.sleep(initialDelay.toMillis());
    }

    // connecting first time
    getLogger().quiet("Connecting first time");
    if (runnable.getAsBoolean()) {
      return;
    }

    // retrying properties
    int retries = 1;
    final int retryCount = properties.getCount().get();
    final var retryInterval = properties.getInterval().get();
    while (retries <= retryCount) {
      getLogger().quiet("Retrying, try number: {}, interval: {}", retries, retryInterval);
      Thread.sleep(retryInterval.toMillis());

      if (runnable.getAsBoolean()) {
        return;
      } else {
        ++retries;
      }
    }

    getLogger().quiet("Failed to connect");
  }
}
