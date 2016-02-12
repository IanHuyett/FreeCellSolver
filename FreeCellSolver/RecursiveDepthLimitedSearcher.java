import java.util.ArrayList;

public class RecursiveDepthLimitedSearcher extends Searcher {

	int depthLimit;
	static long timer;
	static int score = 0;
	static FreeCellNode bestNode;
	static int bestScore;
	static ArrayList<Integer> previousHashes;
	static ArrayList<Integer> generationHashes;
	static int nodeCount = 0;
	
	public RecursiveDepthLimitedSearcher(int depthLimit) {
		this.depthLimit = depthLimit;
		timer = System.currentTimeMillis();
	}

	@Override
	public boolean search(SearchNode node) {
		previousHashes = new ArrayList<Integer>();
		generationHashes = new ArrayList<Integer>();
		bestNode = (FreeCellNode) node;
		boolean goalFound = false;
		while(System.currentTimeMillis() - timer <= 60000 && !goalFound){
		 //   System.out.println(bestNode);
			//System.out.println(bestNode.depth);
			
	
			//System.out.println(bestScore);
			
			bestScore = 0;
			previousHashes.add(bestNode.getHash());
			generationHashes.clear();
			goalFound = search(bestNode, depthLimit);
			if(bestScore == 0){
				return goalFound;
			}
		}
		return goalFound;
	}
	
	private boolean search(FreeCellNode node, int depthLimit){
		if(System.currentTimeMillis() - timer >= 60000){
			//if(FreeCellSolver.scoreNode((FreeCellNode) node) > score){
			//	System.out.println("Depth " + depthLimit + " node: " + node);
			//}
			System.out.println("Time reached.");
			return false;
		}
		nodeCount++;
		if(node.isGoal()){
			goalNode = node;
			return true;
		}
		if(depthLimit > 0){
			for(SearchNode child : node.expand()){
				if(!generationHashes.contains(((FreeCellNode)child).getHash())){
					generationHashes.add(((FreeCellNode)child).getHash());
					int score = FreeCellScorer.scoreNode((FreeCellNode) child);
					if(score > bestScore && !previousHashes.contains(((FreeCellNode) child).getHash())){
						//generationHashes.add(node.getHash());
						//System.out.println(generationHashes);
						bestScore = score;
						bestNode = (FreeCellNode) child;
					}
					
					if(search((FreeCellNode)child, depthLimit - 1)){
						return true;
					}//else if (depthLimit == 1){
					//	if(FreeCellSolver.scoreNode((FreeCellNode) child) > score){
					//		score = FreeCellSolver.scoreNode((FreeCellNode) node);
					//		System.out.printf("D:%d S: %d \n", node.depth, score);
					//	}
					//}
				}
			}
		}
	//	else{
		//	int score = FreeCellSolver.scoreNode((FreeCellNode) node);
		//	if(score > bestScore){
				//generationHashes.add(node.getHash());
				//System.out.println(generationHashes);
			//	bestScore = score;
			//	bestNode = node;
		//	}
			//if(++nodeCount %1000 == 0)
				//System.out.println(nodeCount);
		//}
		return false;
	}

}
