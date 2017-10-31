/*
    Her leser jeg kun inn fra fil.
    Se filen BoyerMooreHorspool.java for algoritmen
*/
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;
import java.io.FileNotFoundException;
import java.io.File;

public class AssignmentFour{
    public static void main(String[] args) {
        char[] needle;
        char[] haystack;
        try{
            //Reading from file: "Needle"
            Scanner sc = new Scanner(new File(args[0]));
            ArrayList<Character> tmp = new ArrayList<Character>();
            while (sc.hasNextLine()){
                String newString = sc.nextLine();
                for (int i = 0; i < newString.length(); i++){
                    tmp.add(newString.charAt(i));
                }
            }

            //Placing info from file into needle
            needle = new char[tmp.size()];
            for (int i = 0; i < tmp.size(); i++){
                needle[i] = tmp.get(i);
            }

            //Reading from file: "Heystack"
            sc = new Scanner(new File(args[1]));
            tmp = new ArrayList<Character>();
            while (sc.hasNextLine()){
                String newString = sc.nextLine();
                for (int i = 0; i < newString.length(); i++){
                    tmp.add(newString.charAt(i));
                }
            }

            //Placing info from file into haystack
            haystack = new char[tmp.size()];
            for (int i = 0; i < tmp.size(); i++){
                haystack[i] = tmp.get(i);
            }

        }catch(FileNotFoundException|ArrayIndexOutOfBoundsException err){
            if (err instanceof ArrayIndexOutOfBoundsException){
                System.out.println("Vennligst kjor filen korrekt ved aa skrive:");
                System.out.println("java AssignmentFour <needle> <heystack>\n");
            }

            if (err instanceof FileNotFoundException){
                System.out.println(err);
                System.out.println("Skriv inn et gydige filnavn\n");
            }
            return;
        }

        if (needle.length == 0){
            System.out.println("Ingen needle funnet i: " + args[0]);
            return;
        }

        if (haystack.length == 0){
            System.out.println("Ingen haystack funnet i: " + args[1]);
            return;
        }

        //Gjor kall paa BoyerMooreHorspool
        ArrayList<Integer> needleInHaystack = BoyerMooreHorspool.boyerMooreHorspool(needle, haystack);

        //Skriver ut possisjoner dersom plassering ble funent. Ellers skrives feilmelding
        if (needleInHaystack.size() == 0){
            System.out.println("Needle finnes ikke i Haystack");
            return;
        }

        System.out.println("________________________________________");
        for (int i = 0; i < needleInHaystack.size(); i++){
            String needleString = "";
            for (int j = 0; j < needle.length; j++){
                needleString += haystack[needleInHaystack.get(i) + j];
            }
            System.out.println("Fant needle i " + needleInHaystack.get(i) + " -> " + needleString);
        }
        System.out.println("________________________________________");
    }
}
