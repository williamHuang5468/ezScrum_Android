package ntut.mobile.ezscrum.sort;

import java.util.Comparator;

public class ComparatorFactory{
	Comparator comparator;
	public Comparator createComparator(String sort, String value) {
		if(sort.toLowerCase().equals("des")){
			if (value.toLowerCase().equals("importance")) {
				comparator = new ImportanceDesComparator();
			}
			else if(value.toLowerCase().equals("value")){
				comparator = new ValueDesComparator();
			}
			else if(value.toLowerCase().equals("estimation")){
				comparator = new EstimationDesComparator();	
			}
			else{
				comparator = null;
			}
		}
		else if(sort.toLowerCase().equals("asc")){
			if (value.toLowerCase().equals("importance")){
				comparator = new ImportanceAscComparator();
			}
			else if(value.toLowerCase().equals("value")){
				comparator = new ValueAscComparator();
			}
			else if(value.toLowerCase().equals("estimation")){
				comparator = new EstimationAscComparator();	
			}
			else{
				comparator = null;
			}
		}
		else{
			comparator = null;
		}
		return comparator;
	}
}