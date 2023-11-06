package mx.uaemex.fi.ico.linc21.databaseconnectivity;

import java.util.Properties;
import static mx.uaemex.fi.ico.linc21.preferences.PreferencesKeys.*;
import static mx.uaemex.fi.ico.linc21.preferences.SharedPreferences.*;

public class ConnectionParameters {
	
	public final static Properties CONNECTION_PROPERTIES = initializeProperties();
	public final static String CONNECTION_URI = "jdbc:postgresql://" + getPref(PG_CONN_HOST) + ":" + getPref(PG_CONN_PORT) +"/";
			
	private static Properties initializeProperties() {
		Properties props = new Properties();
		props.put("user", getPref(PG_CONN_USER));
		props.put("password", getPref(PG_CONN_PASS));
		return props;
	}

}
