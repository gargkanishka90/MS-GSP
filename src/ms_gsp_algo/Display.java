package ms_gsp_algo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Display {
	ArrayList<Sequence> frequentSequences;
	String outputFilePath;

	Display(ArrayList<Sequence> frequentSequences, String outputFilePath){
		this.frequentSequences = frequentSequences;
		this.outputFilePath = outputFilePath;
	}
	
	public void doDisplay(){
		ArrayList<Sequence> tmpseq = new ArrayList<Sequence>();
		
		try {
			
			File file = new File(outputFilePath);
			
			if(file.exists()){
				file.delete();
			}
			
			BufferedWriter output = new BufferedWriter(new FileWriter(file));		
			
			for(int i=1; (tmpseq = Sequence.getOfSize(frequentSequences, i)).size() > 0 ; i++){
				
				System.out.println("");
				System.out.println("The number of length " + i + " sequential patterns is " + tmpseq.size());
				
				
				output.write("The number of length " + i + " sequential patterns is " + tmpseq.size());
				output.write("\n");
				
				for(Sequence seq : tmpseq){
					
					System.out.print("Pattern : <");
					output.write("Pattern : <");
					
					for(ItemSet itemset : seq.getSequence()){
						
						System.out.print("{");
						output.write("{");
						
						for(int j=0; j < itemset.getItemSet().size(); j++){
							
							if(j == itemset.getItemSet().size() - 1){
								System.out.print(itemset.getItemSet().get(j));
								output.write(itemset.getItemSet().get(j).toString());
							}
							else{
								System.out.print(itemset.getItemSet().get(j) + ",");
								output.write(itemset.getItemSet().get(j) + ",");
							}						
						}
						
						System.out.print("}");
						output.write("}");
					}	
					
					System.out.print("> Count : " + seq.getCount() + " \n");
					output.write("> Count : " + seq.getCount() + " \n");
				}	
	}
			output.close();
			System.out.println("");
		
		} catch (IOException e) {
			
			System.out.println("Error in Output file write Operation");
		}
	}
}
