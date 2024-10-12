package crimsonedgehope.minecraft.fabric.socksproxyclient.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class ProjectGitHash extends DefaultTask {
    ProjectGitHash() {
        group = Tasks.GROUP
        description = "\"git_hash\" project property"
    }

    @TaskAction
    void run() {
        String commandShortHash = "git rev-parse --short HEAD"
        String commandHash = "git rev-parse HEAD"
        String commandStatus = "git status --porcelain"
        String res1 = "local"
        String res2 = res1

        try {
            Process statusProcess = commandStatus.execute()
            String statusOutput = statusProcess.text.trim()

            if (statusOutput.isEmpty()) {
                Process shortHashProcess = commandShortHash.execute()
                Process hashProcess = commandHash.execute()
                String shortHash = shortHashProcess.text.trim()
                String hash = hashProcess.text.trim()

                if (!shortHash.isEmpty() && !hash.isEmpty()) {
                    res1 = shortHash
                    res2 = hash
                }
            } else {
                res1 = "local"
                res2 = res1
            }
        } catch (Exception e) {
            // No-op
        }
        project.ext.git_short_hash = res1
        project.ext.git_hash = res2
    }
}
