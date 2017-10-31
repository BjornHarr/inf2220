import java.util.Random;
import java.util.Arrays;

public class Oblig3{
    public static void main(String[] args) {


        for (int i = 1; i <= 7; i++){
            RadixSort sort = new RadixSort();

            int size = (int) Math.pow(10, i);
            int[] a = new int[size];
            int[] b = new int[size];

            Random r = new Random();

            for (int j = 0; j < a.length; j++) {
                a[j] = r.nextInt(size);
                b[j] = a[j];
            }

            double time = sort.vRadixMulti(a);

            long qT = System.nanoTime();
            Arrays.sort(b);
            double qTime = (System.nanoTime() - qT)/1000000.0;

            double diff = qTime / time;

            System.out.println("<------------- 10^(" + i + ") ----------->");
            System.out.printf("   Tid:       %1.4f\n - QuickSort: %1.4f\n", time, qTime);
            System.out.printf(" = Diff:      %1.4f\n<-------------------------------->\n\n", diff);

        }
    }
}
