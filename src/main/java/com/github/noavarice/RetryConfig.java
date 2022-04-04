package com.github.noavarice;

import java.time.Duration;
import javax.inject.Inject;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;

/**
 * Retry configuration for {@link RetryJdbcConnection}.
 *
 * @author noavarice
 * @since 1.0.0
 */
public abstract class RetryConfig {

  private final Property<Duration> initialDelay;

  private final Property<Duration> interval;

  private final Property<Integer> count;

  @Inject
  public RetryConfig(final ObjectFactory objects) {
    initialDelay = objects.property(Duration.class).convention(Duration.ZERO);
    interval = objects.property(Duration.class).convention(Duration.ofSeconds(5));
    count = objects.property(Integer.class).convention(3);
  }

  public Property<Duration> getInitialDelay() {
    return initialDelay;
  }

  public Property<Duration> getInterval() {
    return interval;
  }

  public Property<Integer> getCount() {
    return count;
  }
}
