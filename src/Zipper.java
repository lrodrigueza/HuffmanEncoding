

import java.util.*;
import java.io.*;

public class Zipper {
    
    private String source;
    private String dest;
    private ArrayList<File> arrF = new ArrayList<File>();
    protected StringBuilder TOC = new StringBuilder();
    long totalCount = 0;
    protected StringBuilder contents = new StringBuilder();
    PriorityQueue<WordEntry> contentPath = new PriorityQueue<WordEntry>();
    
    /**
     * Main method checks first if correct number of arguments were passed into
     * the program. It then checks if the second parameter is a file that
     * exists. It then calls zip or unzip methods depending on which first
     * parameter was passed in via command line. 
     * @param args
     */
    public static void main (String[] args){
        String usage = "Usage: java Zipper zip|unzip source destination";
        System.out.println("number of args is " + args.length);
        if (args.length > 3) {
            System.err.println(usage);
            System.exit(0);
        }
        
        try{
            File fileArg = new File(args[1]);
            if (!fileArg.exists()) {
                System.err.println(usage);
                System.exit(0);
            }
            if (args[0].equals("zipper")) {         
                Zipper mainZip = new Zipper(args[1],args[2]);
                mainZip.zip();
            } else if (args[0].equals("unzipper")) {
                Zipper mainZip = new Zipper(args[1],args[2]);
                mainZip.unzip();
            }
        }
        catch(Exception e){
            System.err.println(e);
        }
        
    }
    /**
     * Constructor that initializes the instance variables source and destination.
     * It then makes a file object of the source file passed in. It checks
     * if source files exists, and then checks if source file is a directory.
     * It then adds the the file name to the TOC String. It then adds a /,-1/n
     * to the TOC string. It then calls arrF.addall(makeDir(fileArg))
     * @param source
     * @param dest
     */
    public Zipper(String source, String dest){
        this.source = source;
        this.dest = dest;
        
        File fileArg = new File(source);
      
        if (fileArg.exists()){
            // Catch Zip when source is directory
            if (fileArg.isDirectory()){
                this.arrF.add(fileArg);
                //this.arrF.addAll(makeDir(fileArg));
            // Catch UnZip when source if a file and dest is Directotry 
            } else {
                }
            }
        
        else{
            throw new IllegalArgumentException("Source file doesn't exist");
        }
    }
    
    public Zipper() {};
    
    /**
     * This method is called from main if arg[0] is zipper. It then calls
     * buildFile() it creates a new Array of all Files in the directory
     * recursively. It then calls writeFile to write the compressed files to 
     * the destination file. 
     */
    public void zip(){
        buildFile();
        buildTOC();
        writeFile(dest);
    }
    
    /**
     * This method is called from main if arg[0] is unzipper. It creates a 
     * new file object from the source file. It then creates a new File Reader
     * of the source file and then it creates a Buffered Reader. It calls
     * readTOC(br) which reads the Table of Contents. It then calls readFile with
     * the buffered reader and a count local variable to decompress the file to 
     * its target destination. 
     */
    public void unzip(){
        File f = new File(this.source);
        try{
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            readTOC(br);
            readFile();
            br.close();
            fr.close();
            } catch(IOException e){System.err.println("Something wrong with unzip!");}
    }
    
    /**
    //--------------------------UNZIP METHODS----------------------------------//
                                                                            **/
    private void UNZIPMETHODS(){}

    /**
     * This method is called by unzip(). It takes in as arguments a buffered reader 
     * a counter variable. It creates a StringBuilder object called currContent. 
     * If the priority queue contentPath is not empty, then we poll a word entry 
     * from the queue. It then grabs the value of the first polled Word Entry and sets
     * it startHere. It then peeks from the queue the second WordEntry and 
     * location in the source file, and sets it equal to endHere. It then recurses 
     * through the file until it has reached endHere. In the while loop it appends each
     * line of the file to currContent StringBuilder. After reaching endHere, it makes
     * a file with the firstFile key and the currContent StringBuilder. It then calls it
     * self recursively until the contentPath Priority Queue is empty. 
     * @param br
     * @param count
     */
    
    
    protected void readTOC(BufferedReader br){
        try{
            String line;
            while(!(line = br.readLine()).equals("")){
                String[] TOCline = line.split(",");
                String key = makePath(TOCline[0]);
                System.out.println("line is " + line);
                
                Integer value = Integer.parseInt(TOCline[1]);
                WordEntry inQ = new WordEntry(key, value);
                contentPath.add(inQ);
            }
        } catch(IOException e){}
        
        return;    
    }

    protected void readFile(){
        FileCharIterator charIter = new FileCharIterator(this.source);
        
        int count = 0;
        skipTOC(charIter);
        
        StringBuilder currContent =  new StringBuilder();

        while(charIter.hasNext()){
    
            currContent.setLength(0);
            WordEntry firstFile = contentPath.poll();
            WordEntry secondFile = contentPath.peek();
            Integer endHere = 0; 
            
            if(firstFile.value == -1) {
                File f = new File(this.dest+ "/" + firstFile.key);
                f.mkdirs();
                continue;
            }
            
            if (secondFile == null) {
                endHere = 999999;
            } else if(secondFile != null) {
                endHere = secondFile.value;
            }
            
            while (count < endHere) {
                if (!charIter.hasNext()){
                    break;                    
                }
                String toRtn = charIter.next();
                count++;
            }
            makeFile(firstFile.key, currContent);
        }
        return;
    }
    
    protected void skipTOC(FileCharIterator charIter){
        int count = 0;
        String newLine = "00001010";
        String lastInt = charIter.next();

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
    }
    
    /**
     * This method is called by readFile method. It takes as arguments a file name
     * and a String Builder which contains both the Code Map and the Encoded content
     * of a encoded Source file. It then makes a new file object feeding in the file
     * path name. It then makes file of itself in the folder its in. It opens a FileWriter
     * with the file as the parameter. It converts the StringBuilder to a string. It then
     * writes the entire String to the new file. It then creates a new file of F. 
     * It then calls decode with the file name as the argument. 
     * @param path
     * @param contents
     */
    protected void makeFile(String path, StringBuilder contents){
        System.out.println("contents in there " + contents);
        System.out.println("path is " + path);
        
            try {
        	File f = new File("tempZip");
            f.createNewFile();
            String restString = contents.toString();
            
            // append to the newFileName with the fromCodehelper in binary
            FileOutputHelper.writeBinStrToFile(restString, "tempZip");
           
            //calls the main method in HuffmanEncoding to decode each small file
            String action = "decode";
            String name = f.toString();
            String newname = this.dest + "/" + path;
            String[] stringArray = {action, name, newname};
            HuffmanEncoding.main(stringArray); 
            
            f.delete();
            } catch (IOException e) {}
            
    }

    // implement Priority Queue (key is path, value is byte)
    protected String makePath(String given){
        String path = "";
        for(int i=0;i<given.length();i++){
            if(given.charAt(i) == '/'){            
                path = path + File.separator;
            }
            else{ 
                path = path + given.charAt(i);
            }
        }
        return path;
    }
    

    /**
     * ------------------    WORDENTRY --------------------------
     */
    protected static class WordEntry implements Comparable<WordEntry>{
         String key;
         Integer value;
         
         public WordEntry(String s, Integer i){
             key = s;
             value = i;
         }
         
         public int compareTo(WordEntry entry) {
                if(this.value>entry.value){
                    return 1;
                }
                else if(this.value<entry.value){
                    return -1;
                }
                return 0;
            }
         
         public String toString(){
             String toReturn = "value: " + this.value + " key: " +key;
             return toReturn;
         }
     }
    
    /**
    //--------------------------ZIP METHODS----------------------------------//
                                                                            **/    
    private void ZIPMETHODS(){}
    
    /**
     * This method makes a new Array List of files, names newBuildArr. As long as
     * arrF is not empty it recurses through the source directory and processes
     * each file. To process each file, it calls processFile(f) with an instance of 
     * a file in arrF. After processing each file, it clears the array list and adds the 
     * compressed files to newBuildArr and recursively calls buildFile.
     * @return
     */
    // creates the table of contents and processes each file at the same time
    protected void buildFile(){
        ArrayList<File> newBuildArr = new ArrayList<File>();
        System.out.println("arrF looks like " + arrF);
        
        
        for (File f : this.arrF) {
        	if (!newBuildArr.contains(f)) {
        		newBuildArr.add(f);
        	}
        	
        	ArrayList<File> possibleChildren = flattenArrF(f);
        	for (File child : possibleChildren) {
        		if (newBuildArr.contains(child)) {
        			continue; 
        		}
        		newBuildArr.add(child);
        	}
        	System.out.println("flattened " + newBuildArr);
        }
        this.arrF=newBuildArr; 
        return;
    }
    
    
    protected ArrayList<File> flattenArrF(File f){
        ArrayList<File> toRtn = new ArrayList<File>();
        if(f.isDirectory()){
            File[] dirList = f.listFiles();
            //System.out.println("this is dir list when in directory " + dirList);
            if (dirList == null){
                return toRtn; //There are no files in the directory
            }
            else{
                for (File child : dirList){
                    System.out.println("-------------" + child.toString());             
                    toRtn.add(child);
                    toRtn.addAll(flattenArrF(child));
                }
            }
        }
        return toRtn;
}
    
    protected void buildTOC(){
    	for(File f: this.arrF){
    		TOC.append(f.getPath());
    		TOC.append(",");
    		if(f.isDirectory()){
                TOC.append(-1);
    		}
    		else{
    			TOC.append(totalCount);
    			System.out.println("file about to encode " + f);
    			if(f.length() == 0){
    				System.out.println("file is empty");
    			}
    			encodeFile(f);
    		}
    		TOC.append("\n");
    	}
    }
    
   
    //encodes and adds to the contents StringBuilder for you
    public void encodeFile(File f){
    	String action = "encode";
        String name = f.toString();
        String newname = "temp";
        String[] stringArray = {action, name, newname};
        HuffmanEncoding.main(stringArray);    
        File t = new File("temp");
        addContents(t);
        t.delete();
        return; 
    }
        
    
    //is a helper function for encode
    protected void addContents(File ff){
        try{
            FileReader fr = new FileReader(ff);
            BufferedReader br = new BufferedReader(fr);
            String thisLine;
            int previousLength = contents.length();
            while((thisLine = br.readLine()) != null){
                contents.append(thisLine);
                contents.append("\n");
                System.out.println(thisLine);
            }
            contents.append("\n");
            int crntLength = contents.length();
            totalCount = totalCount + (crntLength - previousLength);
            br.close(); fr.close();
        }
        catch(IOException e){}
        
    }
    
    public ArrayList<File> makeDir(File source){
        ArrayList<File> addThis = new ArrayList<File>();
        File[] dirList = source.listFiles();
        if (dirList == null){
            System.err.println("There are no files in the " + dirList + "directory.");
        }
        else{
            for (File child : source.listFiles()){
                addThis.add(child); // returns an array of file Objects contained in this directory
            }
        }
        return addThis;
    }
    
    //adds the table of contents and the rest of the words together
    public StringBuilder concatAll(){
        TOC.append("\n");
         System.out.println("contents are " + contents);
         TOC.append(contents);
         return TOC;
    }
    
    public void writeFile(String dest){
        try{
        	File f1 = new File(dest);
            if (!f1.exists()) {
                f1.createNewFile();
            }
            
            FileWriter fw1 = new FileWriter(f1);
            String everything = concatAll().toString();
            fw1.write(everything);
            fw1.close();
            
        }catch(IOException e){System.out.println("Something wrong with writeFile!");}
    }


    // Copy contents into stringCopy after TOC
/**
//--------------------------TESTING METHODS------------------------------//
                                                                        **/

    public ArrayList<File> getArrF(){
        return this.arrF;
    }
    
    public WordEntry getWordEntry(String s, Integer i) {
    	return new WordEntry(s,i);
    }

}