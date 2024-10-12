package crimsonedgehope.minecraft.fabric.socksproxyclient.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

// Had caused the whole mod to fail. Ridiculous. Root cause unclear.
class Tasks implements Plugin<Project> {
    static final String GROUP = "SocksProxyClient"

    @Override
    void apply(Project project) {
        project.afterEvaluate {
            project.tasks.register("projectGitHash", ProjectGitHash)
        }
    }
}
