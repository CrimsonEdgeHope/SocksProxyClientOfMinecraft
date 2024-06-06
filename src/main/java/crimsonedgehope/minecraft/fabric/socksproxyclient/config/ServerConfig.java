package crimsonedgehope.minecraft.fabric.socksproxyclient.config;

import com.google.gson.JsonObject;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.DNSOverHTTPSProvider;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.ProxyCredential;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.SocksVersion;

import java.net.Proxy;

public final class ServerConfig extends SocksProxyClientConfig {

    private static final ServerConfig INSTANCE;

    static {
        INSTANCE = new ServerConfig();
    }

    private static final SocksProxyClientConfigEntry<Boolean> proxyMinecraft =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "proxyMinecraft", true);
    private static final SocksProxyClientConfigEntry<Boolean> minecraftRemoteResolve =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "minecraftRemoteResolve", false, 1);
    private static final SocksProxyClientConfigEntry<DNSOverHTTPSProvider> minecraftRemoteResolveProvider =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "minecraftRemoteResolveProvider", DNSOverHTTPSProvider.CLOUDFLARE);
    private static final SocksProxyClientConfigEntry<String> customMinecraftRemoteResolveProvider =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "customMinecraftRemoteResolveProvider", DNSOverHTTPSProvider.CLOUDFLARE.url);
    private static final SocksProxyClientConfigEntry<Boolean> proxyYggdrasil =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "proxyYggdrasil", true, 1);
    private static final SocksProxyClientConfigEntry<Boolean> proxyPlayerSkinDownload =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "proxyPlayerSkinDownload", true, 1);
    private static final SocksProxyClientConfigEntry<Boolean> proxyServerResourceDownload =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "proxyServerResourceDownload", true, 1);
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

    public static boolean shouldProxyYggdrasil() {
        return GeneralConfig.usingProxy() && proxyYggdrasil.getValue();
    }

    public static boolean shouldProxyPlayerSkinDownload() {
        return GeneralConfig.usingProxy() && proxyPlayerSkinDownload.getValue();
    }

    public static boolean shouldProxyServerResourceDownload() {
        return GeneralConfig.usingProxy() && proxyServerResourceDownload.getValue();
    }

    public static ProxyCredential getProxyCredential() {
        return GeneralConfig.getProxyCredential();
    }

    public static SocksVersion getSocksVersion() {
        return GeneralConfig.getSocksVersion();
    }

    public static boolean httpRemoteResolve() {
        return GeneralConfig.usingProxy() && httpRemoteResolve.getValue();
    }

    public static boolean minecraftRemoteResolve() {
        return usingProxyOnMinecraft() && minecraftRemoteResolve.getValue();
    }

    public static String minecraftRemoteResolveProviderUrl() {
        if (!minecraftRemoteResolve()) {
            return null;
        }
        if (minecraftRemoteResolveProvider.getValue().equals(DNSOverHTTPSProvider.CUSTOM)) {
            return customMinecraftRemoteResolveProvider.getValue();
        }
        return minecraftRemoteResolveProvider.getValue().url;
    }

    @Override
    public JsonObject defaultEntries() {
        JsonObject obj = new JsonObject();
        obj.addProperty(proxyMinecraft.getEntry(), proxyMinecraft.getDefaultValue());
        obj.addProperty(minecraftRemoteResolve.getEntry(), minecraftRemoteResolve.getDefaultValue());
        obj.addProperty(minecraftRemoteResolveProvider.getEntry(), minecraftRemoteResolveProvider.getDefaultValue().name());
        obj.addProperty(customMinecraftRemoteResolveProvider.getEntry(), customMinecraftRemoteResolveProvider.getDefaultValue());
        obj.addProperty(proxyYggdrasil.getEntry(), proxyYggdrasil.getDefaultValue());
        obj.addProperty(proxyPlayerSkinDownload.getEntry(), proxyPlayerSkinDownload.getDefaultValue());
        obj.addProperty(proxyServerResourceDownload.getEntry(), proxyServerResourceDownload.getDefaultValue());
        obj.addProperty(httpRemoteResolve.getEntry(), httpRemoteResolve.getDefaultValue());
        obj.addProperty(imposeProxyOnMinecraftLoopback.getEntry(), imposeProxyOnMinecraftLoopback.getDefaultValue());
        return obj;
    }

    @Override
    public JsonObject toJsonObject() {
        JsonObject obj = new JsonObject();
        obj.addProperty(proxyMinecraft.getEntry(), proxyMinecraft.getValue());
        obj.addProperty(minecraftRemoteResolve.getEntry(), minecraftRemoteResolve.getValue());
        obj.addProperty(minecraftRemoteResolveProvider.getEntry(), minecraftRemoteResolveProvider.getValue().name());
        obj.addProperty(customMinecraftRemoteResolveProvider.getEntry(), customMinecraftRemoteResolveProvider.getValue());
        obj.addProperty(proxyYggdrasil.getEntry(), proxyYggdrasil.getValue());
        obj.addProperty(proxyPlayerSkinDownload.getEntry(), proxyPlayerSkinDownload.getValue());
        obj.addProperty(proxyServerResourceDownload.getEntry(), proxyServerResourceDownload.getValue());
        obj.addProperty(httpRemoteResolve.getEntry(), httpRemoteResolve.getValue());
        obj.addProperty(imposeProxyOnMinecraftLoopback.getEntry(), imposeProxyOnMinecraftLoopback.getValue());
        return obj;
    }

    @Override
    public void fromJsonObject(JsonObject object) {
        proxyMinecraft.setValue(object.get(proxyMinecraft.getEntry()).getAsBoolean());
        minecraftRemoteResolve.setValue(object.get(minecraftRemoteResolve.getEntry()).getAsBoolean());
        minecraftRemoteResolveProvider.setValue(DNSOverHTTPSProvider.valueOf(object.get(minecraftRemoteResolveProvider.getEntry()).getAsString()));
        customMinecraftRemoteResolveProvider.setValue(object.get(customMinecraftRemoteResolveProvider.getEntry()).getAsString());
        proxyYggdrasil.setValue(object.get(proxyYggdrasil.getEntry()).getAsBoolean());
        proxyPlayerSkinDownload.setValue(object.get(proxyPlayerSkinDownload.getEntry()).getAsBoolean());
        proxyServerResourceDownload.setValue(object.get(proxyServerResourceDownload.getEntry()).getAsBoolean());
        httpRemoteResolve.setValue(object.get(httpRemoteResolve.getEntry()).getAsBoolean());
        imposeProxyOnMinecraftLoopback.setValue(object.get(imposeProxyOnMinecraftLoopback.getEntry()).getAsBoolean());
    }
}
