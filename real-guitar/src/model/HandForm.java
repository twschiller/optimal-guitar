package model;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Class representing a hand formation
 * @author Todd Schiller
 */
public class HandForm {
	public static final int MAX_SPAN = 4;
	public static final int NUM_FINGERS = 4;
	
	private final int span;
	private final Integer highFret;
	//private final Integer lowFret;
	private final Integer high[] = new Integer[GuitarModel.NUM_STRINGS];
	
	private final Integer form[][] = new Integer[NUM_FINGERS][GuitarModel.NUM_STRINGS];
		
	public Integer[][] getForm() {
		return form;
	}

	public HandForm(Integer[][] form) {
		super();
		if (form.length != NUM_FINGERS){
			throw new IllegalArgumentException("Hand form must describe position of each finger");
		}
	
		for (int i = 0; i < NUM_FINGERS; i++){
			if (form[i].length != GuitarModel.NUM_STRINGS){
				throw new IllegalArgumentException("Hand form must describe position of each finger");
			}
			
			System.arraycopy(form[i], 0, this.form[i], 0, GuitarModel.NUM_STRINGS);
		}
		span = calcSpan();
		highFret = calcHighestFret();
		//lowFret = calcLowestFret();
		
		for (int s = 0; s < GuitarModel.NUM_STRINGS; s++){
			high[s] = calcHighFretOnString(s);
		}
		
		
	}
	
	public HandForm(HandForm right) {
		for (int i = 0; i < NUM_FINGERS; i++){
			System.arraycopy(right.form[i], 0, this.form[i], 0, GuitarModel.NUM_STRINGS);
		}
		span = calcSpan();
		highFret = calcHighestFret();
		//lowFret = calcLowestFret();
		
		for (int s = 0; s < GuitarModel.NUM_STRINGS; s++){
			high[s] = calcHighFretOnString(s);
		}
		
			
	}

	/**
	 * Get the finger span of the hand form
	 * @return the hand span required by the hand form
	 */
	public int getSpan(){
		return span;
	}
	
	private int calcSpan(){
		int max = 0;
		for (Integer far[] : form){
			for (Integer v : far){
				if (v != null && v > max){
					max = v;
				}
			}
		}
		return max;
	}

	/**
	 * Get the highest fret relative to the base fret in the hand form
	 * @return the highest fret relative to the base fret
	 */
	public Integer highestFret(){
		return highFret;
	}
	
	private Integer calcHighestFret(){
		Integer max = null;
		
		for (int s=0;s<GuitarModel.NUM_STRINGS;s++){
			Integer hf = calcHighFretOnString(s);
			if (max == null){
				max = hf;
			}else if(hf != null && hf > max){
				max = hf;
			}
		}
		
		return max;
	}
	
	/*public Integer lowestFret(){
		return lowFret;
	}*/
	/*
	private Integer calcLowestFret(){
		Integer min = null;
		
		for (int s=0;s<GuitarModel.NUM_STRINGS;s++){
			Integer hf = lowFretOnString(s);
			if (min == null){
				min = hf;
			}else if(hf != null && hf < min){
				min = hf;
			}
		}
		
		return min;
	}*/
	

	/**
	 * Determine which fret determines sound on a given string
	 * @param s the string
	 * @return the highest finger on the specified string
	 */
	public Integer calcHighFretOnString(int s){
		Integer max = null;
		
		for (int i =0; i < NUM_FINGERS; i++){
			if (max == null){
				max = form[i][s];
			}else{
				if (form[i][s] != null && form[i][s] > max){
					max = form[i][s];
				}
			}
		}
		return max;
	
	}
	
	/**
	 * Get which fret determines the sound on a given string 
	 * @param s string
	 * @return the highest finger position on the string
	 */
	public Integer highFretOnString(int s){
		return (high[s] == null) ? null : high[s];
		/*Integer max = null;
		
		for (int i =0; i < NUM_FINGERS; i++){
			if (max == null){
				max = form[i][s];
			}else{
				if (form[i][s] != null && form[i][s] > max){
					max = form[i][s];
				}
			}
		}
		return max;*/
	}
	
	
	/*public Integer lowFretOnString(int s){
		Integer min = null;
		
		for (int i =0; i < NUM_FINGERS; i++){
			if (min == null){
				min = form[i][s];
			}else{
				if (form[i][s] != null && form[i][s] < min){
					min = form[i][s];
				}
			}
		}
		return min;
	}*/
	
	/**
	 * @param f finger (0 - index, 4 - little finger)
	 * @return the fret played by the finger, or null
	 */
	public Integer getFingerFret(int f){
		for (int s =0 ; s < GuitarModel.NUM_STRINGS; s++){
			if (form[f][s] != null){
				return form[f][s];
			}
		}
		return null;
	}
	
	/**
	 * Ask whether a finger moves from this handform to another handform
	 * @param f finger (0 - index, 4 - little finger)
	 * @param next the next hand form
	 * @return true if the finger moves from this form to the next
	 */
	public boolean fingerMoves(int f, HandForm next){
		if (getFingerFret(f) == null || next.getFingerFret(f) == null)
			return false;
		
		for (int s =0 ; s < GuitarModel.NUM_STRINGS; s++){
			if (form[f][s] == null && next.form[f][s] == null){
				
			}else if ((form[f][s] == null && next.form[f][s] != null) || !form[f][s].equals(next.form[f][s])){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Ask if the span between adjacent fingers is no more than 1
	 * @return true if the span between adjacent fingers is possible
	 */
	public boolean adjFingerSpanOK(){
		for (int i =0; i < NUM_FINGERS - 1; i++){
			Integer ff1 = getFingerFret(i);
			Integer ff2 = getFingerFret(i+1);
		
			if (ff1 != null && ff2 != null && ff2 - ff1 > 1){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Ask whether any fingers are playing the same fret
	 * @return true if multiple fingers are assigned to the same location
	 */
	public boolean hasCollision(){
		boolean cc[][] = new boolean[MAX_SPAN][GuitarModel.NUM_STRINGS];
		for(boolean t[] : cc){
			Arrays.fill(t, false);
		}
		
		for (int f = 0; f < NUM_FINGERS; f++){
			for (int s = 0; s < GuitarModel.NUM_STRINGS; s++){
				if (form[f][s] != null){
					if(cc[form[f][s]][s]){
						return true;
					}else{
						cc[form[f][s]][s] = true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Checks if the handform is physically possible:
	 * - Hand form span is below some max value
	 * - No fingering collisions
	 * - Span of adjacent fingers is possible
	 * @return true if the handform is physically possible
	 */
	public  boolean isPossible(){
		if (getSpan() > MAX_SPAN)
			return false;
		if (hasCollision())
			return false;
		if (!adjFingerSpanOK())
			return false;
		return true;
	}

	private void calcNumHelper(HashSet<Long> ns, boolean toggle[], int i, int shift){
		int base = MAX_SPAN + 2;
		if (i >= GuitarModel.NUM_STRINGS){
			long num = 0;
			for (int s =0; s < GuitarModel.NUM_STRINGS;s++){
				Integer highFret = highFretOnString(s);
				
				if (highFret == null || highFret + shift < 0){
					num += (long) (Math.pow(base, s) * (toggle[s] ? 1 : 0));
				}else{
					num += (long) (Math.pow(base, s) * (toggle[s] ? highFret + 2 + shift : 0));
				}
			}
			
			if (num < 0){
				throw new Error("num for form is negative!");
			}
			
			ns.add(num);
		}else{
			toggle[i] = false;
			calcNumHelper(ns, toggle, i+1,shift);
			
			toggle[i] = true;
			calcNumHelper(ns, toggle, i+1,shift);
		}
	}
	
	/**
	 * Calculate a set of positions ids that can be played using this hand form
	 * @return set of possible position ids
	 */
	public HashSet<Long> calcNums(){
		HashSet<Long> ns = new HashSet<Long>();
		
		boolean toggle[] = new boolean[GuitarModel.NUM_STRINGS];
		Arrays.fill(toggle,false);
		
		Integer high = highestFret();
		
		int minShift = (high == null ) ? 0 : (-1 * high);
		int maxShift = (high == null) ? 0 : MAX_SPAN - high; 
		
		for (int shift = minShift ; shift < maxShift ;shift++){
			calcNumHelper(ns, toggle, 0,shift);
		}
		
		return ns;
	}
	/* void calcNumHelper(Vector<Long> ns, boolean toggle[], int i){
		if (i >= GuitarModel.NUM_STRINGS){
			long num = 0;
			for (int s = 0; s < GuitarModel.NUM_STRINGS; s++){
				Integer highFret = highFretOnString(s);
				if (!toggle[s]){
					num += Math.pow(3, s) * 0; 
				}else{
					if (highFret == null){
						num += Math.pow(3, s) * 1;
					}else{
						num += Math.pow(3, s) * 2;
					}
				}
			}
			ns.add(num);
		}else{
			toggle[i] = false;
			calcNumHelper(ns, toggle, i+1);
			
			toggle[i] = true;
			calcNumHelper(ns, toggle, i+1);
		}
	}*/
	
	/**
	 * get a graphical representation of the chord form
	 */
	public String toMultiString(){
		Integer cc[][] = new Integer[MAX_SPAN][GuitarModel.NUM_STRINGS];
		for(Integer t[] : cc){
			Arrays.fill(t, null);
		}
		
		for (int f = 0; f < NUM_FINGERS; f++){
			for (int s = 0; s < GuitarModel.NUM_STRINGS; s++){
				if (form[f][s] != null){
					cc[form[f][s]][s] = f +1;
					
				}
			}
		}
		String rt = "";
		for (int r = 0; r< MAX_SPAN; r++){
			rt += "|";
			for (int c =0; c < GuitarModel.NUM_STRINGS; c++){
				rt += " " +  ((cc[r][c] == null) ? "-" : cc[r][c]);
			}
			rt += "|\n";
		}
		
		return rt;
	}

	/**
	 * 
	 * @return true if the index finger is barred
	 */
	public boolean isIndexBarred(){
		int cnt = 0;
		
		for (int s = 0; s < GuitarModel.NUM_STRINGS; s++){
			if (form[0][s] != null)
				cnt++;
			
		}
		return cnt > 1;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(form);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof HandForm))
			return false;
		HandForm other = (HandForm) obj;
		if (!Arrays.deepEquals(form, other.form))
			return false;
		return true;
	}
	
	
}
