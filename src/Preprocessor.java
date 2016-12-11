/**
 * Created by rayyeon on 12/5/16.
 */

import javax.swing.text.StyledEditorKit;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Preprocessor {
    private static final String tempFile= "temp.c";
    private static final String almostFile = "almost.c";
    private static final String closeFile = "close.c";
    private static final String finalFile = "final.c";
    private static Hashtable myTable = new Hashtable();
    public static void main(String[] args) throws IOException
    {

        removeComment("./src/a_test.c",tempFile);
        removeDefine(tempFile, almostFile);
//        System.out.println(myTable.get("A_FILE"));
        removeInclude(almostFile, closeFile);
        removeInclude(closeFile,finalFile);
        removeDefine(finalFile, tempFile);
        removeInclude(tempFile, closeFile);
        removeDefine(closeFile, almostFile);
        removeComment(almostFile,tempFile);
        removeInclude(tempFile, finalFile);
        removeDefine(finalFile,tempFile);
        String str;
        Enumeration names = myTable.keys();
        while(names.hasMoreElements()) {
            str = (String) names.nextElement();
            System.out.println(str + ": " + myTable.get(str));
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
                    String[] words = line.split(" ");
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
        System.out.println("The comments are removed!");
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
                    String[] words = line.split(" ");

                    /*Detect Define*/
                    if( words.length>0 && words[0].equals("#define")){

                            Matcher matcher = pattern.matcher(words[1]);
                            if (matcher.find()) {
                                myTable.put(words[1], words[2]);
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

        System.out.println("The defines are removed!");
    }

    public static void removeInclude(String inFile, String outFile) throws FileNotFoundException{
        File outputFile = new File(outFile);
//
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
                    String[] words = line.split(" ");

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

        System.out.println("The includes are removed!");

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
