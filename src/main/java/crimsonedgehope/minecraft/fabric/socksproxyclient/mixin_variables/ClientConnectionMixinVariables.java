package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin_variables;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.net.InetSocketAddress;

@Environment(EnvType.CLIENT)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClientConnectionMixinVariables {
    @Getter @Setter
    private static InetSocketAddress remote;
}
