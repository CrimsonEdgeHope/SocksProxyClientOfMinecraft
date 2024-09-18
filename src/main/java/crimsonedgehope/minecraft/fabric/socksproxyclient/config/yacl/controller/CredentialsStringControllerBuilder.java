package crimsonedgehope.minecraft.fabric.socksproxyclient.config.yacl.controller;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.api.controller.ValueFormattableController;

public interface CredentialsStringControllerBuilder extends StringControllerBuilder, ValueFormattableController<String, CredentialsStringControllerBuilder> {
    static CredentialsStringControllerBuilder create(Option<String> option) {
        return new CredentialsStringControllerBuilderImpl(option);
    }

    CredentialsStringControllerBuilder starsFormatter();
}
