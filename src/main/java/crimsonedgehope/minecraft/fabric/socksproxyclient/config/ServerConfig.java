package crimsonedgehope.minecraft.fabric.socksproxyclient.config;

import com.google.gson.JsonObject;
import crimsonedgehope.minecraft.fabric.socksproxyclient.i18n.TranslateKeys;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.DNSOverHTTPSProvider;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.ProxyCredential;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.SocksVersion;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.net.Proxy;

@Environment(EnvType.CLIENT)
public final class ServerConfig extends SocksProxyClientConfig {

    private static final ServerConfig INSTANCE;

    static {
        INSTANCE = new ServerConfig();
    }

    private static final SocksProxyClientConfigEntry<Boolean> proxyMinecraft =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "proxyMinecraft",
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_SERVER_PROXYMINECRAFT),
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_SERVER_PROXYMINECRAFT_TOOLTIP),
                    true);
    private static final SocksProxyClientConfigEntry<Boolean> minecraftRemoteResolve =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "minecraftRemoteResolve",
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_SERVER_MINECRAFTDOMAINNAMERESOLUTION_USEPROXY),
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_SERVER_MINECRAFTDOMAINNAMERESOLUTION_USEPROXY_TOOLTIP),
                    false);
    private static final SocksProxyClientConfigEntry<Boolean> minecraftRemoteResolveDismissSystemHosts =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "minecraftRemoteResolveDismissSystemHosts",
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_SERVER_MINECRAFTDOMAINNAMERESOLUTION_DISMISSSYSTEMHOSTS),
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_SERVER_MINECRAFTDOMAINNAMERESOLUTION_DISMISSSYSTEMHOSTS_TOOLTIP),
                    true);
    private static final SocksProxyClientConfigEntry<DNSOverHTTPSProvider> minecraftRemoteResolveProvider =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "minecraftRemoteResolveProvider",
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_SERVER_MINECRAFTDOMAINNAMERESOLUTION_DOHPROVIDER),
                    DNSOverHTTPSProvider.CLOUDFLARE);
    private static final SocksProxyClientConfigEntry<String> customMinecraftRemoteResolveProvider =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "customMinecraftRemoteResolveProvider",
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_SERVER_MINECRAFTDOMAINNAMERESOLUTION_DOHPROVIDERURL),
                    DNSOverHTTPSProvider.CLOUDFLARE.url);
    private static final SocksProxyClientConfigEntry<Boolean> proxyYggdrasil =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "proxyYggdrasil",
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_SERVER_SERVICES_PROXYYGGDRASIL),
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_SERVER_SERVICES_PROXYYGGDRASIL_TOOLTIP),
                    true);
    private static final SocksProxyClientConfigEntry<Boolean> proxyPlayerSkinDownload =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "proxyPlayerSkinDownload",
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_SERVER_SERVICES_PROXYPLAYERSKINDOWNLOAD),
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_SERVER_SERVICES_PROXYPLAYERSKINDOWNLOAD_TOOLTIP),
                    true);
    private static final SocksProxyClientConfigEntry<Boolean> proxyServerResourceDownload =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "proxyServerResourceDownload",
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_SERVER_SERVICES_PROXYSERVERRESOURCEDOWNLOAD),
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_SERVER_SERVICES_PROXYSERVERRESOURCEDOWNLOAD_TOOLTIP),
                    true);
    private static final SocksProxyClientConfigEntry<Boolean> proxyBlockListSupplier =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "proxyBlockListSupplier",
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_SERVER_SERVICES_PROXYBLOCKLISTSUPPLIER),
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_SERVER_SERVICES_PROXYBLOCKLISTSUPPLIER_TOOLTIP),
                    true);
    private static final SocksProxyClientConfigEntry<Boolean> httpRemoteResolve =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "httpRemoteResolve",
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_SERVER_SERVICES_HTTPREMOTERESOLVE),
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_SERVER_SERVICES_HTTPREMOTERESOLVE_TOOLTIP),
                    true);
    private static final SocksProxyClientConfigEntry<Boolean> imposeProxyOnMinecraftLoopback =
            new SocksProxyClientConfigEntry<>(INSTANCE.getClass(), "imposeProxyOnMinecraftLoopback",
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_SERVER_ADVANCED_IMPOSEPROXYONMINECRAFTLOOPBACK),
                    Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_SERVER_ADVANCED_IMPOSEPROXYONMINECRAFTLOOPBACK_TOOLTIP),
                    false);

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

    public static boolean shouldProxyBlockListSupplier() {
        return GeneralConfig.usingProxy() && proxyBlockListSupplier.getValue();
    }

    public static ProxyCredential getProxyCredential() {
        return GeneralConfig.getProxyCredential();
    }

    @Nullable
    public static SocksVersion getSocksVersion() {
        return GeneralConfig.getSocksVersion();
    }

    public static boolean httpRemoteResolve() {
        return GeneralConfig.usingProxy() && httpRemoteResolve.getValue();
    }

    public static boolean minecraftRemoteResolve() {
        return usingProxyOnMinecraft() && minecraftRemoteResolve.getValue();
    }

    public static boolean minecraftRemoteResolveDismissSystemHosts() {
        return minecraftRemoteResolveDismissSystemHosts.getValue();
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
        obj.addProperty(proxyMinecraft.getJsonEntry(), proxyMinecraft.getDefaultValue());
        obj.addProperty(minecraftRemoteResolve.getJsonEntry(), minecraftRemoteResolve.getDefaultValue());
        obj.addProperty(minecraftRemoteResolveDismissSystemHosts.getJsonEntry(), minecraftRemoteResolveDismissSystemHosts.getDefaultValue());
        obj.addProperty(minecraftRemoteResolveProvider.getJsonEntry(), minecraftRemoteResolveProvider.getDefaultValue().name());
        obj.addProperty(customMinecraftRemoteResolveProvider.getJsonEntry(), customMinecraftRemoteResolveProvider.getDefaultValue());
        obj.addProperty(proxyYggdrasil.getJsonEntry(), proxyYggdrasil.getDefaultValue());
        obj.addProperty(proxyPlayerSkinDownload.getJsonEntry(), proxyPlayerSkinDownload.getDefaultValue());
        obj.addProperty(proxyServerResourceDownload.getJsonEntry(), proxyServerResourceDownload.getDefaultValue());
        obj.addProperty(proxyBlockListSupplier.getJsonEntry(), proxyBlockListSupplier.getDefaultValue());
        obj.addProperty(httpRemoteResolve.getJsonEntry(), httpRemoteResolve.getDefaultValue());
        obj.addProperty(imposeProxyOnMinecraftLoopback.getJsonEntry(), imposeProxyOnMinecraftLoopback.getDefaultValue());
        return obj;
    }

    @Override
    public JsonObject toJsonObject() {
        JsonObject obj = new JsonObject();
        obj.addProperty(proxyMinecraft.getJsonEntry(), proxyMinecraft.getValue());
        obj.addProperty(minecraftRemoteResolve.getJsonEntry(), minecraftRemoteResolve.getValue());
        obj.addProperty(minecraftRemoteResolveDismissSystemHosts.getJsonEntry(), minecraftRemoteResolveDismissSystemHosts.getValue());
        obj.addProperty(minecraftRemoteResolveProvider.getJsonEntry(), minecraftRemoteResolveProvider.getValue().name());
        obj.addProperty(customMinecraftRemoteResolveProvider.getJsonEntry(), customMinecraftRemoteResolveProvider.getValue());
        obj.addProperty(proxyYggdrasil.getJsonEntry(), proxyYggdrasil.getValue());
        obj.addProperty(proxyPlayerSkinDownload.getJsonEntry(), proxyPlayerSkinDownload.getValue());
        obj.addProperty(proxyServerResourceDownload.getJsonEntry(), proxyServerResourceDownload.getValue());
        obj.addProperty(proxyBlockListSupplier.getJsonEntry(), proxyBlockListSupplier.getValue());
        obj.addProperty(httpRemoteResolve.getJsonEntry(), httpRemoteResolve.getValue());
        obj.addProperty(imposeProxyOnMinecraftLoopback.getJsonEntry(), imposeProxyOnMinecraftLoopback.getValue());
        return obj;
    }

    @Override
    public void fromJsonObject(JsonObject object) {
        proxyMinecraft.setValue(object.get(proxyMinecraft.getJsonEntry()).getAsBoolean());
        minecraftRemoteResolve.setValue(object.get(minecraftRemoteResolve.getJsonEntry()).getAsBoolean());
        minecraftRemoteResolveDismissSystemHosts.setValue(object.get(minecraftRemoteResolveDismissSystemHosts.getJsonEntry()).getAsBoolean());
        minecraftRemoteResolveProvider.setValue(DNSOverHTTPSProvider.valueOf(object.get(minecraftRemoteResolveProvider.getJsonEntry()).getAsString()));
        customMinecraftRemoteResolveProvider.setValue(object.get(customMinecraftRemoteResolveProvider.getJsonEntry()).getAsString());
        proxyYggdrasil.setValue(object.get(proxyYggdrasil.getJsonEntry()).getAsBoolean());
        proxyPlayerSkinDownload.setValue(object.get(proxyPlayerSkinDownload.getJsonEntry()).getAsBoolean());
        proxyServerResourceDownload.setValue(object.get(proxyServerResourceDownload.getJsonEntry()).getAsBoolean());
        proxyBlockListSupplier.setValue(object.get(proxyBlockListSupplier.getJsonEntry()).getAsBoolean());
        httpRemoteResolve.setValue(object.get(httpRemoteResolve.getJsonEntry()).getAsBoolean());
        imposeProxyOnMinecraftLoopback.setValue(object.get(imposeProxyOnMinecraftLoopback.getJsonEntry()).getAsBoolean());
    }
}
