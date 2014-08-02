
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Lisa
 */
public class HuffmanEncodingTest{
    
    public HuffmanEncodingTest() {
    }
    
    
    /**
     * Tests our main method which will be implemented from commandLine
     */
    @Test
    public void firstMainTest() {
    	System.out.println("testing main**********************1");
        String action = "encode";
    	String name = "TheAdventuresOfSherlockHolmes.txt";
    	String newname = "reallyweirdglen.txt";
        String[] stringArray = {action, name, newname};
        HuffmanEncoding.main(stringArray);	
    } 
    
    
   // @Test
    public void secondMainTest() {
    	System.out.println("testing main**********************2");
        String action = "decode";
    	String name = "RandomTextination.txt";
    	String newname = "RandomDecodenation.txt";
        String[] stringArray = {action, name, newname};
        HuffmanEncoding.main(stringArray);	
    } 
    
    
    
    //@Test
    public void hangInEncode() {
    	System.out.println("testing main**********************1");
        String action = "encode";
    	String name = "HangInThere.jpg";
    	String newname = "HangInThereEncoded.txt";
        String[] stringArray = {action, name, newname};
        HuffmanEncoding.main(stringArray);	
    } 
    
    
    //@Test
    public void hangInDecode() {
    	System.out.println("testing main**********************2");
        String action = "decode";
    	String name = "HangInThere.jpg.huffman";
    	String newname = "HangInThere2.jpg";
        String[] stringArray = {action, name, newname};
        HuffmanEncoding.main(stringArray);	
    }
    
    //@Test
    public void sherLockDecode() {
    	System.out.println("testing main**********************2");
        String action = "decode";
    	String name = "TheAdventuresOfSherlockHolmes.txt.huffman";
    	String newname = "SherlockDecoded.txt";
        String[] stringArray = {action, name, newname};
        HuffmanEncoding.main(stringArray);	
    }
    
    
    
    @Test
    public void testJoey() {
    	System.out.println("testing main**********************2");
        String action = "decode";
    	String name = "101.txt.huffman";
    	String newname = "101decoded.txt";
        String[] stringArray = {action, name, newname};
        HuffmanEncoding.main(stringArray);	
    }
    
    
    
    
    
    
    
    
    
    
    /**
     * Test of buildHashMap method, of class HuffmanEncoding.
     */
   //@Test
    public void testBuildHashMap() {
        System.out.println("testing buildHashMap");
        String inputFileName = "SmallFile.txt";
    	String newname = "Random.txt";
        HuffmanEncoding instance = new HuffmanEncoding(inputFileName, newname);
        instance.buildHashMap(inputFileName);
        instance.printHash();
    }
  
    /**
     * This tests our findMinKey method that grabs a minimun frequency of the hash map
     * and returns the String of the corresponding entry.
     * This method, when called successively will return next minimum frequency and keep 
     * decreasing the size of the hashmap. 
     */
    //@Test
    public void testFindMinKey(){
    	System.out.println("testing findMindKey");
    	String name = "SmallFile.txt";
    	String newname = "Random.txt";
        HuffmanEncoding instance = new HuffmanEncoding(name, newname);
        instance.buildHashMap(name);
	  }
    
   // @Test
    public void testBuildHuffmanTree(){
    	System.out.println("buildHuffmanTree");
        String inputFileName = "SmallFile.txt";
    	String newname = "Random.txt";
        HuffmanEncoding instance = new HuffmanEncoding(inputFileName, newname);
        instance.buildHashMap(inputFileName);
        instance.buildHuffTree();
        instance.printHuffTree();
        
    }

    /**
     * Test of addChar method, of class HuffmanEncoding.
     */
    
    public void testAddChar() {
        System.out.println("addChar");
        String key = "";
        String inputFileName = "SmallFile.txt";
    	String newname = "Random.txt";
        HuffmanEncoding instance = new HuffmanEncoding(inputFileName, newname);
        instance.addChar(key);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFreq method, of class HuffmanEncoding.
     */
    
    public void testGetFreq() {
        System.out.println("getFreq");
        String c = "";
        String inputFileName = "SmallFile.txt";
    	String newname = "Random.txt";
        HuffmanEncoding instance = new HuffmanEncoding(inputFileName, newname);
        Integer expResult = null;
        Integer result = instance.getFreq(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateFreq method, of class HuffmanEncoding.
    */
    
    public void testUpdateFreq() {
        System.out.println("updateFreq");
        String c = "";
        String inputFileName = "SmallFile.txt";
    	String newname = "Random.txt";
        HuffmanEncoding instance = new HuffmanEncoding(inputFileName, newname);
        instance.updateFreq(c);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of printHash method, of class HuffmanEncoding.
     */
    
    public void testPrintHash() {
        System.out.println("printHash");
        String inputFileName = "SmallFile.txt";
    	String newname = "Random.txt";
        HuffmanEncoding instance = new HuffmanEncoding(inputFileName, newname);
        instance.printHash();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class HuffmanEncoding.
     */
//   @Test
    public void testCodeDict() {
	   System.out.println("testing testCodeDict");
       String inputFileName = "SmallFile.txt";
       String newname = "Random.txt";
       HuffmanEncoding instance = new HuffmanEncoding(inputFileName, newname);
       instance.buildHashMap(inputFileName);
       instance.buildHuffTree();
       instance.buildCodeDict();
       instance.printCodeDict();
   }
    
    //@Test
    public void testDeCode1() {
    	System.out.println("testing testCodeDict");
        String inputFileName = "SmallFile.txt";
        String newname = "Random.txt";
        HuffmanEncoding instance = new HuffmanEncoding(inputFileName, newname);
        instance.buildHashMap(inputFileName);
        instance.buildHuffTree();
        instance.buildCodeDict();
        
    }
    
    @Test
    public void testDeCode2() {
    	System.out.println("testing testCodeDict");
        String inputFileName = "SmallFile.txt";
        String newname = "Random.txt";
        HuffmanEncoding instance = new HuffmanEncoding(inputFileName, newname);
    }
    
    

}