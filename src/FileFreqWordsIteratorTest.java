import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;


public class FileFreqWordsIteratorTest {

	/**
	 * Tests constructor of fileFreqWordsd iterator, makes sure
	 * secWord has been initialized to empty
	 */
	@Test 
	public void testConstructor() {
		FileFreqWordsIterator it = new FileFreqWordsIterator("WordTest.txt", 20);
		String secWord = it.whatsSecWord();
		assertEquals(secWord, "");
		assertTrue(it.hasNext());
	}
	
	
	
	
	/**
	 * Tests reading and counting all words in a given file. 
	 * Tests if each word is accounted for in the wordDict hashMap
	 */
	@Test
	public void testCountWordFreq() {
		FileFreqWordsIterator it = new FileFreqWordsIterator("WordTest.txt", 20);
		assertTrue(it.wordDict.containsKey("01101011011010010111010001110100011001010110111001110011"));
		assertTrue(it.wordDict.get("01101011011010010111010001110100011001010110111001110011").equals(new Integer(1)));
		assertTrue(it.wordDict.containsKey("01101101011001010110111101110111"));
		assertTrue(it.wordDict.get("01101101011001010110111101110111").equals(new Integer(2)));
		assertTrue(it.wordDict.containsKey("011001110111001001110010"));
		assertTrue(it.wordDict.get("011001110111001001110010").equals(new Integer(3)));
	}

	/**
	 * Tests being able to transfer all words and their frequencies into a
	 * priority queue, sorted from largest frequency to smallest.
	 * 
	 * Test also tests the nested class wordEntry used to sort words and their 
	 * frequencies in the priority queue, wordQ
	 */	
	@Test
	public void testQWords() {
		FileFreqWordsIterator it = new FileFreqWordsIterator("WordTest.txt", 1);
		FileFreqWordsIterator.WordEntry kittenEntry = new FileFreqWordsIterator.WordEntry("01101011011010010111010001110100011001010110111001110011", 1); 

		FileFreqWordsIterator.WordEntry meowEntry = new FileFreqWordsIterator.WordEntry("01101101011001010110111101110111", 2); 
		FileFreqWordsIterator.WordEntry grrEntry = new FileFreqWordsIterator.WordEntry("011001110111001001110010", 3); 
	
		assertTrue(it.wordQ.contains(kittenEntry));
		assertTrue(it.wordQ.contains(meowEntry));
		assertFalse(it.wordQ.contains(grrEntry));

		
	}
	
	/**
	 * Tests if final number of n most frequent words in the wordSet
	 * here n = 2
	 */
	@Test
	public void testPutSet1(){
		FileFreqWordsIterator it = new FileFreqWordsIterator("WordTest.txt", 2);
		assertTrue(it.wordSet.size() == 2); 
		assertTrue(it.wordSet.contains("011001110111001001110010"));
		assertTrue(it.wordSet.contains("01101101011001010110111101110111"));
		assertFalse(it.wordSet.contains("01101011011010010111010001110100011001010110111001110011"));

		
	}
	
	/**
	 * Tests if final number of n most frequent words in the wordSet
	 * here n = 0.
	 */
	@Test
	public void testPutSet2(){
		FileFreqWordsIterator it = new FileFreqWordsIterator("WordTest.txt", 0);
		assertTrue(it.wordSet.size() == 0); 
		
	}
	
	/**
	 * Tests if final number of n most frequent words in the wordSet
	 * Edge case where n passed in is larger than the number of unique
	 * words in the file.
	 * Doesn't crash, rather sets n = number of unique words in faile
	 */
	@Test
	public void testPutSet3(){
		FileFreqWordsIterator it = new FileFreqWordsIterator("WordTest.txt", 3000);
		assertTrue(it.wordSet.size() == 3); 
		assertTrue(it.wordSet.contains("011001110111001001110010"));
		assertTrue(it.wordSet.contains("01101101011001010110111101110111"));
		assertTrue(it.wordSet.contains("01101011011010010111010001110100011001010110111001110011"));
	}
	
	/**
	 * Tests the hasNext() method before first call ever to to Next
	 */
	@Test
	public void testHastNext1() {
		FileFreqWordsIterator it1 = new FileFreqWordsIterator("WordTest.txt", 3);
		assertTrue(it1.hasNext());
	}
	
	/**
	 * Test hasNext after several calls to next, should return false at EOF
	 */
	
	//@Test
	public void testHasNext2() {
		FileFreqWordsIterator it = new FileFreqWordsIterator("WordTest.txt", 1);
		//kittens
		assertEquals(it.next(), "01101011");
		assertEquals(it.next(), "01101001");
		assertEquals(it.next(), "01110100");
		assertEquals(it.next(), "01110100");
		assertEquals(it.next(), "01100101");
		assertEquals(it.next(), "01101110");
		assertEquals(it.next(), "01110011");
		//space
		assertEquals(it.next(), "00100000");
		// meow
		assertEquals(it.next(), "01101101");
		assertEquals(it.next(), "01100101");
		assertEquals(it.next(), "01101111");
		assertEquals(it.next(), "01110111");
		// space
		assertEquals(it.next(), "00100000");
		// meow
		assertEquals(it.next(), "01101101");
		assertEquals(it.next(), "01100101");
		assertEquals(it.next(), "01101111");
		assertEquals(it.next(), "01110111");
		//space
		assertEquals(it.next(), "00100000");
		// grr
		assertEquals(it.next(), "011001110111001001110010");
		//space
		assertEquals(it.next(), "00100000");
		// grr
		assertEquals(it.next(), "011001110111001001110010");
		//space
		assertEquals(it.next(), "00100000");
		// grr
		assertEquals(it.next(), "011001110111001001110010");
		// newline

		System.out.println(it.hasNext());
		//System.out.println(it.next());
		//System.out.println(it.next());


	}
	
	/** 
	 * Tests Next can return new line characters!!
	 */
	@Test
	public void testHasNext4() {
		FileFreqWordsIterator it = new FileFreqWordsIterator("WordTestLine", 3);
		//kittens
		assertEquals(it.next(), "01101011011010010111010001110100011001010110111001110011");
		//space
		assertEquals(it.next(), "00100000");
		// meow
		assertEquals(it.next(), "01101101011001010110111101110111");
		// space
		assertEquals(it.next(), "00100000");
		// meow
		assertEquals(it.next(), "01101101011001010110111101110111");
		//space
		assertEquals(it.next(), "00100000");
		// grr
		assertEquals(it.next(), "011001110111001001110010");
		//space
		assertEquals(it.next(), "00100000");
		// grr
		assertEquals(it.next(), "011001110111001001110010");
		//space
		assertEquals(it.next(), "00100000");
		// grr
		assertEquals(it.next(), "011001110111001001110010");
		// newline
		assertEquals(it.next(), "00001010");		
		assertFalse(it.hasNext());


	}
	
	
	
	/**
	 * testing spaces and shit
	 */
	@Test
	public void testHasNext3() {
		FileFreqWordsIterator it = new FileFreqWordsIterator("WordTest.txt", 1);
		assertEquals(it.next(), "01101011");
		assertEquals(it.next(), "01101001");
		assertEquals(it.next(), "01110100");
		assertEquals(it.next(), "01110100");
		assertEquals(it.next(), "01100101");
		assertEquals(it.next(), "01101110");
		assertEquals(it.next(), "01110011");
		assertEquals(it.next(), "00100000");
		


	}

	
	/**
	 * Tests next, if if first word is a frequent word returns it as is, not 
	 * byte-by-byte
	 */
	
	@Test
	public void testNext1() {
		FileFreqWordsIterator it = new FileFreqWordsIterator("WordTest.txt", 3);
		assertEquals(it.next(), "01101011011010010111010001110100011001010110111001110011");
	}
	
	/**
	 * Tests next, if first word isn't a frequent word, then it should return it 
	 * byte-by-byte
	 */
	@Test
	public void testNext2() {
		FileFreqWordsIterator it = new FileFreqWordsIterator("WordTest.txt", 1);
		assertEquals(it.next(), "01101011");
		assertEquals(it.next(), "01101001");
		assertEquals(it.next(), "01110100");
		assertEquals(it.next(), "01110100");
		assertEquals(it.next(), "01100101");
		assertEquals(it.next(), "01101110");
		assertEquals(it.next(), "01110011");
	}
	

	//@Test(expected=NullPointerException.class)
	public void testHasNext() {
		FileFreqWordsIterator it1 = new FileFreqWordsIterator("WordTest.txt", 2);
		File file = new File("WordTest.txt");
        long length = file.length();
		for(int i = 0; i<length-2; i++){
			System.out.println(it1.next());
			it1.next();
			assertTrue(it1.hasNext());
		}
		it1.next();
		assertFalse(it1.hasNext());
		
		FileFreqWordsIterator it2 = new FileFreqWordsIterator("Empty.txt", 2);
		assertFalse(it2.hasNext());
	}
	
	@Test
	public void testKleido() {
		FileFreqWordsIterator it = new FileFreqWordsIterator("Kaleidoscope.txt", 20);
		assertFalse(it.wordDict.containsKey(""));

	}
	

}
