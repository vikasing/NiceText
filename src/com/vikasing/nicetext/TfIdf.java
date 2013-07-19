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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jblas.DoubleMatrix;

/**
 * @author vikasing
 *
 */
public class TfIdf {
	private static final String DATA_DIR = "data";
	
	void calculateWordRarity() throws IOException{
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
				
		// matrix of (all ngrams) x (total documets)
		double[][] nGramArr = new double[allTextNgMap.size()][totalDocs];
		//List<String> allGramList = new  ArrayList<String>(allTextNgMap.keySet());
		String[] allGsArr =  allTextNgMap.keySet().toArray(new String[allTextNgMap.size()]);
		Set<String> fileNameSet = ngramDocMap.keySet();
		Map<Integer, String> fileNameMap = new HashMap<Integer, String>();
		int i = 0;
		for (String fileName : fileNameSet) {
			fileNameMap.put(i, fileName);
			Map<String, Integer> singleDocNGramMap = ngramDocMap.get(fileName);
			Set<String> grams = singleDocNGramMap.keySet();
			for (String gram : grams) {
				//if (allGramList.contains(gram)) {
				nGramArr[Arrays.binarySearch(allGsArr, gram)][i] = singleDocNGramMap.get(gram);
				//}
			}
			i++;
		}
		Map<String, Map<String, Double>> keywordMap = calculateTFIDF(nGramArr, allGsArr, fileNameMap);		
		for (String fileName : keywordMap.keySet()) {
			System.out.println("===========================keywords for "+fileName+"=============================================");
			for (String keyword : keywordMap.get(fileName).keySet()) {
				System.out.println(keyword +" "+ keywordMap.get(fileName).get(keyword));
			}
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
					//double tf = Math.log(bigArr[j][k]+1);
					//double tf = bigArr[j][k];
					double tf = 0.4 + (0.6*bigArr[j][k])/(double)maxFreqMap.get(k);
					double idf = Math.log((double)numOfDocs/counter);
					if (tf*idf>1.8) {
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
