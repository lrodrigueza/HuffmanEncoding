import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

public class FileFreqWordsIterator implements Iterator<String>{
	
	protected HashMap<String, Integer> wordDict = new HashMap<String, Integer>();
    private String inputFileName;
    
    // Variables necessary to Count Words
    private FileCharIterator wordIter; 
    private int nextChar;
    private String nextWord; 
    
    // Variables to process Counted Words
    protected Queue<WordEntry> wordQ = new LinkedList<WordEntry>();
    protected HashSet<String> wordSet = new HashSet<String>(); 
    
    // Variables necessary to Iterate through file
    private FileCharIterator charIter; 
    private String secNext;
    private String secWord;
    private int num;
    
	public FileFreqWordsIterator(String inputFileName, int n) {
            this.inputFileName = inputFileName;
            num = n;
            
            // Check file is valid
            File file = new File(inputFileName);
            long length = file.length();
            if(length==0){
            	throw new NullPointerException("Please input an non-empty file.");
            }
            
            // Count word frequencies in the input file
            wordIter = new FileCharIterator(inputFileName);
            countWordFreq();
            
            // Process words and add n items to hash set
            qWords();
            putSet(num);
            
            // Set up first call to next
            charIter = new FileCharIterator(inputFileName);
            secWord = "";
    }
	
	/**
	 * countWordFreq Helper method reads file iteratively word-by-word until the end.
	 * Processes each word by updating its frequency in wordDict hashmap.
	 */
	
	private void countWordFreq() {
		while(wordIter.hasNext()){
			// Loop until build another word as String
			nextWord = "";
			String nextnext = wordIter.next();
			//System.out.println("grabbing first letter of next word " + nextnext);
			
			while(!nextnext.equals(new String("00100000")) 
					&& !nextnext.equals(new String("00001010"))) {
				nextWord = nextWord + nextnext;
				if (!wordIter.hasNext()) {
						break;
				}
				nextnext = wordIter.next();
		      }
		
			// Process the built word
			//System.out.println("word to be processsed " + nextWord);
			if (nextWord.length() < 2) {
				continue; 
			}
			
			wordHash(nextWord);
		}
		
		
	}
	
	/**
	 * WordHash helper method updates word frequency in Hashmap
	 * @param word
	 */
	private void wordHash(String word) {
		if (wordDict.containsKey(word)) {
			updateFreq(word);
			return;
		}
		addWord(word);
		return;
	}

	 private void updateFreq(String c) {
         int oldValue = wordDict.get(c);
         wordDict.put(c, oldValue+1);
         return;
     } 
	
	 private void addWord(String key){
		 wordDict.put(key, 1);
            return;
     }
	 
	 /**
	  * qWords helper method copies everything from wordDict to
	  * a WordEntry Priority Queue, which gives highest priority to
	  * words with highest frequency. 
	  */
	 private void qWords(){
		 for(String i: wordDict.keySet()){
			 Integer value = wordDict.get(i);
			 wordQ.add(new WordEntry(i, value));
		 }
	 }
	 
	 /**
	  * putSet helper method to places top N=n words into 
	  * the wordSet hashSet 
	  * @param num (n frequent words)
	  */
	 private void putSet(int num){
		 // Check if n is larger than 
		 if (num > wordQ.size()) {
			 num = wordQ.size(); 
		 }
		 
		 for(int i=0; i<num; i++){
			WordEntry temp = wordQ.poll();
			wordSet.add(temp.key);
		 }
	 }
	
	 /**
	  * CHANGE THIS CLASS PRIVATE LATER
	  * @author cs61bl-me
	  *
	  */
	 public WordEntry makeEntry(String s, Integer i) {
		 return new WordEntry(s,i);
	 }
	 
	 protected static class WordEntry  implements Comparable<WordEntry>{
		 String key;
		 Integer value;
		 
		 public WordEntry(String s, Integer i){
			 key = s;
			 value = i;
		 }
		 
		 public int compareTo(WordEntry entry) {
				if(this.value<entry.value){
					return 1;
				}
				else if(this.value>entry.value){
					return -1;
				}
				return 0;
			}
		 
		 public String toString() {
			 String toRtn = "";
			 return toRtn = toRtn + "key is " + key + " value is " + value;
					 
		 }
		 
		 public boolean equals(Object o) {
			 WordEntry toComp = (WordEntry) o;
			 return toComp.key.equals(key) && toComp.value.equals(value); 
		 }
	 
	 }
	 
	 /**
	  * Main iterator methods
	  *
	  **/
	 
	public String next(){
		if(secWord.equals("00100000") || secWord.equals("00001010")){
			String toRtn = secWord;
			secWord = "";
			return toRtn;
		}
		
		if (secWord.isEmpty()) {
			// grab the next word
          secNext = charIter.next();
          while (!secNext.equals("00100000") &&
          		!secNext.equals("00001010") ) {
          	secWord = secWord + secNext;
          	
          	if (!charIter.hasNext()) {
          		secNext = "";
          		break;
          	}
          	// This is the space
          	secNext = charIter.next();
          	
          } 
			if (wordSet.contains(secWord)) {
				String toRtn = secWord;
				secWord = secNext;
				return toRtn;
			}
			
			secWord = secWord + secNext;
			//System.out.println("printing out " + secWord);
		}

			String toRtnSub = secWord.substring(0, 8);
			secWord = secWord.substring(8);
			return toRtnSub; 
	}

	public boolean hasNext(){
		return charIter.hasNext() || !secWord.isEmpty();
	}
	
	public void remove(){
        throw new UnsupportedOperationException(
                "FileFreqWordsIterator does not delete from files.");
	}
	/** Helper methods needed for testing
	 * 
	 */
	
	public String whatsSecWord() {
		return secWord; 
	}
}