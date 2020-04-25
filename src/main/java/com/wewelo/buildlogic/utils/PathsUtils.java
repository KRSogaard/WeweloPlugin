package com.wewelo.buildlogic.utils;

import org.gradle.api.Project;

import java.nio.file.Paths;

public class PathsUtils {

    public static String getConfigDir(Project project) {
        return Paths.get(project.getBuildDir().toString(), "configuration").toString();
    }
    public static String getReportDir(Project project) {
        return Paths.get(project.getBuildDir().toString(), "reports").toString();
    }
}
