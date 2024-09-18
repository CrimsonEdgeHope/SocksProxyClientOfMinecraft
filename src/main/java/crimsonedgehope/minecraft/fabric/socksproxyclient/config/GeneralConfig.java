package crimsonedgehope.minecraft.fabric.socksproxyclient.config;

import com.google.gson.JsonObject;
import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.i18n.TranslateKeys;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.ProxyCredential;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.SocksVersion;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.net.InetSocketAddress;
import java.net.Proxy;

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
    private static final SocksProxyClientConfigEntry<SocksVersion> socksVersion =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "socksVersion",
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_GENERAL_PROXY_SOCKSVERSION), SocksVersion.SOCKS5);
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
        socksVersion.setValue(SocksVersion.valueOf(entries.get(socksVersion.getJsonEntry()).getAsString()));
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

    @NotNull
    public static Proxy getProxy() {
        return getProxy(usingProxy());
    }

    @NotNull
    public static Proxy getProxy(
            boolean useProxy
    ) {
        LOGGER.debug("useProxy {}", useProxy);

        if (!useProxy) {
            return Proxy.NO_PROXY;
        }

        return new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(proxyHost.getValue(), proxyPort.getValue()));
    }

    public static ProxyCredential getProxyCredential() {
        return getCustomCredential();
    }

    @Getter
    private static ProxyCredential customCredential = new ProxyCredential(null, null);

    public static void setCustomCredential(@Nullable String username, @Nullable String password) {
        setCustomCredentialUsername(username);
        setCustomCredentialPassword(password);
    }

    public static void setCustomCredentialUsername(@Nullable String username) {
        customCredential.setUsername(username);
    }

    public static void setCustomCredentialPassword(@Nullable String password) {
        customCredential.setPassword(password);
    }

    @Nullable
    public static SocksVersion getSocksVersion() {
        if (!usingProxy()) {
            return null;
        }
        return socksVersion.getValue();
    }
}
