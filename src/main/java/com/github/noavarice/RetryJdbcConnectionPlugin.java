package com.github.noavarice;

import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.NonNullApi;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.TaskProvider;

/**
 * Plugin that registers {@link RetryJdbcConnectionTask} to the {@link Project}.
 *
 * @author noavarice
 * @since 1.0.0
 */
@NonNullApi
public class RetryJdbcConnectionPlugin implements Plugin<Project> {

  @Override
  public void apply(final Project project) {
    final var runtimeClasspath = createRuntimeClasspathConfiguration(project);
    final var configContainer = createConfigContainer(project);
    configContainer.all(config -> registerTask(project, config, runtimeClasspath));
    project.getExtensions().add("retryJdbcConnection", configContainer);
  }

  private static void registerTask(
      final Project project,
      final RetryJdbcConnectionPluginExtension config,
      final Configuration runtimeClasspath
  ) {
    final String configName = config.getName();
    final String taskName = "retryJdbcConnection" + capitalize(configName);
    final TaskProvider<RetryJdbcConnectionTask> taskProvider = project
        .getTasks()
        .register(taskName, RetryJdbcConnectionTask.class, config, runtimeClasspath);
    taskProvider.configure(
        task -> {
          final String taskDesc = "Retries JDBC connection for " + configName;
          task.setDescription(taskDesc);
        }
    );
  }

  private static Configuration createRuntimeClasspathConfiguration(final Project project) {
    return project.getConfigurations().create("retryJdbcConnectionClasspath");
  }

  private static String capitalize(final String s) {
    if (s.isEmpty()) {
      return s;
    }

    return Character.toUpperCase(s.charAt(0)) + s.substring(1);
  }

  private static NamedDomainObjectContainer<RetryJdbcConnectionPluginExtension> createConfigContainer(
      final Project project
  ) {
    final ObjectFactory objects = project.getObjects();
    return objects.domainObjectContainer(
        RetryJdbcConnectionPluginExtension.class,
        name -> objects.newInstance(RetryJdbcConnectionPluginExtension.class, name)
    );
  }
}
