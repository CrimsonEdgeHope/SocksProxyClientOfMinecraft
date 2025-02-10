package crimsonedgehope.minecraft.fabric.socksproxyclient.unittest;

import net.minecraft.Bootstrap;
import net.minecraft.SharedConstants;
import net.minecraft.client.ClientBootstrap;

final class Utils {
    private Utils() {}

    static void preBootstrap() {
        SharedConstants.createGameVersion();
    }

    static void bootstrap() {
        Bootstrap.initialize();
        ClientBootstrap.initialize();
    }
}
