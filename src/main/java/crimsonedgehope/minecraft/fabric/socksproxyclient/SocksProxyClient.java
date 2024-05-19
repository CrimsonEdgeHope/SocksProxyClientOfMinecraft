package crimsonedgehope.minecraft.fabric.socksproxyclient;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.GeneralProxyConfig;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocksProxyClient implements ClientModInitializer {

	private static final String LOGGER_NAME = "SocksProxyClient";
	private static final Logger LOGGER = LoggerFactory.getLogger(LOGGER_NAME);

	@Override
	public void onInitializeClient() {
		GeneralProxyConfig.INSTANCE.load();

		logger().info("SocksProxyClient on.");
		logger().debug("SocksProxyClient debug logging on.");
	}

	public static Logger logger() {
		return LOGGER;
	}

}
