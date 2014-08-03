import static org.junit.Assert.*;

import org.junit.Test;

import java.io.*;
import java.util.*;

public class ZipperTest {

	/**
	 * This tests the ZIpper constructor when Zipping.
	 * First tests that the the source File exists, then appends
	 * the File name etc to the Table of Contents, TOC. It then
	 * adds to the arrayList of Files, arrF, the sourceFile
	 * Initializes source
	 * Initializes destination. Since testFolder is a directory,
	 * it should append root File name to TOC and add rootFile name
	 * to arrF, the array of Files
	 */
	@Test
	public void testConstructor1() {
		System.out.println("****************** 1");
		Zipper zipTest = new Zipper("testFolder", "testCompress");
		StringBuilder toComp = new StringBuilder();
		toComp.append("testFolder");
		toComp.append("/,");
		toComp.append(-1);
		toComp.append("\n");
		assertEquals(zipTest.TOC.toString(), toComp.toString());
		File testFile = new File("testFolder");
		ArrayList<File> testArray = new ArrayList<File>();
		ArrayList<File> afterMakeDir = zipTest.makeDir(testFile);
		testArray.addAll(afterMakeDir);
		assertEquals(zipTest.getArrF(), testArray);
	}
	
	/** Tests what happens when you call unzipper and first
	 * 	argument is a file and the second is the destination.
	 *  It doesn't build a table of contents because the source
	 *  file is encrypted. The TOC is stolen after the constructor. 
	 */
	@Test
	public void testConstructor2() {
		System.out.println("****************** 2");
		Zipper zipTest = new Zipper("FileToUnzip.txt", "UnZippedFolder");
		assertEquals("", zipTest.TOC.toString());
	}

	/**
	 * ----------------------- ZIP TESTING --------------------------
	 */
	
	/**
	 * This method tests that table of contents is Filled with 
	 * each file in the file to be Zipped
	 */
	@Test
	public void testBuildTOC() {
		System.out.println("****************** 3");
		Zipper zippity = new Zipper("testFolder", "meowy");
		zippity.buildFile();
		assertTrue(zippity.TOC.toString().contains("testFolder/level1-folder/level2-1.txt"));
	}
	
	/**
	 * 
	 */
	@Test
	public void testWriteFile() {
		System.out.println("****************** 4");
		Zipper zippity = new Zipper("testFolder", "Compressed");
		zippity.buildFile();
		zippity.writeFile("Compressed.txt");
	}
	
	@Test
	public void testCompress1File() {
		System.out.println("****************** 5");
		Zipper zip = new Zipper("testDir", "testDir.zipper");
		zip.buildFile();
		zip.writeFile("testDir.zipper");
	}
	
	
	//@Test
	public void testArrF(){
		File blahFile = new File("blah");
		Zipper zippity = new Zipper("blah", "derp");
		System.out.println(zippity.getArrF());
	}

	
	public void testTotalCount(){
		// updated in processFile
	}
	
//    @Test
//    public void firstMainTest() {
//    	System.out.println("testing main**********************1");
//      String action = "encode";
//    	String name = "SmallFile.txt";
//    	String newname = "Random.txt";
//      String[] stringArray = {action, name, newname};
//      HuffmanEncoding.main(stringArray);	
//    } 
	
/** ----------------------- TESTING UNZIP ----------------- **/
	
	@Test
	public void testUnzip0() {
		System.out.println("****************** unzip0");
		Zipper zipTest = new Zipper("Compressed.txt", "testUnzip0");
		zipTest.unzip();
		

	}

	//@Test
	public void testUnzip(){
		System.out.println("****************** unzip1");
		Zipper zipTest = new Zipper("Compressed.txt", "testUnzip1");
		zipTest.unzip();
//		FileWriter fw = new FileWriter(f);
//		BufferedReader br = new BufferedReader(fw);
//		zipTest.readTOC(br);
//		assertTrue(inQ.get(0).isClass().equals(WordEntry));
	}
	
	//@Test
	public void testUnzip1(){
		System.out.println("****************** unzip2");
		Zipper zipTest = new Zipper("testDir.zipper", "testDir.unzipped");
		
		try {
			File f = new File("testDir.zipper");
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			zipTest.readTOC(br);
			
			assertFalse(zipTest.contentPath.isEmpty());
			
			zipTest.readFile();
			

		} catch (IOException e) {}
			
	}
	
	//@Test
	public void testMakeFile() {
		System.out.println("****************** makeFile1");
		Zipper zipTest = new Zipper("testDir.zipper", "testDir.unzipped");
		StringBuilder sB = new StringBuilder(); 
		FileCharIterator iter = new FileCharIterator("WordEncodeTest.txt");
		
		boolean lastWasNewLine = false; 
		while(iter.hasNext()) {
			
			String toRtn = iter.next();
			if (toRtn.equals("00001010") && lastWasNewLine) {
				break; 
			} else if (toRtn.equals("00001010")) {
				lastWasNewLine = true; 
			} else if (!toRtn.equals("00001010")) {
				lastWasNewLine = false; 
			}
			
			System.out.println("in char its " + toRtn);
			
			int charCode = Integer.parseInt(toRtn, 2);
			String str = new Character((char) charCode).toString();
			System.out.println("in reg its " + str);

			sB.append(str);
		}
		
		sB.append("\n");
		zipTest.makeFile("testMakeFile.temporary", sB);
		
		FileCharIterator iter2 = new FileCharIterator("WordEncodeTest.txt");
		int count = 0;
        String newLine = "00001010";
		String lastInt = iter2.next();

		
		
		// Skip past the dictionary
		while (count < 2) {
			if (lastInt.equals(newLine)) {
				String nextInt = iter2.next();
				if (lastInt.equals(nextInt)) {
					count = 2;
					continue;
				}
				
				lastInt = nextInt;
			}
			lastInt = iter2.next();
		}
		
		StringBuilder restOfStuff = new StringBuilder(); 
		while(iter2.hasNext()) {
			String toRtn = iter2.next();
			restOfStuff.append(toRtn);
		}
		
		if (restOfStuff.length() % 8 != 0){
            
            int tobeAdded = restOfStuff.length() % 8 ;
            int change = 8 - tobeAdded;
            for(int i=0; i<change; i++){
                restOfStuff = restOfStuff.append("0");
            }
            //System.out.println("current length " + fromCodehelper.length());
        }
       // System.out.println("fromCodehelper:[" + fromCodehelper + "]");
        String restString = restOfStuff.toString();
        // append to the newFileName with the fromCodehelper in binary
        FileOutputHelper.writeBinStrToFile(restString, "tempzo");
		
		
		
		
	}
	
	
	//@Test
	public void testFixShit(){
		System.out.println("****************** fixShit");
		Zipper zipTest = new Zipper("testDir.zipper", "testDir.unzipped");
		StringBuilder sB = new StringBuilder();
		
		FileCharIterator it = new FileCharIterator("WordEncodeTest.txt");
		while(it.hasNext()) {
			sB.append(it.next());
		}
		zipTest.fixShit(sB);
		
	}
	
	//@Test
	public void flattenedToBeauty() {
		System.out.println("****************** flattBeauty");
		Zipper zipTest = new Zipper();
		StringBuilder sB = new StringBuilder();
		
		FileCharIterator it = new FileCharIterator("WordEncodeTest.txt");
		while(it.hasNext()) {
			sB.append(it.next());
		}
		
		//String flatFile = zipTest.fixShit(sB);
		
		zipTest.fixShit(sB);
		zipTest.flatToBeauty("flattened.temporary");
		
	}
	
	//@Test 
	public void testDecodeFile() {
		System.out.println("****************** decodeFile");
		Zipper zipTest = new Zipper("testDir.zipper", "testDir.unzipped");
		File f = new File("tempzo");
		zipTest.decodeFile(f,"tempzoDecoded");
		
	}
	
	//@Test
	public void testDecodeTempzo() {
		String action = "decode";
        String name = "tempzo";
        String newname = "tempzoDecoded";
        String[] stringArray = {action, name, newname};
        HuffmanEncoding.main(stringArray);   
	}
	
	//@Test
	public void testStealCodeDict() {
		System.out.println("****************** stealTest");

		HuffmanEncoding huff = new HuffmanEncoding("tempzo", "tempzo.unzipped");
		try {
			File f = new File("tempzo");
			FileReader fr = new FileReader("tempzo");
			BufferedReader br = new BufferedReader(fr);
			
			huff.stealCodeDict(br);
			
			assertTrue(huff.codeDict.isEmpty());
			
		} catch (IOException e) {}
	}
	
	//@Test 
	public void testInfiniteWHile() {
		System.out.println("****************** WHileTest");
		
		// Iterate past the Code Dictionary
		FileCharIterator charIter = new FileCharIterator("tempzo");
        int count = 0;
        String newLine = "00001010";
		String lastInt = charIter.next();

		// Skip past the dictionary
		while (count < 2) {
			if (lastInt.equals(newLine)) {
				String nextInt = charIter.next();
				if (lastInt.equals(nextInt)) {
					count = 2;
					System.out.println("foudn two newlines");
					continue;
				}
				
				lastInt = nextInt;
			}
			lastInt = charIter.next();
		}
		
		
	}
	
	@Test
	public void testReadFile4() {
		System.out.println("****************** unzip4");
		Zipper zipTest = new Zipper("testDir.zipper", "testUnzip1");
		zipTest.unzip(); 
	}
	
}



/**
 * Copy all contents into a stringCopy. 
 * Delete TOC from stringCopy
 * then start Iteration with first byte of String. 
 * 
 * To fix "Length of outputStr must be multiple of 8!":
 * * multiply the byte count in TOC by 8
 * **Translates the bit to byte, 
 * **which is what you loop through in stringCopy
*/