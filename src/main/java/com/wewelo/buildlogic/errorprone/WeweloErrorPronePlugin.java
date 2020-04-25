package com.wewelo.buildlogic.errorprone;

import com.google.common.collect.ImmutableList;
import net.ltgt.gradle.errorprone.CheckSeverity;
import net.ltgt.gradle.errorprone.ErrorProneOptions;
import net.ltgt.gradle.errorprone.ErrorPronePlugin;
import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.UnknownDomainObjectException;
import org.gradle.api.artifacts.DependencyResolutionListener;
import org.gradle.api.artifacts.DependencySet;
import org.gradle.api.artifacts.ResolvableDependencies;
import org.gradle.api.plugins.ExtensionAware;
import org.gradle.api.reporting.DirectoryReport;
import org.gradle.api.tasks.compile.JavaCompile;
import org.gradle.testing.jacoco.plugins.JacocoPlugin;
import org.gradle.testing.jacoco.tasks.JacocoReport;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class WeweloErrorPronePlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getPlugins().apply("net.ltgt.errorprone");

        //DependencySet classpathDeps = project.getConfigurations().getByName("classpath").getDependencies();
        DependencySet errorproneDeps = project.getConfigurations().getByName("errorprone").getDependencies();
        DependencySet errorproneJavacDeps = project.getConfigurations().getByName("errorproneJavac").getDependencies();

        project.getGradle().addListener(new DependencyResolutionListener() {
            @Override
            public void beforeResolve(ResolvableDependencies resolvableDependencies) {
                // Need to figure how to do this
                //classpathDeps.add(project.getDependencies().create("net.ltgt.gradle:gradle-errorprone-plugin:1.1.1"));
                errorproneDeps.add(project.getDependencies().create("com.google.errorprone:error_prone_core:2.3.4"));
                errorproneJavacDeps.add(project.getDependencies().create("com.google.errorprone:javac:9+181-r4173-1"));
                project.getGradle().removeListener(this);
            }

            @Override
            public void afterResolve(ResolvableDependencies resolvableDependencies) {}
        });

        project.getTasks().withType(JavaCompile.class).configureEach(javaCompile -> {
            ((ExtensionAware) javaCompile.getOptions()).getExtensions()
                    .configure(ErrorProneOptions.class, errorProneOptions -> {
                        errorProneOptions.getDisableWarningsInGeneratedCode().set(true);
                        errorProneOptions.getEnabled().set(true);
                        errorProneOptions.getExcludedPaths().set(".*/build/gen.*/.*");
                        errorProneOptions.getErrorproneArgs().set(ImmutableList.of(
                                "-Xep:WildcardImport:ERROR",
                                "-Xep:UnusedMethod:ERROR",
                                "-Xep:RemoveUnusedImports:ERROR",
                                "-Xep:UnusedVariable:OFF",
                                "-Werror"
                        ));
                    });
        });
    }
}


/*
    tasks.withType(JavaCompile).configureEach {
        options.errorprone.disableWarningsInGeneratedCode = true
        options.errorprone.enabled = true // change it to true to enable
        options.errorprone.excludedPaths = ".*\/build/gen.*\/.*"
        options.errorprone.errorproneArgs = [ "-Xep:DoubleBraceInitialization:ERROR",
        "-Xep:WildcardImport:ERROR",
        "-Xep:UnusedMethod:ERROR",
        "-Xep:RemoveUnusedImports:ERROR",
        "-Xep:UnusedVariable:OFF",
        "-Werror"]
        }
 */