package compiler_porj2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 *
 * Moyaser Tamim 1539152
 * 
 * Faisal Ala'amri 1537405
 *
 *
 */
public class Compiler_porj2 {

    static int number_Of_Tokens = 0;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        String[][] parseTable = SLR_ParseTable();
        File inputFile = new File("input.txt");// input file name
        File outputFile = new File("output.txt");
        PrintWriter write = new PrintWriter(outputFile);
        Scanner read = new Scanner(inputFile);
        Stack stack = new Stack();
        stack.push("0");

        String input = read.next();
         write.println("Input String : " + input);

        String s = "";
        for (int i = 0; i < input.length(); i++) {

            if (input.charAt(i) == 'i') {
                s += 'i';
                continue;
            }
            s += input.charAt(i) + " ";
//             write.println(s);
        }

        StringTokenizer st = new StringTokenizer(s);
        int tokenCount = st.countTokens();
        String str = st.nextToken();

         write.println("\nStack\t\t\t\t\t    Input\t\tAction");

        while (true) {

            int column = -1;
            String number = "";
            if (str.equals("id")) {
                column = 0;
            } else if (str.equals("+")) {
                column = 1;
            } else if (str.equals("*")) {
                column = 2;
            } else if (str.equals("(")) {
                column = 3;
            } else if (str.equals(")")) {
                column = 4;
            } else if (str.equals("$")) {
                column = 5;
            } else if (str.equals("E")) {
                column = 6;
            } else if (str.equals("T")) {
                column = 7;
            } else if (str.equals("F")) {
                column = 8;
            } else {
                 write.println("Syntax Error");
                System.exit(0);
            }

            String action = parseTable[Integer.parseInt((String) stack.peek())][column];

//             write.println("act: "+action);
            if (action.charAt(0) == 's') {

                number = parseTable[Integer.parseInt((String) stack.peek())][column].substring(1);
//                 write.println("number: "+number);
                String ri = remainingInput(s, number_Of_Tokens, tokenCount);
                number_Of_Tokens++;
                 write.printf("%-30s %20s %s  %s\n", stack.toString(), ri, "Shift by  ", action);
                stack.push(str);
                stack.push(number);
                str = st.nextToken();

//                 write.println("next token: "+str);
            } else if (action.charAt(0) == 'r') {

                number = parseTable[Integer.parseInt((String) stack.peek())][column].substring(1);
//                 write.println("number: " + number);
                String rule = rule(action);
                String rin = remainingInput(s, number_Of_Tokens, tokenCount);

                 number = parseTable[Integer.parseInt((String) stack.peek())][column].substring(1);

                 rule = rule(action);
                 rin = remainingInput(s, number_Of_Tokens, tokenCount);
                  write.printf("%-30s %20s %s %s %s %s %s\n", stack.toString(), rin, "Reduce by  ", rule, "(", action, ")");
                
                int popTimes = 6;
                if (Integer.parseInt(number) % 2 == 0) {
                    popTimes = 2;
                }
                for (int i = 0; i < popTimes; i++) {
                    stack.pop();
                }

                String ls = "";
                switch (Integer.parseInt(number)) {
                    case 1:
                        ls = "E";
                        break;
                    case 2:
                        ls = "E";
                        break;
                    case 3:
                        ls = "T";
                        break;
                    case 4:
                        ls = "T";
                        break;
                    case 5:
                        ls = "F";
                        break;
                    case 6:
                        ls = "F";
                        break;
                }

                int indexA = -1;
                switch (ls.charAt(0)) {
                    case 'E':
                        indexA = 6;
                        break;
                    case 'T':
                        indexA = 7;
                        break;
                    case 'F':
                        indexA = 8;
                        break;
                }
             
                int num = Integer.parseInt(parseTable[Integer.parseInt((String) stack.peek())][indexA]);
                stack.push(ls + "");
                stack.push(num + "");

            } else if (action.charAt(0) == 'a') {
                   // this is the end of parsing, when it gets to [1][$] Accept : Successfully
                     write.printf("%-49s %s\n", stack.toString(), "$ ACCEPT");
                     write.flush();
                    write.close();
                    System.exit(0);
            } 

        }
    }

    public static String[][] SLR_ParseTable() {

        String[][] table = new String[12][9];

        //row 0 
        table[0][0] = "s5"; // id
        table[0][3] = "s4"; // (
        table[0][6] = "1";  // E
        table[0][7] = "2";  // T
        table[0][8] = "3";  // F

        //row 1 
        table[1][1] = "s6"; // +
        table[1][5] = "acc"; // $

        //row2
        table[2][1] = "r2"; // +
        table[2][2] = "s7"; // *
        table[2][4] = "r2"; // )
        table[2][5] = "r2"; // $

        //row3
        table[3][1] = "r4"; // +
        table[3][2] = "r4"; // *
        table[3][4] = "r4"; // )
        table[3][5] = "r4"; // $

        //row4
        table[4][0] = "s5"; // id
        table[4][3] = "s4"; // (
        table[4][6] = "8";  // E
        table[4][7] = "2";  // T
        table[4][8] = "3";  // F

        //row5
        table[5][1] = "r6"; // +
        table[5][2] = "r6"; // *
        table[5][4] = "r6";  // )
        table[5][5] = "r6";  // $

        //row6
        table[6][0] = "s5"; // id
        table[6][3] = "s4"; // (
        table[6][7] = "9";  // T
        table[6][8] = "3";  // F

        //row7
        table[7][0] = "s5"; // id
        table[7][3] = "s4"; // (
        table[7][8] = "10"; // F

        //row8
        table[8][1] = "s6"; // +
        table[8][4] = "s11";// )

        //row9
        table[9][1] = "r1";// +
        table[9][2] = "s7";// *
        table[9][4] = "r1";// )
        table[9][5] = "r1";// $

        //row10
        table[10][1] = "r3";// +
        table[10][2] = "r3";// *
        table[10][4] = "r3";// )
        table[10][5] = "r5";// $

        //row11
        table[11][1] = "r5";// +
        table[11][2] = "r5";// *
        table[11][4] = "r5";// )
        table[11][5] = "r5";// $

        return table;
    }

    public static String rule(String action) {
   
        String r = "";

        switch (action) {
            case "r1":
                r = "E → E+T";
                break;
            case "r2":
                r = "E → T";
                break;
            case "r3":
                r = "T → T*F";
                break;
            case "r4":
                r = "T → F";
                break;
            case "r5":
                r = "F → (E)";
                break;
            case "r6":
                r = "F → id";
                break;

        }
        return r;
    }

    public static String remainingInput(String inputString, int number_Of_Tokens, int tokenCount) {
   
        StringTokenizer st = new StringTokenizer(inputString);
        String remaining = "";

        for (int j = 0; j < number_Of_Tokens; j++) {
            st.nextToken();
        }

        for (int j = number_Of_Tokens; j < tokenCount; j++) {
            remaining = remaining.concat(st.nextToken());
        }

        return remaining;
    }

}
