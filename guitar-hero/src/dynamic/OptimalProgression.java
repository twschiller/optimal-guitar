package dynamic;

import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.Vector;

public class OptimalProgression {

	public static final class DoublePositionPair{
		public final Position position;
		public final Double value;
		public DoublePositionPair(Position position, Double value) {
			super();
			this.position = position;
			this.value = value;
		}
		@Override
		public String toString() {
			return "<" + value + "," + position + ">";
		}
		
	}
	
	public static List<Position> findOptimalProgression(List<Chord> score, PlayCostMatrix playCost, TransitionCostMatrix transCost, SortedSet<Position> pos){
		Vector<HashMap<Position,DoublePositionPair>> cache = new Vector<HashMap<Position,DoublePositionPair>>(score.size());
		cache.setSize(score.size());
		for (int b = score.size() - 1; b >= 0; b--){
			cache.set(b,new HashMap<Position,DoublePositionPair>());
			for (Position curPos : pos){
				if (b == score.size() - 1){
					Double cost = playCost.getCost(curPos, score.get(b));
					cache.get(b).put(curPos,new DoublePositionPair(null,cost));
				}else{
					Double costToPlay = playCost.getCost(curPos, score.get(b));
					
					Double min = Double.POSITIVE_INFINITY;
					Position minPos = null;
					for (Position nextPos : pos){
						Double nextAndTrans = cache.get(b+1).get(nextPos).value + transCost.getCost(curPos, nextPos);
						if (nextAndTrans < min){
							min = nextAndTrans;
							minPos = nextPos;
						}
					}
					cache.get(b).put(curPos, new DoublePositionPair(minPos, costToPlay + min));
				}
			}
		}
		
		Double min = Double.POSITIVE_INFINITY;
		Position minPos = null;
		
		for (Position p : pos){
			Double cur = cache.get(0).get(p).value;
			if (cur < min){
				min = cur;
				minPos = p;
			}		
		}
		
		List<Position> bestSeq = new Vector<Position>();
		System.out.println("cost at note 0" + ":\t" + cache.get(0).get(minPos).value);
		
		Position curPos = minPos;
		bestSeq.add(curPos);
		for(int b = 0; b < cache.size() - 1; b++){
			curPos = cache.get(b).get(curPos).position;
			bestSeq.add(curPos);
			System.out.println("cost at note " + (b) + ":\t" + cache.get(b+1).get(curPos).value);
			
			
		}
		
		return bestSeq;
		
	}
	
	
}
