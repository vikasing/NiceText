package nicetext;/*
package nicetext;

import org.jblas.DoubleMatrix;

import java.util.HashMap;

import java.io.*;
import java.util.*;

*/
/**
 * @author vikasing
 *
 *//*


public class TfIdf {
	private static final double ALPHA = 0.4;
	private static final double THRESHOLD = 4.5; 

	public void calculateWordRarity(Set<String> docs) throws IOException {

        // matrix of (all ngrams) x (total documents)
		double[][] nGramMatrix = new double[allTextNgMap.size()][totalDocs];
		String[] allGsArr =  allTextNgMap.keySet().toArray(new String[allTextNgMap.size()]);
		Set<String> fileNameSet = ngramDocMap.keySet();
		Map<Integer, String> fileNameMap = new HashMap<>();
		int i = 0;
		for (String fileName : fileNameSet) {
			fileNameMap.put(i, fileName);
			Map<String, Integer> singleDocNGramMap = ngramDocMap.get(fileName);
			Set<String> grams = singleDocNGramMap.keySet();
			for (String gram : grams) {
				nGramMatrix[Arrays.binarySearch(allGsArr, gram)][i] = singleDocNGramMap.get(gram);
			}
			i++;
		}
		Map<String, Map<String, Double>> keywordMap = calculateTFIDF(nGramMatrix, allGsArr, fileNameMap);
		for (String fileName : keywordMap.keySet()) {
			System.out.println("===========================keywords for "+fileName+"=============================================");
			Set<String> kewordSet = new TreeSet<>(keywordMap.get(fileName).keySet());
			Set<String> gramsToRemove = new HashSet<>();
			String[] keywordArr = kewordSet.toArray(new String[kewordSet.size()]);
			for (int j = 0; j < keywordArr.length; j++) {
				getOverlapping(gramsToRemove, keywordMap.get(fileName), keywordArr,keywordArr[j]);
				kewordSet.removeAll(gramsToRemove);
			}
			for (String keyword : kewordSet) {
				System.out.println(keyword +" "+ keywordMap.get(fileName).get(keyword));
			}
		}
	}
*/
/**
 * @param gramsToRemove
 * @param keywordArr
 *//*



	private void getOverlapping(Set<String> gramsToRemove, Map<String, Double> gramMap, String[] keywordArr, String word) {		
		String[] tempArr = word.split(" ");
		if (tempArr.length>2) {
			searchArray(gramsToRemove, gramMap, keywordArr, word, tempArr[0]+" "+tempArr[1]);
			searchArray(gramsToRemove, gramMap, keywordArr, word, tempArr[1]+" "+tempArr[2]);
		}
		else if (tempArr.length>1) {
			for (int k = 0; k < tempArr.length; k++) {
				searchArray(gramsToRemove, gramMap, keywordArr, word, tempArr[k]);
			}
		}
	}


	private void searchArray(Set<String> gramsToRemove,Map<String, Double> gramMap, String[] keywordArr, String word,String tempWord) {
		int pos = Arrays.binarySearch(keywordArr,tempWord);
		if (pos>-1) {// && (gramMap.get(tempWord).compareTo(gramMap.get(word))==0)) {
			gramsToRemove.add(keywordArr[pos]);
		}
	}
	

	private Map<String, Map<String, Double>> calculateTFIDF(double[][] bigArr, String[] allGs, Map<Integer, String> fileNameMap) {
		DoubleMatrix doubleMatrix = new DoubleMatrix(bigArr);
		int columns = doubleMatrix.columns;
		Map<String, Map<String, Double>> keywordMap = new HashMap<>();
		Map<Integer, Integer> maxFreqMap = new LinkedHashMap<>();
		for (int i = 0; i < columns; i++) {
			DoubleMatrix aColumn = doubleMatrix.getColumn(i);
			int maxFrequency = 0;
			for (int j = 0; j < aColumn.rows; j++) {
				int temp = (int) aColumn.get(j);
				if (temp>maxFrequency) {
					maxFrequency = temp;
				}
			}
			maxFreqMap.put(i, maxFrequency);
		}
		for (int j = 0; j < bigArr.length; j++) {
			double counter = 0;
			int numOfDocs = bigArr[j].length;
			for (int k = 0; k < numOfDocs; k++) {
				if (bigArr[j][k]!=0) {
					counter ++;
				}
			}			
			for (int k = 0; k < numOfDocs; k++) {
				if (bigArr[j][k]!=0) {
					double tf = Math.log(bigArr[j][k]+1);
					//double tf = bigArr[j][k];
					//double tf = ALPHA + ((1-ALPHA)*bigArr[j][k])/(double)maxFreqMap.get(k);
					double idf = Math.log((double)numOfDocs/counter);
					if (tf*idf>THRESHOLD) {
						if (keywordMap.containsKey(fileNameMap.get(k))) {
							keywordMap.get(fileNameMap.get(k)).put(allGs[j],tf*idf);
						}
						else {
							Map<String, Double> keywordScoreMap= new HashMap<>();
							keywordScoreMap.put(allGs[j],tf*idf);
							keywordMap.put(fileNameMap.get(k), keywordScoreMap);
						}
					}
				}
			}
		}
		return keywordMap;
	}
	public static void main(String[] args) throws IOException {
		TfIdf tfIdf = new TfIdf();
		tfIdf.calculateWordRarity();
	}
}
*/
