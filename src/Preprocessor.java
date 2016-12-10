/**
 * Created by rayyeon on 12/5/16.
 */

import javax.swing.text.StyledEditorKit;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.io.FileReader;
import java.nio.file.Paths;

public class Preprocessor {
    private static final String  pF= "preprocessFile.c";
    public static void main(String[] args) throws IOException
    {


        File outputFile = new File("temp.c");
        File outputPre = new File(pF);


        PrintStream output = new PrintStream(outputFile);
        PrintStream outPre = new PrintStream(outputPre);

        StringBuilder sb = new StringBuilder();

        try{
            File file = new File(
                    "./src/a_test.c");
            Scanner input = new Scanner(file);

            boolean seperateLineCommentFlag = false;
            while(input.hasNext())
            {
                String line = input.nextLine();
                if(line.length() != 0) {
                    String[] words = line.split(" ");

                    /*Detect Define*/
                    if( words.length>0 && words[0].equals("#define")){
                        outPre.println(line);
                        continue;
                    }

                    /*Detect Include*/
                    if (words.length>0 && words[0].equals("#include")){
//                        String s = words[1];
                        if(words[1].startsWith("\"")){

//                            Path path = FileSystems.getDefault().getPath(words[1]);


//                            appendFileToOriginal(words[1],pF);
                            appendFileToOriginal("./src/e_file.h",pF);

                            continue;
                        }
                        outPre.println(line);
                        continue;
                    }

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

    }


    public static void appendFileToOriginal(String input, String output) throws FileNotFoundException{
        BufferedWriter bw = null;
        FileWriter fw = null;


        try {

            FileReader fr = new FileReader(input);
            Scanner in = new Scanner(fr);
            File outfile = new File(output);

            // if file doesnt exists, then create it
            if (!outfile.exists()) {
                outfile.createNewFile();
            }

            fw = new FileWriter(outfile.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);

//            int c;
            while(in.hasNext()){
                String line = in.nextLine();
                bw.write(line);
            }
        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

//                if (fr != null)
//                    fr.close();

            } catch (IOException ex) {
                System.out.print("Here is where you are wrong!");
//                ex.printStackTrace();
            }
        }
        System.out.print("Success!");
    }

    public static boolean isNumber(String s){
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            try{
                Double.parseDouble(s);
            }
            catch (NumberFormatException e1){
                try{
                    Float.parseFloat(s);
                }
                catch (NumberFormatException e2){
                    return false;
                }
                return true;
            }
            return true;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
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
