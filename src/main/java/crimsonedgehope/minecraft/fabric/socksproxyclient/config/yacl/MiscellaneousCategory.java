package crimsonedgehope.minecraft.fabric.socksproxyclient.config.yacl;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.MiscellaneousConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.entry.SocksProxyClientConfigEntry;
import crimsonedgehope.minecraft.fabric.socksproxyclient.i18n.TranslateKeys;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import net.minecraft.text.Text;

final class MiscellaneousCategory extends YACLCategory<MiscellaneousConfig> {

    SocksProxyClientConfigEntry<Boolean> buttonsInMultiplayerScreen;

    MiscellaneousCategory(YACLAccess yacl) {
        super(yacl, MiscellaneousConfig.class);
    }

    @Override
    public ConfigCategory buildConfigCategory() throws Exception {
        ConfigCategory.Builder categoryBuilder = ConfigCategory.createBuilder();

        categoryBuilder.name(Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_MISCELLANEOUS));

        buttonsInMultiplayerScreen = entryField("buttonsInMultiplayerScreen", Boolean.class);
        Option<Boolean> yaclButtonsInMultiplayerScreen = Option.<Boolean>createBuilder()
                .name(buttonsInMultiplayerScreen.getEntryTranslateKey())
                .binding(buttonsInMultiplayerScreen.getDefaultValue(), buttonsInMultiplayerScreen::getValue, buttonsInMultiplayerScreen::setValue)
                .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
                .build();
        categoryBuilder.option(yaclButtonsInMultiplayerScreen);

        return categoryBuilder.build();
    }
}
