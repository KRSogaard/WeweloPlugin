package com.wewelo.buildlogic.utils;

import org.apache.commons.io.IOUtils;
import org.gradle.api.Project;
import org.gradle.api.resources.TextResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ResourceUtils {
    public static TextResource readTextResource(Project project, ClassLoader classLoader, String fileName) {
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        try {
            return project.getResources().getText().fromString(
                    IOUtils.toString(inputStream, StandardCharsets.UTF_8.name()));
        } catch (IOException exp) {
            throw new RuntimeException(exp);
        }
    }
}
