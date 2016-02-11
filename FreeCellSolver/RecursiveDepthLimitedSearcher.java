
public class RecursiveDepthLimitedSearcher extends Searcher {

	int depthLimit;
	
	public RecursiveDepthLimitedSearcher(int depthLimit) {
		this.depthLimit = depthLimit;
	}

	@Override
	public boolean search(SearchNode node) {
		return search(node, depthLimit);
	}
	
	private boolean search(SearchNode node, int depthLimit){
		nodeCount++;
		if(node.isGoal()){
			goalNode = node;
			return true;
		}
		if(depthLimit > 0){
			for(SearchNode child : node.expand()){
				if(search(child, depthLimit - 1)){
					return true;
				}
			}
		}
		return false;
	}

}
