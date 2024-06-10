package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin;

import com.google.common.net.InetAddresses;
import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.access.IAllowedAddressResolverMixin;
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
import org.xbill.DNS.hosts.HostsFileParser;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

@Environment(value= EnvType.CLIENT)
@Mixin(AllowedAddressResolver.class)
public class AllowedAddressResolverMixin implements IAllowedAddressResolverMixin {
    @Shadow @Final private AddressResolver addressResolver;
    @Shadow @Final private RedirectResolver redirectResolver;

    @Unique
    private DohResolver resolver;

    @Override
    public DohResolver socksProxyClient$getDohResolver() {
        resolver.setUriTemplate(ServerConfig.minecraftRemoteResolveProviderUrl());
        return resolver;
    }

    @Override
    public void socksProxyClient$setDohResolver() {
        if (Objects.isNull(this.resolver)) {
            this.resolver = new DohResolver(ServerConfig.minecraftRemoteResolveProviderUrl(), 2, Duration.ofSeconds(2L));
            this.resolver.setUsePost(true);
        }
    }

    @Redirect(method = "resolve", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/AllowedAddressResolver;addressResolver:Lnet/minecraft/client/network/AddressResolver;"))
    private AddressResolver redirectGetAddressResolver(AllowedAddressResolver instance) {
        if (!ServerConfig.minecraftRemoteResolve()) {
            return addressResolver;
        }
        return serverAddress -> {
            try {
                if (InetAddresses.isInetAddress(serverAddress.getAddress())) {
                    InetAddress inet = InetAddress.getByName(serverAddress.getAddress());
                    byte[] bytes = inet.getAddress();
                    // TODO: ?
                    return Optional.of(Address.create(new InetSocketAddress(InetAddress.getByAddress(inet.getHostAddress(), bytes), serverAddress.getPort())));
                }
                return addressResolver(serverAddress);
            } catch (Throwable e) {
                SocksProxyClient.LOGGER.debug("Couldn't resolve server {} address", serverAddress.getAddress(), e);
            }
            return Optional.empty();
        };
    }

    @Redirect(method = "resolve", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/AllowedAddressResolver;redirectResolver:Lnet/minecraft/client/network/RedirectResolver;"))
    private RedirectResolver redirectGetRedirectResolver(AllowedAddressResolver instance) {
        if (!ServerConfig.minecraftRemoteResolve()) {
            return redirectResolver;
        }
        return serverAddress -> {
            if (serverAddress.getPort() == 25565) {
                try {
                    if (InetAddresses.isInetAddress(serverAddress.getAddress())) {
                        return Optional.of(new ServerAddress(serverAddress.getAddress(), serverAddress.getPort()));
                    }
                    return redirectResolver(serverAddress);
                } catch (Throwable e) {
                    SocksProxyClient.LOGGER.debug("Couldn't resolve server {} address", serverAddress.getAddress(), e);
                }
            }
            return Optional.empty();
        };
    }

    @Unique
    private Record[] resolver(final String domainName, final int recordType) throws Exception {
        ((IAllowedAddressResolverMixin) this).socksProxyClient$setDohResolver();
        Lookup lookup;
        lookup = new Lookup(domainName, recordType);
        lookup.setResolver(((IAllowedAddressResolverMixin) this).socksProxyClient$getDohResolver());
        if (ServerConfig.minecraftRemoteResolveDismissSystemHosts()) {
            lookup.setHostsFileParser(null);
        } else {
            lookup.setHostsFileParser(new HostsFileParser());
        }
        lookup.run();
        Record[] records = lookup.getAnswers();
        if (records == null || records.length <= 0) {
            throw new UnknownHostException();
        }
        return records;
    }

    @Unique
    private Optional<Address> addressResolver(ServerAddress serverAddress) throws Exception {
        Record[] records = resolver(serverAddress.getAddress(), Type.A);
        final ARecord arec = (ARecord) records[0];
        SocksProxyClient.LOGGER.debug(arec.toString());
        InetAddress inetAddress = arec.getAddress();
        SocksProxyClient.LOGGER.info("Resolve {} to {}", serverAddress.getAddress(), inetAddress.getHostAddress());
        byte[] bytes = inetAddress.getAddress();
        // TODO: ?
        return Optional.of(Address.create(new InetSocketAddress(InetAddress.getByAddress(serverAddress.getAddress(), bytes), serverAddress.getPort())));
    }

    @Unique
    private Optional<ServerAddress> redirectResolver(ServerAddress serverAddress) throws Exception {
        String addr0 = "_minecraft._tcp." + serverAddress.getAddress();
        Record[] records = resolver(addr0, Type.SRV);
        final SRVRecord srv = (SRVRecord) records[0];
        SocksProxyClient.LOGGER.debug(records[0].toString());
        String host = srv.getTarget().toString(true);
        SocksProxyClient.LOGGER.info("Resolve {} to {}:{}", addr0, host, srv.getPort());
        return Optional.of(new ServerAddress(host, srv.getPort()));
    }
}
