package com.wewelo.buildlogic.checkstyle;

import com.wewelo.buildlogic.utils.PathsUtils;
import com.wewelo.buildlogic.utils.ResourceUtils;
import lombok.AllArgsConstructor;
import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.api.resources.TextResource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@AllArgsConstructor
public class CopyCheckstyleConfigTask implements Action<Task> {
    private ClassLoader loader;

    @Override
    public void execute(Task task) {
        try {
            File configFile = task.getProject().file(Paths.get(PathsUtils.getConfigDir(task.getProject()),
                    "checkstyle", "checkstyle.xml"));
            Path configPath = Paths.get(configFile.getPath());
            Path parentDir = configPath.getParent();

            if (!Files.exists(parentDir)) {
                Files.createDirectories(configPath.getParent());
            }
            if (!Files.exists(configPath)) {
                TextResource configText = ResourceUtils.readTextResource(task.getProject(), loader,
                        "checkstyle/checkstyle.xml");
                Files.write(configPath, configText.asString().getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
