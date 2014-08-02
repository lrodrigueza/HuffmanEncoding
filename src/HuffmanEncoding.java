/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lisa
 */
import java.io.File;
import java.util.PriorityQueue;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.io.*;

public class HuffmanEncoding {
    protected HashMap<String,Integer> dict;
    protected HuffmanTree huffTree; 
    protected HashMap<String,String> codeDict;
    protected PriorityQueue<TreeNode> queue;
    
    private String target;
    private String dest;
    private int num;
    
    /**
     * Main method sets up Huffman Encoding for Encode, Encode2 or Decode 
     */
    
    public static void main(String[] args){
        String usage = "Usage: java HuffmanEncoding decode|encode target destination";
        if (args.length > 4) {
            System.err.println(usage);
            System.exit(0);
        }
        
        long start, end, elapse, minutes, seconds;
    	start = System.currentTimeMillis();
        
    	try {
            if (args[0].equals("encode")) {
            	HuffmanEncoding myEncode = new HuffmanEncoding(args[1], args[2]);
            	myEncode.encode();
            } else if (args[0].equals("decode")) {
            	HuffmanEncoding myEncode = new HuffmanEncoding(args[1], args[2]);
            	myEncode.decode();
            } else if (args[0].equals("encode2")) {
            	HuffmanEncoding myEncode = new HuffmanEncoding(args[1], args[2], args[3]);
            	myEncode.encode2();
            } else {
                throw new IllegalArgumentException(usage);
            }
        }
        catch(Exception e){
            System.err.println(e);
        }
        
        end = System.currentTimeMillis();
        elapse = end - start;
        seconds = elapse/1000;
        minutes = seconds/60;
        System.out.println("encode complete at "+minutes+":"+(seconds%60));
        
    }

    /**
     * Constructor with for Encode, sets global references to target file and 
     * destination file. Sets num = 0, because n most frequent words won't be
     * considered in regualar encode. 
     */
    public HuffmanEncoding (String target, String dest) {
        this.target = target;
        this.dest = dest;
        this.num = 0;

        dict = new HashMap<String,Integer>();
        huffTree = new HuffmanTree();
        codeDict = new HashMap<String,String>();
        queue = new PriorityQueue<TreeNode>();
    }

    /**
     * Constructor for Encode 2. Same as above, only sets num=n to  passed in argument
     * for counting n most frequent words. 
     */
    public HuffmanEncoding (String target, String dest, String num) {
        this.target = target;
        this.dest = dest;
        this.num = Integer.parseInt(num);

        dict = new HashMap<String,Integer>();
        huffTree = new HuffmanTree();
        codeDict = new HashMap<String,String>();
        queue = new PriorityQueue<TreeNode>();
    }
    
    
    /**
     * ---------------------------ENCODE----------------------------------------------
     */
    
    /**
     * This method first calls buildHashMap(target) with the target file which builds the
     * Character Frequency Dictionary hashMap. Then it calls buildHuffTree() which builds a 
     * Huffman Tree from the Character Frequency Dictionary (named wordDict) previously 
     * constructed. Then it calls build CodeDict() to build a Code Dictionary (named Char
     * dict) from the previously constructed Huffman Tree (named huffTree). Finally, it calls
     * writeFileCompressed(destination) to compress the original file with the previously
     * constructed Code Dictionary. 
     */
    
    public void encode() {
        buildHashMap(this.target);
        buildHuffTree();        
        buildCodeDict();        
        writeFileCompressed(this.dest);
    }

    /**
     * This method is essentially the same as above, but instead of Building a Character
     * Frequency hashMap it builds a Word Frequency HashMap using the word iterator we built,
     * FileFreqWordsIterator. This iterator is called in buildHashMap2();
     */
    public void encode2(){   	
    	buildHashMap2(this.target);
    	buildHuffTree();
    	buildCodeDict();
        writeFileCompressed(this.dest);
    }
    
    /**
     * This method reads target file either char-by-char or word-by-word, depending on which
     * encode method is being called from command line. It translates each char/word to the
     * corresponding Huffman code from the Code Dictionary and appends it to one large String
     * of HuffmanCode. 
     */
    public StringBuilder encodeHelper(String fileName) throws IOException{ 
    	  StringBuilder output = new StringBuilder(100000);
          // char-by-char for encode
    	  if (num == 0 ) {
        	  FileCharIterator iter = new FileCharIterator(fileName);
              while(iter.hasNext()){
                  String nextChar = iter.next();
                  output.append(codeDict.get(nextChar));
              }
        	
              // word-by-word for encode2
          } else if (num > 0) {
        	  FileFreqWordsIterator iterWord = new FileFreqWordsIterator(fileName, num);
	            while(iterWord.hasNext()){
	     
	                String nextChar = iterWord.next();
	                output.append(codeDict.get(nextChar));
	            }
          }
        return (output.append(codeDict.get("EOF")));
        
    }
    /**
     * This method writes to the target File. It checks if destination file exists, if it doesn't
     * it creates a new destination file. It first writes the Code Dictionary to the destination
     * file. It separates the Code Dictionary from the rest by a newLine. It then grabs the 
     * encoded string from encodeHelper(target) by calling it with the target file name. 
     * It pads the returned encoded String with zeros until it is a multiple of 8. 
     * It finally writes the padded encoded String to the destination file. 
     * 
     */
    private void writeFileCompressed(String newFileName) {
        String oldFileName = this.target;
        File f = new File(newFileName);

        try{            
            if (!f.exists()) {
                f.createNewFile();
            }
            
            FileWriter fw = new FileWriter(f);

            // write the codeDict to file
            BufferedWriter bw = new BufferedWriter(fw);
            int count = 0;
            
            for (String key: codeDict.keySet()){
            	count++;
                String value = codeDict.get(key);
                bw.write(key + "," + value);
                bw.newLine();
                System.out.println(key+","+value);
            }
            
            System.out.println("CODEMAP LENGTH IS: " + count);

            // write the separation line
            bw.newLine();
            bw.close();
            
            //bw.write(encodeHelper(oldFileName));
            StringBuilder fromCodehelper = encodeHelper(oldFileName);
            System.out.println("glen is tired");
            // pad to the fromCodehelper to be multiple of 8
            if(fromCodehelper.length() % 8 != 0){
                
                int tobeAdded = fromCodehelper.length() % 8 ;
                int change = 8 - tobeAdded;
                for(int i=0; i<change; i++){
                    fromCodehelper = fromCodehelper.append("0");
                }
                System.out.println("current length " + fromCodehelper.length());
            }
            System.out.println("fromCodehelper:[" + fromCodehelper + "]");
            String fromCodehelperString = fromCodehelper.toString();
            // append to the newFileName with the fromCodehelper in binary
            FileOutputHelper.writeBinStrToFile(fromCodehelperString, newFileName);
            
        }
        catch(IOException e){
            return;
        }
    }
    
    /**
     * This encode helper method creates a Char Frequency HashMap for any given file as input
     * by reading char-by-char and adding each char and its frequency to the wordDict HashMap. 
     */
    public void buildHashMap(String inputFileName) {
        FileCharIterator iter = new FileCharIterator(inputFileName);
        while(iter.hasNext()){
            String toRtn = iter.next();
            if (dict.containsKey(toRtn)){
                    updateFreq(toRtn);
            }else{
                    addChar(toRtn);
            }
        }

    }
    /**
     * This encode2 helper method creates a Char Frequency HashMap for any given file as input
     * by reading word-by-word and adding each word and its frequency to the wordDict HashMap. 
     */
	public void buildHashMap2(String inputFileName){
		FileFreqWordsIterator iter = new FileFreqWordsIterator(inputFileName, num);
	    while(iter.hasNext()){
	        String toRtn = iter.next();
	        if (dict.containsKey(toRtn)){
	                updateFreq(toRtn);
	        }else{
	                addChar(toRtn);
	        }
	    }
	}
		
    
    /**
     * ---------------------------DECODE----------------------------------------------
     */
    
    /**
     * This method decodes a target file to a destination file. 
     * 
     */
    
    private void decode() throws IOException{ //converts all the characters to the codeMap
    	String finalString = "";
    	String oldFileName = this.target;
    	String newFileName = this.dest;
    	
    	try{
    		System.out.println("DECODING NOW");
    		File f1 = new File(oldFileName);
    		File f2 = new File(newFileName);
    		
    		if(!f2.exists()){
    			f2.createNewFile();
    			System.out.println("making new file");
    		}
    		
    		// Reader to read the old file
    		FileReader fr = new FileReader(f1);
    		BufferedReader br = new BufferedReader(fr);
    		

    		// Steal the Code Dictionary, close the br
    		stealCodeDict(br);
    		fr.close();
    			        
    		// Iterate past the Code Dictionary
    		FileCharIterator charIter = new FileCharIterator(oldFileName);
	        int count = 0;
	        String newLine = "00001010";
    		String lastInt = charIter.next();

    		// Skip past the dictionary
    		while (count < 2) {
    			if (lastInt.equals(newLine)) {
    				String nextInt = charIter.next();
    				if (lastInt.equals(nextInt)) {
    					count = 2;
    					continue;
    				}
    				
    				lastInt = nextInt;
    			}
    			lastInt = charIter.next();
    		}

	        
	        StringBuilder tempString = new StringBuilder(100000);

	        while(charIter.hasNext()){
    			String toCheck = charIter.next();
    			tempString.append(toCheck);
            }
	            		
	       finalString = decodeHelperGLEN(tempString).toString();
	       FileOutputHelper.writeBinStrToFile(finalString, newFileName);
    	}
    	catch(Exception e){
    		System.out.println("decoding isn't working");
    	}
  }

    /**
     * This helper method steals the Code Dictionary from an Huffman encoded file. It reads
     * line-by-line given the line contains a ',' character. It splits the line by the ',' 
     * char into a String array, lineContent. lineContent[0] contains the ascii value, and 
     * lineContent[1] contains the Huffman Code Value.  
     * It creates a new Code Dictionary, where the key is the Huffman Code and the value is 
     * the ascii value. 
     */
    public void stealCodeDict(BufferedReader br){
    	System.out.println("STEAL CODE DICT");
		try{
			String currentLine;
			while((currentLine = br.readLine()).contains(",")){
				String[] lineContent = currentLine.split("[,]");
				String ascii = lineContent[0];
				String huff = lineContent[1];
				codeDict.put(huff, ascii);
			}
		br.close();
		}
		catch(IOException e){
			System.out.println(e);
			System.out.println("out of bounds???");
			return;
		}	
    }
   
    
    /**
     * This helper method translates a Huffman encoded String back to ascii values by using
     * the stolen Code Dictionary from an encoded target file. 
     */
    public StringBuilder decodeHelperGLEN(StringBuilder tempString){
    	String toCheck = new String("");
    	StringBuilder output = new StringBuilder(100000);

	    	for(int i = 0; i<tempString.length(); i++){
	    		toCheck = toCheck + tempString.charAt(i);
	   
	    		if(codeDict.get(toCheck) != null){
	    			
	    			if (codeDict.get(toCheck).equals("EOF")) {
	    				break;
	    			}
	    			output.append(codeDict.get(toCheck));
	    			toCheck = new String("");
	    		}
	    	}
	    return output;
	}
    
    
    
    /**
     * Helper Method constructs HuffmanTree Map after a dictionary has been filled
     */
    public void buildHuffTree () {
        if (dict != null) {
            huffTree.build();
            return;
        }
        throw new IllegalArgumentException("dictionary is empty");
    }
    
    /**
     * Helper Method prints Huffman Tree 
     */
    public void printHuffTree() {
        huffTree.printHuff();
    }
    
    
    /**
     * Helper method builds Code Dictionary using the HuffmanTree.
     * Since this uses the built Huffman Tree structure, the Code Dictionary
     * is a method inside of TreeNode class. 
     */
    public void buildCodeDict() {
        if (huffTree != null) {
            huffTree.buildCodeDict();
            //hasCodeDict = true;
            return;
        }
        throw new IllegalArgumentException("must Build huffman tree first");
    }
    
    /**
     * Helper method prints out Code Dictionary. 
     */
    public void printCodeDict() {
        huffTree.printCode();
    }
    
    

    

           
 
        private TreeNode findNode (String str, TreeNode t){
            if (t.myItem.equals(str)){
                    return t;
            }
            if (((String) t.myItem).compareTo(str) < 0){
                    return findNode (str, t.myRight);
            } else {
                    return findNode (str, t.myLeft);
            }
        }

        
        public HashMap<String,Integer> getHashMap(){
            return dict;
        }
        
        public void addChar(String key){
            dict.put(key, 1);
;            return;
        }
    
        public Integer getFreq(String c){
                //TODO
            return null;
        }
    
        public void updateFreq(String c) {
            int oldValue = dict.get(c);
            dict.put(c, oldValue+1);
            return;
        } 
        
        public void printHash(){
        	System.out.println("PRINTHASH METHOD");
            String gotThisKey;
            int freq;
            Iterator<String> hashIter = dict.keySet().iterator();
            while (hashIter.hasNext()){
                gotThisKey = hashIter.next();
                freq = dict.get(gotThisKey);
                System.out.println("the key is " + gotThisKey + ", frequency: " + freq);
            }
            return;
        }
        
        
    /**
     * Huffman Tree Methods
     * @author cs61bl-me
     *
     * @param <T>
     */
        
    protected class HuffmanTree {
        Integer currentMin=0;
        String minKey;
        TreeNode huffRoot;
 
        /**
         * magic: 
         * queue already created
         * 
         * for loop to go through entire hashmap and add it to the queue
         * 
         * 
         * grab two min values from queue, deltes from queue
         * 
         * merge two min values into one node, and connect via triangle add back to queue
         * 
         * repeat until the size is equal to 1
         * 
         * now entire tree is built 

         */
        
        
        public void build() {
            // Building Queue
            for (String key : dict.keySet()) {
                int val = dict.get(key);
                
                queue.add(new TreeNode (key, val));
                
            }
            
            queue.add(new TreeNode( "EOF", 1));
            
            // Grabbing min value
            
            while (queue.size() > 1) {
                TreeNode key1 = queue.poll();
                TreeNode key2 = queue.poll();
                
                
                int combinedVal = key1.myValue + key2.myValue;
                
                TreeNode mergeRoot = new TreeNode(combinedVal, key1, key2);
                
                queue.add(mergeRoot);      
            }
            
            huffRoot = queue.poll();
            
        }
        
/**    
    public String searchTree(FileReader fr) throws Exception{
    	try{
	    	if (huffRoot != null) {
	    		return searchTree(fr, huffRoot);
	    	}
    	}
    	catch(Exception e){
    		throw new IllegalArgumentException();
    	}
    	return null;
    }
        
	private String searchTree(FileReader fr, TreeNode t) throws IOException{ //
		try{
			if (t.myLeft == null & t.myRight == null) { 
	    		return t.myItem; 
	    	}	    
	    	
	    	Integer nxtChar = fr.read();
		    	if (nxtChar == 0) {
		    		return searchTree(fr, t.myLeft);		
		    	} 
    	}
    	catch(IOException e){
    		System.out.println(e);
    	}
    	
    	return searchTree(fr, t.myRight);
    }
**/

        public void printHuff() {
            if (huffRoot != null) {
            huffRoot.print(0);
            }
        }

        //-----------------------------------------ENCODE---------------------------
        
        public void buildCodeDict(){  //some sort of iteration that keeps encoding for the size of the hashmap
            //for each key in the hashmap
            findInTree(huffRoot, ""); 
           
        }
            
        //tree traversal, find the node that matches the key
        private void findInTree(TreeNode tNode, String newCode){
           
            if ( tNode.myRight != null){
                findInTree( tNode.myRight, newCode + "1");
            }

            if(tNode.myLeft != null){
                findInTree( tNode.myLeft, newCode + "0");    
            }
            //newCode.concat("X");
            if (tNode.myItem != null) {
                assignCode(tNode.myItem, newCode);
            }
            return;
                
        }

        private void assignCode(String ascii, String newCode){
            //System.out.println("assignCode: [code,char]=["+newCode+","+keyChar+"]");
            codeDict.put(ascii, newCode);
        }
        
        
         
        public void printCodeQueue() {
            for (String key : codeDict.keySet()) {
                System.out.println(key + " maps to " + codeDict.get(key));
            }
        }
        
        public void printCode() {
            if (codeDict != null) {
                printCodeQueue();
                return; 
            }
        }
 
    }
    
    private class TreeNode implements Comparable<TreeNode> {
        public String myItem;
        public int myValue; 
        public TreeNode myLeft;
        public TreeNode myRight;

        public TreeNode() {
            myItem = null;
            myLeft = myRight = null;
        }
        
        public TreeNode(String item) {
            myItem = item;
            myLeft = myRight = null;
        }
        
        
        public TreeNode(int value, TreeNode left, TreeNode right) {
            myValue = value; 
            myItem = null;
            myLeft = left;
            myRight = right;
            ;
        }
        
        public TreeNode(String key, int value) {
            myItem = key;
            myValue = value; 
            myLeft = myRight = null;
        }

        public TreeNode(String item, TreeNode left, TreeNode right) {
            myItem = item;
            myLeft = left;
            myRight = right;
        }
 
        private static final String indent1 = "    ";

        private void print(int indent) {
            // YOUR CODE HERE
            // println myRight
            if (myRight != null) {
                myRight.print(indent+1);
            }
            println(myItem, indent);
            // YOUR CODE HERE
            // println myLeft
            if (myLeft != null) {
                myLeft.print(indent+1);
            }
        }
 
        private void println(Object obj, int indent) {
            for (int k = 0; k < indent; k++) {
                System.out.print(indent1);
            }
            System.out.println ("TREENODE CLASS, PRINTLN" + obj);
    
        }

        @Override
        public int compareTo(TreeNode o) {
            if (this.myValue == o.myValue) {
                return 0;
            } else if (this.myValue < o.myValue) {
                return -1;
            }
            return 1; 
            
        }

    }
}