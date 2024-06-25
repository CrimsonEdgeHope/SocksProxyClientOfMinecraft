package crimsonedgehope.minecraft.fabric.socksproxyclient;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ConfigUtils;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.HttpToSocksServer;
import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SocksProxyClient implements ClientModInitializer {

	public static void preInit() throws Exception {
		ConfigUtils.loadAll();
		HttpToSocksServer.INSTANCE.fire();
	}

	@Override
	public void onInitializeClient() {

	}

	public static Logger logger(final String path) {
		return LogManager.getLogger("SocksProxyClient/" + path);
	}
}
