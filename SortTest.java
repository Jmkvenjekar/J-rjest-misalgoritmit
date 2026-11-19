import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

public class SortTest {

    public static void insertionSort(int[] data) {
        for (int i = 1; i < data.length; i++) {
            int key = data[i];
            int j = i - 1;

            while (j >= 0 && data[j] > key) {
                data[j + 1] = data[j];
                j--;
            }
            data[j + 1] = key;
        }
    }

    public static void bubbleSort(int[] data) {
        for (int i = 0; i < data.length - 1; i++) {
            boolean swapped = false;

            for (int j = 0; j < data.length - 1 - i; j++) {
                if (data[j] > data[j + 1]) {
                    int temp = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = temp;
                    swapped = true;
                }
            }

            if (!swapped) {
                break;
            }
        }
    }

    public static int[] CreateAscendingTable(int n) {
        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = i;
        }
        return data;
    }

    public static int[] CreateDescendingTable(int n) {
        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = n - 1 - i;
        }
        return data;
    }

    public static int[] createRandomData(int n) {
        int[] data = new int[n];
        Random gen = new Random(12345);
        for (int i = 0; i < n; i++) {
            data[i] = gen.nextInt(n);
        }
        return data;
    }

    public static void shellSort(int[] data) {
        for (int gap = data.length / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < data.length; i++) {
                int temp = data[i];
                int j = i;

                while (j >= gap && data[j - gap] > temp) {
                    data[j] = data[j - gap];
                    j -= gap;
                }
                data[j] = temp;
            }
        }
    }

    public static void quickSort(int[] data) {
        if (data.length > 1) {
            quickSortRecursive(data, 0, data.length - 1);
        }
    }

    private static void quickSortRecursive(int[] data, int left, int right) {
        int i = left;
        int j = right;
        int pivot = data[left + (right - left) / 2];

        while (i <= j) {
            while (data[i] < pivot) {
                i++;
            }
            while (data[j] > pivot) {
                j--;
            }

            if (i <= j) {
                int temp = data[i];
                data[i] = data[j];
                data[j] = temp;
                i++;
                j--;
            }
        }

        if (left < j) {
            quickSortRecursive(data, left, j);
        }
        if (i < right) {
            quickSortRecursive(data, i, right);
        }
    }

    public static void mergeSort(int[] data) {
        if (data.length > 1) {
            mergeSortRecursive(data, 0, data.length - 1);
        }
    }

    private static void mergeSortRecursive(int[] data, int left, int right) {
        if (left >= right) {
            return;
        }

        int mid = left + (right - left) / 2;
        mergeSortRecursive(data, left, mid);
        mergeSortRecursive(data, mid + 1, right);

        int[] a = Arrays.copyOfRange(data, left, mid + 1);
        int[] b = Arrays.copyOfRange(data, mid + 1, right + 1);
        int i = 0;
        int j = 0;
        int k = left;

        while (i < a.length && j < b.length) {
            data[k++] = (a[i] <= b[j]) ? a[i++] : b[j++];
        }
        while (i < a.length) {
            data[k++] = a[i++];
        }
        while (j < b.length) {
            data[k++] = b[j++];
        }
    }

    public static void mergeSortOptimized(int[] data) {
        int[] temp = new int[data.length];
        if (data.length > 1) {
            mergeSortOptimizedRecursive(data, temp, 0, data.length - 1);
        }
    }

    private static void mergeSortOptimizedRecursive(
            int[] data, int[] temp, int left, int right) {
        if (left >= right) {
            return;
        }

        int mid = left + (right - left) / 2;
        mergeSortOptimizedRecursive(data, temp, left, mid);
        mergeSortOptimizedRecursive(data, temp, mid + 1, right);

        if (data[mid] <= data[mid + 1]) {
            return;
        }

        System.arraycopy(data, left, temp, left, right - left + 1);
        int i = left;
        int j = mid + 1;

        for (int k = left; k <= right; k++) {
            if (i > mid) {
                data[k] = temp[j++];
            } else if (j > right) {
                data[k] = temp[i++];
            } else if (temp[i] <= temp[j]) {
                data[k] = temp[i++];
            } else {
                data[k] = temp[j++];
            }
        }
    }

    public static void testSorts(String title, int[] original) {
        LinkedHashMap<String, Consumer<int[]>> algorithms = new LinkedHashMap<>();
        algorithms.put("Insertion Sort", SortTest::insertionSort);
        algorithms.put("Bubble Sort", SortTest::bubbleSort);
        algorithms.put("Arrays.sort", Arrays::sort);
        algorithms.put("Shell Sort", SortTest::shellSort);
        algorithms.put("Quick Sort", SortTest::quickSort);
        algorithms.put("Merge Sort", SortTest::mergeSort);
        algorithms.put("Merge Sort (optimoitu)", SortTest::mergeSortOptimized);

        int[] expected = original.clone();
        Arrays.sort(expected);

        System.out.println("\n" + title + " (N = " + original.length + ")");

        for (Map.Entry<String, Consumer<int[]>> entry : algorithms.entrySet()) {

            if (original.length >= 1_000_000 &&
                (entry.getKey().equals("Insertion Sort") ||
                 entry.getKey().equals("Bubble Sort"))) {

                System.out.println(entry.getKey() + ": ohitettu suuren taulukon vuoksi");
                continue;
            }

            int[] data = original.clone();

            long start = System.nanoTime();
            entry.getValue().accept(data);
            long stop = System.nanoTime();

            if (!Arrays.equals(data, expected)) {
                throw new IllegalStateException(
                    entry.getKey() + " lajitteli virheellisesti!");
            }

            System.out.printf(Locale.US, "%-24s %.7f s%n",
                entry.getKey() + ":", (stop - start) / 1_000_000_000.0);
        }
    }

    public static void main(String[] args) {
        int size = 100_000;

        testSorts("SATUNNAINEN TAULUKKO", createRandomData(size));
        testSorts("NOUSEVA TAULUKKO", CreateAscendingTable(size));
        testSorts("LASKEVA TAULUKKO", CreateDescendingTable(size));
    }
}
