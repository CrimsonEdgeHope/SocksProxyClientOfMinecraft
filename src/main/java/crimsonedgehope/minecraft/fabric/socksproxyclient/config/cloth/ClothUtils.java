package crimsonedgehope.minecraft.fabric.socksproxyclient.config.cloth;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ConfigUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClothUtils {

    public static void loadAll() throws Exception {
        ConfigUtils.loadAll();
    }

    public static void saveAll() throws Exception {
        ConfigUtils.saveAll();
    }
}
