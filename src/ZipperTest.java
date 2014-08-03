import static org.junit.Assert.*;

import org.junit.Test;

import java.io.*;
import java.util.*;

public class ZipperTest {

	/** ------------------------ Main Method Test ----------------------------*/
	@Test
	public void testMain1() {
		System.out.println("****************** main1");
		

	}
	
	
	
	
	
	
	/**
	 * This tests the Zipper constructor when Zipping.
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
		System.out.println("****************** constructor1");
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
	 *  It doesn't build a stable of contents because the source
	 *  file is encrypted. The TOC is stolen after the constructor. 
	 */
	//@Test
	public void testConstructor2() {
		System.out.println("****************** 2");
		Zipper zipTest = new Zipper("FileToUnzip.txt", "UnZippedFolder");
		assertEquals("", zipTest.TOC.toString());
	}

	/** ----------------------- ZIP TESTING -------------------------- */
	
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
	
	/** -------------------------- Unzip Testing ---------------------------- **/
	
	/**
	 * test checks if the over all unzip method works with a Compressed.txt file 
	 * that has empty directories, empty files and embedded files
	 */
	@Test
	public void testUnzip1(){
		System.out.println("****************** unzip1");
		Zipper zipTest = new Zipper("Compressed.txt", "testUnzip1");
		zipTest.unzip();
	}
	/**
	 * test checks if unzip method works with a smaller directory that only
	 * contains one nested file with a small number of bytes. 
	 */
	@Test
	public void testUnzip2(){
		System.out.println("****************** unzip2");
		Zipper zipTest = new Zipper("testDir.zipper", "testUnzip2");
		zipTest.unzip(); 
	}
	
	@Test
	public void testUnzip3() {
		System.out.println("****************** unzip2");
		Zipper zipTest = new Zipper("testDir.zipper", "testDestination3");
		zipTest.unzip(); 
		}
	
	
	/**
	 * Test checks how to Read the Table of Contents from a given compressed
	 * file as input
	 */
	//@Test
	public void testReadTOC2() {
		System.out.println("****************** readTOC2");
		Zipper zipTest = new Zipper("testDir.zipper", "testUnzip1");
		File f = new File("testDir.zipper");
		try {
			File fi = new File("testDir.zipper");
			FileReader fr = new FileReader(fi);
			BufferedReader bf = new BufferedReader(fr);
			assertTrue(zipTest.contentPath.isEmpty());
			zipTest.readTOC(bf);
			Zipper.WordEntry wdEntry1 = zipTest.getWordEntry("testDir/", -1);
			Zipper.WordEntry wdEntry2 = zipTest.getWordEntry("testDir/file1", 0);
			assertTrue(zipTest.contentPath.contains(wdEntry1));
			assertTrue(zipTest.contentPath.contains(wdEntry2));
			
		} catch (FileNotFoundException e) {	}
		
	}
	
	/**
	 * tests another thing
	 */
	
	
}
