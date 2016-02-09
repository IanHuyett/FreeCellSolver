import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class FreeCellNode extends SearchNode {
	private CardNode[] cascades;
	private CardNode[] cells;
	private CardNode[] foundations;
	public String moveMade;
	
	final static int NUM_CASCADES = 8;//array sizes
	final static int NUM_CELLS = 4;
	final static int NUM_FOUNDATIONS = 4;
	
	final static int CASCADE = 0;//identifiers
	final static int CELL = 1;
	final static int FOUNDATION = 2;
	
	
	final static String[] CELL_IDS = {"a", "b", "c", "d"};	
	
	public FreeCellNode(int seed) {//for start node
		cascades = new CardNode[NUM_CASCADES];
		cells = new CardNode[NUM_CELLS];
		foundations = new CardNode[NUM_FOUNDATIONS];
		moveMade = "";
		
		Stack<Card> deck = Card.getShuffle(seed);
		
		int cardCount = 0;
		while(!deck.isEmpty()){
			putCard(cascades, cardCount++%8, deck.pop());
		}
		
		autoMove();
		
	}
	
	public boolean canSupermove(CardNode node){
		if (node.getPrevious() == null){
			return false;
		}
		Card card = node.getCard();
		Card previous = node.getPrevious().getCard();
		int currRank = node.getCard().getRank(); 
			
		if(previous.getRank() != (currRank + 1)){
			return false;
		}
		if(previous.isRed() == card.isRed()){
			return false;
		}
		return true;
	}
	
	public int availableMoves(){
		//Count free cells
		int cellNum = 0;
		for (CardNode c : cells){
			if(c == null){
				cellNum++;
			}
		}
		//Count free cascades
		int cascadeNum = 0;
		for (CardNode s : cascades){
			if(s == null){
				cascadeNum++;
			}
		}
		//return count
		return (cellNum + 1) * (cascadeNum + 1);
	}
	
	public void putCard(CardNode[] area, int position, Card card){//puts a card in a new CardNode as top of specified cardstack
		CardNode newNode = new CardNode(card, area[position]);
		area[position] = newNode;
	}
	
	
	public void moveCard(int originAreaID, int originStack, int destAreaID, int destStack){//moves card from one stack to another, param arrays should contain constant for cascade,cell or home, 
		//record move
		if(originAreaID == CASCADE)
			moveMade += (originStack +1);
		else
			moveMade += CELL_IDS[originStack];
		if(destAreaID == CASCADE)
			moveMade += (destStack +1) + " ";
		else if(destAreaID == CELL)
			moveMade += CELL_IDS[destStack] + " ";
		else
			moveMade += "h ";
		
		//make move
		CardNode[] originArea = getArea(originAreaID);
		CardNode[] destArea = getArea(destAreaID);
		CardNode cardNode = originArea[originStack];
		originArea[originStack] = cardNode.getPrevious();
		putCard(destArea, destStack, cardNode.getCard());//places moved card in new cardNode so as not to interfere with previous FreeCellNodes
	}
	
	public void autoMove(){//checks if each top card can be moved to foundation;
		for(int i = 0; i < NUM_CASCADES; i++){
			if(cascades[i] != null &&checkAutoMove(cascades[i].getCard())){
				moveCard(CASCADE, i, FOUNDATION, cascades[i].getCard().suit);
				autoMove();
			}
		}
		for(int i = 0; i < NUM_CELLS; i++){
			if(cells[i] != null &&checkAutoMove(cells[i].getCard())){
				moveCard(CELL, i, FOUNDATION, cells[i].getCard().suit);
				autoMove();
			}
		}
		
	}
	
	public boolean checkAutoMove(Card currentCard){//checks if given card can be automatically moved to foundation
		if(currentCard.rank == 0){//automatically move aces
			return true;
		}
		else if(currentCard.rank == 1){
			return foundations[currentCard.suit] != null;//move 2s if ace is in place
		}
		else if(foundations[currentCard.getSuit()] != null && foundations[currentCard.getSuit()].getCard().getRank() == currentCard.rank -1){
			//check if card can be moved to foundation
			for(int i = 0; i < NUM_FOUNDATIONS; i++){//check other foundations against automove conditions
				if(i != currentCard.getSuit()){
					if(foundations[i] == null){
						return false;
					}
					else if(currentCard.isRed() != Card.isSuitRed[i]){//opposite color foundation
						if(!(foundations[i].getCard().rank >= currentCard.rank - 2))
							return false;
					}
					else if(!(foundations[i].getCard().rank >= currentCard.rank - 3))//same color
						return false;
				}
			}
			return true;

		}
		else return false;
	}
	
	public CardNode[] getArea(int i){
		switch (i){
		case CASCADE: return cascades;
		case CELL: return cells;
		case FOUNDATION: return foundations;
		default: return null;
		}
	}

	@Override
	public boolean isGoal() {
		for(int i = 0; i < NUM_FOUNDATIONS; i++){
			if(foundations[i].getCard().getRank() != 12){
				return false;
			}
		}
		return true;
	}

	@Override
	public ArrayList<SearchNode> expand() {
		ArrayList<SearchNode> children = new ArrayList<SearchNode>();
		for(int origin = 0; origin < NUM_CASCADES; origin++){//moves from cascades
			if(cascades[origin] == null)
				continue;
			
			CardNode toMoveNode = cascades[origin];
			Card toMove = toMoveNode.getCard();
			int count = 0;
			if (canSupermove(toMoveNode) && count <= availableMoves()){
				toMoveNode = toMoveNode.getPrevious();
				toMove = toMoveNode.getCard();
			}
			for(int dest = 0; dest < NUM_CASCADES; dest++){//to cascades
				if(dest == origin){
					continue;
				}
				else if(cascades[dest] == null || (toMove.rank == cascades[dest].getCard().rank - 1 && toMove.isRed() != cascades[dest].getCard().isRed())){
					children.add(createAndMove(CASCADE, origin, CASCADE, dest));
				}
			}
			for(int dest = 0; dest < NUM_CELLS; dest++){//to cells
				if(cells[dest] == null){
					children.add(createAndMove(CASCADE, origin, CELL, dest));
					break;
				}
			}

			if(foundations[toMove.suit] != null && foundations[toMove.suit].getCard().rank == toMove.rank - 1){
				createAndMove(CASCADE, origin, FOUNDATION, toMove.suit);
			}
		}
		
		
		for(int origin = 0; origin < NUM_CELLS; origin++){//from cells
			if(cells[origin] == null)
				continue;
			Card toMove = cells[origin].getCard();
			for(int dest = 0; dest < NUM_CASCADES; dest++){//to cascades
				if(cascades[dest] == null || (toMove.rank == cascades[dest].getCard().rank - 1 && toMove.isRed() != cascades[dest].getCard().isRed())){
					children.add(createAndMove(CELL, origin, CASCADE, dest));
				}
			}
			if(foundations[toMove.suit] != null && foundations[toMove.suit].getCard().rank == toMove.rank - 1){
				createAndMove(CELL, origin, FOUNDATION, toMove.suit);
			}
		}
//		System.out.println(children);
		return children;
	}
	
	public FreeCellNode createAndMove(int originAreaID, int originStack, int destAreaID, int destStack){
		FreeCellNode newNode = clone();
		newNode.moveCard(originAreaID, originStack, destAreaID, destStack);
		newNode.autoMove();
		return newNode;
	}
	
	public FreeCellNode clone(){
		
		FreeCellNode newNode = (FreeCellNode) super.clone();
		newNode.cascades = cascades.clone();
		newNode.cells = cells.clone();
		newNode.foundations = foundations.clone();
		newNode.moveMade = "";
		newNode.parent = this;
		newNode.depth = depth+1;
		return newNode;
	}
	
	public String getStackString(CardNode[] area, int position){//initiates building of a string representation of a stack if stack is not empty
		if(area[position]==null){
			return " ";
		}
		else{
			StringBuilder sb = new StringBuilder();
			buildStackString(area[position], sb);
			return sb.toString();
		}
	}
	
	public void buildStackString(CardNode cardNode, StringBuilder sb){//recursively builds string representation of stack
		if(cardNode.getPrevious() != null){
			buildStackString(cardNode.getPrevious(), sb);
			sb.append(", ");
		}
		sb.append(cardNode.getCard().toString());
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Foundations\n");
		for(int i = 0; i < NUM_FOUNDATIONS; i++){
			sb.append(Card.suitNames[i]);
			sb.append(": ");
			sb.append(getStackString(foundations, i));
			sb.append("\n");
		}
		
		sb.append("Cells\n");
		for(int i = 0; i < NUM_CELLS; i++){
			sb.append(CELL_IDS[i]);
			sb.append(": ");
			sb.append(getStackString(cells, i));
			sb.append("\n");
		}
		
		sb.append("Cascades\n");
		for(int i = 0; i < NUM_CASCADES; i++){
			sb.append(i + 1);
			sb.append(": ");
			sb.append(getStackString(cascades, i));
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	public String getMoveHistory(){
		if(parent != null)
			return ((FreeCellNode) parent).getMoveHistory() + moveMade;
		else
			return moveMade;
	}
	
	public CardNode[] getCascades() {
		return cascades;
	}


	public CardNode[] getCells() {
		return cells;
	}


	public CardNode[] getFoundations() {
		return foundations;
	}
	
	
	/*public static void main(String[] args){
		FreeCellNode startNode = new FreeCellNode(9998);
		//FreeCellNode startNode = new FreeCellNode(11987 );
		System.out.println(startNode.toString());
		startNode.autoMove();
		System.out.println(startNode.toString());
		//System.out.println(startNode.moveMade);
		//manualTest(startNode);
		
		//System.out.println(startNode.getMoveHistory());
		//FreeCellNode childNode = startNode.clone();
		//childNode.moveCard(childNode.cascades, 0, childNode.cells, 1);
		ArrayList<SearchNode> children = startNode.expand();
		children.get(0).expand();
		//System.out.println(childNode.toString());
		//System.out.println(childNode.getMoveHistory());
	}*/
	
	/*public static void manualTest(FreeCellNode startNode){
		//allows entering of moves via terminal using standard notation
		//does not check legality or handle out of bounds numbers/letters
		Scanner in = new Scanner(System.in);
		FreeCellNode currentNode = startNode;
		CardNode[] originArea = currentNode.cells;
		int originStack = 0;
		CardNode[] destArea= currentNode.foundations;
		int destStack = 0;
		while(true){
			System.out.printf("Depth: %d\n", currentNode.depth);
			System.out.println("Move History: " + currentNode.getMoveHistory());
			System.out.println(currentNode.toString());
			System.out.println("Move: ");
			String input = in.nextLine();
			if(input.length() != 2){
				System.out.println("invalid");
				continue;
			}
			String origin = input.substring(0,1);
			String dest = input.substring(1,2);
			
			
			
			FreeCellNode newNode = currentNode.clone();
			currentNode = newNode;
			
			
			if(Character.isDigit(origin.charAt(0))){
				originArea = currentNode.cascades;
				originStack = Integer.parseInt(origin) - 1;
				
			}
			else{
				originArea = currentNode.cells;
				for(int i = 0; i < 4; i ++){
					if(CELL_IDS[i].equals(origin));
						originStack = i;
				}
			}
			if(dest.equals("h")){
				destArea = currentNode.foundations;
				destStack = originArea[originStack].card.suit;
			}
			else if(Character.isDigit(dest.charAt(0))){
				destArea = currentNode.cascades;
				destStack = Integer.parseInt(dest) - 1;
			}
			else{
				destArea = currentNode.cells;
				for(int i = 0; i < 4; i ++){
					if(CELL_IDS[i].equals(dest)){
						destStack = i;
					}
				}
					
			}
			if(originArea[originStack] == null){
				System.out.println("invalid");
				continue;
			}
			
			

			currentNode.moveCard(originArea, originStack, destArea, destStack);
			if(currentNode.isGoal()){
				System.out.println(currentNode.toString() + "\n\n Finished at depth " + currentNode.depth);
			}
			

		}
	}
*/
}
