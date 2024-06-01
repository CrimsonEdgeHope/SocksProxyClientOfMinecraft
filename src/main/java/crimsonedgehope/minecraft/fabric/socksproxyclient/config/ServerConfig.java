package crimsonedgehope.minecraft.fabric.socksproxyclient.config;

import com.google.gson.JsonObject;

import java.net.Proxy;

public final class ServerConfig extends SocksProxyClientConfig {

    private static final ServerConfig INSTANCE;

    static {
        INSTANCE = new ServerConfig();
    }

    private static final SocksProxyClientConfigEntry<Boolean> imposeProxyOnLoopback =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "imposeProxyOnLoopback", false, 1);
    private static final SocksProxyClientConfigEntry<Boolean> proxyYggdrasilAuth =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "proxyYggdrasilAuth", true, 1);
    private static final SocksProxyClientConfigEntry<Boolean> proxyPlayerSkinDownload =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "proxyPlayerSkinDownload", true, 1);
    private static final SocksProxyClientConfigEntry<Boolean> proxyServerResourceDownload =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "proxyServerResourceDownload", true, 1);
    private static final SocksProxyClientConfigEntry<Boolean> proxyBlockListSupplier =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "proxyBlockListSupplier", true, 1);
    private static final SocksProxyClientConfigEntry<Boolean> httpRemoteResolve =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "httpRemoteResolve", true);

    public static final String CATEGORY = "server";

    private ServerConfig() {
        super(CATEGORY + ".json");
    }

    public static boolean loopbackProxyOption() {
        return imposeProxyOnLoopback.getValue();
    }

    public static boolean usingProxy() {
        return GeneralConfig.usingProxy();
    }

    public static Proxy getProxy() {
        return GeneralConfig.getProxy();
    }

    public static Proxy getProxy(boolean useProxy) {
        return GeneralConfig.getProxy(useProxy);
    }

    public static Proxy getProxyForLoopback() {
        return getProxy(loopbackProxyOption());
    }

    public static boolean shouldProxyYggdrasilAuth() {
        return proxyYggdrasilAuth.getValue();
    }

    public static boolean shouldProxyPlayerSkinDownload() {
        return proxyPlayerSkinDownload.getValue();
    }

    public static boolean shouldProxyServerResourceDownload() {
        return proxyServerResourceDownload.getValue();
    }

    public static boolean shouldProxyBlockListSupplier() {
        return proxyBlockListSupplier.getValue();
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
        obj.addProperty(imposeProxyOnLoopback.getEntry(), imposeProxyOnLoopback.getDefaultValue());
        obj.addProperty(proxyYggdrasilAuth.getEntry(), proxyYggdrasilAuth.getDefaultValue());
        obj.addProperty(proxyPlayerSkinDownload.getEntry(), proxyPlayerSkinDownload.getDefaultValue());
        obj.addProperty(proxyServerResourceDownload.getEntry(), proxyServerResourceDownload.getDefaultValue());
        obj.addProperty(proxyBlockListSupplier.getEntry(), proxyBlockListSupplier.getDefaultValue());
        obj.addProperty(httpRemoteResolve.getEntry(), httpRemoteResolve.getDefaultValue());
        return obj;
    }

    @Override
    public JsonObject toJsonObject() {
        JsonObject obj = new JsonObject();
        obj.addProperty(imposeProxyOnLoopback.getEntry(), imposeProxyOnLoopback.getValue());
        obj.addProperty(proxyYggdrasilAuth.getEntry(), proxyYggdrasilAuth.getValue());
        obj.addProperty(proxyPlayerSkinDownload.getEntry(), proxyPlayerSkinDownload.getValue());
        obj.addProperty(proxyServerResourceDownload.getEntry(), proxyServerResourceDownload.getValue());
        obj.addProperty(proxyBlockListSupplier.getEntry(), proxyBlockListSupplier.getValue());
        obj.addProperty(httpRemoteResolve.getEntry(), httpRemoteResolve.getValue());
        return obj;
    }

    @Override
    public void fromJsonObject(JsonObject object) {
        imposeProxyOnLoopback.setValue(object.get(imposeProxyOnLoopback.getEntry()).getAsBoolean());
        proxyYggdrasilAuth.setValue(object.get(proxyYggdrasilAuth.getEntry()).getAsBoolean());
        proxyPlayerSkinDownload.setValue(object.get(proxyPlayerSkinDownload.getEntry()).getAsBoolean());
        proxyServerResourceDownload.setValue(object.get(proxyServerResourceDownload.getEntry()).getAsBoolean());
        proxyBlockListSupplier.setValue(object.get(proxyBlockListSupplier.getEntry()).getAsBoolean());
        httpRemoteResolve.setValue(object.get(httpRemoteResolve.getEntry()).getAsBoolean());
    }
}
