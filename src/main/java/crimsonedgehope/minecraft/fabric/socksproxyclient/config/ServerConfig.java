package crimsonedgehope.minecraft.fabric.socksproxyclient.config;

import com.google.gson.JsonObject;

import java.net.Proxy;

public final class ServerConfig extends SocksProxyClientConfig {

    private static final ServerConfig INSTANCE;

    static {
        INSTANCE = new ServerConfig();
    }

    private static final SocksProxyClientConfigEntry<Boolean> proxyMinecraft =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "proxyMinecraft", true);
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
    private static final SocksProxyClientConfigEntry<Boolean> imposeProxyOnMinecraftLoopback =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "imposeProxyOnMinecraftLoopback", false, 1);

    public static final String CATEGORY = "server";

    private ServerConfig() {
        super(CATEGORY + ".json");
    }

    public static boolean usingProxyOnMinecraft() {
        return GeneralConfig.usingProxy() && proxyMinecraft.getValue();
    }

    public static boolean minecraftLoopbackProxyOption() {
        return usingProxyOnMinecraft() && imposeProxyOnMinecraftLoopback.getValue();
    }

    public static Proxy getProxyForMinecraft() {
        return getProxyForMinecraft(usingProxyOnMinecraft());
    }

    public static Proxy getProxyForMinecraft(boolean useProxy) {
        return GeneralConfig.getProxy(useProxy);
    }

    public static Proxy getProxyForMinecraftLoopback() {
        return getProxyForMinecraft(minecraftLoopbackProxyOption());
    }

    public static boolean shouldProxyYggdrasilAuth() {
        return GeneralConfig.usingProxy() && proxyYggdrasilAuth.getValue();
    }

    public static boolean shouldProxyPlayerSkinDownload() {
        return GeneralConfig.usingProxy() && proxyPlayerSkinDownload.getValue();
    }

    public static boolean shouldProxyServerResourceDownload() {
        return GeneralConfig.usingProxy() && proxyServerResourceDownload.getValue();
    }

    public static boolean shouldProxyBlockListSupplier() {
        return GeneralConfig.usingProxy() && proxyBlockListSupplier.getValue();
    }

    public static ProxyCredential getProxyCredential() {
        return GeneralConfig.getProxyCredential();
    }

    public static SocksVersion getSocksVersion() {
        return GeneralConfig.getSocksVersion();
    }

    public static boolean remoteResolve() {
        return GeneralConfig.usingProxy() && httpRemoteResolve.getValue();
    }

    @Override
    public JsonObject defaultEntries() {
        JsonObject obj = new JsonObject();
        obj.addProperty(proxyMinecraft.getEntry(), proxyMinecraft.getDefaultValue());
        obj.addProperty(proxyYggdrasilAuth.getEntry(), proxyYggdrasilAuth.getDefaultValue());
        obj.addProperty(proxyPlayerSkinDownload.getEntry(), proxyPlayerSkinDownload.getDefaultValue());
        obj.addProperty(proxyServerResourceDownload.getEntry(), proxyServerResourceDownload.getDefaultValue());
        obj.addProperty(proxyBlockListSupplier.getEntry(), proxyBlockListSupplier.getDefaultValue());
        obj.addProperty(httpRemoteResolve.getEntry(), httpRemoteResolve.getDefaultValue());
        obj.addProperty(imposeProxyOnMinecraftLoopback.getEntry(), imposeProxyOnMinecraftLoopback.getDefaultValue());
        return obj;
    }

    @Override
    public JsonObject toJsonObject() {
        JsonObject obj = new JsonObject();
        obj.addProperty(proxyMinecraft.getEntry(), proxyMinecraft.getValue());
        obj.addProperty(proxyYggdrasilAuth.getEntry(), proxyYggdrasilAuth.getValue());
        obj.addProperty(proxyPlayerSkinDownload.getEntry(), proxyPlayerSkinDownload.getValue());
        obj.addProperty(proxyServerResourceDownload.getEntry(), proxyServerResourceDownload.getValue());
        obj.addProperty(proxyBlockListSupplier.getEntry(), proxyBlockListSupplier.getValue());
        obj.addProperty(httpRemoteResolve.getEntry(), httpRemoteResolve.getValue());
        obj.addProperty(imposeProxyOnMinecraftLoopback.getEntry(), imposeProxyOnMinecraftLoopback.getValue());
        return obj;
    }

    @Override
    public void fromJsonObject(JsonObject object) {
        proxyMinecraft.setValue(object.get(proxyMinecraft.getEntry()).getAsBoolean());
        proxyYggdrasilAuth.setValue(object.get(proxyYggdrasilAuth.getEntry()).getAsBoolean());
        proxyPlayerSkinDownload.setValue(object.get(proxyPlayerSkinDownload.getEntry()).getAsBoolean());
        proxyServerResourceDownload.setValue(object.get(proxyServerResourceDownload.getEntry()).getAsBoolean());
        proxyBlockListSupplier.setValue(object.get(proxyBlockListSupplier.getEntry()).getAsBoolean());
        httpRemoteResolve.setValue(object.get(httpRemoteResolve.getEntry()).getAsBoolean());
        imposeProxyOnMinecraftLoopback.setValue(object.get(imposeProxyOnMinecraftLoopback.getEntry()).getAsBoolean());
    }
}
