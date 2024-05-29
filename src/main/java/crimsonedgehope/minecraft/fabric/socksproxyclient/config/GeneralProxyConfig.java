package crimsonedgehope.minecraft.fabric.socksproxyclient.config;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.net.Proxy;

public final class GeneralProxyConfig extends SocksProxyClientConfig {

    public static final GeneralProxyConfig INSTANCE;

    static {
        INSTANCE = new GeneralProxyConfig();
    }

    public enum SocksVersion {
        SOCKS4,
        SOCKS5;
    }
    public static final String CATEGORY = "general";

    public static final SocksProxyClientConfigEntry<Boolean> useProxy =
            new SocksProxyClientConfigEntry<>(GeneralProxyConfig.class, "useProxy", false);
    public static final SocksProxyClientConfigEntry<SocksVersion> socksVersion =
            new SocksProxyClientConfigEntry<>(GeneralProxyConfig.class, "socksVersion", SocksVersion.SOCKS5);
    public static final SocksProxyClientConfigEntry<String> customProxyHost =
            new SocksProxyClientConfigEntry<>(GeneralProxyConfig.class, "customProxyHost", "localhost");
    public static final SocksProxyClientConfigEntry<Integer> customProxyPort =
            new SocksProxyClientConfigEntry<>(GeneralProxyConfig.class, "customProxyPort", 1080);
    public static final SocksProxyClientConfigEntry<String> customProxyUsername =
            new SocksProxyClientConfigEntry<>(GeneralProxyConfig.class, "customProxyUsername", "");
    public static final SocksProxyClientConfigEntry<String> customProxyPassword =
            new SocksProxyClientConfigEntry<>(GeneralProxyConfig.class, "customProxyPassword", "");
    public static final SocksProxyClientConfigEntry<Boolean> imposeProxyOnLoopback =
            new SocksProxyClientConfigEntry<>(GeneralProxyConfig.class, "imposeProxyOnLoopback", false);

    private GeneralProxyConfig() {
        super("general.json");
    }

    @Override
    public JsonObject defaultEntries() {
        JsonObject obj = new JsonObject();
        obj.addProperty(useProxy.getEntry(), useProxy.getDefaultValue());
        obj.addProperty(socksVersion.getEntry(), socksVersion.getDefaultValue().name());
        obj.addProperty(customProxyHost.getEntry(), customProxyHost.getDefaultValue());
        obj.addProperty(customProxyPort.getEntry(), customProxyPort.getDefaultValue());
        obj.addProperty(customProxyUsername.getEntry(), customProxyUsername.getDefaultValue());
        obj.addProperty(customProxyPassword.getEntry(), customProxyPassword.getDefaultValue());
        obj.addProperty(imposeProxyOnLoopback.getEntry(), imposeProxyOnLoopback.getDefaultValue());
        return obj;
    }

    @Override
    public void fromJsonObject(JsonObject object) {
        JsonObject entries = object;
        useProxy.setValue(entries.get(useProxy.getEntry()).getAsBoolean());
        socksVersion.setValue(SocksVersion.valueOf(entries.get(socksVersion.getEntry()).getAsString()));
        customProxyHost.setValue(entries.get(customProxyHost.getEntry()).getAsString());
        customProxyPort.setValue(entries.get(customProxyPort.getEntry()).getAsInt());
        customProxyUsername.setValue(entries.get(customProxyUsername.getEntry()).getAsString());
        customProxyPassword.setValue(entries.get(customProxyPassword.getEntry()).getAsString());
        imposeProxyOnLoopback.setValue(entries.get(imposeProxyOnLoopback.getEntry()).getAsBoolean());
    }

    @Override
    public JsonObject toJsonObject() {
        JsonObject obj = new JsonObject();
        obj.addProperty(useProxy.getEntry(), useProxy.getValue());
        obj.addProperty(socksVersion.getEntry(), socksVersion.getValue().name());
        obj.addProperty(customProxyHost.getEntry(), customProxyHost.getValue());
        obj.addProperty(customProxyPort.getEntry(), customProxyPort.getValue());
        obj.addProperty(customProxyUsername.getEntry(), customProxyUsername.getValue());
        obj.addProperty(customProxyPassword.getEntry(), customProxyPassword.getValue());
        obj.addProperty(imposeProxyOnLoopback.getEntry(), imposeProxyOnLoopback.getValue());
        return obj;
    }

    @Nullable
    public static Proxy getProxy() {
        return getProxy(useProxy.getValue());
    }
    public static Proxy getProxyForLoopback() {
        return getProxy(loopbackProxyOption());
    }

    @Nullable
    public static Proxy getProxy(
            boolean useProxy
    ) {
        LOGGER.debug("useProxy {}", useProxy);

        if (!useProxy) {
            return null;
        }

        return new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(customProxyHost.getValue(), customProxyPort.getValue()));
    }

    public static Credential getProxyCredential() {
        return getCustomCredential();
    }

    public static boolean loopbackProxyOption() {
        return imposeProxyOnLoopback.getValue();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static final class Credential {
        @Nullable private String username;
        @Nullable private String password;
    }

    @Getter
    private static Credential customCredential = new Credential(null, null);
    @Getter
    private static Credential credentialFromGameParam;

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

    public static SocksVersion getSocksVersion() {
        return socksVersion.getValue();
    }
}
