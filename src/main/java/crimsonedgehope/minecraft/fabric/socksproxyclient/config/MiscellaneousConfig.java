package crimsonedgehope.minecraft.fabric.socksproxyclient.config;

import com.google.gson.JsonObject;
import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public final class MiscellaneousConfig extends SocksProxyClientConfig {

    private static final MiscellaneousConfig INSTANCE;

    static {
        INSTANCE = new MiscellaneousConfig();
    }

    private static final Logger LOGGER = SocksProxyClient.logger(MiscellaneousConfig.class.getSimpleName());

    public static final String CATEGORY = "miscellaneous";

    private MiscellaneousConfig() {
        super(CATEGORY + ".json");
    }

    @Override
    public JsonObject defaultEntries() {
        JsonObject obj = new JsonObject();

        return obj;
    }

    @Override
    public void fromJsonObject(JsonObject entries) {

    }

    @Override
    public JsonObject toJsonObject() {
        JsonObject obj = new JsonObject();

        return obj;
    }
}
