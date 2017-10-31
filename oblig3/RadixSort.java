import java.util.Arrays;

public class RadixSort{
    /** Sorter  a[] stigende - antar at: 0 <= a[i] < 2^(32) , returner tid i milliSek */

    final static int NUM_BIT = 8; // eller: 6 - 13 (Finn ut hva som er raskest)
    final static int MIN_NUM = 31; // 16 - 60 , quicksort bruker 47

    public double vRadixMulti(int[] a){
        long t = System.nanoTime();
        int[] b = new int[a.length];

        int max = -1;
        int numBit = 0;

        // a) finn hoyeste verdi
        for (int x : a){
            if (max < x){
                max = x;
            }
        }

        // b) bestem numBit = hoyeste (mest venstre) bit i 'max' som == 1
        while (max >= (1 << numBit)) numBit++;

        // c) Forste kall (rot-kallet) paa venstreRadix med a[], b[], numBit og lengden av forste siffer
        if (numBit < NUM_BIT) {
            venstreRadix(a, b, 0, a.length, numBit, numBit);
        } else {
            venstreRadix(a, b, 0, a.length, numBit, NUM_BIT);
        }

        // Passer paa at det mest sorterte arrayet er i a
        int aVal = a[0];
        int bVal = b[0];
        int i = 1;
        while (aVal == bVal && i < a.length){
            aVal = a[i];
            bVal = b[i++];
        }

        if (bVal < aVal){
            a = b;
        }

        double tid = (System.nanoTime() - t)/1000000.0;
        testSort(a);

        return tid;     //Returnerer tiden i ms. det tok aa sortere a
    }


    //Sorterer a[left...right] paa siffer med start i bit : leftSortBit, og med lende maskLen bit
    public void venstreRadix(int[] a, int[] b, int left, int right, int leftSortBit, int maskLen){
        // System.out.printf("leftSortBit: %2d | maskLen: %2d\n", leftSortBit, maskLen);
        int mask = (1 << maskLen) -1;
        int[] count  = new int[mask + 1];
        int[] newPos = new int[mask + 1];
        int placement = left;
        int shift = 0;

        if (leftSortBit > maskLen) {
          shift = leftSortBit - maskLen;
        }

        // d) count[] = hvor mange det er av de ulike verdiene av dette sifferet i a[left...right]
        for (int i = left; i < right; i++){
            int current = mask & (a[i] >> shift);
            count[current]++;
        }

        // e) Tell opp verdiene i count[] slik at count[i] sier hvor i b[] vi skal flytte
        //    det forste elementet med verdien 'i' som vi sorterer.
        for (int i = 0; i < count.length; i++){
            newPos[i] = placement;
            placement += count[i];
        }

        // f) Flytt tallene fra a[] til b[], sorter paa dette sifferet i a[left...right] for alle
        //    de ulike verdiene for dette sifferet
        for (int i = left; i < right; i++){
            int current = mask & (a[i] >> shift);
            b[newPos[current]++] = a[i];
        }

        // g) Kall enten instikkSort eller rekursivt venstreRadix() paa neste siffer
        //    (hvis vi ikke er ferdige) for alle verdiene vi har av naaverende siffer
        leftSortBit -= maskLen;
        for (int i = 0; i < count.length; i++){
            if (count[i] > MIN_NUM && shift > 0){
                venstreRadix(b, a, (newPos[i]-count[i]), newPos[i], leftSortBit, maskLen);
            }else if (shift > 0){
                InnstikkSort.sort(b, (newPos[i]-count[i]), newPos[i]);
            }
        }
        copyArray(a, b, left, right);
    }

    private void copyArray(int[] a, int[] b, int left, int right){
        for (int i = left; i < right; i++){
            a[i] = b[i];
        }
    }

    public void testSort(int[] a){
        for (int i = 0; i < a.length - 1; i++){
            if(a[i] > a[i + 1]){
                System.out.println("\nFEIL: (" + i + ") - a[" + i + "]: " + a[i] + " > a[" + (i + 1) +"]: " + a[i + 1]);
                return;
            }
        }
    }
}
