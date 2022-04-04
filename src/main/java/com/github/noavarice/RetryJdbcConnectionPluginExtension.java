package com.github.noavarice;

import javax.inject.Inject;
import org.gradle.api.model.ObjectFactory;

/**
 * Configuration for {@link RetryJdbcConnection}.
 *
 * @author noavarice
 * @since 1.0.0
 */
public abstract class RetryJdbcConnectionPluginExtension {

  private final String name;

  private final ConnectionConfig connection;

  private final RetryConfig retry;

  @Inject
  public RetryJdbcConnectionPluginExtension(final ObjectFactory objects, final String name) {
    this.name = name;
    connection = objects.newInstance(ConnectionConfig.class);
    retry = objects.newInstance(RetryConfig.class);
  }

  public String getName() {
    return name;
  }

  public ConnectionConfig getConnection() {
    return connection;
  }

  public RetryConfig getRetry() {
    return retry;
  }
}
