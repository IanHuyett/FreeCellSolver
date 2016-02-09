
public class FreeCellSolver {
	
	private static final int HOME_MULT = 10;
	private static final int EMPTY_MULT = 2;
	private static final int REACHABLE_MULT = 5;

	public static void main(String[] args) {
		FreeCellNode node = new FreeCellNode(9998);
		System.out.println(node.toString());
		System.out.println(scoreNode(node));
		

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
		
		//score += emptyTotal * EMPTY_MULT;
		
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
				if(node.getCells()[cell].getCard().getRank() == nextRank[node.getCells()[cell].getCard().getSuit()]);
			}
		}
		
		for(int casc = 0; casc < FreeCellNode.NUM_CASCADES; casc ++){
			if(node.getCascades()[casc] == null)
				continue;
			
			CardNode cardNode = node.getCascades()[casc];
			
			for(int depth = 0; depth <= emptyTotal; depth++){
				if(cardNode.getCard().getRank() == nextRank[cardNode.getCard().getSuit()])
					reachableCards++;
				
				if(cardNode.getPrevious() == null)
					break;
				else
					cardNode = cardNode.getPrevious();
			}
		}
		
		score += reachableCards * REACHABLE_MULT;
		
		
		
		return score;
	}

}
