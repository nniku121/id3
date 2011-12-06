import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class ID3 {
	public static ID3Node getNode( HypothesisSpace hs, List<HypothesisSpace.Example> examples, List<String> attributes, ID3Node.Link parentalLink ) {
		String firstCat = null;
		boolean allTheSameCat = true;
		for( HypothesisSpace.Example ex : examples ) {
			if( firstCat == null ) {
				firstCat = ex.category;
			} else if( !ex.category.equals( firstCat ) ) {
				allTheSameCat = false;
				break;
			}
		}
		if( allTheSameCat ) {
			return new ID3LeafNode( hs, parentalLink, examples, firstCat );
		}
		
		if( attributes.isEmpty() ) {
			return new ID3LeafNode( hs, parentalLink, examples, hs.mostCommonCategory(examples) );
		}
		
		
		ID3InnerNode node = maxGainAttrNode( hs, examples, attributes, parentalLink );
		
		
		List<String> attributesSubset = new ArrayList<String>();
		attributesSubset.addAll( attributes );
		attributesSubset.remove( node.attribute );
		
		List<String> values = hs.attributeValues.get(node.attribute);
		for( String value : values ) {
			ID3Node.Link link = new ID3Node.Link( node, null, node.attribute, value);
			List<HypothesisSpace.Example> examplesSubset = hs.getAllMatching(examples, node.attribute, value);

			if( examplesSubset.isEmpty() ) {
				link.child = new ID3LeafNode( hs, link, examplesSubset, hs.mostCommonCategory(examples) );
			} else {
				 
				link.child = ID3.getNode( hs, examplesSubset, attributesSubset, link );
			}

			node.childs.add( link );
		}
		
		return node;
	}
	
	public static ID3InnerNode maxGainAttrNode( HypothesisSpace hs, List<HypothesisSpace.Example> examples, List<String> attributes, ID3Node.Link parentalLink ) {
		Map<String, Float> gains = new HashMap< String, Float>();
		for( String attr : attributes ) {
			gains.put( attr, hs.getGain(examples, attr) );
		}
		
		Float maxGain = null;
		String maxGainAttr = null;
		
		for( Map.Entry<String,Float> entry : gains.entrySet() ) {
			if( ( maxGain == null ) ||
					( entry.getValue() > maxGain ) ) {
				maxGain = entry.getValue();
				maxGainAttr = entry.getKey();
			}
		}
		ID3InnerNode node = new ID3InnerNode( hs, parentalLink, examples, maxGainAttr );
		node.gain = maxGain;
		node.examples = examples;
		
		return node;
	}
	public static ID3InnerNode minGainAttrNode( HypothesisSpace hs, List<HypothesisSpace.Example> examples, List<String> attributes, ID3Node.Link parentalLink ) {
		Map<String, Float> gains = new HashMap< String, Float>();
		for( String attr : attributes ) {
			gains.put( attr, hs.getGain(examples, attr) );
		}

		Float minGain = null;
		String minGainAttr = null;
		
		for( Map.Entry<String,Float> entry : gains.entrySet() ) {
			if( ( minGain == null ) ||
				( entry.getValue() < minGain ) ) {
				minGain = entry.getValue();
				minGainAttr = entry.getKey();
			}
		}
		ID3InnerNode node = new ID3InnerNode( hs, parentalLink, examples, minGainAttr );
		node.gain = minGain;
		node.examples = examples;
		
		return node;
	}
}
