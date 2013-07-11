/**
 * 
 */
package com.vikasing.nicetext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author vikasing
 *
 */
public class TfIdf {
	private static final String DATA_DIR = "data";
	
	void calculateWordRarity() throws IOException{
		File file = new File(DATA_DIR);
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
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
				int tSize = text.split(" ").length;
				
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			finally {
				in.close();iReader.close();fileStream.close();
				
			}
		}
	}
	public static void main(String[] args) throws IOException {
		TfIdf tfIdf = new TfIdf();
		tfIdf.calculateWordRarity();
	}
}
