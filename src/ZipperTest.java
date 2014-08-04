import static org.junit.Assert.*;

import org.junit.Test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
public class ZipperTest {



	/** ------------------------ test constructor ---------------*/
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

	/** ----------------------- zip testing -------------------------- */
	
	
	/**
	 * This method tests that table of contents is Filled with 
	 * each file in the file to be Zipped
	 */
	@Test
	public void testBuildTOC() {
		System.out.println("****************** 3");
		Zipper zippity = new Zipper("testFolder", "whatsMyToC");
		zippity.buildFile();
		zippity.buildTOC();
		assertTrue(zippity.TOC.toString().contains("testFolder/level1-folder/"));
		assertTrue(zippity.TOC.toString().contains("testFolder/,-1"));
		assertTrue(zippity.TOC.toString().contains("estFolder/level1-folder/level2-folder"));
		assertTrue(zippity.TOC.toString().contains("testFolder/level1-1.txt/,0"));
		
	}
	
	/** ------------------------ main method test ----------------------*/	
	//@Test
	public void testMain1() {
		System.out.println("****************** main1");
		String action = "zipper";
        String name = "testFolder";
        String newname = "ZIPME.zipper";
        String[] stringArray = {action, name, newname};
        Zipper.main(stringArray);   
	}
		
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


	@Test
	public void testSkipTOC() {
		Zipper zip = new Zipper("testing.zipper", "testUnzip1");
		FileCharIterator f1 = new FileCharIterator("testing.zipper");
		zip.skipTOC(f1);
		assertFalse(f1.next().contains("testFolder/,-1"));
		assertFalse(f1.next().contains("testFolder/level1-folder/level2-folder/level3.txt,575"));
	}
	
	@Test
	public void testMakePath(){
		Zipper test1 = new Zipper("testFolder", "Compressed.txt");
		String input = "moo/pew/woo";
		String ding = test1.makePath(input);
		assertTrue(ding.equals(input));
	}
	
	@Test
	public void testReadTOC() {
		Zipper test1 = new Zipper("testing.zipper", "testUnzip");
		FileReader fr;
		try {
			fr = new FileReader("testing.zipper");
			BufferedReader br = new BufferedReader(fr);
			
			assertTrue(test1.contentPath.isEmpty());
			test1.readTOC(br);
			assertFalse(test1.contentPath.isEmpty());

		} catch (FileNotFoundException e) {
		}
	}

	@Test
	public void testBuildFile() {
		Zipper zip = new Zipper("testFolder", "Compressed.txt");
		zip.buildFile();
		ArrayList<File> s = zip.getArrF();
		System.out.println("######33 " + s);
		File f = new File("testFolder");
		assertTrue(s.contains(f));
		File f1 = new File("testFolder/level1-1.txt");
		assertTrue(s.contains(f1));
		File f2 = new File("testFolder/level1-folder/level2-1.txt");
		assertTrue(s.contains(f2));
		File f3 = new File("testFolder/level1-folder/level2-1.txt");
		
	}
//	
//	//@Test
//	public void testProcessFile() {
//		Zipper zip = new Zipper("testFolder", "Compressed.txt");
//		File f = new File("testFolder/level1-2.txt");
//		long oldCount = zip.totalCount;
//		zip.processFile(f);
//		assertTrue(zip.TOC.indexOf("testFolder/level1-2.txt,0") != -1);
//		File g = new File("testFolder/level1-folder/level2-folder");
//		zip.processFile(g);
//		assertTrue(zip.TOC.indexOf("testFolder/level1-folder/level2-folder") != -1);
	//}
	
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
	
	@Test
	public void testEncodeFile() {
		Zipper zip = new Zipper("testFolder", "Compressed.txt");
		File f = new File("testFolder/level1-1.txt");
		zip.encodeFile(f);
	}

	@Test
	public void testAddContents() {
		Zipper zip = new Zipper("testFolder", "Compressed.txt");
		File f = new File("testFolder/level1-1.txt");
		zip.addContents(f);
		assertTrue(zip.contents.indexOf("Two sturdy oaks") != -1);
	}

	@Test
	public void testMakeDir(){
		Zipper zip = new Zipper("testFolder", "Compressed.txt");
		ArrayList<File> pew = zip.makeDir(new File("testFolder/level1-folder/level2-folder"));
		assertTrue(pew.size() != 0);
	}
	
	@Test
	public void testConcatAll() {
		Zipper zip = new Zipper("testFolder/level1-folder", "Compressed.txt");
		zip.contents.append("c");
		int length = zip.TOC.length();
		assertTrue(zip.TOC.length() == 29);
		assertTrue(zip.contents.length() == 1);
		StringBuilder all = zip.concatAll();
		assertTrue(all.length() == length + zip.contents.length() + 1);
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
	
	//@Test
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

