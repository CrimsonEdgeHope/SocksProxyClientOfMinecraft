package crimsonedgehope.minecraft.fabric.socksproxyclient.config;

import com.google.gson.JsonObject;
import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.entry.SocksProxyClientConfigEntry;
import crimsonedgehope.minecraft.fabric.socksproxyclient.i18n.TranslateKeys;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public final class MiscellaneousConfig extends SocksProxyClientConfig {

    private static final MiscellaneousConfig INSTANCE;

    static {
        INSTANCE = new MiscellaneousConfig();
    }

    private static final Logger LOGGER = SocksProxyClient.logger(MiscellaneousConfig.class.getSimpleName());

    public static final String CATEGORY = "miscellaneous";

    private static final SocksProxyClientConfigEntry<Boolean> buttonsInMultiplayerScreen =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "buttonsInMultiplayerScreen",
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_MISCELLANEOUS_BUTTONINMULTIPLAYERSCREEN),
                    true);
    private static final SocksProxyClientConfigEntry<Boolean> checkUpdates =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "checkUpdates",
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_MISCELLANEOUS_CHECKUPDATES),
                    true);

    private MiscellaneousConfig() {
        super(CATEGORY + ".json");
    }

    @Override
    public JsonObject defaultEntries() {
        JsonObject obj = new JsonObject();
        obj.addProperty(buttonsInMultiplayerScreen.getJsonEntry(), buttonsInMultiplayerScreen.getDefaultValue());
        obj.addProperty(checkUpdates.getJsonEntry(), checkUpdates.getDefaultValue());
        return obj;
    }

    @Override
    public void fromJsonObject(JsonObject entries) {
        buttonsInMultiplayerScreen.setValue(entries.get(buttonsInMultiplayerScreen.getJsonEntry()).getAsBoolean());
        checkUpdates.setValue(entries.get(checkUpdates.getJsonEntry()).getAsBoolean());
    }

    @Override
    public JsonObject toJsonObject() {
        JsonObject obj = new JsonObject();
        obj.addProperty(buttonsInMultiplayerScreen.getJsonEntry(), buttonsInMultiplayerScreen.getValue());
        obj.addProperty(checkUpdates.getJsonEntry(), checkUpdates.getValue());
        return obj;
    }

    public static boolean showButtonsInMultiplayerScreen() {
        return buttonsInMultiplayerScreen.getValue();
    }

    public static boolean shouldCheckUpdates() {
        return checkUpdates.getValue();
    }
}
