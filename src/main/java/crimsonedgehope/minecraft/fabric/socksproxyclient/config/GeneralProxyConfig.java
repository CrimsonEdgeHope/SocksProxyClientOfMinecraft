package crimsonedgehope.minecraft.fabric.socksproxyclient.config;

import com.google.gson.JsonObject;
import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.net.InetSocketAddress;
import java.net.Proxy;

public final class GeneralProxyConfig extends SocksProxyClientConfig {

    public static final GeneralProxyConfig INSTANCE;

    static {
        INSTANCE = new GeneralProxyConfig();
    }

    public enum SocksVersion {
        SOCKS4(4),
        SOCKS5(5);

        public final int ver;
        SocksVersion(int ver) {
            this.ver =  ver;
        }
    }

    public enum SocksSelection {
        GAME,
        CUSTOM
    }

    public static final String CATEGORY = "general";

    private static final Logger LOGGER = SocksProxyClient.logger();

    public static final SocksProxyClientConfigEntry<Boolean> useProxy =
            new SocksProxyClientConfigEntry<>(GeneralProxyConfig.class, "useProxy", false);
    public static final SocksProxyClientConfigEntry<SocksSelection> useProxyFrom =
            new SocksProxyClientConfigEntry<>(GeneralProxyConfig.class, "useProxyFrom", SocksSelection.GAME);
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
        obj.addProperty(useProxyFrom.getEntry(), useProxyFrom.getDefaultValue().name());
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
        useProxyFrom.setValue(SocksSelection.valueOf(entries.get(useProxyFrom.getEntry()).getAsString()));
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
        obj.addProperty(useProxyFrom.getEntry(), useProxyFrom.getValue().name());
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

    public static Proxy getProxy(boolean useProxy) {
        return getProxy(useProxy, useProxyFrom.getValue());
    }

    public static Proxy getProxy(SocksSelection proxyOption) {
        return getProxy(useProxy.getValue(), proxyOption);
    }

    public static Proxy getProxyForLoopback() {
        return getProxy(loopbackProxyOption());
    }

    public static Proxy getProxy(boolean useProxy, SocksSelection proxyOption) {
        return switch (proxyOption) {
            case GAME -> getProxy(useProxy, true);
            case CUSTOM -> getProxy(useProxy, false);
            default -> getProxy(false);
        };
    }

    @Nullable
    public static Proxy getProxy(
            boolean useProxy,
            boolean useProxyHostFromGameParam
    ) {

        LOGGER.debug("useProxy {}, useProxyHostFromGameParam {}", useProxy, useProxyHostFromGameParam);

        if (!useProxy) {
            return null;
        }

        Proxy proxy;

        if (useProxyHostFromGameParam) {
            proxy = MinecraftClient.getInstance().getNetworkProxy();
            if (!proxy.type().equals(Proxy.Type.SOCKS)) {
                return null;
            }
            LOGGER.debug("Use proxyHost from game parameters.");
            return proxy;
        }

        proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(customProxyHost.getValue(), customProxyPort.getValue()));
        LOGGER.debug("Use custom proxyHost field.");

        return proxy;
    }

    public static Credential getProxyCredential() {
        return getProxyCredential(useProxyFrom.getValue());
    }

    public static Credential getProxyCredential(SocksSelection proxyOption) {
        return switch (proxyOption) {
            case GAME -> getProxyCredential(true);
            case CUSTOM -> getProxyCredential(false);
            default -> new Credential(null, null);
        };
    }

    public static Credential getProxyCredential(boolean useProxyHostFromGameParam) {
        LOGGER.debug("getProxyCredential: useProxyHostFromGameParam {}", useProxyHostFromGameParam);
        if (useProxyHostFromGameParam) {
            return getCredentialFromGameParam();
        }
        return getCustomCredential();
    }

    public static Credential getProxyCredentialForLoopback() {
        return getProxyCredential(loopbackProxyOption());
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

    public static void setCredentialFromGameParam(@Nullable String username, @Nullable String password) {
        credentialFromGameParam = new Credential(username, password);
    }

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

    public static int getSocksVersion() {
        return socksVersion.getValue().ver;
    }
}
