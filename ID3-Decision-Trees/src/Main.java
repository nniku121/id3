import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
	public static ClassLoader loader =  Main.class.getClassLoader();
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Properties logging = new Properties();
		try
		{
			logging.load(loader.getResourceAsStream("log4j.properties"));
			PropertyConfigurator.configure(logging);
		} catch (Exception e) {
			System.out.println("Could not find file 'log4j.properties'. Check your classpath!");

			System.exit(0);
		}

		
		HypothesisSpace hs = new HypothesisSpace();
//		hs.loadFromFile( "bool.properties" ); 
		hs.loadFromFile( "car.properties" ); 
//		hs.loadFromFile( "playtennis.properties" ); 
//		hs.loadFromFile( "enjoysports.properties" ); 
		
		for( String attrib : hs.attributes ) {
			System.out.println("Gain(S, " + attrib + ") = " + hs.getGain(hs.examples, attrib));
		}
		
		ID3Node id3 = ID3.getNode(hs, hs.examples, hs.attributes, null);
		System.out.println(id3);
	}

}
