import java.util.*;
import java.io.*;

public class Zipper {
    
    private String source;
    private String dest;
    private ArrayList<File> arrF = new ArrayList<File>();
    protected StringBuilder TOC = new StringBuilder();
    long totalCount = 0;
    long sourceSize;
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
        this.dest = dest;
        this.source = source;
        File fileArg = new File(source);
        sourceSize = fileArg.length();
        if (fileArg.exists()){
            // Catch Zip when source is directory
            if (fileArg.isDirectory()){
                TOC.append(fileArg.toString());
                TOC.append("/,"); TOC.append(-1); TOC.append("\n");
                this.arrF.addAll(makeDir(fileArg));
            // Catch UnZip when source if a file and dest is Directotry 
            } else {
                File d = new File(this.dest);
                d.mkdir();
                }
            }
        
        else{
           // throw new IllegalArgumentException();
        }
    }

    
    /**
     * This method is called from main if arg[0] is zipper. It then calls
     * buildFile() it creates a new Array of all Files in the directory
     * recursively. It then calls writeFile to write the compressed files to 
     * the destination file. 
     */
    public void zip(){
        buildFile(); 
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
            int count = 0;
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            readTOC(br);
            readFile();
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

    protected void readFile(){
        FileCharIterator charIter = new FileCharIterator(this.source);
        
        int count = 0;
        skipTOC(charIter);
        
        StringBuilder currContent =  new StringBuilder();


        while(charIter.hasNext()){
    
            
            currContent.setLength(0);
            
            //System.out.println("currCont is " + currContent);
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
                String Char; 
                Char = charIter.next(); 
                currContent.append(Char);
                //System.out.println(Char);          
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
        try{
            File f = new File("tempzo");
            f.createNewFile();
            FileWriter fw = new FileWriter(f);
            fw.write(contents.toString());
            
//            System.out.println("contents length is " + contents.toString().length());
//            if(contents.length() % 8 != 0){
//                
//                int tobeAdded = contents.length() % 8 ;
//                int change = 8 - tobeAdded;
//                for(int i=0; i<change; i++){
//                    contents = contents.append("0");
//                }
//                System.out.println("mod better be zero " + contents.length()%8);
//            }
           // System.out.println("fromCodehelper:[" + fromCodehelper + "]");
            
            //String contentsFixed = contents.toString();
            
            
            //FileOutputHelper.writeBinStrToFile(contentsFixed, "temp");

//            
//            String action = "decode";
//            String name = f.toString();
//            String newname = this.dest + "/" + path;
//            String[] stringArray = {action, name, newname};
//            HuffmanEncoding.main(stringArray);  
            
            //f.delete();
            
            //fw.write("i'm so happy!!!!!");
            fw.close();
            decodeFile(f, path);
            
        }
        catch(IOException e){System.err.println("makeFile caught an error");}
    }
    
    /**
     * This method is called by makeFile. It takes in as argument the filename 
     * of a file that has been created but is still encoded. It creates an instance 
     * of the HuffmanEncoding class and passes in as arguments the encoded fileName and
     * the destination file. 
     */
    protected void decodeFile(File f, String destination){
        destination =  this.dest + "/" + destination;
        //File d = new File(destination);
        //d.mkdirs();
        
        String action = "decode";
        String name = f.toString();
        String newname = destination;
        String[] stringArray = {action, name, newname};
        HuffmanEncoding.main(stringArray);    
        
        //f.delete(); 
        
    }

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
            
//            for (WordEntry w : contentPath) {
//                System.out.println(w);
//            }
            
        } catch(IOException e){}
        return;    
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
    protected StringBuilder buildFile(){
        ArrayList<File> newBuildArr = new ArrayList<File>();
        if(this.arrF.isEmpty()){
            return TOC; 
        }
        else{
            for (File f: this.arrF){
                newBuildArr.addAll(processFile(f));
            }
            this.arrF.clear();
        }
        this.arrF.addAll(newBuildArr);
        System.out.println(this.arrF.toString());
        return buildFile();
    }
    
    /**
     * 
     */
    //updates the totalCount, keeping track of the byte at which file is written
    //calls encode and stores in the contents thing for each file
    protected ArrayList<File> processFile(File f){
            ArrayList<File> newBuildArr = new ArrayList<File>();
            TOC.append(f.getPath());
            TOC.append(",");
            if(f.isDirectory()){
                TOC.append(-1);
                File[] dirList = f.listFiles();
                if (dirList == null){
                    System.err.println("There are no files in the " + dirList + "directory.");
                }
                else{
                    for (File child : dirList){
                        System.out.println("-------------" + child.toString());
                        newBuildArr.add(child); // returns an array of file Objects contained in this directory
                    }
                }
            }
            else{
                File t = encodeFile(f);
                TOC.append(totalCount);
                if (f.length() == 0){
                    this.totalCount = totalCount + 1;
                }
                else{
                    this.totalCount=totalCount+t.length();}
                
            }
            TOC.append("\n");
            return newBuildArr;
    }
    
    //encodes and adds to the contents StringBuilder for you
    public File encodeFile(File f){
        String action = "encode";
        String name = f.toString();
        String newname = "temp";
        String[] stringArray = {action, name, newname};
        HuffmanEncoding.main(stringArray);    
        
        File t = new File("temp");
        
        addContents(t);
        
        //t.delete(); 
        
        //HuffmanEncoding huffEncoder = new HuffmanEncoding(f.toString(), dest);
        //huffEncoder.encode(); 
        return t; 
    }
        
    
    //is a helper function for encode
    protected void addContents(File ff){
        try{
            FileReader fr = new FileReader(ff);
            BufferedReader br = new BufferedReader(fr);
            String thisLine; 
            while((thisLine = br.readLine()) != null){
                contents.append(thisLine);
                contents.append("\n");
            }
            contents.append("\n");
            br.close(); fr.close();
            //System.out.println("contents so far " +contents);
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
                System.out.println("-------------" + child.toString());
                addThis.add(child); // returns an array of file Objects contained in this directory
            }
        }
        return addThis;
    }
    
    //adds the table of contents and the rest of the words together
    public StringBuilder concatAll(){
        TOC.append("\n");
        return TOC.append(contents);
    }
    
    public void writeFile(String dest){
        File f = new File(dest);
        try{
            
            if (!f.exists()) {
                f.createNewFile();
            }
            
            FileWriter fw = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(fw);
            String everything = concatAll().toString();
            //System.out.println("this is everything " + everything);
            
            fw.write(everything);
            
            bw.close();
            fw.close();
            
            //FileOutputHelper.writeBinStrToFile(everything, dest);
        }catch(IOException e){}
    }


    // Copy contents into stringCopy after TOC
/**
//--------------------------TESTING METHODS------------------------------//
                                                                        **/

    public ArrayList<File> getArrF(){
        return this.arrF;
    }

}