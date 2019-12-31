import java.util.TreeMap;

public class Query {
	
	/*Class to hold the information from a user's query. 
	 * 
	 * Conditions: the list of conditions wanting to be solved for
	 * Given Conditions: the known conditions for the problem
	 * 
	 * */
	
	private TreeMap<String, Boolean> conditions;
	
	private TreeMap<String, Boolean> givenConditions;

	public Query() {
		conditions = new TreeMap<String, Boolean>();
		givenConditions = new TreeMap<String, Boolean>();
	}
	
	public void addCondition(Node node, Boolean condition) {
		conditions.put(node.getName(), condition);
	}
	
	public Boolean removeCondition(Node node) {
		return conditions.remove(node.getName());
	}
	
	public Boolean addGiven(Node node, Boolean condition) {
		return givenConditions.put(node.getName(), condition);
	}
	
	public Boolean removeGiven(Node node) {
		return givenConditions.remove(node.getName());
	}
	
	public TreeMap<String, Boolean> getConditions() {
		return conditions;
	}

	public TreeMap<String, Boolean> getGiven() {
		return givenConditions;
	}

	public TreeMap<String, Boolean> getAll(){
		TreeMap<String, Boolean> all = new TreeMap<String, Boolean>(conditions);
		all.putAll(givenConditions);
		return all;
	}
	
	public String toString() {
		String queryString = "P(";
		
		for(String cond : conditions.keySet()) {
			queryString += " " + cond +  "=" + conditions.get(cond) + " ";
		}
		
		queryString += "|";
		
		for(String cond : givenConditions.keySet()) {
			queryString += " " + cond +  "=" + givenConditions.get(cond) + " ";
		}
		
		queryString += ")";
		return queryString;
	}

}
