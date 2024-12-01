package crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.doh;

import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.xbill.DNS.DohResolver;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.hosts.HostsFileParser;

import java.net.UnknownHostException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
@Getter
public final class DOHResolver {
    private final DohResolver resolver;
    
    public DOHResolver(String url) {
        this.resolver = new DohResolver(url, 10, Duration.ofSeconds(2L));
        this.resolver.setUsePost(true);
    }
    
    public void setUrl(String url) {
        this.resolver.setUriTemplate(url);
    }
    
    public List<Record> resolve(final String domainName, final int recordType, final boolean dismissSystemHosts) throws Exception {
        Lookup lookup;
        lookup = new Lookup(domainName, recordType);
        lookup.setResolver(this.resolver);
        if (dismissSystemHosts) {
            lookup.setHostsFileParser(null);
        } else {
            lookup.setHostsFileParser(new HostsFileParser());
        }
        lookup.run();
        Record[] records = lookup.getAnswers();
        if (records == null || records.length == 0) {
            throw new UnknownHostException();
        }
        return new ArrayList<>(List.of(records));
    }
}
