package crimsonedgehope.minecraft.fabric.socksproxyclient.proxy;

import com.mojang.datafixers.util.Pair;
import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.Logger;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.DohResolver;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.SRVRecord;
import org.xbill.DNS.Type;
import org.xbill.DNS.hosts.HostsFileParser;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DNSUtils {

    private static DohResolver RESOLVER;

    private static final Logger LOGGER = SocksProxyClient.LOGGER;

    private static Record[] resolve(final String domainName, final int recordType) throws Exception {
        if (Objects.isNull(RESOLVER)) {
            RESOLVER = new DohResolver(ServerConfig.minecraftRemoteResolveProviderUrl(), 2, Duration.ofSeconds(2L));
            RESOLVER.setUsePost(true);
        } else {
            RESOLVER.setUriTemplate(ServerConfig.minecraftRemoteResolveProviderUrl());
        }
        final Lookup lookup = new Lookup(domainName, recordType);
        lookup.setResolver(RESOLVER);
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

    public static InetAddress resolveA(String host) throws Exception {
        Record[] records = resolve(host, Type.A);
        final ARecord arec = (ARecord) records[0];
        LOGGER.debug(arec.toString());
        InetAddress inetAddress = arec.getAddress();
        LOGGER.info("Resolve {} to {}", host, inetAddress.getHostAddress());
        byte[] bytes = inetAddress.getAddress();
        // TODO: ?
        return InetAddress.getByAddress(host, bytes);
    }

    public static Pair<String, Integer> resolveSRV(String address) throws Exception {
        String addr0 = "_minecraft._tcp." + address;
        Record[] records = resolve(addr0, Type.SRV);
        final SRVRecord srv = (SRVRecord) records[0];
        LOGGER.debug(records[0].toString());
        String host = srv.getTarget().toString(true);
        LOGGER.info("Resolve {} to {}:{}", addr0, host, srv.getPort());
        return Pair.of(host, srv.getPort());
    }
}
