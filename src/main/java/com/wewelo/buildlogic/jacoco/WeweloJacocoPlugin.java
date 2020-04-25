package com.wewelo.buildlogic.jacoco;

import com.wewelo.buildlogic.checkstyle.CopyCheckstyleConfigTask;
import com.wewelo.buildlogic.utils.PathsUtils;
import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.internal.AbstractTask;
import org.gradle.api.plugins.quality.CheckstyleExtension;
import org.gradle.api.plugins.quality.CheckstylePlugin;
import org.gradle.api.reporting.DirectoryReport;
import org.gradle.api.reporting.ReportingExtension;
import org.gradle.testing.jacoco.plugins.JacocoPlugin;
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension;
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension;
import org.gradle.testing.jacoco.tasks.JacocoReport;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class WeweloJacocoPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getPlugins().apply(JacocoPlugin.class);
        Action<JacocoReport> jacocoReportAction = new Action<JacocoReport>() {
            @Override
            public void execute(final JacocoReport reportTask) {
                reportTask.getReports().getCsv().setEnabled(false);
                reportTask.getReports().getXml().setEnabled(false);
                DirectoryReport dirReport = reportTask.getReports().getHtml();
                dirReport.setEnabled(true);
                dirReport.setDestination( project.file(Paths.get(PathsUtils.getReportDir(project), "Kcoverage")));
            }
        };
         project.getTasks().withType(JacocoReport.class).configureEach(jacocoReportAction);

        Optional<Task> testTask = project.getTasksByName("test", true)
                .stream().findFirst();
        if (!testTask.isPresent()) {
            throw new RuntimeException("Could not find the task test");
        }
        testTask.get().finalizedBy("jacocoTestReport");


        Optional<Task> checkTask = project.getTasksByName("check", true)
                .stream().findFirst();
        if (!checkTask.isPresent()) {
            throw new RuntimeException("Could not find the task check");
        }
        checkTask.get().dependsOn("jacocoTestCoverageVerification");
    }

}


/*
 jacocoTestCoverageVerification {
     violationRules {
         rule {
             limit {
                 minimum = 0.8
             }
             excludes = [
                 '**\/Application.*',
                 '**\/*Configuration.*'
             ]
         }
     }
 }
 check.dependsOn jacocoTestCoverageVerification

 */