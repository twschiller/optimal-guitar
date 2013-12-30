package model;

import model.Chord.NoHighestFretException;
import model.Chord.NoLowestFretException;


/**
 * Class for a handposition, a hand form a base position on the guitar
 * @author Todd Schiller
 *
 */
public class HandPosition {
	public final int baseFret;
	public final HandForm form;

	public HandPosition(int baseFret, HandForm form) {
		super();
		
		if (baseFret < 0 || baseFret > GuitarModel.NUM_FRETS){
			throw new IllegalArgumentException("Invalid base fret for hand position: " + baseFret);
		}
		this.baseFret = baseFret;
	
		this.form = form;
	}

	/**
	 * Get the fret that dictates sound on a string
	 * @param s the string
	 * @return the highest fret being played on the string
	 */
	public Integer highFretOnString(int s){
		Integer hf = form.highFretOnString(s);
		
		if (hf == null)
			return null;
		
		return hf + baseFret;
		
	}
	
	/**
	 * Ask whether a given chord can be played with this hand position
	 * @param c chord
	 * @return true if the chord can be played from the hand position, false otherwise
	 */
	public boolean canPlay(Chord c){
		Integer cLowFret = null;
		try {
			cLowFret = c.getLowestFret();
		} catch (NoLowestFretException e1) {}
	
		Integer cHighFret = null;
		try {
			cHighFret = c.getHighestFret();
		} catch (NoHighestFretException e1) {}
		
		if (cLowFret != null && cLowFret != 0 && cLowFret < baseFret){
			return false;
		}
		if (cHighFret != null && cHighFret > baseFret + HandForm.MAX_SPAN){
			return false;
		} 
		Integer notes[] = c.getNotes();
		for (int s = 0; s < GuitarModel.NUM_STRINGS; s++){
			
			if (notes[s] != null){
				if (notes[s].equals(0) && highFretOnString(s) == null){
					continue;
				}else if (highFretOnString(s) != null && notes[s].equals(highFretOnString(s))){
					continue;
				}else{
					return false;
				}
			}
		}
		
		
		if (form.isIndexBarred()){
			for (int s = 0; s < GuitarModel.NUM_STRINGS; s++){
				if (notes[s] != null && notes[s] == 0 && form.getForm()[0][s] != null)
					return false;
			}
		}
		
		
		return true;
	}

	public String toString(){
		Integer f[][] = form.getForm();
		
		String s = "|";
		
		for (int fi = 0; fi < HandForm.NUM_FINGERS; fi++){
			
			for (int si = 0; si < GuitarModel.NUM_STRINGS; si++){
				s += " " +  (f[fi][si] == null ? "-" : f[fi][si] + 1);
			}
			s += "|";
		}
		
		return s;
	}
	
	
	public String toMultiString(){
		return "-----------base:" + baseFret + "\n" + form.toMultiString() + "---------------\n";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + baseFret;
		result = prime * result + ((form == null) ? 0 : form.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof HandPosition))
			return false;
		HandPosition other = (HandPosition) obj;
		if (baseFret != other.baseFret)
			return false;
		if (form == null) {
			if (other.form != null)
				return false;
		} else if (!form.equals(other.form))
			return false;
		return true;
	}
	
	
	
}
