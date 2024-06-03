package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin;

import com.google.common.net.InetAddresses;
import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.Address;
import net.minecraft.client.network.AddressResolver;
import net.minecraft.client.network.AllowedAddressResolver;
import net.minecraft.client.network.RedirectResolver;
import net.minecraft.client.network.ServerAddress;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.DohResolver;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.SRVRecord;
import org.xbill.DNS.Type;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Optional;

@Environment(value= EnvType.CLIENT)
@Mixin(AllowedAddressResolver.class)
public class AllowedAddressResolverMixin {
    @Shadow @Final private AddressResolver addressResolver;
    @Shadow @Final private RedirectResolver redirectResolver;

    private static DohResolver RESOLVER;

    @Redirect(method = "resolve", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/AllowedAddressResolver;addressResolver:Lnet/minecraft/client/network/AddressResolver;"))
    private AddressResolver redirectGetAddressResolver(AllowedAddressResolver instance) {
        if (!ServerConfig.minecraftRemoteResolve()) {
            return addressResolver;
        }
        return address -> {
            try {
                if (InetAddresses.isInetAddress(address.getAddress())) {
                    return Optional.of(Address.create(new InetSocketAddress(address.getAddress(), address.getPort())));
                }
                return addressResolver(address);
            } catch (Throwable e) {
                SocksProxyClient.LOGGER.debug("Couldn't resolve server {} address", address.getAddress(), e);
            }
            return Optional.empty();
        };
    }

    @Redirect(method = "resolve", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/AllowedAddressResolver;redirectResolver:Lnet/minecraft/client/network/RedirectResolver;"))
    private RedirectResolver redirectGetRedirectResolver(AllowedAddressResolver instance) {
        if (!ServerConfig.minecraftRemoteResolve()) {
            return redirectResolver;
        }
        return address -> {
            if (address.getPort() == 25565) {
                try {
                    if (InetAddresses.isInetAddress(address.getAddress())) {
                        return Optional.of(new ServerAddress(address.getAddress(), address.getPort()));
                    }
                    return redirectResolver(address);
                } catch (Throwable e) {
                    SocksProxyClient.LOGGER.debug("Couldn't resolve server {} address", address.getAddress(), e);
                }
            }
            return Optional.empty();
        };
    }

    @Unique
    private static Record[] resolver(final String address, final int recordType) throws Exception {
        if (RESOLVER == null) {
            RESOLVER = new DohResolver(ServerConfig.minecraftRemoteResolveProviderUrl(), 2, Duration.ofSeconds(2L));
            RESOLVER.setUsePost(true);
        } else {
            RESOLVER.setUriTemplate(ServerConfig.minecraftRemoteResolveProviderUrl());
        }
        Lookup lookup;
        lookup = new Lookup(address, recordType);
        lookup.setResolver(RESOLVER);
        lookup.setHostsFileParser(null);
        lookup.run();
        Record[] records = lookup.getAnswers();
        if (records == null || records.length <= 0) {
            throw new UnknownHostException();
        }
        return records;
    }

    @Unique
    private Optional<Address> addressResolver(ServerAddress address) throws Exception {
        Record[] records = resolver(address.getAddress(), Type.A);
        ARecord arec = (ARecord) records[0];
        SocksProxyClient.LOGGER.debug(arec.toString());
        InetAddress inetAddress = arec.getAddress();
        SocksProxyClient.LOGGER.info("Resolve {} to {}", address.getAddress(), inetAddress.getHostAddress());
        return Optional.of(Address.create(new InetSocketAddress(inetAddress.getHostAddress(), address.getPort())));
    }

    @Unique
    private Optional<ServerAddress> redirectResolver(ServerAddress address) throws Exception {
        String addr0 = "_minecraft._tcp." + address.getAddress();
        Record[] records = resolver(addr0, Type.SRV);
        final SRVRecord srv = (SRVRecord) records[0];
        SocksProxyClient.LOGGER.debug(records[0].toString());
        String host = srv.getTarget().toString(true);
        SocksProxyClient.LOGGER.info("Resolve {} to {}:{}", addr0, host, srv.getPort());
        return Optional.of(new ServerAddress(host, srv.getPort()));
    }
}
