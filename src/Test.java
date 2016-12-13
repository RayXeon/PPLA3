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
        for(int i = 0; i< 30; i++){
            Replacement(tempFile,finalFile);
            Replacement(finalFile,tempFile);
        }
    }

    public static void Replacement(String inFile, String outFile) throws  FileNotFoundException{
        Pattern pattern1 = Pattern.compile("([A-Z]+(_[0-9]+)*(_[A-Z]+)*)+");
        Pattern pattern2 = Pattern.compile("(\"[A-Z]+[^\"]*\")");
        Pattern pattern3 = Pattern.compile("(\'[A-Z]+[^\']*\')");


        File newfile = new File(inFile);
        File finalfile = new File(outFile);

        PrintStream output = new PrintStream(finalfile);
        Scanner lastIn = new Scanner(newfile);
        int index = 0;
        while(lastIn.hasNext()){
            String finalLine = lastIn.nextLine();
            Matcher matcher1 = pattern1.matcher(finalLine);
            Matcher matcher2 = pattern2.matcher(finalLine);
            Matcher matcher3 = pattern3.matcher(finalLine);

            String newlineOFfinal ;
            if(finalLine.length()!=0){
                System.out.println("Ori :: " + finalLine);
                if( (matcher1.find() && !matcher2.find()&& !matcher3.find())){

                    String str1 = matcher1.group();

                    if(myTable.containsKey(str1)) {

                        String myValue = myTable.get(str1);
                        System.out.println("This is value " + myTable.get(str1));
                        newlineOFfinal = matcher1.replaceFirst(myValue);
                        output.println(newlineOFfinal);
                        continue;
                    }
                    System.out.println("Result :: " + str1);
                }
                else{
                    System.out.println("RE :: ");
                }
                output.println(finalLine);
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
                                myTable.put(new String(words[1]), new String(words[2]));
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
