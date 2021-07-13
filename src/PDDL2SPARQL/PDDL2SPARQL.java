package PDDL2SPARQL;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PDDL2SPARQL {
	
	/*
	public static void main(String[] args) throws IOException {
		
		String InputPDDL = "(and (not (http://127.0.0.1/ontology/SUMO.owl#equal ?http://127.0.0.1/services/1.1/addressDistanceCalculator.owls#_ADDRESS1 ?http://127.0.0.1/services/1.1/addressDistanceCalculator.owls#_ADDRESS2))\n" + 
		"				     (http://127.0.0.1/ontology/protont.owl#locatedIn ?http://127.0.0.1/services/1.1/addressDistanceCalculator.owls#_CITY1 ?http://127.0.0.1/services/1.1/addressDistanceCalculator.owls#_STATE1)\n" + 
		"					 (http://127.0.0.1/ontology/protont.owl#locatedIn ?http://127.0.0.1/services/1.1/addressDistanceCalculator.owls#_CITY2 ?http://127.0.0.1/services/1.1/addressDistanceCalculator.owls#_STATE2))";
		
		
		//String InputPDDL = "(not (http://127.0.0.1/ontology/books.owl#Novel ?http://127.0.0.1/services/1.1/book_authorprice_service.owls#_BOOK))";
		translator(InputPDDL);
	}
	*/
	
	
	
	public static String translator(String InputPDDL) throws IOException 
	{
		
		// STRING INPUT
		String PDDL = InputPDDL;
		String OutputSPARQL = "";

		
		String uri = "[?]http:\\/\\/(\\S*?)+.#";
	    Matcher matcher_1 = Pattern.compile(uri).matcher(PDDL);
	    while(matcher_1.find()) {
	    	PDDL = PDDL.replace(matcher_1.group(), "?"); //trim URI & add ? before variables
	    }
	    String T = "(.*)";
	    Matcher matcher_2 = Pattern.compile(T).matcher(PDDL);
	    String triplet = "";
	    while(matcher_2.find()) {
	    	triplet = triplet.concat(matcher_2.group() + "\n");
	    }	    
	    triplet = triplet.replace("(and ", "");
	    triplet = triplet.replace("(or ", "");
	    triplet = triplet.replace("(not ", "");
	    triplet = triplet.replace("))))", ")");
	    triplet = triplet.replace(")))", ")");
	    triplet = triplet.replace("))", ")");
	    triplet = triplet.replace(")", "");
	    triplet = triplet.replace("(", "");
	    triplet = triplet.replace(" *", "");
	    //System.out.println("Triplet: "+triplet);
	    
	    String Filter = "";
		Scanner scan_3 = new Scanner(PDDL);
		while(scan_3.hasNextLine()) {
			Filter = Filter.concat(scan_3.nextLine());
		}
		//System.out.println("PDDL: "+PDDL);
		
		Filter = Filter.replace("  (", "");
		Filter = Filter.replace(" (", "");
		Filter = Filter.replace("(", "");
		Filter = Filter.replace(")", "");
		Filter = Filter.replace("   ?", "?");
		Filter = Filter.replace("  ?", "?");
		Filter = Filter.replace(" ?", "?");
		Filter = Filter.replace("not ", "not");	
		//System.out.println("Filter: "+Filter);
		
	    String URI = "";
	    String uri_2 = "http:\\/\\/(\\S*?)+.#";
	    Pattern p = Pattern.compile(uri_2);
	    Matcher matcher_uri = p.matcher(triplet);
	    int j = 1;
	    while(matcher_uri.find()) {
	    	
	    	URI = URI.concat(matcher_uri.group() + ">" + "\n").replace("http","PREFIX " + "URI" + j + ":" + " <");
	    	
	    	triplet = triplet.replace(matcher_uri.group(), "URI" + j + ":");
	    	Filter = Filter.replace(matcher_uri.group(), "URI" + j + ":");
	    	
	    	matcher_uri = p.matcher(triplet);
	    	j++;
	    }
	    URI = URI.replace("<", "<http");
	    //System.out.println("URI: "+URI);
	    //System.out.println("Filter: "+Filter);
	    
	    String Triplet_Pair = "";
	    Scanner scan_2 = new Scanner(triplet);
	    while(scan_2.hasNextLine()) {
	    	//reorder input to form Triplet & Pair
	    	String string = scan_2.nextLine();
	    	if (wordcount(string.trim()) == 2) {
				String[] words = string.trim().split("\\s+");
				Triplet_Pair = Triplet_Pair.concat("    " + words[1] + " rdf:type " + words[0] + "." + "\n");
			} else if (wordcount(string.trim()) == 3) {
				String[] words = string.trim().split("\\s+");
				Triplet_Pair = Triplet_Pair.concat("    " + words[1] + " " + words[0] + " " + words[2] + "." + "\n");
			} else if (wordcount(string.trim()) == 1) {
				Triplet_Pair = Triplet_Pair.concat("" + "\n");
			} else {
				Triplet_Pair = Triplet_Pair.concat("" + "\n");
			}
		}
	    scan_2.close();
	    //System.out.println("Triplet_Pair: "+Triplet_Pair);//
	    Matcher matcher_rdf = Pattern.compile("rdf:type").matcher(Triplet_Pair);
	    if (matcher_rdf.find()) {
	    	URI = URI + "PREFIX rdf:" + " " + "<http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
	    }
	    
	    //System.out.println("Filter: "+Filter);
	    //Filter = Filter.replace("or","or ");
	    String or = "^or";
	    Matcher matcher_3 = Pattern.compile(or).matcher(Filter);
	    while(matcher_3.find()) {
	    	Filter = Filter.replace(matcher_3.group(), "or "); //separate "or" at the beginning
	    }
	    //Filter = Filter.replace("and","and ");
	    String and = "^and";
	    Matcher matcher_4 = Pattern.compile(and).matcher(Filter);
	    while(matcher_4.find()) {
	    	Filter = Filter.replace(matcher_4.group(), "and "); //separate "and" at the beginning
	    }
	    //System.out.println(Filter);
		if (wordcount(Filter) == 1) { 
			//Filter = Filter.replace("not", "not ");
			String not = "^not";
		    Matcher matcher_5 = Pattern.compile(not).matcher(Filter);
		    while(matcher_5.find()) {
		    	Filter = Filter.replace(matcher_5.group(), "not "); //separate "not" at the beginning
		    }
			
			
			if (wordcount(Filter) == 2) {
				Filter = Filter.replace("?", " ?");
				String[] words = Filter.trim().split("\\s+");
				String Filter_new = "";		
				if (wordcount(Filter) == 3) {
					Filter_new = "not" + " {" + words[2] + " " + "rdf:type" + " " + words[1] + "}";
					Filter_new = Filter_new.replace("not ", "NOT EXISTS ");
					//System.out.println("Filter_new: "+Filter_new);
				
					String SPARQL = URI + "\n" + "\n" + "ASK {\n" + Triplet_Pair + "FILTER(\n" + Filter_new + "\n)\n" + "}";
					//System.out.println(SPARQL);
					OutputSPARQL = SPARQL;
				}else if (wordcount(Filter) == 4) {
					Filter_new = "not" + " {" + words[2] + " " + words[1] + " " + words[3] + "}";
					Filter_new = Filter_new.replace("not ", "NOT EXISTS ");
					System.out.println("Filter_new: "+Filter_new);//
					String SPARQL = URI + "\n" + "\n" + "ASK {\n" + Triplet_Pair + "FILTER(\n" + Filter_new + "\n)\n" + "}";
					//System.out.println(SPARQL);
					OutputSPARQL = SPARQL;
				}else {
					System.out.println("Error: Please check again the PDDL input syntax");
				}				
			}else if (wordcount(Filter) == 1) {  //NO FILTER
				String SPARQL = URI + "\n" + "\n" + "ASK {\n" + Triplet_Pair + "\n" + "}";
				//System.out.println(SPARQL);
				OutputSPARQL = SPARQL;
			}else {
				System.out.println("Error: Please check again the PDDL input syntax");
			}
			
		} else {
			int i = 1;
			String[] words = Filter.trim().split("\\s+");
			String Filter_new = "";
			while (i <= (2 * wordcount(Filter) - 3)) {
				words[(i+1)/2] = words[(i+1)/2].replace("?", " ?");
				words[(i+1)/2] = words[(i+1)/2].replace("not", "not ");
				if (i % 2 == 1) {
					if (wordcount(words[(i+1)/2].trim()) == 2) {
						String[] w = words[(i+1)/2].trim().split("\\s+");
						words[(i+1)/2] = "    " + "(" + w[1] + " rdf:type " + w[0] + ")";
					} else if (wordcount(words[(i+1)/2].trim()) == 3) {
						String[] w = words[(i+1)/2].trim().split("\\s+");
						Matcher matcher_not = Pattern.compile("not").matcher(words[(i+1)/2]);
						if (!matcher_not.find()) {
							words[(i+1)/2] = "    " + "(" + w[1] + " " + w[0] + " " + w[2] + ")";
						} else {
							words[(i+1)/2] = "    " + "(" + w[0] + " {" + w[2] + " rdf:type " + w[1] + "})";
						}
					} else if (wordcount(words[(i+1)/2].trim()) == 4) {
						String[] w = words[(i+1)/2].trim().split("\\s+");
						words[(i+1)/2] = "    " + "(" + w[0] + " {" + w[2] + " " + w[1] + " " + w[3] + "})";
					} else {
						System.out.println("Error: Please check again the PDDL input syntax");
					}
					Filter_new = Filter_new.concat(words[(i+1)/2] + "   ");
				
				} else if (i % 2 == 0) {
					Filter_new = Filter_new.concat(words[0] + " ");
				}
			
				i++;
			}
			scan_3.close();
			Filter_new = Filter_new.replace("and ", "&& ");
			Filter_new = Filter_new.replace("or ", "|| ");
			Filter_new = Filter_new.replace("not ", "NOT EXISTS ");
		
			String SPARQL = URI + "\n" + "\n" + "ASK {\n" + Triplet_Pair + "FILTER(\n" + Filter_new + "\n)\n" + "}";
			OutputSPARQL = SPARQL;
			
				
		}
		System.out.println(OutputSPARQL);
		return OutputSPARQL;
		
	}

	
	public static int wordcount(String input)  
    {  
		if (input == null || input.isEmpty()) {
			return 0; } 
		
		String[] words = input.split("\\s+"); 
		return words.length;

    }  

}
