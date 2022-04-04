package com.github.noavarice;

import org.gradle.api.provider.Property;

/**
 * JDBC connection configuration for {@link RetryJdbcConnection}.
 *
 * @author noavarice
 * @since 1.0.0
 */
public interface ConnectionConfig {

  Property<String> getUrl();

  Property<String> getUsername();

  Property<String> getPassword();
}
