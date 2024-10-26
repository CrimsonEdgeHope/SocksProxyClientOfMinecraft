package crimsonedgehope.minecraft.fabric.socksproxyclient.config.yacl;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.GeneralConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.entry.ProxyEntry;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.entry.SocksProxyClientConfigEntry;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.yacl.controller.ProxyEntryController;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.yacl.screen.ProxyEntryEditScreen;
import crimsonedgehope.minecraft.fabric.socksproxyclient.i18n.TranslateKeys;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.SocksUtils;
import dev.isxander.yacl.api.ButtonOption;
import dev.isxander.yacl.api.ConfigCategory;
import dev.isxander.yacl.api.ListOption;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.api.OptionFlag;
import dev.isxander.yacl.api.OptionGroup;
import dev.isxander.yacl.gui.controllers.ActionController;
import dev.isxander.yacl.gui.controllers.BooleanController;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.List;

final class GeneralCategory extends YACLCategory<GeneralConfig> {

    SocksProxyClientConfigEntry<Boolean> useProxy;
    SocksProxyClientConfigEntry<List> proxies;

    GeneralCategory(YACLAccess yacl) {
        super(yacl, GeneralConfig.class);
    }

    @Override
    public ConfigCategory buildConfigCategory() throws Exception {
        ConfigCategory.Builder categoryBuilder = ConfigCategory.createBuilder();

        categoryBuilder.name(Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_GENERAL))
                .tooltip(Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_GENERAL_TOOLTIP));

        useProxy = entryField("useProxy", Boolean.class);
        Option<Boolean> yaclUseProxy = Option.createBuilder(Boolean.class)
                .name(useProxy.getEntryTranslateKey())
                .binding(useProxy.getDefaultValue(), useProxy::getValue, useProxy::setValue)
                .controller(opt -> new BooleanController(opt, BooleanController.YES_NO_FORMATTER, true))
                .flag(OptionFlag.GAME_RESTART)
                .build();
        categoryBuilder.option(yaclUseProxy);

        OptionGroup.Builder proxyGroupBuilder = OptionGroup.createBuilder()
                .name(Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_GENERAL_PROXY));

        proxies = entryField("proxies", List.class);
        ListOption<ProxyEntry> yaclProxies = ListOption.createBuilder(ProxyEntry.class)
                .name(proxies.getEntryTranslateKey())
                .tooltip(proxies.getDescriptionTranslateKey())
                .initial((ProxyEntry) proxies.getDefaultValue().get(0))
                .binding((List<ProxyEntry>) proxies.getDefaultValue(), proxies::getValue, proxies::setValue)
                .collapsed(false)
                .controller(opt -> new ProxyEntryController((Option<ProxyEntry>) opt, (screen, entry, callback) -> {
                    MinecraftClient.getInstance().setScreen(new ProxyEntryEditScreen(screen, entry, callback));
                }))
                .flag(OptionFlag.GAME_RESTART)
                .available(useProxy.getValue())
                .build();

        categoryBuilder.group(yaclProxies);

        ButtonOption yaclTestReachability = ButtonOption.createBuilder()
                .name(Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_GENERAL_PROXY_TEST))
                .tooltip(Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_GENERAL_PROXY_TEST_TOOLTIP))
                .available(true)
                .action((screen, opt) -> SocksUtils.testReachability())
                .controller(opt -> new ActionController(opt))
                .available(useProxy.getValue())
                .build();

        proxyGroupBuilder.option(yaclTestReachability);

        yaclUseProxy.addListener((opt, v) -> {
            yaclProxies.setAvailable(v);
            yaclTestReachability.setAvailable(v);
        });

        categoryBuilder.group(proxyGroupBuilder.build());

        return categoryBuilder.build();
    }
}
