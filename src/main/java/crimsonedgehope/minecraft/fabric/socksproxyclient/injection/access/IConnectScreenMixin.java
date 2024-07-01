package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.access;

public interface IConnectScreenMixin {
    void socksProxyClient$setHost(String host);
    String socksProxyClient$getHost();
}
