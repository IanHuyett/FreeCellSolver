import java.util.Comparator;

public class FreeCellSolver {
	
	private static final int HOME_MULT = 15;
	private static final int EMPTY_MULT = 8;
	private static final int REACHABLE_MULT = 2;
	private static final int MIN_DEPTH_MULT = -5;
	private static final int TOTAL_DEPTH_MULT = -5;

	public static void main(String[] args) {
		FreeCellNode node = new FreeCellNode(9997);
		System.out.println(node.getHash());
		for(SearchNode child : node.expand()){
			System.out.println(((FreeCellNode)child).getHash());
		}
		

	}
	
	public static int scoreNode(FreeCellNode node){
		int score = 0;
		
		//score cards in foundation
		int foundationTotal = 0;
		for(int suit = 0; suit < Card.NUM_SUITS; suit++){
			if(node.getFoundations()[suit] != null){
				foundationTotal += node.getFoundations()[suit].getCard().getRank() + 1;
			}
		}
		
		score += foundationTotal * HOME_MULT;
		
		//score empty cascades/cells
		int emptyTotal = 0;
		for(int casc = 0; casc < FreeCellNode.NUM_CASCADES; casc ++){
			if(node.getCascades()[casc] == null){
				emptyTotal ++;
			}
		}
		
		for(int cell = 0; cell < FreeCellNode.NUM_CELLS; cell ++){
			if(node.getCells()[cell] == null){
				emptyTotal ++;
			}
		}
		
		score += emptyTotal * EMPTY_MULT;
		
		int minDepth = 19;
		int totalDepth = 0;
		
		int reachableCards = 0;
		int[] nextRank = new int[4];
		for(int suit = 0; suit < Card.NUM_SUITS; suit++){
			if(node.getFoundations()[suit] == null){
				nextRank[suit] = 0;
			}
			else{
				nextRank[suit] = node.getFoundations()[suit].getCard().getRank() + 1;
			}
		}
		
		for(int cell = 0; cell < FreeCellNode.NUM_CELLS; cell ++){
			if(node.getCells()[cell] != null){
				if(node.getCells()[cell].getCard().getRank() == nextRank[node.getCells()[cell].getCard().getSuit()]){
					minDepth = 0;
					reachableCards++;
				};
			}
		}
		
		for(int casc = 0; casc < FreeCellNode.NUM_CASCADES; casc ++){
			if(node.getCascades()[casc] == null)
				continue;
			
			CardNode cardNode = node.getCascades()[casc];
			int depth = 0;
			while(cardNode.getPrevious() != null){
				if(cardNode.getCard().getRank() == nextRank[cardNode.getCard().getSuit()]){
					if(depth <= emptyTotal){
						reachableCards++;
					}
					if(depth < minDepth)
						minDepth = depth;
					totalDepth += depth;
				}
				depth++;
				cardNode = cardNode.getPrevious();
			}
		}
		
		score += reachableCards * REACHABLE_MULT;
		score += totalDepth * TOTAL_DEPTH_MULT;
		score += totalDepth * MIN_DEPTH_MULT;
		
		
		return score;
	}
	

}
