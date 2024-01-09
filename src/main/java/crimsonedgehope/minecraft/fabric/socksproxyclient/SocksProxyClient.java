package crimsonedgehope.minecraft.fabric.socksproxyclient;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocksProxyClient implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("socksproxyclient");

	@Override
	public void onInitializeClient() {
		LOGGER.info("SocksProxyClient...");
	}

	public static final class Auth {
		private Auth() {}

		private static String username;
		private static String password;

		public static void setUsername(String s) {
			username = s;
		}
		public static void setPassword(String s) {
			password = s;
		}
		public static String getUsername() {
			return username;
		}
		public static String getPassword() {
			return password;
		}
	}
}
