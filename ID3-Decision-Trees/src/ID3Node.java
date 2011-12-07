import java.util.List;


public class ID3Node {
	public static class Link {
		public ID3Node parent;
		public ID3Node child;
		
		public String attribute;
		public String value;
		
		public Link( ID3Node parent, ID3Node child, String attribute, String value ) {
			this.parent = parent;
			this.child = child;
			this.attribute = attribute;
			this.value = value;
		}
	}

	public Link parentalLink;
	public List<HypothesisSpace.Example> examples;
	public HypothesisSpace hs;
	
	public ID3Node( HypothesisSpace hs, Link parentalLink, List<HypothesisSpace.Example> examples ) {
		this.hs = hs;
		this.parentalLink = parentalLink;
		this.examples = examples;
	}
}
