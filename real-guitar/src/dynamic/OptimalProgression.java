package dynamic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import model.Chord;
import model.GuitarModel;
import model.HandForm;
import model.HandPosition;
import model.Chord.NoLowestFretException;

/**
 * Class / methods for solving the optimal progression dynamic programming problem
 * @author Todd Schiller
 */
public class OptimalProgression {

	public static final double ADD_FINGER_COST = .6;
	public static final double REM_FINGER_COST = .2;
	public static final double MOV_FINGER_COST = 1.0;
	public static final double SHIFT_ALPHA = 50.0;
	public static final double LITTLE_FINGER_COST = .5;
	public static final double INDEX_BARRE_COST = 3.0;
	
	/**
	 * A class representing a playing a note/chord from 1 position and then transfering to another position to play another note
	 * @author Todd Schiller
	 */
	public static class Transition{
		Chord b_prime;
		HandPosition p_prime;
		Chord b;
		HandPosition p;
		public Transition(Chord b, HandPosition p, Chord b_prime,HandPosition p_prime) {
			super();
			this.b = b;
			this.b_prime = b_prime;
			this.p = p;
			this.p_prime = p_prime;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((b == null) ? 0 : b.hashCode());
			result = prime * result
					+ ((b_prime == null) ? 0 : b_prime.hashCode());
			result = prime * result + ((p == null) ? 0 : p.hashCode());
			result = prime * result
					+ ((p_prime == null) ? 0 : p_prime.hashCode());
			return result;
		}
	}
	
	/**
	 * Find the energy required for a given transition
	 * @param t transition
	 * @return energy required for transition
	 */
	public static double getCost(Transition t){
		//Assume that the chords are playable from the positions
		
		double cost = 0.0;
		
		//CHECK FOR ADDs
		for (int f = 0; f < HandForm.NUM_FINGERS; f++){
			if (t.p.form.getFingerFret(f) == null && t.p_prime.form.getFingerFret(f) != null){
				cost += ADD_FINGER_COST;
			}
		}
		
		//CHECK FOR REMs
		for (int f = 0; f < HandForm.NUM_FINGERS; f++){
			if (t.p.form.getFingerFret(f) != null && t.p_prime.form.getFingerFret(f) == null){
				cost += REM_FINGER_COST;
			}
		}
		
		//CHECK FOR MOVs
		for (int f = 0; f < HandForm.NUM_FINGERS; f++){
			if (t.p.form.fingerMoves(f, t.p_prime.form)){
				cost += MOV_FINGER_COST;
			}
		}
		
		//CHECK FOR ADD Barre
		if (!t.p.form.isIndexBarred() && t.p_prime.form.isIndexBarred()){
			cost += INDEX_BARRE_COST;
		}
		
		if (t.p_prime.form.getFingerFret(3) != null){
			cost += LITTLE_FINGER_COST;
		}
		
		if (t.p.baseFret - t.p_prime.baseFret != 0){
			cost += SHIFT_ALPHA * (t.p.baseFret - t.p_prime.baseFret) * (t.p.baseFret - t.p_prime.baseFret) ;
		}
		return cost;
	}
	
	/**
	 * A class to allow backtracking in the dynamic programming problem
	 * @author Todd Schiller
	 */
	public static final class DoublePositionPair{
		public final HandPosition position;
		public final Double value;
		public final DoublePositionPair prev;
		
		public DoublePositionPair(HandPosition position, Double value,DoublePositionPair prev) {
			super();
			this.position = position;
			this.value = value;
			this.prev = prev;
		}
		@Override
		public String toString() {
			return "<" + value + "," + position + ">";
		}

	}
	
	/**
	 * Solve the dynamic programming problem
	 * @param hps handforms organized by id
	 * @param score song to play
	 * @return optimal sequence of hand positions
	 */
	public static List<HandPosition> generateOptimalProgression(Hashtable<Long,HashSet<HandForm>> hps,List<Chord> score){
		int B = score.size();	
	
		HashMap<HandPosition,DoublePositionPair> lastLayer = new HashMap<HandPosition,DoublePositionPair>();
		
		//Start at the beginning of the song an work our way to the end
		for (int b = 0; b < B; b++){
			//System.out.print(".");
			Chord beat_prime = score.get(b);
			
			HashMap<HandPosition,DoublePositionPair> layer = new HashMap<HandPosition,DoublePositionPair>();
		
			Integer lowFret = null;
			try {
				lowFret = beat_prime.getLowestFret();
			} catch (NoLowestFretException e) {
				throw new Error("all rests are not allowed");
			}	
			
			long num_prime = beat_prime.calcNum();
			
			int cnt = 0;
			
			for (int f = (lowFret-HandForm.MAX_SPAN > 0 ) ? lowFret-HandForm.MAX_SPAN : 1; 
					(lowFret == 0 && f <= GuitarModel.NUM_FRETS) || f <= lowFret; f++){
			
				for (HandForm f_prime : hps.get(num_prime)){
					HandPosition p_prime = new HandPosition(f,f_prime);
					
					cnt++;
					
					if (!p_prime.canPlay(beat_prime)){
						continue;
					}else if (b == 0){
						//its the beginning of the song, so we just count this position as a possibility
						//with no cost associated with it
						layer.put(p_prime, new DoublePositionPair(p_prime,0.0,null));
					}else{
						Chord beat = score.get(b-1);
						Double min = Double.POSITIVE_INFINITY;
						HandPosition minPos = null;
						for (HandPosition p : lastLayer.keySet()){
							if (!p.canPlay(beat)){
								continue;
							}
							//We know that p_prime can play b_prime
							//We know that p can play b
						
							Double cost = getCost(new Transition(beat, p, beat_prime, p_prime));
						
							if (cost != Double.POSITIVE_INFINITY) {
								cost += lastLayer.get(p).value;
							}
							if (cost < min){
								min = cost;
								minPos = p;
							}
							
						}
						layer.put(p_prime, new DoublePositionPair(p_prime, min, lastLayer.get(minPos)));
					}
				}
			}
			System.out.println("prev: " + lastLayer.size() + " next:" + cnt + "  " + beat_prime);
			
			lastLayer = layer;
			if (lastLayer.size() == 0){
				throw new Error("FATAL ERROR. No positions in layer. Chord: " + beat_prime + " #:" + beat_prime.calcNum());
			}
		}
		
		
		Double min = Double.POSITIVE_INFINITY;
		HandPosition minPos = null;
		
		//Find the best overall cost
		for (HandPosition p : lastLayer.keySet()){
			//Double cur = cache.get(B-1).get(p).value;
			Double cur = lastLayer.get(p).value;
			if (cur < min){
				min = cur;
				minPos = p;
			}		
		}
		
		List<HandPosition> opt = new Vector<HandPosition>();

		DoublePositionPair dpp = lastLayer.get(minPos);
		opt.add(dpp.position);
		
		//Backtrack to find the optimal solution
		for(int b = B-1; b > 0; b--){
			dpp = dpp.prev;
			opt.add(0, dpp.position);	
		}
		System.out.println();
		return opt;
	}
}
