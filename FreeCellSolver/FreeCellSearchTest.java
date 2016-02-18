import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

public class FreeCellSearchTest {

	public static void main(String[] args) {
		Random random = new Random();
		int maxSeed = 6;
		
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
		int solved = 0;
		
		long totalTime = 0;
		
		try {
			PrintWriter writer = new PrintWriter("results.txt");			
			
					
			for(int i = 1; i<=100; i++){
				System.out.printf("\n%d: ",i);
				long startMillis = System.currentTimeMillis();
				
				int seed = (int) Math.pow(10, random.nextInt(maxSeed));
				writer.println("Seed: " + seed);
				
				Searcher searcher = new RecursiveDepthLimitedSearcher(10);
				boolean found = searcher.search(new FreeCellNode(seed));
				
				long time = System.currentTimeMillis() - startMillis;
				
				if(found){
					solved++;
					System.out.print("found");
					writer.println(((FreeCellNode) searcher.goalNode).moveMade);
				}
				else{
					time += 120000;
					System.out.print("not found");
					writer.println("No solution found");
				}
				totalTime += time;

				System.out.printf(" %d\n%d out of %d", time, solved, i);
			}
			System.out.printf("\nSolved %d out of 100\n Average time: %d", solved, totalTime/100);
			
			writer.close();
		
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
