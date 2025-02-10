package crimsonedgehope.minecraft.fabric.socksproxyclient.unittest;

import net.minecraft.Bootstrap;
import net.minecraft.SharedConstants;

final class Utils {
    private Utils() {}

    static void preBootstrap() {
        SharedConstants.createGameVersion();
    }

    static void bootstrap() {
        Bootstrap.initialize();
    }
}
