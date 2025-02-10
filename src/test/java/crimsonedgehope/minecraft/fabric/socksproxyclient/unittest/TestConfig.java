package crimsonedgehope.minecraft.fabric.socksproxyclient.unittest;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ConfigUtils;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.GeneralConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.MiscellaneousConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.entry.SocksProxyClientConfigEntry;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.doh.DOHProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

class TestConfig {

    @BeforeAll
    static void prepare() {
        Utils.preBootstrap();
    }

    GeneralConfig generalConfig;
    ServerConfig serverConfig;
    MiscellaneousConfig miscellaneousConfig;

    private void getObjects() {
        Assertions.assertDoesNotThrow(() -> {
            generalConfig = ConfigUtils.getConfigInstance(GeneralConfig.class);
            serverConfig = ConfigUtils.getConfigInstance(ServerConfig.class);
            miscellaneousConfig = ConfigUtils.getConfigInstance(MiscellaneousConfig.class);
        });

        Assertions.assertNotNull(generalConfig.getConfigFile());
        Assertions.assertNotNull(serverConfig.getConfigFile());
        Assertions.assertNotNull(miscellaneousConfig.getConfigFile());
    }
    
    <T> void setEntry(SocksProxyClientConfigEntry<T> entry, T value) {
        entry.setValue(value);
    }
    
    <T> void compareEntry(SocksProxyClientConfigEntry<T> entry, T expected) {
        Assertions.assertEquals(expected, entry.getValue());
    }

    <T> void compare(Predicate<T> predicate, T expected) {
        Assertions.assertTrue(predicate.test(expected));
    }

    @Test
    @DisplayName("Test config saving")
    void testConfigSave() {
        Assertions.assertDoesNotThrow(ConfigUtils::loadAllConfig);
        getObjects();

        generalConfig.getConfigFile().delete();
        serverConfig.getConfigFile().delete();
        miscellaneousConfig.getConfigFile().delete();

        Assertions.assertDoesNotThrow(ConfigUtils::saveAllConfig);
        Assertions.assertDoesNotThrow(ConfigUtils::loadAllConfig);
        getObjects();

        Assertions.assertDoesNotThrow(() -> {
            SocksProxyClientConfigEntry<Boolean> useProxy = generalConfig.getEntryField("useProxy", Boolean.class);
            setEntry(useProxy, !useProxy.getDefaultValue());
            
            SocksProxyClientConfigEntry<Boolean> buttonsInMultiplayerScreen = miscellaneousConfig.getEntryField("buttonsInMultiplayerScreen", Boolean.class);
            SocksProxyClientConfigEntry<Boolean> checkUpdates = miscellaneousConfig.getEntryField("checkUpdates", Boolean.class);
            setEntry(buttonsInMultiplayerScreen, !buttonsInMultiplayerScreen.getDefaultValue());
            setEntry(checkUpdates, !checkUpdates.getDefaultValue());

            SocksProxyClientConfigEntry<Boolean> proxyMinecraft = serverConfig.getEntryField("proxyMinecraft", Boolean.class);
            SocksProxyClientConfigEntry<Boolean> minecraftDomainNameResolutionUseProxy = serverConfig.getEntryField("minecraftDomainNameResolutionUseProxy", Boolean.class);
            SocksProxyClientConfigEntry<Boolean> minecraftDomainNameResolutionDismissSystemHosts = serverConfig.getEntryField("minecraftDomainNameResolutionDismissSystemHosts", Boolean.class);
            SocksProxyClientConfigEntry<String> minecraftDomainNameResolutionDohProviderUrl = serverConfig.getEntryField("minecraftDomainNameResolutionDohProviderUrl", String.class);
            SocksProxyClientConfigEntry<Boolean> proxyYggdrasil = serverConfig.getEntryField("proxyYggdrasil", Boolean.class);
            SocksProxyClientConfigEntry<Boolean> proxyPlayerSkinDownload = serverConfig.getEntryField("proxyPlayerSkinDownload", Boolean.class);
            SocksProxyClientConfigEntry<Boolean> proxyServerResourceDownload = serverConfig.getEntryField("proxyServerResourceDownload", Boolean.class);
            SocksProxyClientConfigEntry<Boolean> proxyBlockListSupplier = serverConfig.getEntryField("proxyBlockListSupplier", Boolean.class);
            SocksProxyClientConfigEntry<Boolean> httpRemoteResolve = serverConfig.getEntryField("httpRemoteResolve", Boolean.class);
            SocksProxyClientConfigEntry<Boolean> imposeProxyOnMinecraftLoopback = serverConfig.getEntryField("imposeProxyOnMinecraftLoopback", Boolean.class);
            setEntry(proxyMinecraft, !proxyMinecraft.getDefaultValue());
            setEntry(minecraftDomainNameResolutionUseProxy, !minecraftDomainNameResolutionUseProxy.getDefaultValue());
            setEntry(minecraftDomainNameResolutionDismissSystemHosts, !minecraftDomainNameResolutionDismissSystemHosts.getDefaultValue());
            setEntry(minecraftDomainNameResolutionDohProviderUrl, "https://example.org/dns-query");
            setEntry(proxyYggdrasil, !proxyYggdrasil.getDefaultValue());
            setEntry(proxyPlayerSkinDownload, !proxyPlayerSkinDownload.getDefaultValue());
            setEntry(proxyServerResourceDownload, !proxyServerResourceDownload.getDefaultValue());
            setEntry(proxyBlockListSupplier, !proxyBlockListSupplier.getDefaultValue());
            setEntry(httpRemoteResolve, !httpRemoteResolve.getDefaultValue());
            setEntry(imposeProxyOnMinecraftLoopback, !imposeProxyOnMinecraftLoopback.getDefaultValue());

            SocksProxyClientConfigEntry<DOHProvider> minecraftDomainNameResolutionDohProvider = serverConfig.getEntryField("minecraftDomainNameResolutionDohProvider", DOHProvider.class);
            setEntry(minecraftDomainNameResolutionDohProvider, DOHProvider.CUSTOM);
            SocksProxyClientConfigEntry<List> proxies = generalConfig.getEntryField("proxies", List.class);
            setEntry(proxies, new ArrayList<String>());
        });

        Assertions.assertDoesNotThrow(ConfigUtils::saveAllConfig);
        Assertions.assertDoesNotThrow(ConfigUtils::loadAllConfig);
        getObjects();
        
        Assertions.assertDoesNotThrow(() -> {
            SocksProxyClientConfigEntry<Boolean> useProxy = generalConfig.getEntryField("useProxy", Boolean.class);
            compareEntry(useProxy, !useProxy.getDefaultValue());

            SocksProxyClientConfigEntry<Boolean> buttonsInMultiplayerScreen = miscellaneousConfig.getEntryField("buttonsInMultiplayerScreen", Boolean.class);
            SocksProxyClientConfigEntry<Boolean> checkUpdates = miscellaneousConfig.getEntryField("checkUpdates", Boolean.class);
            compareEntry(buttonsInMultiplayerScreen, !buttonsInMultiplayerScreen.getDefaultValue());
            compareEntry(checkUpdates, !checkUpdates.getDefaultValue());

            SocksProxyClientConfigEntry<Boolean> proxyMinecraft = serverConfig.getEntryField("proxyMinecraft", Boolean.class);
            SocksProxyClientConfigEntry<Boolean> minecraftDomainNameResolutionUseProxy = serverConfig.getEntryField("minecraftDomainNameResolutionUseProxy", Boolean.class);
            SocksProxyClientConfigEntry<Boolean> minecraftDomainNameResolutionDismissSystemHosts = serverConfig.getEntryField("minecraftDomainNameResolutionDismissSystemHosts", Boolean.class);
            SocksProxyClientConfigEntry<String> minecraftDomainNameResolutionDohProviderUrl = serverConfig.getEntryField("minecraftDomainNameResolutionDohProviderUrl", String.class);
            SocksProxyClientConfigEntry<Boolean> proxyYggdrasil = serverConfig.getEntryField("proxyYggdrasil", Boolean.class);
            SocksProxyClientConfigEntry<Boolean> proxyPlayerSkinDownload = serverConfig.getEntryField("proxyPlayerSkinDownload", Boolean.class);
            SocksProxyClientConfigEntry<Boolean> proxyServerResourceDownload = serverConfig.getEntryField("proxyServerResourceDownload", Boolean.class);
            SocksProxyClientConfigEntry<Boolean> proxyBlockListSupplier = serverConfig.getEntryField("proxyBlockListSupplier", Boolean.class);
            SocksProxyClientConfigEntry<Boolean> httpRemoteResolve = serverConfig.getEntryField("httpRemoteResolve", Boolean.class);
            SocksProxyClientConfigEntry<Boolean> imposeProxyOnMinecraftLoopback = serverConfig.getEntryField("imposeProxyOnMinecraftLoopback", Boolean.class);
            compareEntry(proxyMinecraft, !proxyMinecraft.getDefaultValue());
            compareEntry(minecraftDomainNameResolutionUseProxy, !minecraftDomainNameResolutionUseProxy.getDefaultValue());
            compareEntry(minecraftDomainNameResolutionDismissSystemHosts, !minecraftDomainNameResolutionDismissSystemHosts.getDefaultValue());
            compareEntry(minecraftDomainNameResolutionDohProviderUrl, "https://example.org/dns-query");
            compareEntry(proxyYggdrasil, !proxyYggdrasil.getDefaultValue());
            compareEntry(proxyPlayerSkinDownload, !proxyPlayerSkinDownload.getDefaultValue());
            compareEntry(proxyServerResourceDownload, !proxyServerResourceDownload.getDefaultValue());
            compareEntry(proxyBlockListSupplier, !proxyBlockListSupplier.getDefaultValue());
            compareEntry(httpRemoteResolve, !httpRemoteResolve.getDefaultValue());
            compareEntry(imposeProxyOnMinecraftLoopback, !imposeProxyOnMinecraftLoopback.getDefaultValue());

            SocksProxyClientConfigEntry<DOHProvider> minecraftDomainNameResolutionDohProvider = serverConfig.getEntryField("minecraftDomainNameResolutionDohProvider", DOHProvider.class);
            compareEntry(minecraftDomainNameResolutionDohProvider, DOHProvider.CUSTOM);

            SocksProxyClientConfigEntry<List> proxies = generalConfig.getEntryField("proxies", List.class);
            compare(List::isEmpty, proxies.getValue());
        });
    }
}
