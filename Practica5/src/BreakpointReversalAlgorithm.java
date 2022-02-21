
import java.util.LinkedList;
import java.util.List;

import es.uma.ada.greedy.sequence.rearrangement.GreedySequenceRearrangement;
import es.uma.ada.problem.combinatorial.sequence.rearrangement.Permutation;
import es.uma.ada.problem.combinatorial.sequence.rearrangement.Reversal;
import es.uma.ada.problem.combinatorial.sequence.rearrangement.SequenceRearrangementOperation;

/**
 * Implementation of a basic rearrangement greedy algorithm that performs
 * reversals between breakpoints
 * 
 * @author ccottap
 *
 */
public class BreakpointReversalAlgorithm extends GreedySequenceRearrangement {

	/**
	 * Default constructor
	 */
	public BreakpointReversalAlgorithm() {
		super();
	}

	@Override
	protected List<SequenceRearrangementOperation> getCandidates(Permutation p) {
		int n = p.size();
		List<SequenceRearrangementOperation> ops = new LinkedList<SequenceRearrangementOperation>();
		
		//------------- TO COMPLETE ------------- 

		// 1) Find where the breakpoints are.
		int[] breakpoints = new int[n];
		int numBP = 0;
		
		for (int i = 0; i < n-1 ; i++) {
			if (Math.abs(p.get(i)-p.get(i+1)) > 1) {				
				breakpoints[numBP] = i;
				numBP++;
			}
		}		
		
		// 2) Candidates are reversals between breakpoints.
		for (int i = 0; i < numBP; i++) {
			for (int j = i+1; j < numBP; j++) {
				ops.add(new Reversal(breakpoints[i]+1,breakpoints[j]));				
			}
		}
		
		// getCandidates(π) = {ρ(i + 1, j) | i < j, {i, j} c_ breakpoints(π)}
		//--------------------------------------- 

		return ops;
	}

	@Override
	protected double getOperationQuality(Permutation p, SequenceRearrangementOperation op) {
		Reversal r = (Reversal) op;
		int result = 0;
		//------------- TO COMPLETE ------------- 
		
		if (Math.abs(p.get(r.getStart()-1)  - p.get(r.getFinish()) ) == 1) {
			result++;
		}
		if (Math.abs(p.get(r.getFinish()+1)  - p.get(r.getStart()) ) == 1) {
			result++;
		}
		 
		// getQuality(ρ(i, j), π) = [|πi−1 − πj| = 1] + [|πj+1 − πi| = 1]
		//--------------------------------------- 

		return (double) result;
	}

	@Override
	public String getName() {
		return "BreakpointReversal";
	}

	@Override
	public boolean isSignedCompatible() {
		return false;
	}

}
