package crimsonedgehope.minecraft.fabric.socksproxyclient.config.yacl;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.entry.SocksProxyClientConfigEntry;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.yacl.controller.ValidStringController;
import crimsonedgehope.minecraft.fabric.socksproxyclient.i18n.TranslateKeys;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.DOHProvider;
import dev.isxander.yacl.api.ConfigCategory;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.api.OptionGroup;
import dev.isxander.yacl.gui.controllers.BooleanController;
import dev.isxander.yacl.gui.controllers.cycling.EnumController;
import net.minecraft.text.Text;

final class ServerCategory extends YACLCategory<ServerConfig> {

    SocksProxyClientConfigEntry<Boolean> proxyMinecraft;
    SocksProxyClientConfigEntry<Boolean> minecraftDomainNameResolutionUseProxy;
    SocksProxyClientConfigEntry<Boolean> minecraftDomainNameResolutionDismissSystemHosts;
    SocksProxyClientConfigEntry<DOHProvider> minecraftDomainNameResolutionDohProvider;
    SocksProxyClientConfigEntry<String> minecraftDomainNameResolutionDohProviderUrl;

    SocksProxyClientConfigEntry<Boolean> proxyYggdrasil;
    SocksProxyClientConfigEntry<Boolean> proxyPlayerSkinDownload;
    SocksProxyClientConfigEntry<Boolean> proxyServerResourceDownload;
    SocksProxyClientConfigEntry<Boolean> proxyBlockListSupplier;
    SocksProxyClientConfigEntry<Boolean> httpRemoteResolve;

    SocksProxyClientConfigEntry<Boolean> imposeProxyOnMinecraftLoopback;

    ServerCategory(YACLAccess yacl) {
        super(yacl, ServerConfig.class);
    }

    @Override
    public ConfigCategory buildConfigCategory() throws Exception {
        ConfigCategory.Builder categoryBuilder = ConfigCategory.createBuilder();

        categoryBuilder.name(Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_SERVER))
                .tooltip(Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_SERVER_TOOLTIP));

        proxyMinecraft = entryField("proxyMinecraft", Boolean.class);
        Option<Boolean> yaclProxyMinecraft = Option.createBuilder(Boolean.class)
                .name(proxyMinecraft.getEntryTranslateKey())
                .tooltip(proxyMinecraft.getDescriptionTranslateKey())
                .binding(proxyMinecraft.getDefaultValue(), proxyMinecraft::getValue, proxyMinecraft::setValue)
                .controller(opt -> new BooleanController(opt, BooleanController.YES_NO_FORMATTER, true))
                .build();
        categoryBuilder.option(yaclProxyMinecraft);

        OptionGroup.Builder groupMinecraftServerDomainName = OptionGroup.createBuilder();
        groupMinecraftServerDomainName.name(Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_SERVER_MINECRAFTDOMAINNAMERESOLUTION));

        minecraftDomainNameResolutionUseProxy = entryField("minecraftDomainNameResolutionUseProxy", Boolean.class);
        Option<Boolean> yaclMinecraftDomainNameResolutionUseProxy = Option.createBuilder(Boolean.class)
                .name(minecraftDomainNameResolutionUseProxy.getEntryTranslateKey())
                .tooltip(minecraftDomainNameResolutionUseProxy.getDescriptionTranslateKey())
                .binding(minecraftDomainNameResolutionUseProxy.getDefaultValue(), minecraftDomainNameResolutionUseProxy::getValue, minecraftDomainNameResolutionUseProxy::setValue)
                .available(proxyMinecraft.getValue())
                .controller(opt -> new BooleanController(opt, BooleanController.YES_NO_FORMATTER, true))
                .build();

        minecraftDomainNameResolutionDismissSystemHosts = entryField("minecraftDomainNameResolutionDismissSystemHosts", Boolean.class);
        Option<Boolean> yaclMinecraftDomainNameResolutionDismissSystemHosts = Option.createBuilder(Boolean.class)
                .name(minecraftDomainNameResolutionDismissSystemHosts.getEntryTranslateKey())
                .tooltip((minecraftDomainNameResolutionDismissSystemHosts.getDescriptionTranslateKey()))
                .binding(minecraftDomainNameResolutionDismissSystemHosts.getDefaultValue(), minecraftDomainNameResolutionDismissSystemHosts::getValue, minecraftDomainNameResolutionDismissSystemHosts::setValue)
                .available(proxyMinecraft.getValue())
                .controller(opt -> new BooleanController(opt, BooleanController.YES_NO_FORMATTER, true))
                .build();

        minecraftDomainNameResolutionDohProvider = entryField("minecraftDomainNameResolutionDohProvider", DOHProvider.class);
        Option<DOHProvider> yaclMinecraftDomainNameResolutionDohProvider = Option.createBuilder(DOHProvider.class)
                .name(minecraftDomainNameResolutionDohProvider.getEntryTranslateKey())
                .binding(minecraftDomainNameResolutionDohProvider.getDefaultValue(), minecraftDomainNameResolutionDohProvider::getValue, minecraftDomainNameResolutionDohProvider::setValue)
                .controller(opt -> new EnumController<>(opt, v -> Text.literal(v.displayName)))
                .available(proxyMinecraft.getValue())
                .build();

        minecraftDomainNameResolutionDohProviderUrl = entryField("minecraftDomainNameResolutionDohProviderUrl", String.class);
        Option<String> yaclMinecraftDomainNameResolutionDohProviderUrl = Option.createBuilder(String.class)
                .name(minecraftDomainNameResolutionDohProviderUrl.getEntryTranslateKey())
                .binding(minecraftDomainNameResolutionDohProviderUrl.getDefaultValue(), minecraftDomainNameResolutionDohProviderUrl::getValue, minecraftDomainNameResolutionDohProviderUrl::setValue)
                .controller(opt -> new ValidStringController(opt, s -> s.startsWith("https://")))
                .available(proxyMinecraft.getValue())
                .build();

        groupMinecraftServerDomainName.option(yaclMinecraftDomainNameResolutionUseProxy);
        groupMinecraftServerDomainName.option(yaclMinecraftDomainNameResolutionDismissSystemHosts);
        groupMinecraftServerDomainName.option(yaclMinecraftDomainNameResolutionDohProvider);
        groupMinecraftServerDomainName.option(yaclMinecraftDomainNameResolutionDohProviderUrl);

        yaclProxyMinecraft.addListener((opt, v) -> {
            yaclMinecraftDomainNameResolutionUseProxy.setAvailable(v);
            yaclMinecraftDomainNameResolutionDismissSystemHosts.setAvailable(v);
            yaclMinecraftDomainNameResolutionDohProvider.setAvailable(v);
            yaclMinecraftDomainNameResolutionDohProviderUrl.setAvailable(v);
        });

        categoryBuilder.group(groupMinecraftServerDomainName.build());

        OptionGroup.Builder groupServices = OptionGroup.createBuilder();
        groupServices.name(Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_SERVER_SERVICES));

        proxyYggdrasil = entryField("proxyYggdrasil", Boolean.class);
        Option<Boolean> yaclProxyYggdrasil = Option.createBuilder(Boolean.class)
                .name(proxyYggdrasil.getEntryTranslateKey())
                .tooltip((proxyYggdrasil.getDescriptionTranslateKey()))
                .binding(proxyYggdrasil.getDefaultValue(), proxyYggdrasil::getValue, proxyYggdrasil::setValue)
                .controller(opt -> new BooleanController(opt, BooleanController.YES_NO_FORMATTER, true))
                .build();
        proxyPlayerSkinDownload = entryField("proxyPlayerSkinDownload", Boolean.class);
        Option<Boolean> yaclProxyPlayerSkinDownload = Option.createBuilder(Boolean.class)
                .name(proxyPlayerSkinDownload.getEntryTranslateKey())
                .tooltip((proxyPlayerSkinDownload.getDescriptionTranslateKey()))
                .binding(proxyPlayerSkinDownload.getDefaultValue(), proxyPlayerSkinDownload::getValue, proxyPlayerSkinDownload::setValue)
                .controller(opt -> new BooleanController(opt, BooleanController.YES_NO_FORMATTER, true))
                .build();
        proxyServerResourceDownload = entryField("proxyServerResourceDownload", Boolean.class);
        Option<Boolean> yaclProxyServerResourceDownload = Option.createBuilder(Boolean.class)
                .name(proxyServerResourceDownload.getEntryTranslateKey())
                .tooltip((proxyServerResourceDownload.getDescriptionTranslateKey()))
                .binding(proxyServerResourceDownload.getDefaultValue(), proxyServerResourceDownload::getValue, proxyServerResourceDownload::setValue)
                .controller(opt -> new BooleanController(opt, BooleanController.YES_NO_FORMATTER, true))
                .build();
        proxyBlockListSupplier = entryField("proxyBlockListSupplier", Boolean.class);
        Option<Boolean> yaclProxyBlockListSupplier = Option.createBuilder(Boolean.class)
                .name(proxyBlockListSupplier.getEntryTranslateKey())
                .tooltip((proxyBlockListSupplier.getDescriptionTranslateKey()))
                .binding(proxyBlockListSupplier.getDefaultValue(), proxyBlockListSupplier::getValue, proxyBlockListSupplier::setValue)
                .controller(opt -> new BooleanController(opt, BooleanController.YES_NO_FORMATTER, true))
                .build();
        httpRemoteResolve = entryField("httpRemoteResolve", Boolean.class);
        Option<Boolean> yaclHttpRemoteResolve = Option.createBuilder(Boolean.class)
                .name(httpRemoteResolve.getEntryTranslateKey())
                .tooltip((httpRemoteResolve.getDescriptionTranslateKey()))
                .binding(httpRemoteResolve.getDefaultValue(), httpRemoteResolve::getValue, httpRemoteResolve::setValue)
                .controller(opt -> new BooleanController(opt, BooleanController.YES_NO_FORMATTER, true))
                .build();

        groupServices.option(yaclProxyYggdrasil)
                .option(yaclProxyPlayerSkinDownload)
                .option(yaclProxyServerResourceDownload)
                .option(yaclProxyBlockListSupplier)
                .option(yaclHttpRemoteResolve);

        categoryBuilder.group(groupServices.build());

        OptionGroup.Builder groupAdvanced = OptionGroup.createBuilder().collapsed(true);
        groupAdvanced.name(Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_SERVER_ADVANCED));

        imposeProxyOnMinecraftLoopback = entryField("imposeProxyOnMinecraftLoopback", Boolean.class);
        Option<Boolean> yaclImposeProxyOnMinecraftLoopback = Option.createBuilder(Boolean.class)
                .name(imposeProxyOnMinecraftLoopback.getEntryTranslateKey())
                .tooltip((imposeProxyOnMinecraftLoopback.getDescriptionTranslateKey()))
                .binding(imposeProxyOnMinecraftLoopback.getDefaultValue(), imposeProxyOnMinecraftLoopback::getValue, imposeProxyOnMinecraftLoopback::setValue)
                .controller(opt -> new BooleanController(opt, BooleanController.YES_NO_FORMATTER, true))
                .build();
        groupAdvanced.option(yaclImposeProxyOnMinecraftLoopback);

        categoryBuilder.group(groupAdvanced.build());

        return categoryBuilder.build();
    }
}
