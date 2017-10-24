public class InnstikkSort{
    public static void sort(int[] a, int left, int right){
        for (int i = left; i < right-1; i++){
            int usortert = a[i+1];
            int j = i+1;

            while (j > left && usortert < a[j-1]){
                a[j] = a[j-1];
                j--;
            }
            a[j] = usortert;
        }
    }
}
