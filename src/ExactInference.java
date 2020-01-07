import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.TreeMap;

public class ExactInference {
	
	//Utility class for solving an Exact Inference
	
	
	/**
	   * Function to determin an Exact Inference from a list of conditions
	   * @param query - the user's query
	   * @param network - the referenced network
	   * @return float value of the result
	   */
	public static Float calculate(Query query, ArrayList<Node> network) {
		
		TreeMap<String, Node> mapNetwork = convertToMap(network);
		//hidden nodes for the top portion
		TreeMap<String, Node> hiddenTop = findHidden(query.getAll(), mapNetwork);
		//hidden nodes for the bottom portion
		TreeMap<String, Node> hiddenBottom = findHidden(query.getGiven(), mapNetwork);
		
		//P(Query and Given) / P(Given)
		return calculateRec(query.getAll(), hiddenTop, network, 0) / calculateRec(query.getGiven(), hiddenBottom, network, 0);
		
	}

	/**
	   * Recursive function to determin the probability of a given set of conditions within a network
	   * @param known - condititions for the probability
	   * @param hidden - the nodes that are hidden in the problem
	   * @param nodeList - list of nodes that the function is going through
	   * @param currNode - current node being evaluted for the known conditions
	   * @return float value of the probability
	   */
	public static Float calculateRec(TreeMap<String, Boolean> known, TreeMap<String, Node> hidden, ArrayList<Node> nodeList, int currNode) {
		
		//End of list
		if(currNode > nodeList.size() - 1) {
			return 1.0f;
		}
		
		Node node = nodeList.get(currNode);
		TreeMap<String, Boolean> conditions = getConditionsForNode(known, node);
		
		//Node is not involved in calculation
		if(known.get(node.getName()) == null && !hidden.containsKey(node.getName())) {
			return 1.0f * calculateRec(known, hidden, nodeList, currNode + 1);
		} 
		//Node is hidden, need to evaluate True and False branches
		else if(hidden.containsKey(node.getName())) {

			//Ugh, hate that I had to do this twice...
			//List of conditions to find probability
			TreeMap<String, Boolean> condPlusTrue = new TreeMap<String, Boolean>(conditions);
			condPlusTrue.put(node.getName(), true);
			TreeMap<String, Boolean> condPlusFalse = new TreeMap<String, Boolean>(conditions);
			condPlusFalse.put(node.getName(), false);
			
			//List of all known conditions plus this one
			TreeMap<String, Boolean> allPlusTrue = new TreeMap<String, Boolean>(known);
			allPlusTrue.put(node.getName(), true);
			TreeMap<String, Boolean> allPlusFalse = new TreeMap<String, Boolean>(known);
			allPlusFalse.put(node.getName(), false);
			
			//P(T) * P(Next) + P(F) * P(Next)
			return node.getProbability(condPlusTrue) * calculateRec(allPlusTrue, hidden, nodeList, currNode + 1) +
					node.getProbability(condPlusFalse) * calculateRec(allPlusFalse, hidden, nodeList, currNode + 1);
		} 
		//Node is known so P(Value) * P(Next), don't branch
		else {
			return node.getProbability(conditions) * calculateRec(known, hidden, nodeList, currNode + 1);
		}
	}
	
	/**
	   * Get the list of conditions needed to find the correct probability in a table
	   * @param known - condititions for the probability
	   * @param node - node to find conditions for
	   * @return list of conditions
	   */
	public static TreeMap<String, Boolean> getConditionsForNode(TreeMap<String, Boolean> known, Node node) {
		TreeMap<String, Boolean> conditions = new TreeMap<String, Boolean>();
		
		//Add all parent conditions
		for(Node parent : node.getParents()) {
			String name = parent.getName();
			if(known.containsKey(name)) {
				conditions.put(name, known.get(name));
			}
		}
		
		//Add self
		if(known.containsKey(node.getName())) {
			conditions.put(node.getName(), known.get(node.getName()));
		}
		
		return conditions;
	}
	
	/**
	   * Finds the hidden nodes from a list of conditions in a query
	   * @param conditions - condititions from the query
	   * @param network - full network
	   * @return list of hidden nodes
	   */
	static TreeMap<String, Node> findHidden(TreeMap<String, Boolean> conditions, TreeMap<String, Node> network){
		TreeMap<String, Node> hidden = new TreeMap<String, Node>();
		
		//Find hidden for each node in the conditions
		for(String queryNode : conditions.keySet()) {
			Node node = network.get(queryNode);
			
			findHiddenRec(hidden, conditions, node);
				
		}
		return hidden;
	}
	
	/**
	   * Recursive function to find hidden that aren't direct parents
	   * @param hidden - the nodes that are hidden in the problem
	   * @param conditions - condititions from the query
	   * @param node - node to find the hidden ancestors of
	   * @return list of hidden nodes
	   */
	static TreeMap<String, Node> findHiddenRec(TreeMap<String, Node> hidden, TreeMap<String, Boolean> conditions, Node node){
		
		if(node.getParents().size() == 0) {
			return hidden;
		} 
		else {
			for(Node parent : node.getParents()) {
				if(conditions.get(parent.getName()) == null){
					hidden.put(parent.getName(), parent);
					findHiddenRec(hidden, conditions, parent);
				}
			}
		}
		return hidden;
	}
	
	//Realized it would be easier to use a treemap here... Didn't want to modify other code so I made this function
	static TreeMap<String, Node> convertToMap(ArrayList<Node> network){
		TreeMap<String, Node> newNetwork = new TreeMap<String, Node>();
		
		for(Node node : network) {
			newNetwork.put(node.getName(), node);
		}
		return newNetwork;
	}

}
