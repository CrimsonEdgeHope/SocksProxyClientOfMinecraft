package crimsonedgehope.minecraft.fabric.socksproxyclient.config.yacl.controller;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;

import java.util.function.Predicate;

public interface ValidStringControllerBuilder extends StringControllerBuilder {
    ValidStringControllerBuilder validityPredication(Predicate<String> validityPredication);

    static ValidStringControllerBuilder create(Option<String> option) {
        return new ValidStringControllerBuilderImpl(option);
    }
}
