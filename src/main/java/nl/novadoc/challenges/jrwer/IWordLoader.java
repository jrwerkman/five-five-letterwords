package nl.novadoc.challenges.jrwer;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IWordLoader {
	void start() throws Exception;
	
	public default String print(Set<Integer> results, Map<Integer, List<String>> words) {
		if(results.isEmpty())
			return "";
		
		StringBuilder sb = new StringBuilder();
		
		for(Integer r : results) {
			List<String> wordList = words.get(r);
			
			for(int i=0; i<wordList.size(); i++) {
				sb.append(wordList.get(i));
				
				if(i<wordList.size() - 1)
					sb.append('|');
			}
			
			sb.append(' ');
		}
		
		return sb.toString().substring(0, sb.length() - 1);
	}
}
