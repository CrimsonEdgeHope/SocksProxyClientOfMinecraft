package crimsonedgehope.minecraft.fabric.socksproxyclient.config;

import com.google.gson.JsonObject;

import java.net.Proxy;

public final class ServerConfig extends SocksProxyClientConfig {

    private static final ServerConfig INSTANCE;

    static {
        INSTANCE = new ServerConfig();
    }

    private static final SocksProxyClientConfigEntry<Boolean> httpRemoteResolve =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "httpRemoteResolve", true);
    private static final SocksProxyClientConfigEntry<Boolean> imposeProxyOnLoopback =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "imposeProxyOnLoopback", false);

    private ServerConfig() {
        super("server.json");
    }

    public static final String CATEGORY = "server";

    public static boolean loopbackProxyOption() {
        return imposeProxyOnLoopback.getValue();
    }

    public static boolean usingProxy() {
        return GeneralConfig.usingProxy();
    }

    public static Proxy getProxy() {
        return GeneralConfig.getProxy();
    }

    public static Proxy getProxyForLoopback() {
        return GeneralConfig.getProxy(loopbackProxyOption());
    }

    public static ProxyCredential getProxyCredential() {
        return GeneralConfig.getProxyCredential();
    }

    public static SocksVersion getSocksVersion() {
        return GeneralConfig.getSocksVersion();
    }

    public static boolean remoteResolve() {
        return httpRemoteResolve.getDefaultValue();
    }

    @Override
    public JsonObject defaultEntries() {
        JsonObject obj = new JsonObject();
        obj.addProperty(httpRemoteResolve.getEntry(), httpRemoteResolve.getDefaultValue());
        obj.addProperty(imposeProxyOnLoopback.getEntry(), imposeProxyOnLoopback.getDefaultValue());
        return obj;
    }

    @Override
    public JsonObject toJsonObject() {
        JsonObject obj = new JsonObject();
        obj.addProperty(httpRemoteResolve.getEntry(), httpRemoteResolve.getValue());
        obj.addProperty(imposeProxyOnLoopback.getEntry(), imposeProxyOnLoopback.getValue());
        return obj;
    }

    @Override
    public void fromJsonObject(JsonObject object) {
        httpRemoteResolve.setValue(object.get(httpRemoteResolve.getEntry()).getAsBoolean());
        imposeProxyOnLoopback.setValue(object.get(imposeProxyOnLoopback.getEntry()).getAsBoolean());
    }
}
