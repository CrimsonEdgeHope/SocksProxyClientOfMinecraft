package crimsonedgehope.minecraft.fabric.socksproxyclient.i18n;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collection;

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

    public static String item(String prefix, String... subs) {
        return item(prefix, Arrays.stream(subs).toList());
    }

    public static String item(Collection<String> subs) {
        return item(PREFIX, subs);
    }

    public static String configItem(String... subs) {
        return configItem(Arrays.stream(subs).toList());
    }

    public static String configItem(Collection<String> subs) {
        return item(CONF_PREFIX, subs);
    }
}
