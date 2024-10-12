package crimsonedgehope.minecraft.fabric.socksproxyclient.config.yacl;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.MiscellaneousConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.i18n.TranslateKeys;
import dev.isxander.yacl3.api.ConfigCategory;
import net.minecraft.text.Text;

final class MiscellaneousCategory extends YACLCategory<MiscellaneousConfig> {

    MiscellaneousCategory(YACLAccess yacl) {
        super(yacl, MiscellaneousConfig.class);
    }

    @Override
    public ConfigCategory buildConfigCategory() throws Exception {
        ConfigCategory.Builder categoryBuilder = ConfigCategory.createBuilder();

        categoryBuilder.name(Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_MISCELLANEOUS));

        return categoryBuilder.build();
    }
}
