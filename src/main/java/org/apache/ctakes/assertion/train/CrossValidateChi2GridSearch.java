package org.apache.ctakes.assertion.train;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ctakes.assertion.eval.AssertionEvaluation;
import org.apache.ctakes.assertion.util.AssertionConst;

import scala.actors.threadpool.Arrays;

public class CrossValidateChi2GridSearch {

	public static void main(String[] args) throws Exception {
		
		AssertionEvaluation.useEvaluationLogFile = true;
		
		float[] threshs = {2f, 5f, 10f, 20f, 50f, 100f};
		for (Float chi2threshold : threshs ) {
			System.out.println("BEGIN Chi2 Grid Search with threshold = "+ Float.toString(chi2threshold));
			AssertionEvaluation.evaluationLogFileOut.write("BEGIN Chi2 Grid Search with threshold = "+ Float.toString(chi2threshold));
			AssertionEvaluation.evaluationLogFileOut.flush();
			
			for (String attribute : AssertionConst.annotationTypes) {

				ArrayList<String> params = new ArrayList<String>();

				params.add("--train-dir"); 			params.add(AssertionConst.trainingDirectories.get(attribute));
				params.add("--models-dir"); 		params.add(AssertionConst.modelDirectory+Float.toString(chi2threshold));
				params.add("--cross-validation"); 	params.add("5");
				params.add("--feature-selection");	params.add(Float.toString(chi2threshold));

				// Build up an "ignore" string
				for (String ignoreAttribute : AssertionConst.annotationTypes) {
					if (!ignoreAttribute.equals(attribute)) { 

						if (ignoreAttribute.equals("historyOf")) {
							ignoreAttribute = ignoreAttribute.substring(0, ignoreAttribute.length()-2);
						}

						params.add("--ignore-" + ignoreAttribute);
					}
				}
				String[] paramList = params.toArray(new String[]{});

				//			System.out.println(Arrays.asList(paramList).toString());

				// Run the actual assertion training on just one attribute
				AssertionEvaluation.main( paramList );
			}
		}
		
		
		
	}
}
