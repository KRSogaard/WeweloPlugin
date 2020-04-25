package com.wewelo.buildlogic.checkstyle;

import com.wewelo.buildlogic.utils.PathsUtils;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.quality.CheckstyleExtension;
import org.gradle.api.plugins.quality.CheckstylePlugin;

import java.nio.file.Paths;
import java.util.Optional;

public class WeweloCheckStylePlugin implements Plugin<Project> {
    private static final String DEFAULT_CHECKSTYLE_VERSION = "8.13";
    private final ClassLoader loader = getClass().getClassLoader();

    public void apply(Project project) {
        project.getPlugins().apply(CheckstylePlugin.class);
        // Set default version (outside afterEvaluate so it can be overridden).
        CheckstyleExtension ce = project.getExtensions().getByType(CheckstyleExtension.class);
        project.getExtensions()
                .configure(CheckstyleExtension.class, ext -> ext.setToolVersion(DEFAULT_CHECKSTYLE_VERSION));
        ce.setConfigDir(project.file(Paths.get(PathsUtils.getConfigDir(project), "checkstyle")));
        ce.setShowViolations(true);
        ce.setIgnoreFailures(false);
        ce.setMaxWarnings(0);
        ce.setMaxErrors(0);

        Optional<Task> checkstyleTask = project.getTasksByName("checkstyleMain", true)
                .stream().findFirst();
        if (!checkstyleTask.isPresent()) {
            throw new RuntimeException("Could not find the task checkstyleMain");
        }
        checkstyleTask.get().doFirst(new CopyCheckstyleConfigTask(loader));
    }
}
