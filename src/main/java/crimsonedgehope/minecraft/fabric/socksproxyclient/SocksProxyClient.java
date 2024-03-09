package crimsonedgehope.minecraft.fabric.socksproxyclient;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.SocksProxyClientConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocksProxyClient implements ClientModInitializer {

	private static Logger LOGGER;
	private static String LOGGER_NAME;

	public static final String PROXY_CONFIG_CATEGORY = "proxy";
	public static final String MISCELLANEOUS_CONFIG_CATEGORY = "miscellaneous";

	@Override
	public void onInitializeClient() {
		LOGGER_NAME = this.getClass().getSimpleName();
		initConfig();
		logger().info("SocksProxyClient on.");
		logger().debug("SocksProxyClient debug logging on.");
	}

	private static void initConfig() {
		AutoConfig.register(SocksProxyClientConfig.class, GsonConfigSerializer::new);
	}

	public static Logger logger() {
		if (LOGGER == null) {
			LOGGER = LoggerFactory.getLogger(LOGGER_NAME);
		}
		return LOGGER;
	}

}
