package drivers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.SortedSet;

import score.SimpleScoreReader;
import dynamic.Chord;
import dynamic.OptimalProgression;
import dynamic.PlayCostMatrix;
import dynamic.Position;
import dynamic.TransitionCostMatrix;
public class SimpleDriver {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 6){
			System.out.println("usage: java -jar optseq position_file chord_file transition_cost_file play_cost_file score_file output_file");
			System.exit(1);
		}
		
		// TODO Auto-generated method stub
		SortedSet<Position> positions = Position.readPositions(new File(args[0]));
		
		Chord.readChords(new File(args[1]));
		
		/*TransitionCostMatrix tcm = new TransitionCostMatrix();
		for (int p0 = 0; p0 < 5; p0++ ){
			for (int p = 0; p < 5; p++){
				tcm.setCost(Position.getPositionByID(p0), Position.getPositionByID(p), new Double(Math.abs(p - p0)));
			}
		}
		
		tcm.writeMatrix(new File("C:\\tcm.matrix"));
		
		PlayCostMatrix pcm = new PlayCostMatrix();
		for (int p =0; p < 5; p++){
			for (int n = p; n < 5; n++){
				pcm.setCost(Position.getPositionByID(p), chords.get(n), new Double((n-p)/2.));
			}
		}
		
		pcm.writeMatrix(new File("C:\\pcm.matrix"));*/
		
		TransitionCostMatrix tcm = TransitionCostMatrix.readMatrix(new File(args[2]));
		PlayCostMatrix pcm = PlayCostMatrix.readMatrix(new File(args[3]));
		
		List<Chord> song = SimpleScoreReader.readSimpleScore(new File(args[4]));
		
		List<Position> opt = OptimalProgression.findOptimalProgression(song, pcm, tcm, positions);
		
		System.out.println("====SEQUENCE=====");
		
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(args[5])));
		
		for (Position pos : opt){
			pw.println(pos);
			System.out.println(pos);
		}
		
		pw.close();
		
	}

}
