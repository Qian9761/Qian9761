import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.huaban.analysis.jieba.JiebaSegmenter;


public class DC {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 Scanner read = new Scanner(System.in);
         System.out.println("文件1路径:");
         String file1 = read.nextLine();
         System.out.println("文件2路径:");
         String file2 = read.nextLine();
         
         String content1 = readfile(file1,1);
         String content2 = readfile(file2,2);
         System.out.println("输出结果路径:");
         String resultpath = read.nextLine();
         
         Map<String, Integer> vector1 = WordFrequency(content1);
         Map<String, Integer> vector2 = WordFrequency(content2);
         
         double similarity = CCS(vector1, vector2);
         String result1=String.format("%.2f", similarity);
         result1="相似度为:"+result1;
         System.out.println(result1);
         FileOutputStream file;
		 try {
			 file = new FileOutputStream(resultpath);
	
			 byte[] bs = result1.getBytes();
			 file.write(bs);
			 file.close();
		 } catch (FileNotFoundException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		 } catch (IOException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		 }
        

	}
	
	public static String readfile(String file,int i){
		File f=new File(file);
		String str=null;
		try (FileInputStream fis=new FileInputStream(f)){
			byte[] bytes=new byte[(int) f.length()];
			fis.read(bytes);
//			window记事本默认编码方式是gbk,如果保存文件以UTF-8保存，便以UTF-8读出
//			String str = new String(bytes,"GBK");
            str = new String(bytes,"UTF-8");
            System.out.println("文件"+i+":"+str);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return str;
	}
	public static Map<String, Integer> WordFrequency(String text) {
        Map<String, Integer> vector = new HashMap<>();
         
        
        JiebaSegmenter segmenter = new JiebaSegmenter();
        String sentences = text;
        List<String> content=segmenter.sentenceProcess(sentences);
        String analysisedText=String.join(",",content);
        //System.out.println(analysisedText);
        
        String[] words=analysisedText.replaceAll( "\\pP", " ").split("\\s+");;
        
        //System.out.println(words);
        
		 for(String s:words){
			 //System.out.println(s);
			 s = s.toLowerCase(); // 转换为小写
	         vector.put(s, vector.getOrDefault(s, 0) + 1);
		 }
        return vector;
    }
	private static double CCS(Map<String, Integer> hash1, Map<String, Integer> hash2) {
        double numerator = 0.0;
        double n1 = 0.0;
        double n2 = 0.0;

        for (String word : hash1.keySet()) {
            if (hash2.containsKey(word)) {
            	numerator += hash1.get(word) * hash2.get(word);
            }
            n1 += Math.pow(hash1.get(word), 2);
        }

        for (String word : hash2.keySet()) {
            n2 += Math.pow(hash2.get(word), 2);
        }

        if (n1 == 0 || n2 == 0) {
            return 0.0; 
        }
        return numerator / (Math.sqrt(n1) * Math.sqrt(n2));
    }
}
