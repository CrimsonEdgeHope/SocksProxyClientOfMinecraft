package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.network;

import com.google.common.net.InetAddresses;
import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.injection.access.IMixinAllowedAddressResolver;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.doh.DOHResolver;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.Address;
import net.minecraft.client.network.AddressResolver;
import net.minecraft.client.network.AllowedAddressResolver;
import net.minecraft.client.network.RedirectResolver;
import net.minecraft.client.network.ServerAddress;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.SRVRecord;
import org.xbill.DNS.Type;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Environment(EnvType.CLIENT)
@Mixin(AllowedAddressResolver.class)
public class MixinAllowedAddressResolver implements IMixinAllowedAddressResolver {
    @Unique
    private static final Logger LOGGER = SocksProxyClient.getLogger("Resolve");
    
    @Shadow @Final @Mutable
    private AddressResolver addressResolver;
    @Shadow @Final @Mutable
    private RedirectResolver redirectResolver;

    @Unique
    private DOHResolver resolver;

    @Override
    public DOHResolver socksProxyClient$getDohResolver() {
        resolver.setUrl(ServerConfig.minecraftRemoteResolveProviderUrl());
        return resolver;
    }

    @Override
    public void socksProxyClient$setDohResolver() {
        if (Objects.isNull(this.resolver)) {
            this.resolver = new DOHResolver(ServerConfig.minecraftRemoteResolveProviderUrl());
        }
    }

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/network/AllowedAddressResolver;addressResolver:Lnet/minecraft/client/network/AddressResolver;",
                    opcode = Opcodes.PUTFIELD
            )
    )
    private void redirectGetAddressResolver(AllowedAddressResolver instance, final AddressResolver originalAddressResolver) {
        this.addressResolver = serverAddress -> {
            if (!ServerConfig.minecraftRemoteResolve()) {
                return originalAddressResolver.resolve(serverAddress);
            }
            try {
                if (InetAddresses.isInetAddress(serverAddress.getAddress())) {
                    InetAddress inet = InetAddress.getByName(serverAddress.getAddress());
                    byte[] bytes = inet.getAddress();
                    // TODO: ?
                    return Optional.of(Address.create(new InetSocketAddress(InetAddress.getByAddress(inet.getHostAddress(), bytes), serverAddress.getPort())));
                }
                return addressResolver(serverAddress);
            } catch (Throwable e) {
                LOGGER.debug("Couldn't resolve server {} address", serverAddress.getAddress(), e);
            }
            return Optional.empty();
        };
    }

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/network/AllowedAddressResolver;redirectResolver:Lnet/minecraft/client/network/RedirectResolver;",
                    opcode = Opcodes.PUTFIELD
            )
    )
    private void redirectGetRedirectResolver(AllowedAddressResolver instance, final RedirectResolver originalRedirectResolver) {
        this.redirectResolver = serverAddress -> {
            if (!ServerConfig.minecraftRemoteResolve()) {
                return originalRedirectResolver.lookupRedirect(serverAddress);
            }
            if (serverAddress.getPort() == 25565) {
                try {
                    if (InetAddresses.isInetAddress(serverAddress.getAddress())) {
                        return Optional.of(new ServerAddress(serverAddress.getAddress(), serverAddress.getPort()));
                    }
                    return redirectResolver(serverAddress);
                } catch (Throwable e) {
                    LOGGER.debug("Couldn't resolve server {} address", serverAddress.getAddress(), e);
                }
            }
            return Optional.empty();
        };
    }

    @Unique
    private List<Record> resolve(final String domainName, final int recordType) throws Exception {
        ((IMixinAllowedAddressResolver) this).socksProxyClient$setDohResolver();
        return resolver.resolve(domainName, recordType, ServerConfig.minecraftRemoteResolveDismissSystemHosts());
    }

    @Unique
    private Optional<Address> addressResolver(ServerAddress serverAddress) throws Exception {
        List<Record> records = resolve(serverAddress.getAddress(), Type.A);
        final ARecord arec = (ARecord) records.get(0);
        LOGGER.debug("{}", arec);
        InetAddress inetAddress = arec.getAddress();
        LOGGER.info("Resolve {} to {}", serverAddress.getAddress(), inetAddress.getHostAddress());
        byte[] bytes = inetAddress.getAddress();
        // TODO: ?
        return Optional.of(Address.create(new InetSocketAddress(InetAddress.getByAddress(serverAddress.getAddress(), bytes), serverAddress.getPort())));
    }

    @Unique
    private Optional<ServerAddress> redirectResolver(ServerAddress serverAddress) throws Exception {
        String addr0 = "_minecraft._tcp." + serverAddress.getAddress();
        List<Record> records = resolve(addr0, Type.SRV);
        final SRVRecord srv = (SRVRecord) records.get(0);
        LOGGER.debug("{}", srv);
        String host = srv.getTarget().toString(true);
        LOGGER.info("Resolve {} to {}:{}", addr0, host, srv.getPort());
        return Optional.of(new ServerAddress(host, srv.getPort()));
    }
}
