import java.util.ArrayList;
import java.util.Scanner;

public class BayesianNetwork {
	
	//Main class for getting user input, setting up the network, and starting the calculation.
	
	public static void main(String[] args) {
		//Build weather network
		ArrayList<Node> weatherNetwork = getWeatherNetwork();
		
		Scanner input = new Scanner(System.in);
		Query query = new Query();
	
		System.out.println("Welcome to the Weather Network!");
		System.out.println("Please follow the prompts to determine the probability of an outcome.");
		System.out.println("\n");
		
		//Getting user defined query and given conditions
		getUserQueryConditions(input, query, weatherNetwork);
		getUserGivenConditions(input, query, weatherNetwork);
		
		input.close();
		
		System.out.println("\nQuery: " + query.toString());

		//Solve user's query
		System.out.println("Result: " + ExactInference.calculate(query, weatherNetwork));
		

		/*TEST QUERIES - uncomment runTestQueries() to see result */

		//runTestQueries(weatherNetwork);
		
	}
	
	//Get the User's conditions they are wanting to be solved for
	public static void getUserQueryConditions(Scanner input, Query query, ArrayList<Node> network ) {
		String userInput = "";
		while(!userInput.equals("No")) {
			System.out.println("Which topic is your question about? (Pick one of the following)");
		
			for( int i = 0; i < network.size() ; i++) {
				int selectValue = i + 1;
				System.out.println("[" + selectValue + "] " + network.get(i).getName());
			}
			
			int nodeIndex = input.nextInt() - 1;
			
			System.out.print("True or False?");
			String nodeValue = input.next();
			
			Node node = network.get(nodeIndex);
			if(nodeValue.equals("True") || nodeValue.equals("true")) {
				query.addCondition(node, true);
			} else if(nodeValue.equals("False") || nodeValue.equals("false")) {
				query.addCondition(node, false);
			}
			
			System.out.print("Would you like to add a topic to the query? (Yes or No)");
			userInput = input.next();
			
		}
	
	}
	
	//Get the given conditions from the User
	public static void getUserGivenConditions(Scanner input, Query query, ArrayList<Node> network ) {
		String userInput = "";
		System.out.println("\n Now for the given conditions... \n");
		while(!userInput.equals("No")) {
			System.out.println("What are the given conditions? (Pick one of the following)");
		
			for( int i = 0; i < network.size() ; i++) {
				int selectValue = i + 1;
				System.out.println("[" + selectValue + "] " + network.get(i).getName());
			}
			
			int nodeIndex = input.nextInt() - 1;
			
			System.out.print("True or False?");
			String nodeValue = input.next();
			
			Node node = network.get(nodeIndex);
			if(nodeValue.equals("True") || nodeValue.equals("true")) {
				query.addGiven(node, true);
			} else if(nodeValue.equals("False") || nodeValue.equals("false")) {
				query.addGiven(node, false);
			}
			
			System.out.print("Would you like to add a given condition? (Yes or No)");
			userInput = input.next();
			
		}
	
	}
	
	
	public static ArrayList<Node> getWeatherNetwork(){
		
		//Create Winter node with probabilities
		Node winter = new Node("Winter");
		winter.addProbability(0.6f, new Node [] {winter}, new Boolean [] {true});
		winter.addProbability(0.4f, new Node [] {winter}, new Boolean [] {false});
		
		//Create Sprinkler node with probabilities
		Node sprinkler = new Node("Sprinkler");
		sprinkler.addParent(winter);
		sprinkler.addProbability(0.2f, new Node [] {winter, sprinkler}, new Boolean [] {true, true});
		sprinkler.addProbability(0.8f, new Node [] {winter, sprinkler}, new Boolean [] {true, false});
		sprinkler.addProbability(0.75f, new Node [] {winter, sprinkler}, new Boolean [] {false, true});
		sprinkler.addProbability(0.25f, new Node [] {winter, sprinkler}, new Boolean [] {false, false});
		
		//Create Rain node with probabilities
		Node rain = new Node("Rain");
		rain.addParent(winter);
		rain.addProbability(0.8f, new Node [] {winter, rain}, new Boolean [] {true, true});
		rain.addProbability(0.2f, new Node [] {winter, rain}, new Boolean [] {true, false});
		rain.addProbability(0.1f, new Node [] {winter, rain}, new Boolean [] {false, true});
		rain.addProbability(0.9f, new Node [] {winter, rain}, new Boolean [] {false, false});
		
		//Create wetRain node with probabilities
		Node wetGrass = new Node("WetGrass");
		wetGrass.addParent(sprinkler);
		wetGrass.addParent(rain);
		wetGrass.addProbability(0.95f, new Node [] {sprinkler, rain, wetGrass}, new Boolean [] {true, true, true});
		wetGrass.addProbability(0.05f, new Node [] {sprinkler, rain, wetGrass}, new Boolean [] {true, true, false});
		wetGrass.addProbability(0.9f, new Node [] {sprinkler, rain, wetGrass}, new Boolean [] {true, false, true});
		wetGrass.addProbability(0.1f, new Node [] {sprinkler, rain, wetGrass}, new Boolean [] {true, false, false});
		wetGrass.addProbability(0.8f, new Node [] {sprinkler, rain, wetGrass}, new Boolean [] {false, true, true});
		wetGrass.addProbability(0.2f, new Node [] {sprinkler, rain, wetGrass}, new Boolean [] {false, true, false});
		wetGrass.addProbability(0.0f, new Node [] {sprinkler, rain, wetGrass}, new Boolean [] {false, false, true});
		wetGrass.addProbability(1.0f, new Node [] {sprinkler, rain, wetGrass}, new Boolean [] {false, false, false});
		
		//Create slipperyRoad node with probabilities
		Node slipperyRoad = new Node("SlipperyRoad");
		slipperyRoad.addParent(rain);
		slipperyRoad.addProbability(0.7f, new Node [] {rain, slipperyRoad}, new Boolean [] {true, true});
		slipperyRoad.addProbability(0.3f, new Node [] {rain, slipperyRoad}, new Boolean [] {true, false});
		slipperyRoad.addProbability(0.0f, new Node [] {rain, slipperyRoad}, new Boolean [] {false, true});
		slipperyRoad.addProbability(1.0f, new Node [] {rain, slipperyRoad}, new Boolean [] {false, false});
	
		//Add all nodes to the network
		ArrayList<Node> network = new ArrayList<Node>();
		network.add(winter);
		network.add(sprinkler);
		network.add(rain);
		network.add(wetGrass);
		network.add(slipperyRoad);
		
		return network;
	}

	public static void runTestQueries(ArrayList<Node> weatherNetwork){
		//The following are the order in which the nodes were added.
		//	0: Winter, 1: Sprinkler, 2: Rain, 3: Wet Grass, 4: Slippery Road

		//Probability(SlipperyRoad = false | Winter = true)
		Query testQuery = new Query();
		testQuery.addCondition(weatherNetwork.get(4), false);
		testQuery.addGiven(weatherNetwork.get(0), true);
		System.out.println("\n" + testQuery.toString());
		System.out.println("Result: " + ExactInference.calculate(testQuery, weatherNetwork));
		
		//Probability(WetGrass = true | Sprinkler = true, Winter = true)
		Query test1Query = new Query();
		test1Query.addCondition(weatherNetwork.get(3), true);
		test1Query.addGiven(weatherNetwork.get(1), true);
		test1Query.addGiven(weatherNetwork.get(0), true);
		System.out.println("\n" + test1Query.toString());
		System.out.println("Result: " + ExactInference.calculate(test1Query, weatherNetwork));
		
		//Probability(Winter = true | WetGrass = true, SlipperyRoad = true)
		Query test2Query = new Query();
		test2Query.addCondition(weatherNetwork.get(0), true);
		test2Query.addGiven(weatherNetwork.get(3), true);
		test2Query.addGiven(weatherNetwork.get(4), true);
		System.out.println("\n" + test2Query.toString());
		System.out.println("Result: " + ExactInference.calculate(test2Query, weatherNetwork));
		
		//Probability(Sprinkler = true | WetGrass = true)
		Query test3Query = new Query();
		test3Query.addCondition(weatherNetwork.get(1), true);
		test3Query.addGiven(weatherNetwork.get(3), true);
		System.out.println("\n" + test3Query.toString());
		System.out.println("Result: " + ExactInference.calculate(test3Query, weatherNetwork));
		
		//Probability(Winter = true | WetGrass = true, Rain = true, SlipperyRoad = true)
		Query test4Query = new Query();
		test4Query.addCondition(weatherNetwork.get(0), true);
		test4Query.addGiven(weatherNetwork.get(3), true);
		test4Query.addGiven(weatherNetwork.get(2), true);
		test4Query.addGiven(weatherNetwork.get(4), true);
		System.out.println("\n" + test4Query.toString());
		System.out.println("Result: " + ExactInference.calculate(test4Query, weatherNetwork));

	}

}
