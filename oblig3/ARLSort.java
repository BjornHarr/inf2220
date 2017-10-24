//file: ALRSort.java - 1 aug. jan 2009
/*Copyright (c) 2000,2009, Arne Maus, Dept. of Informatics, Univ. of Oslo, Norway.
All rights reserved.  Use regulated by the BSD license (full text, see below)*/

import java.util.*;  // only used in test

/** class for the ARL (Adaptive Radix Left) algorithm, sorting of integers. */
public class ARLSort{

    static int MAX_NUM_BIT=8,     // largest bit num in sorting digit(might bast be tuned between 6 and 12)
               MIN_NUM_BIT=2;
    static int INSERT_MAX =32;;  // any value below this, use Insertion sort


   /** Interface method to ARL sort, finds highest bit set*/
    public static void ARLsort(int [] a) {
     //  ARL Sort from a[left] 'up to and including'  a[right]
     if (a.length < INSERT_MAX) {
               insertSort(a,0,a.length-1);
     } else {
         int max = a[0];
         for (int i =1; i < a.length;i++)
         if ( a[i] > max) max=a[i];

         int leftBitNo=0;
          // find highest bit set
          while (max > 0) {
              max  >>= 1;
              leftBitNo++;
           }
           sortARLwithBorders(a,0,a.length-1,leftBitNo);
        } // end else
     } // end ARLsort


    /**
    *  ARL sort with border array, The 2002 version Adaptive Left Radix with Insert-sort as a subalgorithm.
    *          Sorts positive integers from a[start] 'up to and including'  a[end]
    *           on bits: leftBitNo, leftBitNo-1,..,leftBitNo -numBit+1 (31..0)
    *          Uses only internal moves by shifting along permutation cycles <br>
    *           UNSTABLE
    *
    *   @Author: Arne Maus, Dept.of Informatics,Univ. of Oslo,  2000-2009
    Copyright (c) 2000,2009, Arne Maus, Dept. of Informatics, Univ. of Oslo, Norway.
    All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions are met:
        * Redistributions of source code must retain the above copyright
          notice, this list of conditions and the following disclaimer.
        * Redistributions in binary form must reproduce the above copyright
          notice, this list of conditions and the following disclaimer in the
          documentation and/or other materials provided with the distribution.
        * Neither the name of the <organization> nor the
          names of its contributors may be used to endorse or promote products
          derived from this software without specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY ARNE MAUS, DEPT. OF INFORMATICS, UNIV. OF OSLO, NORWAY ''AS
     IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
     WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
     EVENT SHALL ARNE MAUS, BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
     OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
     SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
     ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
     NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
     ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */
     static void sortARLwithBorders( int a[], int start, int end, int leftBitNo ) {

        int  i, lim, temp,
            t1, t2, mask,
            rBitNo, numBit,
            newNum,
            nextbox,
            adr2,
            k,k2,
            num = end-start +1;

        int [] point, border;

        // adaptive part - adjust numBit : number of bits to sort on in this pass
        // a) adapts to bits left to sort to sort AND cache-size level 1 (8-32KB)
        numBit = Math.min(leftBitNo+1,MAX_NUM_BIT);
        // b) adapts to 'sparse' distribution
        while( (1<<(numBit-1)) > num && numBit > MIN_NUM_BIT)
           numBit --;
        if (numBit == leftBitNo ) numBit++; // eventually, do the last bit

        // sort on leftmost 'numBits' starting at bit no: leftBitNo
        // setting constants
        rBitNo = leftBitNo - numBit+1;
        lim = 1 << numBit;
        mask =  (lim - 1)<< rBitNo;
        point   = new int [lim+1];
        border  = new int [lim+1];

        // sort on  'numBit' bits, from: leftBitNo'to 'rBitNo+1' in a[start..end]

        // c) count-scan 'numBit' bits
        for (i=start; i <= end ; i++)
          point [ (a[i] & mask) >> rBitNo ]++;

        t2 = point[0];
        point[0] = start;

        for (i =1; i <= lim; i++) {
            // d)  point [i] points to where bundle 'i' starts, stopvalue in borders[lim-1]
            t1 =t2;
            t2 = point[i];
            border[i-1] =point[i] =  point[i-1] + t1;
        }

        border[lim-1] = point[lim];
        border[lim]= end+1;

        int currentBox= 0, pos=start;

        //  find next element to move in  permtation cycles
        // skip cycles of length =1

         while (point[currentBox] == border[currentBox] )
           currentBox++;

         while (currentBox <lim){
             // find next cycle, skip (most)cycles of length =1
             pos = point[currentBox];
             k = a[pos];

             // start of new permutation cycle
             adr2 = point[(k&mask)>> rBitNo]++;

             if (adr2>pos ) {
                // permuttion cycle longer than 1 element
                do{
                    k2 = a[adr2];
                    // central loop
                    a[adr2] = k;
                    adr2 = point[(k2& mask) >> rBitNo]++;
                    k =k2;
                }while(adr2 > pos);

                a[pos] = k;

          }// end perm cycle

          // find box where to find start of new  permutation cycle
          while (currentBox < lim && point[currentBox] == border[currentBox] )
             currentBox++;

        } // end more to sort

        leftBitNo = leftBitNo - numBit;

        if ( leftBitNo >= 0) {
            //  more to sort - recursively
            t2 = start;
            for (i = 0; i < lim; i++) {
                t1 = t2;
                t2 = point[i];
                newNum = t2-t1;

                // call each cell if more than one number
                if (newNum > 1) {
                    if ( newNum <= INSERT_MAX ) {
                        insertSort (a,t1,t2-1);
                     } else {
                        sortARLwithBorders(a,t1,t2-1,leftBitNo);
                    }
                } // if newNum > 1
            } // end for
        } // end if leftBitNo

    }// end sortARLwithBorders


     /** sorts a [left .. right]  by Insertion sort alg. Sub-alg for short segments */
    public static void insertSort(int a[],int left, int right) {
          int i,k,t;      ;

          for (k = left ; k < right; k++) {
            if (a[k] > a[k+1]) {
              t = a[k+1];
              i = k;

              while (i >= left && a[i] > t) {
                   a[i+1] = a[i];
                   i--;
              }
              a[i+1] = t;
          }
        }
   } // end insertSort

     /** Returns a random filled (Uniform 0:n-1) array */
     static int [] getArray(int n) {
          Random r = new Random( 123789+ n);
          int [] a = new int[n];
          for (int i=0; i < n ; i++)
               a[i]=  r.nextInt(n) ;
          return a;
     } // end getArray

     /** simple test if sorted array */
     static void sortTest(int [] a) {
         for (int i =1; i < a.length; i++)
         if (a[i-1] > a[i] ) {
           System.out.println("- sort-error;, a["+(i-1)+"]:"+ a[i-1]+", a["+i+"]:"+a[i]);
           while (i == i); // Stop output, terminate with ctrl-C
      }} // end sortTest


     /** main: test sorting method - compare with Quicksort (Arrays.sort)*/
     public static void main (String [] args) {
         if (args.length <3)
            System.out.println(" Use : >java -Xmx1000m ARLSort nlow stepMult nhigh  ");
         else {

            int nLow  = new Integer(args[0]).intValue(),
                step  = new Integer(args[1]).intValue(),
                nHigh = new Integer(args[2]).intValue();
            double  num=0.0;

            // central test loop
            for (int n = nLow; n <= nHigh; n*=step) {
               double startTime=0,ARLTime=0, quickTime=0;

               System.out.println("\nLength of  a:" + n + ", MAX_NUM_BIT:" + MAX_NUM_BIT);
               num= nHigh/n; //iterate sorting to gain accuracy;

               // Quicksort, for comparison
                   int [] a = new int[n],b = getArray(n);

                   for (int j = 0; j<num;j++){
                       System.arraycopy(b,0,a,0,n);
                       startTime = System.currentTimeMillis();
                       Arrays.sort(a);
                       quickTime+= System.currentTimeMillis() - startTime;
                   }
                   quickTime = quickTime/num;
                   sortTest(a);
                   System.out.println("   Quicksort:  "+quickTime+" millisec");

                // ARL with borders sort test:
                    for (int j = 0; j<num;j++){
                       System.arraycopy(b,0,a,0,n);
                       startTime = System.currentTimeMillis();
                       ARLsort(a);
                       ARLTime += System.currentTimeMillis() - startTime;
                   }

                    ARLTime = ARLTime/num;
                    int relative= (int)(100*ARLTime/quickTime);
                    sortTest(a);
                    System.out.println("   ARL border: "+ ARLTime +" millisec, ARL/Quick:"+relative+"%");
            }// end for
        }// end else
    } // end main - test

} // end class
