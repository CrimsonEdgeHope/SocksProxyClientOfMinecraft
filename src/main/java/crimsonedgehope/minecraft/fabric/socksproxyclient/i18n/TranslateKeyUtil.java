package crimsonedgehope.minecraft.fabric.socksproxyclient.i18n;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TranslateKeyUtil {
    public static final String PREFIX = "socksproxyclient";
    public static final String CONF_PREFIX = item(PREFIX, "config");

    public static String item(String prefix, Collection<String> subs) {
        StringBuilder builder = new StringBuilder(prefix);
        for (String s : subs) {
            builder.append(".");
            builder.append(s);
        }
        return builder.toString();
    }

    public static Text itemAsText(String prefix, Collection<String> subs) {
        return new TranslatableText(item(prefix, subs));
    }

    public static String item(String prefix, String... subs) {
        return item(prefix, Arrays.stream(subs).collect(Collectors.toList()));
    }

    public static Text itemAsText(String prefix, String... subs) {
        return new TranslatableText(item(prefix, subs));
    }

    public static String item(Collection<String> subs) {
        return item(PREFIX, subs);
    }

    public static Text itemAsText(Collection<String> subs) {
        return new TranslatableText(item(subs));
    }

    public static String configItem(String... subs) {
        return configItem(Arrays.stream(subs).collect(Collectors.toList()));
    }

    public static Text configItemAsText(String... subs) {
        return new TranslatableText(configItem(subs));
    }

    public static String configItem(Collection<String> subs) {
        return item(CONF_PREFIX, subs);
    }

    public static Text configItemAsText(Collection<String> subs) {
        return new TranslatableText(configItem(subs));
    }
}
