package crimsonedgehope.minecraft.fabric.socksproxyclient.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePluginExtension
import org.gradle.language.jvm.tasks.ProcessResources

abstract class Tasks implements Plugin<Project> {
    static final String GROUP = "SocksProxyClient"

    @Override
    void apply(Project project) {
        project.afterEvaluate {
            project.tasks.register("projectGitHash", ProjectGitHash) {
                it.doFirst {
                    project.ext.compilation_java_version = project.targetCompatibility
                }
                it.doLast {
                    def h = project.ext.git_short_hash
                    println "Now on ${h}"
                    def p = project.extensions.getByType(BasePluginExtension).archivesName
                    p.set("${p.get()}-${h}")
                }
            }
            project.tasks.named("processResources", ProcessResources) {
                it.dependsOn(project.tasks.named("projectGitHash", ProjectGitHash))
            }
        }
    }
}
