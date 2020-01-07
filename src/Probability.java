import java.util.TreeMap;

public class Probability {

	/*Represents the relationship between conditions and a probability value
	 * 
	 * Conditions: the list of required conditions for the probability
	 * Value: the concrete value of the probability
	 * 
	 * Example: 
	 * If this Probility has the conditions...
	 * 
	 * 1. Sprinkler, True
	 * 2. Rain, True
	 * 3. WetGrass, True
	 * 
	 * Then the probability value will be 0.95*/
	private TreeMap<String, Boolean> conditions;
	
	private Float value;

	public Probability(Float value) {
		conditions = new TreeMap<String, Boolean>();
		this.value = value;
	}
	
	public void addCondition(Node node, Boolean value) {
		conditions.put(node.getName(), value);
	}
	
	public void removeCondition(Node node) {
		conditions.remove(node.getName());
	}
	
	public Float getProbability() {
		return value;
	}

	public void setProbability(Float value) {
		this.value = value;
	}
	
	public boolean isProbabilityForConditions(TreeMap<String, Boolean> conditions) {
		
		if(conditions.size() > 0) {
			for(String node : conditions.keySet())
			{
				if(!this.conditions.containsKey(node)) {
					return false;
				} 
				else if(conditions.get(node) != this.conditions.get(node)) {
					return false;
				}
			}
			
			return true;
			
		} else {
			return false;
		}
	}
	
	public String toString() {
		String probString = "\n\t" + this.value;
		
		for(String cond : conditions.keySet()) {
			probString += "\n\t\t" + cond + " is " + conditions.get(cond) + "\n"; 
		}
		
		return probString;
	}

}
