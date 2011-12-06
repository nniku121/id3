import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ID3LeafNode extends ID3Node {
	public String category;
	
	public ID3LeafNode( HypothesisSpace hs, ID3Node.Link parentalLink, List<HypothesisSpace.Example> examples, String category ) {
		super( hs, parentalLink, examples );
		this.category = category;
	}
	
	public String toString1() {
		return "[" + category + "]";
	}
	public String toStringXML() {
		StringBuilder sb = new StringBuilder();
		String tagname = parentalLink == null ? "tree" : "node";
		
		StringBuilder classes = new StringBuilder();
		
		Map<String,Integer> categoryOccurences = new HashMap<String, Integer>();
		
		for( String cat : hs.categories ) {
			categoryOccurences.put( cat, 0 );
		}
		for( HypothesisSpace.Example ex : examples ) {
			categoryOccurences.put( ex.category, categoryOccurences.get( ex.category ) + 1);
		}
		
		for( Map.Entry<String, Integer> entry : categoryOccurences.entrySet() ) {
			if( entry.getValue() > 0 ) {
				classes.append( String.format("%s:%s,", entry.getKey(), entry.getValue().toString()) );
			}
		}
		if( classes.length() > 0 ) {
			classes.setLength( classes.length()-1 );
		}
		
		
		
		String entropy = Float.valueOf(hs.getEntropy(examples)).toString();
		sb.append( String.format( "<%s classes=\"%s\" entropy=\"%s\"", tagname, classes.toString(), entropy ) );
		if( parentalLink != null ) {
			sb.append( String.format(" %s=\"%s\"", parentalLink.attribute, parentalLink.value ) );
		}
		sb.append( String.format( ">%s</%s>", category, tagname ) );
				
		return sb.toString();
	}
	public String toString() {
		return toStringXML();
	}
}
