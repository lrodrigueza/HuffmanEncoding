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
		Zipper zipTest = new Zipper("FileToUnzip.txt", "UnZippedFolder");
		assertTrue(zipTest.TOC.toString().contains("FileToUnzip"));
	}

	/** ----------------------- zip testing -------------------------- */
	
	
	/**
	 * This method tests that table of contents is Filled with 
	 * each file in the file to be Zipped
	 */
	//@Test
	public void testBuildTOC() {
		Zipper zippity = new Zipper("testFolder", "whatsMyToC");
		zippity.buildFile();
		zippity.buildTOC();
		System.out.println(zippity.TOC);
		assertTrue(zippity.TOC.toString().contains("testFolder/level1-folder/"));
		assertTrue(zippity.TOC.toString().contains("testFolder/,-1"));
		assertTrue(zippity.TOC.toString().contains("estFolder/level1-folder/level2-folder"));
		assertTrue(zippity.TOC.toString().contains("testFolder/level1-1.txt/,0"));
		
	}
	
	/** ------------------------ main method tests ----------------------*/	
	//@Test
	public void testMain1() {
		String action = "zipper";
        String name = "testFolder";
        String newname = "ZIPME.zipper";
        String[] stringArray = {action, name, newname};
        Zipper.main(stringArray);   
	}
		
	//@Test
	public void testMain3() {
		String action = "zipper";
        String name = "testDir";
        String newname = "testDir.zipper";
        String[] stringArray = {action, name, newname};
        Zipper.main(stringArray);   
	}
	
	//@Test
	public void testMain4() {
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
	

	//@Test
	public void testZipLayers() {
		System.out.println("****************** zip4");
		Zipper zippity = new Zipper("testFolder2", "lisaRequest");
		zippity.zip();
	}
	
	@Test
	public void testUnzipLayers() {
		System.out.println("****************** zip4");
		Zipper zippity = new Zipper("lisaRequest", "food");
		zippity.unzip();
	}

	//@Test
	public void testMakePath(){
		Zipper test1 = new Zipper("testFolder", "Compressed.txt");
		String input = "moo/pew/woo";
		String ding = test1.makePath(input);
		assertTrue(ding.equals(input));
	}
	
	//@Test
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

	//@Test
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
	
	//@Test
	public void testFlatten() {
		Zipper zip = new Zipper("testFolder", "Compressed.txt");
		File f = new File("testFolder/");
		ArrayList<File> flat = zip.flattenArrF(f);
		File g = new File("testFolder/level1-folder/level2-folder");
		assertTrue(!flat.isEmpty());
		assertTrue(flat.contains(g));
		File h = new File("testFolder/level1-folder/level2-folder");
		assertTrue(flat.contains(h));		
		File i = new File("testFolder/level1-folder/level2-folder/level3-2.txt");
		assertTrue(flat.contains(i));
		File j = new File("testFolder/level1-folder/level2-1.txt");
		assertTrue(flat.contains(j));
	}

	//@Test
	public void testAddContents() {
		Zipper zip = new Zipper("testFolder", "Compressed.txt");
		File f = new File("testFolder/level1-1.txt");
		assertTrue(zip.contents.length() == 0); 
		zip.addContents(f);
		assertTrue(zip.contents.length() >10);
	}
	
	//@Test
	public void testMakeDir(){
		Zipper zip = new Zipper("testFolder", "Compressed.txt");
		ArrayList<File> pew = zip.makeDir(new File("testFolder/level1-folder/level2-folder"));
		assertTrue(pew.size() != 0);
	}

	
	//@Test
	public void testEncodeFile() {
		Zipper zip = new Zipper("testFolder", "Compressed.txt");
		File f = new File("testFolder/level1-1.txt");
		zip.encodeFile(f);
		assertFalse(f.length()==0);
	}

	//@Test
	public void testConcatAll() {
		Zipper zip = new Zipper("testFolder/level1-folder", "Compressed.txt");
		zip.TOC.append("YO");
		zip.TOC.append("c");
		zip.TOC.append("b");
		zip.contents.append("1");
		StringBuilder s = zip.concatAll();
		assertTrue(s.toString().equals("YOcb\n1"));
		
	}

	//@Test
	public void testTotalCount(){
		Zipper zippity = new Zipper("testing.zipper", "yourmom");
		assertTrue(zippity.totalCount==0);

	}
	
	/** --------------------------- general testers -------------------- */
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
	public void testUnzip1(){
		System.out.println("****************** unzip1");
		Zipper zipTest = new Zipper("Compressed.txt", "testUnzip1");
		zipTest.unzip();
	}
	
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

}

