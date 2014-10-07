package ms_gsp_algo;

import java.util.Comparator;

/**
 * 
 * This is the smallest individual unit in the transaction database.
 *
 */
public class Item implements Comparator<Item>{
	
	Integer ITEM;
	Float MIS=0.0f;
	Float ActualMIS=0.0f;
	Integer support=0;
	
	public void setsupport(Integer num){
		this.support = num;
	}
	
	public Integer getsupport(){
		return this.support;
	}
	
	public void setActualMIS(Float num){
		this.ActualMIS = num;
	}
	
	public Float getActualMIS(){
		return this.ActualMIS;
	}
	
	public void setItem(Integer num){
		this.ITEM = num;
	}
	
	public Integer getItem(){
		return this.ITEM;
	}
	
	public void setMIS(Float minsup){
		this.MIS=minsup;
	}
	
	public Float getMIS(){
		return this.MIS;
	}
	
	
	public int compare(Item i1, Item i2){ 
		// This function sorts the items according 
		//to the value of their respective MIS
		return (i1.getMIS()).compareTo(i2.getMIS());
	}
}
