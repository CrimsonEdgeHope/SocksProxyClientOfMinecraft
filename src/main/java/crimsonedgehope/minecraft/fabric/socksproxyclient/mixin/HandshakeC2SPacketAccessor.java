package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(HandshakeC2SPacket.class)
public interface HandshakeC2SPacketAccessor {
    @Accessor("address") @Mutable
    void setAddress(String address);
}
