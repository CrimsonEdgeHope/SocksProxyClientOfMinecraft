package crimsonedgehope.minecraft.fabric.socksproxyclient.config.yacl.controller;

import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ValueFormatter;
import dev.isxander.yacl3.impl.controller.StringControllerBuilderImpl;
import org.apache.commons.lang3.Validate;

public final class CredentialsStringControllerBuilderImpl extends StringControllerBuilderImpl implements CredentialsStringControllerBuilder {
    private ValueFormatter<String> formatter = CredentialsStringController.STARS::apply;

    public CredentialsStringControllerBuilderImpl(Option<String> option) {
        super(option);
    }

    @Override
    public CredentialsStringControllerBuilder formatValue(ValueFormatter<String> formatter) {
        Validate.notNull(formatter, "formatter cannot be null");
        this.formatter = formatter;
        return this;
    }

    @Override
    public CredentialsStringControllerBuilder starsFormatter() {
        this.formatter = CredentialsStringController.STARS::apply;
        return this;
    }

    @Override
    public Controller<String> build() {
        return CredentialsStringController.createInternal(option, formatter);
    }
}
