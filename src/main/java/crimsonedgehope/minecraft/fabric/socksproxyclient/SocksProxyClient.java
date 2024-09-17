package crimsonedgehope.minecraft.fabric.socksproxyclient;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ConfigUtils;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.HttpToSocksServer;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocksProxyClient implements ClientModInitializer {

	public static void preInit() throws Exception {
		ConfigUtils.loadAllConfig();
		HttpToSocksServer.INSTANCE.fire();
	}

	@Override
	public void onInitializeClient() {

	}

	public static Logger logger(final String path) {
		return LoggerFactory.getLogger("SocksProxyClient/" + path);
	}
}
