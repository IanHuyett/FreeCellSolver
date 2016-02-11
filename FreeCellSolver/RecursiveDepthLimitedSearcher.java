
public class RecursiveDepthLimitedSearcher extends Searcher {

	int depthLimit;
	static long timer;
	static int score = 0;
	
	public RecursiveDepthLimitedSearcher(int depthLimit) {
		this.depthLimit = depthLimit;
		timer = System.currentTimeMillis();
	}

	@Override
	public boolean search(SearchNode node) {
		return search(node, depthLimit);
	}
	
	private boolean search(SearchNode node, int depthLimit){
		if(System.currentTimeMillis() - timer >= 60000){
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
				if(search(child, depthLimit - 1)){
					return true;
				}else if (depthLimit == 1){
					if(FreeCellSolver.scoreNode((FreeCellNode) child) > score){
						score = FreeCellSolver.scoreNode((FreeCellNode) node);
						System.out.println(score);
					}
				}
			}
		}
		return false;
	}

}
