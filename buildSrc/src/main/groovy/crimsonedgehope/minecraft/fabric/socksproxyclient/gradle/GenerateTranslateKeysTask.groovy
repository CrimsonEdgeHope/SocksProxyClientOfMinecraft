package crimsonedgehope.minecraft.fabric.socksproxyclient.gradle

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.stream.JsonReader
import com.squareup.javapoet.*
import lombok.NoArgsConstructor
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction

import javax.lang.model.element.Modifier

abstract class GenerateTranslateKeysTask extends DefaultTask {
    @InputFile File inputFile
    @Input String outputDir
    @Input String packageTarget

    GenerateTranslateKeysTask() {
        group = Tasks.GROUP
        description = "Generate translate keys. All keys in accordance with en_us.json"
    }

    @TaskAction
    void generate() throws Exception {
        final FileReader reader = new FileReader(inputFile)
        final Gson gson = new Gson()
        final JsonObject object = gson.fromJson(new JsonReader(reader), JsonObject.class)

        final AnnotationSpec lombok =
                AnnotationSpec.builder(NoArgsConstructor.class)
                        .addMember("access", CodeBlock.builder().add('$L', "lombok.AccessLevel.PRIVATE").build()).build()

        final TypeSpec.Builder typeSpec =
                TypeSpec.classBuilder("TranslateKeys").addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addAnnotation(lombok)

        for (String key : object.keySet()) {
            if (!key.startsWith("socksproxyclient")) {
                continue
            }
            final String k0 = key.toUpperCase().replace(".", "_")
            typeSpec.addField(FieldSpec.builder(String.class, k0)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer("\"" + key + "\"").build())
        }

        JavaFile out
        out = JavaFile.builder(packageTarget, typeSpec.build()).build()
        out.writeTo(new File(outputDir).toPath())
    }
}
