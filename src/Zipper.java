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
    
    public static void main (String[] args){
    	String usage = "Usage: java Zipper zip|unzip source destination";
    	File fileArg = new File(args[0]);
    	if (!fileArg.exists()) {
            System.err.println(usage);
            System.exit(0);
        }

        try{
        	Zipper mainZip = new Zipper(args[0],args[1]);
            if (fileArg.isDirectory()) {
                mainZip.zip();
            } else{
            	mainZip.unzip();
            }
        }
        catch(Exception e){
            System.err.println(e);
        }
    	
    }
    
	public Zipper(String source, String dest){
		//new File(feedDir).mkdirs();
		this.dest = dest;
		this.source = source;
		File fileArg = new File(source);
		if (fileArg.exists()){
			if (fileArg.isDirectory()){
				TOC.append(fileArg.toString());
				TOC.append("/,"); TOC.append(-1); TOC.append("\n");
				this.arrF.addAll(makeDir(fileArg));
			}
		}
		else{
			throw new IllegalArgumentException();
		}
	}
	
	public void zip(){
		buildFile(); 
		writeFile(dest);
	}
	
	public void unzip(){
		File f = new File(this.source);
		try{
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			readTOC(br);
			int count = 0;
			readFile(br, count);}
		catch(IOException e){}
	}

	
	/**
	//--------------------------UNZIP METHODS----------------------------------//
	 																       **/
	private void UNZIPMETHODS(){}

	public void readFile(BufferedReader br, int count){
		StringBuilder currContent =  new StringBuilder();
		if (contentPath.size()!=0){
			WordEntry fileThing = contentPath.poll();
			Integer lookHere = fileThing.value;
			while(count != lookHere){
				try{
				if(lookHere == -1){
					count = count + br.readLine().length();
					readFile(br, count);
				}
				currContent.append(br.read());}
				catch(IOException e){};
				count++;
			}
			makeFile(fileThing.key, currContent);
			readFile(br, count);
		}
		return;
	}

	public void readTOC(BufferedReader br){
		StringBuilder sb = new StringBuilder();
		try{
			String line;
			while(!(line = br.readLine()).equals("/nl/nl")){
				String[] TOCline = line.split(",");
				String key = makePath(TOCline[0]);
				Integer value = Integer.parseInt(TOCline[1]);
				WordEntry inQ = new WordEntry(key, value);
				contentPath.add(inQ);
			}
		} catch(IOException e){}
		return;	
	}
	
	// implement PQ (key is path, value is byte)
	// dequeue key, and put into path
	
	// given is going to be contentPath[0]
	public String makePath(String given){
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
	
	public void makeFile(String path, StringBuilder contents){
		File f = new File(path);
		try{
			f.mkdirs();
			FileWriter fw = new FileWriter(f);
			String fileContent = contents.toString();
			fw.write(fileContent);
			f.createNewFile();
			decodeFile(path);
		}
		catch(IOException e){}
	}
	
	public void decodeFile(String fileName){
		HuffmanEncoding unzipHuff = new HuffmanEncoding(fileName, dest);
		try{
			unzipHuff.decode();
		} catch(IOException e){};
	}
		
	/**
	 * ------------------	WORDENTRY --------------------------
	 */
	public class WordEntry implements Comparable<WordEntry>{
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
	 
	 }
	
	
	/**
	//--------------------------ZIP METHODS----------------------------------//
	 																       **/	
	private void ZIPMETHODS(){}
	
	// creates the table of contents and processes each file at the same time
	public StringBuilder buildFile(){
		ArrayList<File> newBuildArr = new ArrayList<File>();
		if(this.arrF.isEmpty()){
			return TOC; 
		}
		else{
			for (File f: this.arrF){
				newBuildArr = processFile(f);
			}
			this.arrF.clear();
		}
		this.arrF.addAll(newBuildArr);
		return buildFile();
	}
	
	//updates the totalCount, keeping track of the byte at which file is written
	//calls encode and stores in the contents thing for each file
	public ArrayList<File> processFile(File f){
			ArrayList<File> newBuildArr = new ArrayList<File>();
			TOC.append(f.getPath());
			TOC.append(",");
			if(f.isDirectory()){
				TOC.append(-1);
				newBuildArr = makeDir(f);
			}
			else{
				encodeFile(f);
				String fName = "decoded".concat(f.toString());
				File f2 = new File(fName);
				System.out.println("f" + f.length());
				this.totalCount=totalCount+f.length();
				TOC.append(totalCount);
			}
			TOC.append("\n");
			return newBuildArr;
	}
	
	//encodes and adds to the contents StringBuilder for you
	public void encodeFile(File f){
		System.out.println("WHAT IS F?   "+ f.toString());
//		HuffmanEncoding huffEncoder = new HuffmanEncoding(f.toString(), "decoded".concat(f.toString()));
		addContents(f);
		HuffmanEncoding huffEncoder = new HuffmanEncoding(f.toString(), dest);
		huffEncoder.encode(); 
		return;
	}
	
	//is a helper function for encode
	public void addContents(File ff){
		try{
			FileReader fr = new FileReader(ff);
			BufferedReader br = new BufferedReader(fr);
			String thisLine; 
			while((thisLine = br.readLine()) != null){
				contents.append(thisLine);
			}
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
		return TOC.append(contents);
	}
	
	public void writeFile(String dest){
		File f = new File(dest);
		try{
			FileWriter fw = new FileWriter(f);
			BufferedWriter br = new BufferedWriter(fw);
			String everything = concatAll().toString();
			FileOutputHelper.writeBinStrToFile(everything, dest);
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