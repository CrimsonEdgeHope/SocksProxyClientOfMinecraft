package crimsonedgehope.minecraft.fabric.socksproxyclient.config;

import com.google.gson.JsonObject;
import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.i18n.TranslateKeys;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.Credential;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.Socks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.net.InetSocketAddress;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public final class GeneralConfig extends SocksProxyClientConfig {

    private static final GeneralConfig INSTANCE;

    static {
        INSTANCE = new GeneralConfig();
    }

    private static final Logger LOGGER = SocksProxyClient.logger(GeneralConfig.class.getSimpleName());

    public static final String CATEGORY = "general";

    private static final SocksProxyClientConfigEntry<Boolean> useProxy =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "useProxy",
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_GENERAL_USEPROXY), false);
    private static final SocksProxyClientConfigEntry<Socks> socksVersion =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "socksVersion",
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_GENERAL_PROXY_SOCKSVERSION), Socks.SOCKS5);
    private static final SocksProxyClientConfigEntry<String> proxyHost =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "proxyHost",
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_GENERAL_PROXY_HOST), "localhost");
    private static final SocksProxyClientConfigEntry<Integer> proxyPort =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "proxyPort",
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_GENERAL_PROXY_PORT), 1080);
    private static final SocksProxyClientConfigEntry<String> proxyUsername =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "proxyUsername",
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_GENERAL_PROXY_USERNAME), "");
    private static final SocksProxyClientConfigEntry<String> proxyPassword =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "proxyPassword",
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_GENERAL_PROXY_PASSWORD), "");

    private GeneralConfig() {
        super(CATEGORY + ".json");
    }

    @Override
    public JsonObject defaultEntries() {
        JsonObject obj = new JsonObject();
        obj.addProperty(useProxy.getJsonEntry(), useProxy.getDefaultValue());
        obj.addProperty(socksVersion.getJsonEntry(), socksVersion.getDefaultValue().name());
        obj.addProperty(proxyHost.getJsonEntry(), proxyHost.getDefaultValue());
        obj.addProperty(proxyPort.getJsonEntry(), proxyPort.getDefaultValue());
        obj.addProperty(proxyUsername.getJsonEntry(), proxyUsername.getDefaultValue());
        obj.addProperty(proxyPassword.getJsonEntry(), proxyPassword.getDefaultValue());
        return obj;
    }

    @Override
    public void fromJsonObject(JsonObject entries) {
        useProxy.setValue(entries.get(useProxy.getJsonEntry()).getAsBoolean());
        socksVersion.setValue(Socks.valueOf(entries.get(socksVersion.getJsonEntry()).getAsString()));
        proxyHost.setValue(entries.get(proxyHost.getJsonEntry()).getAsString());
        proxyPort.setValue(entries.get(proxyPort.getJsonEntry()).getAsInt());
        proxyUsername.setValue(entries.get(proxyUsername.getJsonEntry()).getAsString());
        proxyPassword.setValue(entries.get(proxyPassword.getJsonEntry()).getAsString());
    }

    @Override
    public JsonObject toJsonObject() {
        JsonObject obj = new JsonObject();
        obj.addProperty(useProxy.getJsonEntry(), useProxy.getValue());
        obj.addProperty(socksVersion.getJsonEntry(), socksVersion.getValue().name());
        obj.addProperty(proxyHost.getJsonEntry(), proxyHost.getValue());
        obj.addProperty(proxyPort.getJsonEntry(), proxyPort.getValue());
        obj.addProperty(proxyUsername.getJsonEntry(), proxyUsername.getValue());
        obj.addProperty(proxyPassword.getJsonEntry(), proxyPassword.getValue());
        return obj;
    }

    public static boolean usingProxy() {
        return useProxy.getValue();
    }

    @Nullable
    public static ProxyEntry getProxyEntry() {
        return getProxyEntry(usingProxy());
    }

    @Nullable
    public static ProxyEntry getProxyEntry(boolean useProxy) {
        LOGGER.debug("useProxy {}", useProxy);
        return useProxy ? new ProxyEntry(
                socksVersion.getValue(),
                new InetSocketAddress(proxyHost.getValue(), proxyPort.getValue()),
                proxyUsername.getValue(),
                proxyPassword.getValue()
        ) : null;
    }
    @Nullable
    public static Credential getProxyCredential() {
        ProxyEntry entry = getProxyEntry();
        return Objects.isNull(entry) ? null : entry.getCredential();
    }
}
