import java.util.ArrayList;
import java.util.TreeMap;

public class Node {
	
	/*Class to represent a Node. 
	 * 
	 * Parents: the immediate parents of a node
	 * Probabilities: collection of probabilities that represent the probabilities table
	 * Name: the unique identifier of the node
	 * 
	 * */
	
	private ArrayList<Node> parents;
	
	private ArrayList<Probability> probabilities;
	
	private String name;
	
	public Node(String name) {
		parents = new ArrayList<Node>();
		probabilities = new ArrayList<Probability>();
		
		this.name = name;
	}
	
	public boolean addParent(Node parent) {
		if(!parents.contains(parent)) {
			parents.add(parent);
		} else {
			return false;
		}
		return true;
	}
	
	public boolean removeParent(Node parent) {
		return parents.remove(parent);
	}
	
	public void addProbability(Float value, Node [] nodes, Boolean [] values) {
		Probability prob = new Probability(value);
		for(int i = 0; i < nodes.length; i++) {
			prob.addCondition(nodes[i], values[i]);
		}
		probabilities.add(prob);
	}
	
	public Float getProbability(TreeMap<String, Boolean> conditions) {
		
		for(int i = 0; i < probabilities.size(); i++) {
			if(probabilities.get(i).isProbabilityForConditions(conditions)) {
				return probabilities.get(i).getProbability();
			}
		}
		
		return null;
		
	}
	
	public void removeProbability(Probability prob) {
		probabilities.remove(prob);
	}
	
	public Boolean equals(Node node) {
		return this.name.equals(node.name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<Node> getParents() {
		return parents;
	}

	public ArrayList<Probability> getProbabilities() {
		return probabilities;
	}

	public String toString() {
		String nodeString = this.name;
		
		nodeString += "\nParents: ";
		for(Node node : parents) {
			nodeString += node.getName() + " ";
		}
		
		nodeString += "\nProbabilities: ";
		for(Probability prob : probabilities) {
			nodeString += prob.toString();
		}
		
		return nodeString;
		
	}
	
	
	

}
