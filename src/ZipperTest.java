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
		Zipper zippity = new Zipper("testFolder", "Compressed.txt");
		zippity.buildFile();
		assertTrue(zippity.TOC.toString().contains("testFolder/level1-folder/level2-1.txt"));
	}
	
	/**
	 * 
	 */
	@Test
	public void testWriteFile() {
		System.out.println("****************** 4");
		Zipper zippity = new Zipper("testFolder", "Compressed.txt");
		zippity.buildFile();
		zippity.writeFile("Compressed.txt");
	}
	
	
	//@Test
	public void testARRF(){
		File blahFile = new File("blah");
		Zipper zippity = new Zipper("blah", "derp");
		System.out.println(zippity.getArrF());
	}
	
	public void testContentPath(){
		//tests the construction of the priority queue
		// carried out in readFile and readTOC
		
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
	
	//@Test
	public void testZip(){
		Zipper zippity = new Zipper("blah", "SmallFile.txt");
		zippity.zip();
	}
	
	public void testUnZip(){
		Zipper zippity = new Zipper("blah", "SmallFile.txt");
		String dest = "SmallFile.txt";
		zippity.zip(); // void has new File f
		zippity.unzip();
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