/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpeg;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author mahmoudsaeed
 */
public class Jpeg {

    static HashMap<String, String> getBinaryCode = new HashMap<String, String>();

    public static String convertBinary(String n) {

//        getBinaryCode.get(n);
        return (getBinaryCode.get(n));
    }

    public static String getDecimal(String n) {
        Iterator iterator = getBinaryCode.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry me2 = (Map.Entry) iterator.next();
            if (me2.getValue().equals(n)) {
                return (String) me2.getKey();
            }
        }
        return "";
    }

    public static void compress() throws IOException {
        List<String> fileStream = Files.readAllLines(Paths.get("compress.txt"));
        int noOfLines = fileStream.size();
        int count = 0;
        ArrayList<String> stringBuffer = new ArrayList<>();
        ArrayList<String> intBuffer = new ArrayList<>();
        String[] c = null;
        for (String line : fileStream) {
            c = line.split(",");
        }
        int num = 0;
        for (String c1 : c) {
            num++;
            if (c1.equals("0")) {
                count++;
            } else {
                boolean bool = false;
                if (c1.equals("-1") || c1.equals("1")) {
                    bool = true;
                }
                if (c1.length() > 1) {
                    if (!bool) {
                        stringBuffer.add(count + "/" + "2" + "");
                    } else {
                        stringBuffer.add(count + "/" + c1.charAt(1) + "");
                    }
                    intBuffer.add(c1);
                    count = 0;
                } else {
                    if (!bool) {
                        stringBuffer.add(count + "/" + "2" + "");
                    } else {
                        stringBuffer.add(count + "/" + c1 + "");
                    }
                    intBuffer.add(c1);
                    count = 0;
                }
            }
            if (c1.equals("0") && num == c.length - 1) {
                stringBuffer.add("eob");
                intBuffer.add("0");
            }
        }
        FileWriter fileWriter = new FileWriter("todecompress.txt");
//        System.out.println(stringBuffer.size());
        StandarHufman standar = new StandarHufman();
        ArrayList<String> returnBuffer = new ArrayList<>();
        ArrayList<String> compress = new ArrayList<>();
        returnBuffer = standar.main(stringBuffer);
        for (int i = 0; i < stringBuffer.size(); i++) {
            for (int j = 0; j < returnBuffer.size(); j++) {
                String[] s1;
                s1 = returnBuffer.get(j).split(":");
                if (stringBuffer.get(i).equals(s1[0])) {
                    compress.add(s1[1]);
                    compress.add(",");
                }
            }
            compress.add(convertBinary(intBuffer.get(i)));
            compress.add(",");
        }
        for (int i = 0; i < returnBuffer.size(); i++) {
            fileWriter.write(returnBuffer.get(i));
            fileWriter.write("/n");
        }
        String compressFile = "";
        for (int i = 0; i < compress.size(); i++) {
            compressFile += compress.get(i);
        }
        fileWriter.write(compressFile);
        fileWriter.close();
    }

    public static void deCompress() throws FileNotFoundException, IOException {
//        BufferedReader reader;
//        reader = new BufferedReader(new FileReader("decompress.txt"));
//        String line = reader.readLine();
        int lineNumber = 0;
        String[] c1 = null;
        ArrayList<String> stringBuffer = new ArrayList<>();
        ArrayList<String> intBuffer = new ArrayList<>();
        List<String> fileStream = Files.readAllLines(Paths.get("decompress.txt"));
        int noOfLines = fileStream.size();
        for (String line : fileStream) {
            if (lineNumber == noOfLines - 1) {
//                System.out.println("ok");
                c1 = line.split(",");
            } else {
                String c[] = line.split(":");
                stringBuffer.add(c[0]);
                intBuffer.add(c[1]);
            }
            lineNumber++;
        }
        ArrayList<String> decompress = new ArrayList<>();
        for (int i = 0; i < c1.length; i++) {
            //if this index first index
            if (i % 2 == 0) {
                for (int j = 0; j < intBuffer.size(); j++) {
                    if (c1[i].equals(intBuffer.get(j))) {
                        if (stringBuffer.get(j).equals("eof")) {

                            decompress.add("eof");
                        } else {
                            int countOfZeros = stringBuffer.get(j).charAt(0) - '0';
                            String numOfZeros = "";
                            for (int k = 0; k < countOfZeros; k++) {
                                numOfZeros += "0";
                            }
                            decompress.add(numOfZeros);
                            decompress.add(",");
                        }
                    }
                }
            } else {
                String additionNumber;
                decompress.add(getDecimal(c1[i]));
                decompress.add(",");

            }
        }
        String decompressFile = "";
        for (int i = 0; i < decompress.size(); i++) {
            decompressFile+=decompress.get(i);
        }
        //get hash table from user
        FileWriter fileWriter = new FileWriter("tocompress.txt");
        fileWriter.write(decompressFile);
        fileWriter.close();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        getBinaryCode.put("1", "1");
        getBinaryCode.put("-1", "0");
        getBinaryCode.put("2", "10");
        getBinaryCode.put("-2", "01");
        getBinaryCode.put("3", "11");
        getBinaryCode.put("-3", "00");
        getBinaryCode.put("4", "100");
        getBinaryCode.put("-4", "011");
        getBinaryCode.put("5", "101");
        getBinaryCode.put("-5", "010");
        getBinaryCode.put("6", "110");
        getBinaryCode.put("-6", "001");
        getBinaryCode.put("7", "111");
        getBinaryCode.put("-7", "000");
        compress();
        deCompress();

    }

}
//bootstrap 4 
