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
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author vikasing
 *
 */
public class TfIdf {
	private static final String DATA_DIR = "data";
	
	void calculateWordRarity() throws IOException{
		File file = new File(DATA_DIR);
		File[] files = file.listFiles();
		Map<String, Map<String, SortedSet<Entry<String, Integer>>>> ngramDocMap = new HashMap<String, Map<String,SortedSet<Entry<String,Integer>>>>();
		NGramExtracter nGramExtracter = new NGramExtracter();
		StringBuffer allText = new StringBuffer();
		int totalDocs = files.length;

		Map<String, Integer> sizeMap = new HashMap<String, Integer>();
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
				allText.append(text+" ");
				int tSize = text.split(" ").length;
				sizeMap.put(files[i].getName(), tSize);
				ngramDocMap.put(files[i].getName(),nGramExtracter.getNGrams(text));
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			finally {
				in.close();iReader.close();fileStream.close();
			}
		}
		Map<String, SortedSet<Entry<String, Integer>>> allTextNgMap = nGramExtracter.getNGrams(allText.toString());
		SortedSet<Entry<String, Integer>> allTextMonos = allTextNgMap.get("mono");
		SortedSet<Entry<String, Integer>> allTextBis = allTextNgMap.get("bi");
		SortedSet<Entry<String, Integer>> allTextTris = allTextNgMap.get("tri");
		
		SortedSet<String> allMonos = new TreeSet<String>();
		SortedSet<String> allBis = new TreeSet<String>();
		SortedSet<String> allTris = new TreeSet<String>();
				
		for (Entry<String, Integer> entry : allTextMonos) {
			allMonos.add(entry.getKey());
		}
		for (Entry<String, Integer> entry : allTextBis) {
			allBis.add(entry.getKey());
		}
		for (Entry<String, Integer> entry : allTextTris) {
			allTris.add(entry.getKey());
		}
		
		int[][] monoArr = new int[allMonos.size()][totalDocs];
		int[][] biArr = new int[allBis.size()][totalDocs];
		int[][] triArr = new int[allTris.size()][totalDocs];
		
		String[] allMonoArr =  allMonos.toArray(new String[allMonos.size()]);
		String[] allBiArr =  allBis.toArray(new String[allBis.size()]);
		String[] allTriArr = allTris.toArray(new String[allTris.size()]);
		
		Set<String> fileNameSet = ngramDocMap.keySet();
		int i = 0;
		for (String fileName : fileNameSet) {
			Map<String, SortedSet<Entry<String, Integer>>> ngMap = ngramDocMap.get(fileName);
			SortedSet<Entry<String, Integer>> monos = ngMap.get("mono");
			for (Entry<String, Integer> monoEntry : monos) {
				String mono = monoEntry.getKey();
				if (allMonos.contains(mono)) {
					monoArr[Arrays.binarySearch(allMonoArr, mono)][i] = monoEntry.getValue();
				}
			}
			SortedSet<Entry<String, Integer>> bis = ngMap.get("bi");
			for (Entry<String, Integer> biEntry : bis) {
				String bi = biEntry.getKey();
				if (allBis.contains(bi)) {
					biArr[Arrays.binarySearch(allBiArr, bi)][i] = biEntry.getValue();
				}
			}
			SortedSet<Entry<String, Integer>> tris = ngMap.get("tri");
			for (Entry<String, Integer> triEntry : tris) {
				String tri = triEntry.getKey();
				if (allTris.contains(tri)) {
					triArr[Arrays.binarySearch(allTriArr, tri)][i] = triEntry.getValue();
				}
			}
			i++;
		}
		calculateTFIDF(monoArr, allMonoArr);		
		calculateTFIDF(biArr, allBiArr);
		calculateTFIDF(triArr, allTriArr);		
	}
	/**
	 * @param bigArr
	 * @param allGs
	 */
	private void calculateTFIDF(int[][] bigArr, String[] allGs) {
		for (int j = 0; j < bigArr.length; j++) {
			int counter = 0;
			for (int k = 0; k < bigArr[j].length; k++) {
				if (bigArr[j][k]!=0) {
					counter++;
				}
			}
			
			for (int k = 0; k < bigArr[j].length; k++) {
				if (bigArr[j][k]!=0 && k==5) {
					double tf = (double)bigArr[j][k];
					double idf = Math.log((double)bigArr[j].length/(double)counter);
					if (tf*idf>4) {
						System.out.println("Doc "+k+" "+allGs[j]+" "+ tf*idf);
					}
				}
			}
		}
	}
	public static void main(String[] args) throws IOException {
		TfIdf tfIdf = new TfIdf();
		tfIdf.calculateWordRarity();
	}
}
