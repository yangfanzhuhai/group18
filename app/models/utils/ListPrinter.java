package models.utils;

import java.util.List;

public class ListPrinter {
	
	public static String print(List<String> list){
		String s = "[";
		
		if (!list.isEmpty()) {
			int i;
			for (i = 0; i < list.size() - 1; i++) {
				s += "\"" + list.get(i) + "\",";
			}
			s += "\"" + list.get(i) + "\"";
		}
		
		return s += "]";
		
	}

}
