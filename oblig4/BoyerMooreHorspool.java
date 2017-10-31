/*
    Algoritmen er STERKT inspirert av slides fra forelesningen
*/

import java.util.ArrayList;

public class BoyerMooreHorspool{
    private static int MAX_CHAR = 256;

    /**
    * @param    needle      Naalen metoden skal lete etter i heystack
    * @param    heystack    En "blokk" av characters, som vi skal lete etter naalen i
    * @return               Plassering (forste character) av forekomstene til needle i heystack
    */
    public static ArrayList<Integer> boyerMooreHorspool(char[] needle, char[] haystack){
        ArrayList<Integer> plassering = new ArrayList<Integer>();
        boolean[] exists = new boolean[MAX_CHAR];
        int[] badCharShift = new int[MAX_CHAR];
        int offset = 0;

        //Grunner til aa ikke gaa videre i algoritmen
        if (needle.length > haystack.length) return plassering; //Needle kan ikke passe i Haystack, dersom den er storre
        if (needle.length == 1) return finnEnBokstav(haystack, needle[0]); // Ikke noe poeng aa gaa gjennom alt, dersom


        //Setter maks forflyning lik lengden paa naalen for alle bokstaver
        for (int i = 0; i < badCharShift.length; i++){
            badCharShift[i] = needle.length;
            exists[i] = false;
        }

        //Setter maksimal forflytning for alle bokstavene i needle
        int siste = needle.length - 1;
        for (int i = 0; i < siste; i++){
            badCharShift[ (int) needle[i] ] = siste - i;
            exists[ (int) needle[i] ] = true;
        }

        int maxOffset = haystack.length - needle.length;
        while (offset <= maxOffset){
            funnet:
            if (exists[haystack[offset]]){
                for (int check = siste; (needle[check] == haystack[offset + check]) || (needle[check] == '_'); check--){
                    if (check == 0){
                        System.out.println("Satt inn: " + offset);
                        plassering.add(offset);
                        break funnet;
                    }
                }
            }
            offset += badCharShift[haystack[offset + siste]];
        }
        return plassering; //returnerer alle plassseringer funnet
    }

    private static ArrayList<Integer> finnEnBokstav(char[] haystack, char needle){
        ArrayList<Integer> ret = new ArrayList<Integer>();
        for (int i = 0; i < haystack.length; i++){
            if (haystack[i] == needle || needle == '_'){
                ret.add(i);
            }
        }
        return ret;
    }
}
