import java.util.ArrayList;
import java.util.Random;

public class FreeCellNodeTest {

	public static void main(String[] args) {
		Random random = new Random();
		/*int maxNodes = 1000000;
		ArrayList<SearchNode> nodes = new ArrayList<SearchNode>();
		long startMillis = System.currentTimeMillis();

		SearchNode node = null;
		int gameNodeCount = 0;
		int gameNodeCountLimit = 50;
		while (nodes.size() < maxNodes) {
			if (gameNodeCount == gameNodeCountLimit) {
				gameNodeCount = 0;
				node = null;
			}
			if (node == null)
				node = new FreeCellNode(random.nextInt(1000000) + 1);
			nodes.add(node);
			gameNodeCount++;
			ArrayList<SearchNode> children = node.expand();
			if (children.isEmpty()) {
				node = null;
				gameNodeCount = 0;
			}
			node = children.isEmpty() ? null : children.get(random.nextInt(children.size()));
		}
		
		long endMillis = System.currentTimeMillis();
		System.out.println("Used time in milliseconds: " + (endMillis - startMillis));
		
		// From http://www.vogella.com/tutorials/JavaPerformance/article.html#runtimeinfo_memory:
		// Get the Java runtime
	    Runtime runtime = Runtime.getRuntime();
	    // Run the garbage collector
	    runtime.gc();
	    // Calculate the used memory
	    long memory = runtime.totalMemory() - runtime.freeMemory();
	    System.out.println("Used memory in bytes: " + memory);
	    
	    // To ensure the ArrayList isn't garbage-collected:
	    System.out.println("Number of nodes generated: " + nodes.size());*/
		
		Searcher searcher = new RecursiveDepthLimitedSearcher(1000);
		boolean found = searcher.search(new FreeCellNode(random.nextInt(1000000) + 1));
		System.out.println(found);
	}

}
