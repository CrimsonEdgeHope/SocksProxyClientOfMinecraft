package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.network;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ServerAddress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Environment(EnvType.CLIENT)
@Mixin(ServerAddress.class)
public interface ServerAddressAccessor {
    @Invoker("resolveServer")
    static Pair<String, Integer> invokeResolveServer(String address) throws Exception {
        throw new NoSuchMethodException();
    }
}
