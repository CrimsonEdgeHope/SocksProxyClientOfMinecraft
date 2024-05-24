package crimsonedgehope.minecraft.fabric.socksproxyclient;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.GeneralProxyConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.HttpToSocksServer;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocksProxyClient implements ClientModInitializer {

	private static final String LOGGER_NAME = "SocksProxyClient";
	public static final Logger LOGGER = LoggerFactory.getLogger(LOGGER_NAME);

	public static void preInit() {
		GeneralProxyConfig.INSTANCE.load();
		HttpToSocksServer.INSTANCE.fire();
	}

	@Override
	public void onInitializeClient() {

	}
}
