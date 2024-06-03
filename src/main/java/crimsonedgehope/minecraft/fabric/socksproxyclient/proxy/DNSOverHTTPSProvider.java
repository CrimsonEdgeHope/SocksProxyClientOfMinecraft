package crimsonedgehope.minecraft.fabric.socksproxyclient.proxy;

public enum DNSOverHTTPSProvider {
    CLOUDFLARE("Cloudflare", "https://cloudflare-dns.com/dns-query"),
    GOOGLE("Google", "https://dns.google/dns-query"),
    DNS_SB("dns.sb", "https://doh.sb/dns-query"),
    QUAD9("Quad9", "https://dns.quad9.net/dns-query"),
    CISCO_UMBRELLA("Cisco OpenDNS/Cisco Umbrella", "https://doh.opendns.com/dns-query"),
    YANDEX("Yandex DNS", "https://common.dot.dns.yandex.net/dns-query"),
    ADGUARD("AdGuard DNS", "https://dns.adguard-dns.com/dns-query"),
    CUSTOM("Custom", null);

    public final String displayName;
    public final String url;

    DNSOverHTTPSProvider(String displayName, String url) {
        this.displayName = displayName;
        this.url = url;
    }

    public static DNSOverHTTPSProvider byDisplayName(String v) {
        for (DNSOverHTTPSProvider provider : DNSOverHTTPSProvider.values()) {
            if (provider.displayName.equals(v)) {
                return provider;
            }
        }
        return null;
    }
}
