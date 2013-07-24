/**
 * 
 */
package com.vikasing.nicetext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.jblas.DoubleMatrix;

/**
 * @author vikasing
 *
 */
public class TfIdf {
	private static final String DATA_DIR = "data";
	private static final double ALPHA = 0.4; 
	private static final double THRESHOLD = 4.5; 

	public void calculateWordRarity() throws IOException{
		File file = new File(DATA_DIR);
		File[] files = file.listFiles();
		// map of all ngrams for all the text combined
		Map<String, Integer> allTextNgMap = new TreeMap<String,Integer>();
		Map<String, Map<String, Integer>> ngramDocMap = new HashMap<String, Map<String,Integer>>();
		NGramExtracter nGramExtracter = new NGramExtracter();
		int totalDocs = files.length;

		for (int i = 0; i < totalDocs; i++) {
			FileInputStream fileStream = null;
			InputStreamReader iReader = null;
			BufferedReader in = null;
			try {
				fileStream = new FileInputStream(files[i]);
				iReader = new InputStreamReader(fileStream);
				in = new BufferedReader(iReader);
				String xString = null;
				StringBuffer stringBuffer = new StringBuffer();
				while ((xString=in.readLine())!=null) {
					stringBuffer.append(xString+" ");
				}
				String text = stringBuffer.toString();
				NGrams oneDocGrams = nGramExtracter.getNGrams(text);
				allTextNgMap.putAll(oneDocGrams.getCombinedGramMap());
				ngramDocMap.put(files[i].getName(),oneDocGrams.getCombinedGramMap());

			} catch(Exception e) {
				e.printStackTrace();
			}
			finally {
				in.close();iReader.close();fileStream.close();
			}
		}

		// matrix of (all ngrams) x (total documents)
		double[][] nGramArr = new double[allTextNgMap.size()][totalDocs];
		String[] allGsArr =  allTextNgMap.keySet().toArray(new String[allTextNgMap.size()]);
		Set<String> fileNameSet = ngramDocMap.keySet();
		Map<Integer, String> fileNameMap = new HashMap<Integer, String>();
		int i = 0;
		for (String fileName : fileNameSet) {
			fileNameMap.put(i, fileName);
			Map<String, Integer> singleDocNGramMap = ngramDocMap.get(fileName);
			Set<String> grams = singleDocNGramMap.keySet();
			for (String gram : grams) {
				nGramArr[Arrays.binarySearch(allGsArr, gram)][i] = singleDocNGramMap.get(gram);
			}
			i++;
		}
		Map<String, Map<String, Double>> keywordMap = calculateTFIDF(nGramArr, allGsArr, fileNameMap);		
		for (String fileName : keywordMap.keySet()) {
			System.out.println("===========================keywords for "+fileName+"=============================================");
			Set<String> kewordSet = new TreeSet<String>(keywordMap.get(fileName).keySet());
			Set<String> gramsToRemove = new HashSet<String>();
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
	/**
	 * @param gramsToRemove
	 * @param keywordArr
	 * @param firstBi
	 */
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
	/**
	 * @param gramsToRemove
	 * @param gramMap
	 * @param keywordArr
	 * @param word
	 * @param tempArr
	 * @param k
	 */
	private void searchArray(Set<String> gramsToRemove,Map<String, Double> gramMap, String[] keywordArr, String word,String tempWord) {
		int pos = Arrays.binarySearch(keywordArr,tempWord);
		if (pos>-1) {// && (gramMap.get(tempWord).compareTo(gramMap.get(word))==0)) {
			gramsToRemove.add(keywordArr[pos]);
		}
	}
	
	
	/**
	 * @param bigArr
	 * @param allGs
	 * @param fileNameMap 
	 */
	private Map<String, Map<String, Double>> calculateTFIDF(double[][] bigArr, String[] allGs, Map<Integer, String> fileNameMap) {
		DoubleMatrix doubleMatrix = new DoubleMatrix(bigArr);
		int columns = doubleMatrix.columns;
		Map<String, Map<String, Double>> keywordMap = new HashMap<String, Map<String, Double>>();
		Map<Integer, Integer> maxFreqMap = new LinkedHashMap<Integer, Integer>();
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
							Map<String, Double> keywordScoreMap= new HashMap<String, Double>();
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
