import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ID3InnerNode extends ID3Node {

	public String attribute;
	public List< ID3Node.Link > childs = new ArrayList<Link>();
	public float gain;
	
	
	public ID3InnerNode( HypothesisSpace hs, ID3Node.Link parentalLink, List<HypothesisSpace.Example> examples, String attribute ) {
		super( hs, parentalLink, examples );
		this.attribute = attribute;
	}
	
	public String toString1() {
		StringBuilder sb = new StringBuilder();
		sb.append( attribute );
		sb.append( "\n" );
		for( ID3Node.Link link : childs ) {
			sb.append(" ->");
			sb.append(link.value);
			sb.append( "\n" );
			String childString = link.child.toString();
			String[] lines = childString.split( "\\n" );
			for( String line : lines ) {
				sb.append( " \t" );
				sb.append( line );
				sb.append( "\n" );
			}
		}
		return sb.toString();
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
		sb.append( String.format( "<%s gain=\"%s\" classes=\"%s\" entropy=\"%s\" decide_on=\"%s\"", tagname, gain, classes.toString(), entropy, attribute ) );
		if( parentalLink != null ) {
			sb.append( String.format(" %s=\"%s\"", parentalLink.attribute, parentalLink.value ) );
		}
		sb.append( ">\n" );
		
		for( ID3Node.Link link : childs ) {
			String childString = link.child.toString();
			String[] lines = childString.split( "\\n" );
			for( String line : lines ) {
				sb.append( "\t" );
				sb.append( line );
				sb.append( "\n" );
			}
		}
		
		sb.append( String.format( "</%s>", tagname ) );
		
		return sb.toString();
	}
	
	public String toString() {
		return toStringXML();
	}
}
