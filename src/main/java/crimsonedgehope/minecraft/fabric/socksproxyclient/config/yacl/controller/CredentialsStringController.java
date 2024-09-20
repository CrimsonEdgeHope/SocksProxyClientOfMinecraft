package crimsonedgehope.minecraft.fabric.socksproxyclient.config.yacl.controller;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ValueFormatter;
import dev.isxander.yacl3.gui.controllers.string.StringController;
import net.minecraft.text.Text;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;

public final class CredentialsStringController extends StringController {

    public static final Function<String, Text> STARS = v -> Text.of("*".repeat(v.length()));

    private final ValueFormatter<String> valueFormatter;

    public CredentialsStringController(Option<String> option, Function<String, Text> formatter) {
        super(option);
        this.valueFormatter = formatter::apply;
    }

    @Override
    public Text formatValue() {
        return valueFormatter.format(option().pendingValue());
    }

    @ApiStatus.Internal
    public static CredentialsStringController createInternal(Option<String> option, ValueFormatter<String> formatter) {
        return new CredentialsStringController(option, formatter::format);
    }
}
