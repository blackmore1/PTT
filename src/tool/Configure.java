package tool;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class Configure {
	public static Logger log = Logger.getLogger(Configure.class);
	static{
		PropertyConfigurator.configure("./log4j.properties");
	}
}
