import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import model.Chord;
import model.GuitarModel;
import model.HandForm;
import model.HandPosition;
import score.ScoreReader;
import dynamic.OptimalProgression;
public class driver {

	/**
	 * Read a score and output the optimal progression hand positions
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		List<Chord> score = ScoreReader.readSimpleScore(new File(args[0]));
		
		/*for (Chord c: score){
			System.out.println(c + "   " + c.calcNum());
		}*/
		
		Hashtable<Long,HashSet<HandForm>> hps = GuitarModel.getForms();
		
		List<HandPosition> opt = OptimalProgression.generateOptimalProgression(hps,score);
		
		for(HandPosition p : opt){
			if (p != null){
				System.out.println(p.toMultiString());
			}
		}
	}

}
