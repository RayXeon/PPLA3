/**
 * Created by rayyeon on 12/5/16.
 */

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Test {
    private static final String tempFile= "temp.c";
    private static final String almostFile = "almost.c";
    private static final String closeFile = "close.c";
    private static final String finalFile = "final.c";
    private static Hashtable<String, String> myTable = new Hashtable<String, String>();

    public static void main(String[] args) throws IOException
    {

        removeComment("./src/a_test.c",tempFile);

        for(int i= 0; i< 20; i++) {
            removeDefine(tempFile, almostFile);
            removeInclude(almostFile, closeFile);
            removeComment(closeFile, tempFile);
        }

        String str1;
        Enumeration<String> namesq = myTable.keys();
        int idex = 1;
        while(namesq.hasMoreElements()) {
            str1 =  namesq.nextElement();
            System.out.println(idex +": " + str1 + ": " + myTable.get(str1));
            idex++;
        }


        for(int i = 0; i< 4; i++){
            Replacement(tempFile,finalFile);
            Replacement(finalFile,tempFile);
        }
        Pattern pattern1 = Pattern.compile("\\s+");
        Pattern pattern2 = Pattern.compile("(\\s\\+)");


        File in = new File(tempFile);
        File out = new File(finalFile);
        PrintStream output = new PrintStream(out);
        Scanner input = new Scanner(in);
        while(input.hasNext()){
            String outline = input.nextLine();
            Matcher matcher1 = pattern1.matcher(outline);

            if(matcher1.find()){
                String newString = matcher1.replaceAll(" ");
                Matcher matcher2 = pattern2.matcher(newString);
                newString = matcher2.replaceAll("+");

                System.out.println("OUTPUT: " + newString);
                output.println(newString);
                continue;
            }
        }
    }


    public static void Replacement(String inFile, String outFile) throws  FileNotFoundException{
        Pattern pattern1 = Pattern.compile("([A-Z]+(_[0-9]+)*(_[A-Z]+)*)+");
        Pattern pattern2 = Pattern.compile("(\"[A-Z]+[^\"]*\")");
        Pattern pattern3 = Pattern.compile("(\'[A-Z]+[^\']*\')");
        String newLine1 = new String();
        String newLine2 = new String();
        String newLine3 = new String();
        String newLine4 = new String();
        String newLine5 = new String();


        File newfile = new File(inFile);
        File finalfile = new File(outFile);

        PrintStream output = new PrintStream(finalfile);
        Scanner input = new Scanner(newfile);

        while(input.hasNext()){
            String newLine0 = input.nextLine();



            if(newLine0.length()!=0){

                newLine1 = newLine0.replaceAll("\\(","\\( ");
                newLine2 = newLine1.replaceAll("\\)"," \\)");
                newLine3 = newLine2.replaceAll(";"," ;");
                newLine4 = newLine3.replaceAll(",", " , ");
                newLine5 = newLine4.replaceAll("\\+"," \\+");
//                newLine6 = newLine5.replaceAll("\\+ \\+", "++");
//                Matcher matcher1 = pattern1.matcher(newLine4);
//                Matcher matcher2 = pattern2.matcher(newLine4);
//                Matcher matcher3 = pattern3.matcher(newLine4);

//                System.out.println("Ori :: " + newLine4);

                String[] words = newLine5.split(" ");
                String tempString = newLine5;
                for(String s : words){
                    if(myTable.containsKey(s)){
//                        System.out.println("Catched word: " + s);
                        tempString = tempString.replaceAll(s, myTable.get(s));
//                        output.println("Come from 2");
//                        output.println(tempString);
//                        System.out.println("Result :: " + tempString);
                        break;
                    }
                }
//                output.println("Come from 1");
                output.println(tempString);

                continue;
            }

        }
    }

    public static void removeComment(String inFile, String outFile) throws FileNotFoundException{
        File outputFile = new File(outFile);

        PrintStream output = new PrintStream(outputFile);
        try{
            File file = new File(
                    inFile);
            Scanner input = new Scanner(file);

            boolean seperateLineCommentFlag = false;
            while(input.hasNext())
            {
                String line = input.nextLine();
                if(line.length() != 0) {
                    String[] words = line.split("\\s+");
                    /*Single line comment detection*/
                    if(commentTest3(words))
                        continue;
                    if(commentTest1(words) == 3) {
                        output.println(wordsWithoutCommentEntail(words));
                        continue;
                    }
                    if(commentTest1(words) == 1){
                        seperateLineCommentFlag = true;
                        continue;
                    }
                    if(commentTest1(words) == 2){
                        seperateLineCommentFlag = false;
                        continue;
                    }
                    if(seperateLineCommentFlag)
                        continue;
                }
                output.println(line);
            }
        }
        catch (java.io.FileNotFoundException ex){
            System.out.println("Something is Wrong!");
        }
//        System.out.println("The comments are removed!");
    }

    public static void removeDefine(String inFile, String outFile) throws FileNotFoundException{
        File outputFile = new File(outFile);

        PrintStream output = new PrintStream(outputFile);
        Pattern pattern = Pattern.compile("[A-Z]+");

        try{
            File file = new File(
                    inFile);
            Scanner input = new Scanner(file);

            while(input.hasNext())
            {
                String line = input.nextLine();
                if(line.length() != 0) {
                    String[] words = line.split("\\s+");

                    /*Detect Define*/
                    if( words.length>0 && words[0].equals("#define")){

                            Matcher matcher = pattern.matcher(words[1]);
                            if (matcher.find()) {
                                if(words.length<4) {
                                    myTable.put(new String(words[1]), new String(words[2]));
                                    continue;
                                }
                                else{
                                    StringBuilder builder = new StringBuilder();
                                    for(int i= 2; i< words.length; i++) {
                                        builder.append(words[i]);
                                        builder.append(" ");
                                    }
                                    myTable.put(new String(words[1]), new String(builder));
                                    continue;
                                }
                            }
                        output.println(line);
                        continue;
                    }

                }
                output.println(line);
            }
        }
        catch (java.io.FileNotFoundException ex){
            System.out.println("Something is Wrong in removing define!");
        }

    }

    public static void removeInclude(String inFile, String outFile) throws FileNotFoundException{
        File outputFile = new File(outFile);
        PrintStream output = new PrintStream(outputFile);
        Pattern pattern = Pattern.compile("[A-Z]+");

        try{
            File file = new File(
                    inFile);
            Scanner input = new Scanner(file);

            while(input.hasNext())
            {
                String line = input.nextLine();
                if(line.length() != 0) {
                    String[] words = line.split("\\s+");

                    /*Detect Include*/
                    if (words.length>0 && words[0].equals("#include")){
                        if(words[1].startsWith("\"")){

                            String withoutQuotes_path = words[1].replace("\"", "");
                            try {
                                File fr = new File(withoutQuotes_path);
                                Scanner in = new Scanner(fr);
                                while(in.hasNext()){
                                    String lineofnewFile = in.nextLine();
//                                    String[] lineNew = lineofnewFile.split(";");
//                                    for(int i = 0; i < lineNew.length; i++) {
//                                        output.println(lineNew[i]);
//                                    }
                                    output.println(lineofnewFile);
                                }
                            }
                            catch (IOException e){
                                System.out.print("Something wront in get another file.");
                            }
                            continue;
                        }
                        Matcher matcher = pattern.matcher(words[1]);
                        if(matcher.find()) {
                            output.println(words[0] + " " + myTable.get(words[1]));
                            continue;
                        }
                        output.println(line);
                        continue;
                    }
                }
                output.println(line);
            }
        }
        catch (java.io.FileNotFoundException ex){
            System.out.println("Something is Wrong in removing include!");
        }
    }

    public static int commentTest1(String[] s){

        int head = 0;
        int tail = 0;
        int allget = 0;
        for(int i =0; i< s.length; i++)
        {
            if(s[i].equals("/*"))
                head = 1;
            if(s[i].equals("*/"))
                tail = 2;
        }
        //allget: 1 comment start, 2 commet finish, 3 comment complete in one line;
        allget = head + tail;
        return allget;
    }

    public static String wordsWithoutCommentEntail(String[] s){
        int index = 0;
        for(int i = 0; i < s.length; i++)
        {
            if(s[i].equals("/*"))
                index = i;
        }
        StringBuilder trimedString = new StringBuilder();

        for(int i = 0; i < index; i++)
        {
            trimedString.append(s[i]);
            trimedString.append(" ");
        }

        String newString = trimedString.toString();
        return newString;
    }

    public static boolean commentTest3(String[] s){
        boolean commenttest = false;
        for(int i =0; i< s.length; i++)
        {
            if(s[i].equals("//"))
                commenttest = true;
        }
        return commenttest;
    }
}
