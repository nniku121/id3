import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HypothesisSpace {
	private static final Logger logger = LoggerFactory.getLogger(HypothesisSpace.class);

	public List<String> attributes;
	public Map<String, List<String>> attributeValues;
	public List<String> categories;
	public List<Example> examples;
	
	public class Example {
		public Map<String, String> attributes;
		public String category;
	}
	
	public float getGain( List<Example> examples, String attribute ) {
		float gain = getEntropy(examples);
		
		List<String> values = attributeValues.get(attribute);
		for( String value : values ) {
			List<Example> matching = getAllMatching(examples, attribute, value);
			if( matching.size() > 0 ) {
				gain -= ((float)matching.size()/(float)examples.size())*getEntropy(matching);
			}
		}
		
		return gain;
	}

	public float getEntropy( List<Example> examples ) {
		float entropy = 0;
		float log2=(float) Math.log(2);
		
		for( String category : categories ) {
			float p = getProportion(examples, category);
			if ( p != 0 ) {
				entropy -= p*Math.log(p)/log2;
			}
		}
		
		return entropy;
	}
	
	public List<Example> getAllMatching( List<Example> examples, String attribute, String value ) {
		List<Example> matching = new ArrayList<Example>();
		for( Example example : examples ) {
			if( example.attributes.get(attribute).equals( value ) ) {
				matching.add( example );
			}
		}
		return matching;
	}
	
	public float getProportion( List<Example> examples, String category ) {
		int k = 0;
		for( Example example : examples ) {
			if( example.category.equals( category ) ) {
				k++;
			}
		}
		return (float)k / examples.size();
	}
	
	public String mostCommonCategory( List<Example> examples ) {
		Map<String, Integer> occurences = new HashMap< String, Integer>();
		for( Example ex : examples ) {
			if( occurences.containsKey(ex.category) ) {
				occurences.put( ex.category, occurences.get(ex.category) + 1 );
			} else {
				occurences.put( ex.category, 1 );
			}
		}
		List<String> cats = new ArrayList<String>();
		cats.addAll( categories );

		Integer max = null;
		String maxCat = null;
		
		for( Map.Entry<String,Integer> entry : occurences.entrySet() ) {
			if( ( max == null ) ||
				( entry.getValue() >= max ) ) {
				max = entry.getValue();
				maxCat = entry.getKey();
			}
		}
		
		return maxCat;

	}
	
	public void loadFromFile( String filename ) {
		Properties props = new Properties();
		try {
			props.load(Main.loader.getResourceAsStream( filename ));
		} catch (Exception e) {
			logger.error("Couldn't find file '" + filename + "'. Check your classpath!");
			System.exit(0);
	
		}
	
		String attributesString = props.getProperty("attributes");
		attributes = Arrays.asList( attributesString.split("\\s*,\\s*") );
		attributeValues = new HashMap<String, List<String>>();
		for( String attrib : attributes ) {
			String valuesString = props.getProperty( attrib );
			attributeValues.put( attrib, Arrays.asList( valuesString.split("\\s*,\\s*") ));
		}
		
		String categoriesString = props.getProperty("categories");
		categories = Arrays.asList( categoriesString.split("\\s*,\\s*") );
		
		String examplesString = props.getProperty("examples");
		String[] examplesArray = (">" + examplesString + "<").split("[\\s\r\n]*>[\\s\r\n]*<[\\s\r\n]*");
		List<String> examplesStrings = Arrays.asList( Arrays.copyOfRange(examplesArray, 1, examplesArray.length));
		
		examples = new ArrayList<Example>();
		for( String ex : examplesStrings ) {
			Example example = new Example();
			example.attributes = new  HashMap<String, String>();
			String[] values = ex.split("\\s*,\\s*");
			for( int k = 0; k < attributes.size(); k++ ) {
				example.attributes.put( attributes.get(k), values[k]);
			}
			example.category = values[ attributes.size() ];
			examples.add(example);
		}
	}
}
