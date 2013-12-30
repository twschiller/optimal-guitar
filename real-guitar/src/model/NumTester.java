package model;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Test;

/**
 * Tests for verifying the system to assign a number to each chord.
 * @author Todd Schiller
 *
 */
public class NumTester {

	@Test
	public void myTest(){
		
		Integer a[] = {null,null,7,null,null,null };
		Chord c = new Chord(a);
		
		Long cnum = c.calcNum();
		System.out.println(cnum);
		Integer test[][] = { {null,null,0,0,0,0},
				{null,null,null,null,null,null},
				{null,null,2,null,null,null},
				{null,null,null,null,null,null}};
		
		HandForm tf = new HandForm(test);
		
		HashSet<Long> ll = tf.calcNums();
		
		System.out.println(ll);
		
		assertTrue(ll.contains(cnum));	
		
	}
	
	@Test
	public void myTestB(){
		
		Integer a[] = {null,null,7,null,null,null };
		Chord c = new Chord(a);
		
		Long cnum = c.calcNum();
		
		System.out.println(cnum);
		
		Integer test[][] = { {null,null,null,null,null,null},
				{null,null,null,null,null,null},
				{null,null,2,null,null,null},
				{null,null,null,null,null,null}};
		
		HandForm tf = new HandForm(test);
		HashSet<Long> ll = tf.calcNums();
		
		System.out.println(ll);
		
		assertTrue(ll.contains(cnum));	
	}
	
	
	@Test
	public void myTestC(){
		
		Integer a[] = {null,3,2,null,1,null };
		Chord c = new Chord(a);
		
		Long cnum = c.calcNum();
		
		System.out.println(cnum);
		
		Integer test[][] = { {null,null,null,null,0,null},
				{null,null,1,null,null,null},
				{null,2,null,null,null,null},
				{null,null,null,null,null,null}};
		
		HandForm tf = new HandForm(test);
		
		HashSet<Long> ll = tf.calcNums();
		
		System.out.println(ll);
		
		assertTrue(ll.contains(cnum));	
		
	}
	
}
