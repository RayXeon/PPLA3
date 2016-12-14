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

        removeComment(args[0],tempFile);

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
            fireParser(tempFile,finalFile);
            fireParser(finalFile,tempFile);
        }
    }
    public static void fireParser(String in, String out) throws IOException{
        File inputFile = new File(in);
        File outputFile = new File(out);

        PrintStream output = new PrintStream(outputFile);
        String newLine1 = new String();
        String newLine2 = new String();
        String newLine3 = new String();
        String newLine4 = new String();

        Scanner input = new Scanner(inputFile);
        while(input.hasNext()){

            String newLine0 = input.nextLine();
            if(newLine0.length()!=0){

                newLine1 = newLine0.replaceAll("\\(","\\( ");
                newLine2 = newLine1.replaceAll("\\)"," \\)");
                newLine3 = newLine2.replaceAll(";"," ;");
                newLine4 = newLine3.replaceAll(",", " , ");
                System.out.println("Before split: " + newLine4);
                String[] words = newLine4.split(" ");

                StringBuilder builder = new StringBuilder();
                for(int i= 0; i< words.length; i++) {
                    builder.append(ReplacementOfString(words[i]));
                    builder.append(" ");
                }
                System.out.println("The new String  :: " + builder +"\n");
                output.println(builder);
                continue;
            }
        }
    }

    public static String ReplacementOfString(String inFile) throws  FileNotFoundException{
        Pattern pattern1 = Pattern.compile("(([A-Za-z]+)(_[a-zA-Z0-9]+)*)+");
        Pattern pattern = Pattern.compile("(\"[^\"]*\")|(\"[^\"]*)");

        Matcher matcher1 = pattern1.matcher(inFile);
        Matcher matcher = pattern.matcher(inFile);
        if(matcher1.find() && !matcher.find()){
            String string = matcher1.group(1);
            System.out.println("Catched the words :: " + string );
            if(myTable.containsKey(string)) {
                System.out.println("The value of  table is: " + myTable.get(string));
//                System.out.println("Changing to : " + matcher1.replaceFirst(myTable.get(string)));
                System.out.println("Change to:" + inFile.replace(string, myTable.get(string)));
//                return matcher1.replaceAll(myTable.get(string));
                return inFile.replace(string, myTable.get(string));
            }
        }
        return inFile;
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
                    if(commentTest3(words)){
                        if(words[0].equals("//"))
                            continue;
                        else{
                            output.println(wordsWithoutCommentEntail(words));
                            continue;}
                    }

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
    }

    public static void removeDefine(String inFile, String outFile) throws FileNotFoundException{
        File outputFile = new File(outFile);

        PrintStream output = new PrintStream(outputFile);
//        Pattern pattern = Pattern.compile("[A-Z]+");

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

//                            Matcher matcher = pattern.matcher(words[1]);
//                            if (matcher.find()) {
                                if(words.length<=4) {
                                    myTable.put(new String(words[1]), new String(words[2]));
                                    continue;
                                }
                                if(words.length>4){
                                    StringBuilder builder = new StringBuilder();
                                    for(int i= 2; i< words.length; i++) {
                                        builder.append(words[i]);
                                        builder.append(" ");
                                    }
                                    myTable.put(new String(words[1]), new String(builder));
                                    continue;
                                }
//                            }
//                        output.println(line);
//                        continue;
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
                                    output.println(lineofnewFile);
                                }
                            }
                            catch (IOException e){
                                System.out.println("Something wront in get another file.");
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
        int index1 = -1;
        int index2 = -1;
        int index3 = -1;
        for(int i = 0; i < s.length; i++)
        {
            if(s[i].equals("/*"))
                index1 = i;
            if(s[i].equals("*/"))
                index2 = i;
            if(s[i].equals("//"))
                index3 = i;

        }
        StringBuilder trimedString = new StringBuilder();

        for(int i = 0; i < s.length; i++)
        {
            if((i >= index1 && i <= index2)||(index3 >0 && i >= index3)){
                continue;
            }
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
