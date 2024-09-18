package crimsonedgehope.minecraft.fabric.socksproxyclient.config;

import com.google.gson.JsonObject;
import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.i18n.TranslateKeys;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.ProxyCredential;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.SocksVersion;
import lombok.Getter;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.net.InetSocketAddress;
import java.net.Proxy;

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
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_GENERAL_SOCKSVERSION), SocksVersion.SOCKS5);
    private static final SocksProxyClientConfigEntry<String> customProxyHost =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "customProxyHost",
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_GENERAL_CUSTOMPROXYHOST), "localhost");
    private static final SocksProxyClientConfigEntry<Integer> customProxyPort =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "customProxyPort",
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_GENERAL_CUSTOMPROXYPORT), 1080);
    private static final SocksProxyClientConfigEntry<String> customProxyUsername =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "customProxyUsername",
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_GENERAL_CUSTOMPROXYUSERNAME), "");
    private static final SocksProxyClientConfigEntry<String> customProxyPassword =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "customProxyPassword",
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_GENERAL_CUSTOMPROXYPASSWORD), "");

    private GeneralConfig() {
        super(CATEGORY + ".json");
    }

    @Override
    public JsonObject defaultEntries() {
        JsonObject obj = new JsonObject();
        obj.addProperty(useProxy.getJsonEntry(), useProxy.getDefaultValue());
        obj.addProperty(socksVersion.getJsonEntry(), socksVersion.getDefaultValue().name());
        obj.addProperty(customProxyHost.getJsonEntry(), customProxyHost.getDefaultValue());
        obj.addProperty(customProxyPort.getJsonEntry(), customProxyPort.getDefaultValue());
        obj.addProperty(customProxyUsername.getJsonEntry(), customProxyUsername.getDefaultValue());
        obj.addProperty(customProxyPassword.getJsonEntry(), customProxyPassword.getDefaultValue());
        return obj;
    }

    @Override
    public void fromJsonObject(JsonObject entries) {
        useProxy.setValue(entries.get(useProxy.getJsonEntry()).getAsBoolean());
        socksVersion.setValue(SocksVersion.valueOf(entries.get(socksVersion.getJsonEntry()).getAsString()));
        customProxyHost.setValue(entries.get(customProxyHost.getJsonEntry()).getAsString());
        customProxyPort.setValue(entries.get(customProxyPort.getJsonEntry()).getAsInt());
        customProxyUsername.setValue(entries.get(customProxyUsername.getJsonEntry()).getAsString());
        customProxyPassword.setValue(entries.get(customProxyPassword.getJsonEntry()).getAsString());
    }

    @Override
    public JsonObject toJsonObject() {
        JsonObject obj = new JsonObject();
        obj.addProperty(useProxy.getJsonEntry(), useProxy.getValue());
        obj.addProperty(socksVersion.getJsonEntry(), socksVersion.getValue().name());
        obj.addProperty(customProxyHost.getJsonEntry(), customProxyHost.getValue());
        obj.addProperty(customProxyPort.getJsonEntry(), customProxyPort.getValue());
        obj.addProperty(customProxyUsername.getJsonEntry(), customProxyUsername.getValue());
        obj.addProperty(customProxyPassword.getJsonEntry(), customProxyPassword.getValue());
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

        return new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(customProxyHost.getValue(), customProxyPort.getValue()));
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
