package crimsonedgehope.minecraft.fabric.socksproxyclient.config.cloth;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ConfigUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClothUtils {

    public static void loadAll() {
        ConfigUtils.loadAll();
    }

    public static void saveAll() {
        ConfigUtils.saveAll();
    }
}
