package crimsonedgehope.minecraft.fabric.socksproxyclient;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ConfigUtils;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.HttpToSocksServer;
import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SocksProxyClient implements ClientModInitializer {

	private static final String LOGGER_NAME = "SocksProxyClient";
	public static final Logger LOGGER = LogManager.getLogger(LOGGER_NAME);

	public static void preInit() throws Exception {
		ConfigUtils.loadAll();
		HttpToSocksServer.INSTANCE.fire();
	}

	@Override
	public void onInitializeClient() {

	}
}
