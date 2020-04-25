package com.wewelo.buildlogic;

import com.wewelo.buildlogic.checkstyle.CopyCheckstyleConfigTask;
import com.wewelo.buildlogic.checkstyle.WeweloCheckStylePlugin;
import com.wewelo.buildlogic.errorprone.WeweloErrorPronePlugin;
import com.wewelo.buildlogic.jacoco.WeweloJacocoPlugin;
import com.wewelo.buildlogic.utils.PathsUtils;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.quality.CheckstyleExtension;
import org.gradle.api.plugins.quality.CheckstylePlugin;

import java.nio.file.Paths;
import java.util.Optional;

public class WeweloPlugin implements Plugin<Project> {
    public void apply(Project project) {
        project.getPluginManager().withPlugin("java", plugin -> {
            project.getPlugins().apply(WeweloCheckStylePlugin.class);
            project.getPlugins().apply(WeweloJacocoPlugin.class);
            project.getPlugins().apply(WeweloErrorPronePlugin.class);


            project.getTasks().register("dealwithit", a -> {
                System.out.println("(•_•) ( •_•)>⌐■-■ (⌐■_■)");
            });
        });
    }
}