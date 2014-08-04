import static org.junit.Assert.*;

import org.junit.Test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
public class ZipperTest {

	/** ------------------------ Main Method Test ----------------------------*/
	//@Test
	public void testMain1() {
		System.out.println("****************** main1");

		String action = "zipper";
        String name = "testFolder";
        String newname = "testFolderFromMain.zipper";
        String[] stringArray = {action, name, newname};
        Zipper.main(stringArray);   
	}
	
	//@Test
	public void testMain2() {
		System.out.println("****************** main2");

		String action = "unzipper";
        String name = "testFolderFromMain.zipper";
        String newname = "testFolderUnzippedFromMain";
        String[] stringArray = {action, name, newname};
        Zipper.main(stringArray);   
	}
	
	/** ------------------------ Main Method Test ----------------------------*/
	//@Test
	public void testMain3() {
		System.out.println("****************** main3");

		String action = "zipper";
        String name = "testDir";
        String newname = "testDir.zipper";
        String[] stringArray = {action, name, newname};
        Zipper.main(stringArray);   
	}
	
	//@Test
	public void testMain4() {
		System.out.println("****************** 2");
		Zipper zipTest = new Zipper("FileToUnzip.txt", "UnZippedFolder");
		assertTrue(zipTest.TOC.toString().contains("FileToUnzip"));
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
	//@Test
	public void testConstructor1() {
		System.out.println("****************** constructor1");
		Zipper zipTest = new Zipper("testFolder", "testCompress");
		StringBuilder toComp = new StringBuilder();
		toComp.append("testFolder");
		toComp.append("/,");
		toComp.append(-1);
		toComp.append("\n");
		assertTrue(zipTest.TOC.toString().contains(toComp.toString()));
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
		assertTrue(zipTest.TOC.toString().contains("FileToUnzip"));
	}

	/** ----------------------- ZIP TESTING -------------------------- */
	
	
	//@Test
	public void testZip() {
		System.out.println("****************** ziptest1");
		Zipper zippity = new Zipper("testDir", "testDir123.zipper");
		zippity.zip();
//		System.out.println("toc now " + zippity.TOC);
//		zippity.buildFile();
//		zippity.buildTOC();
//		System.out.println("toc after " + zippity.TOC);
	}

	
	/**
	 * This method tests that table of contents is Filled with 
	 * each file in the file to be Zipped
	 */
	//@Test
	public void testBuildTOC() {
		System.out.println("****************** 3");
		Zipper zippity = new Zipper("testFolder", "meowy111.zipper");
		zippity.buildFile();
		zippity.buildTOC();
		System.out.println(zippity.TOC);
		//assertTrue(zippity.TOC.toString().contains("testFolder/level1-folder/level2-1.txt"));
	}
	
	//@Test
	public void testZIP() {
		System.out.println("****************** zip3");
		Zipper zippity = new Zipper("testFolder", "meowy699.zipper");
		zippity.zip();
	}
	
	//@Test
	public void testUNZIPMEOWY() {
		System.out.println("****************** zip3");
		Zipper zippity = new Zipper("meowy699.zipper", "unzippedMEOY");
		zippity.unzip();
	}
	
	@Test
	public void testHARD() {
		System.out.println("****************** zip4");
		Zipper zippity = new Zipper("testFolder2", "lisaRequest");
		zippity.zip();
	}
	/**
	 * 
	 */
	//@Test
	public void testWriteFile() {
		System.out.println("****************** 4");
		Zipper zippity = new Zipper("testFolder", "Comp1.zipper");
		zippity.buildFile();
		zippity.writeFile("Compressed.txt");
	}
	
	//@Test
	public void testCompress1File() {
		System.out.println("****************** 5");
		Zipper zip = new Zipper("testDir", "testDir1.zipper");
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

	
	/** -------------------------- Unzip Testing ---------------------------- **/
	
	/**
	 * test checks if the over all unzip method works with a Compressed.txt file 
	 * that has empty directories, empty files and embedded files
	 */
	//@Test
	public void testUnzip1(){
		System.out.println("****************** unzip1");
		Zipper zipTest = new Zipper("Compressed.txt", "testUnzip1");
		zipTest.unzip();
	}
	/**
	 * test checks if unzip method works with a smaller directory that only
	 * contains one nested file with a small number of bytes. 
	 */
	//@Test
	public void testUnzip2(){
		System.out.println("****************** unzip2");
		Zipper zipTest = new Zipper("testDir.zipper", "testUnzip2");
		zipTest.unzip(); 
	}
	
	//@Test
	public void testUnzip3() {
		System.out.println("****************** unzip3");
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

