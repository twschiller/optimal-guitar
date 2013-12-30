package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;

/**
 * A class for a model of a guitar. Can generate a list of all possible hand positions on the guitar.
 * @author Todd Schiller
 *
 */
public class GuitarModel {
	public static final int NUM_FRETS = 13;
	public static final int NUM_STRINGS = 6;
	
	static Integer nullOrPos[] = {null, 0,1, 2, 3};
	static Integer nullOrZero[] = {null, 0};
	static Integer nullOrZeroOrOne[] = {null, 0,1};
	static Integer nullOrOne[] = {null, 1};
	static Integer nullOrNotZero[] = {null,1,2,3};

	
	/**
	 * Get at the hand forms possible on the guitar
	 * @return
	 */
	public static Hashtable<Long,HashSet<HandForm>> getForms(){
		HashSet<HandForm> forms = new HashSet<HandForm>();
		Hashtable<Long,HashSet<HandForm>> pos = new Hashtable<Long,HashSet<HandForm>>();

		/* Create non-barred chord forms */
		genNonBarred(forms);
		/* Create barred chord forms */
		genBarred(forms);
		
		/* Create hand positions along the neck for the hand forms */
		for (HandForm form : forms){
			HashSet<Long> ns = form.calcNums();
			for (long num : ns){			
				if (pos.get(num) == null){
					pos.put(num,new HashSet<HandForm>(1,.90f));
				}
				pos.get(num).add(new HandForm(form));
			}
		}
		return pos;
	}
	
	public static void genBarred(HashSet<HandForm> forms){
		for (int startBarre = 0; startBarre < NUM_STRINGS-1; startBarre++){
			for (Integer mP : nullOrOne){
				for (int mS = 0; mS < NUM_STRINGS; mS++){
					if (mP == null && mS > 0){
						break;
					}
					
					for (Integer rP : nullOrNotZero){
						for (int rS = 0; rS < NUM_STRINGS; rS++){
							if (rP == null && rS > 0)
								break;
							if (mP != null && rP != null && rP.compareTo(mP) < 0)
								continue;
							
							if (mP != null && rP != null && rP.equals(mP) && rS <= mS)
								continue;
						
							for (Integer lP : nullOrPos){
								for (int lS = 0; lS < NUM_STRINGS; lS++){
									if (lP == null && lS > 0)
										break;
									if (rP != null && lP != null && lP.compareTo(rP) < 0)
										continue;
									if (mP != null && lP != null && lP.compareTo(mP) < 0)
										continue;
									if (mP != null && lP != null && lP.equals(mP) && lS <= mS)
										continue;
									if (rP != null && lP != null && lP.equals(rP) && lS <= rS)
										continue;
									
									Integer relF[][] = new Integer[HandForm.NUM_FINGERS][];
									for (int i = 0; i < HandForm.NUM_FINGERS; i++){
										relF[i] = new Integer[GuitarModel.NUM_STRINGS];
										Arrays.fill(relF[i],null);
									}
									
									for (int s = startBarre; s < NUM_STRINGS; s++){
										relF[0][s] = 0;
									}
									
									relF[1][mS] = mP; 
									relF[2][rS] = rP;
									relF[3][lS] = lP;
									
									HandForm form = new HandForm(relF);
							
									if (form.isPossible()){
										forms.add(new HandForm(relF));
									}
									
								}
							}
						}
					}
				}
			}
		}
	}
	
	public static void genNonBarred(HashSet<HandForm> forms){
		for (Integer iP : nullOrZero){
			for (int iS = 0; iS < NUM_STRINGS; iS++){
				if (iP == null && iS > 0)
					break;
				
				for (Integer mP : nullOrZeroOrOne){
					if (mP != null && iP == null && mP.equals(0))
						continue;
				
					for (int mS = 0; mS < NUM_STRINGS; mS++){
						if (mP == null && mS > 0)
							break;
						if (iP != null && mP != null && mP.compareTo(iP) < 0)
							continue;
						if (iP != null && mP != null && mP.equals(iP) && mS <= iS)
							continue;
						
						for (Integer rP : nullOrPos){
							if (rP != null && iP == null && rP.equals(0))
								continue;
							
							for (int rS = 0; rS < NUM_STRINGS; rS++){
								if (rP == null && rS > 0)
									break;
								if (mP != null && rP != null && rP.compareTo(mP) < 0)
									continue;
								if (iP != null && rP != null && rP.compareTo(iP) < 0)
									continue;
								
								if (iP != null && rP != null && rP.equals(iP) && rS <= iS)
									continue;
								if (mP != null && rP != null && rP.equals(mP) && rS <= mS)
									continue;
							
								
								for (Integer lP : nullOrPos){
									if (lP != null && iP == null && lP.equals(0))
										continue;
									
									for (int lS = 0; lS < NUM_STRINGS; lS++){
								
										if (lP == null && lS > 0)
											break;
										
										if (rP != null && lP != null && lP.compareTo(rP) < 0)
											continue;
										if (mP != null && lP != null && lP.compareTo(mP) < 0)
											continue;
										if (iP != null && lP != null && lP.compareTo(iP) < 0)
											continue;
										
										if (iP != null && lP != null && lP.equals(iP) && lS <= iS)
											continue;
										if (mP != null && lP != null && lP.equals(mP) && lS <= mS)
											continue;
										if (rP != null && lP != null && lP.equals(rP) && lS <= rS)
											continue;
										
										
										Integer relF[][] = new Integer[HandForm.NUM_FINGERS][];
										for (int i = 0; i < HandForm.NUM_FINGERS; i++){
											relF[i] = new Integer[GuitarModel.NUM_STRINGS];
											Arrays.fill(relF[i],null);
										}
										relF[0][iS] = iP;
										relF[1][mS] = mP; 
										relF[2][rS] = rP;
										relF[3][lS] = lP;
										
										HandForm form = new HandForm(relF);
								
										if (form.isPossible()){
											forms.add(new HandForm(relF));
										}
									}
								}
							}
						}				
					}
				}
			}
		}
		
	}
	
	
}
