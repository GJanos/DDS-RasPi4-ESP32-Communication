public class Main {
    static int bufferSize = 18;
    static double[] buffer;
    static int inputBufferSize = 36;
    static long[] inputBuffer = {
            10,
            10,
            15,
            20,
            24,
            20,
            18,
            27,
            27,
            27,
            27,
            27,
            27,
            27,
            27,
            30,
            30,
            30,
            30,
            30,
            100,
            100,
            100,
            100,
            100,
            10,
            10,
            15,
            20,
            24,
            20,
            18,
            27,
            27,
            27,
            27
    };
    static double prevDisctance = 100;
    static int dataCnt = 0;
    static double normalLowerBound = 75.0;
    static double normalUpperBound = 125.0;
    static int distanceBound = 5;
    static double distance = 0;
    static double finalDistance = 0;
    static double filteredDistance = 0;

    public static void main(String[] args) {
        buffer = new double[bufferSize];
        do {
            for (int j = 0; j < bufferSize; j++) {
                buffer[j] = saveDistance();
            }
            sortBuffer();
            finalDistance = calcDistanceAvg();
            printBuffer();
            sendDistance();
        } while (dataCnt < inputBufferSize);
    }
    private static void calcDistanceFilter(double prevDistance, double deviationPrc) {
        if (deviationPrc >= normalLowerBound) {
            if (deviationPrc <= normalUpperBound) {
                finalDistance = distance;
            } else {
                finalDistance = distance * ((prevDistance / distance) + 0.25);
            }
        } else {
            finalDistance = distance / ((distance / prevDistance) + 0.25);
        }
    }
    private static double saveDistance() {
        distance = inputBuffer[dataCnt++];
        if (dataCnt == 1) {
            prevDisctance = distance;
        }
        double deviationPrc = distance / prevDisctance * 100;
        calcDistanceFilter(prevDisctance, deviationPrc);
        prevDisctance = distance;
        return filteredDistance;
    }
    private static double calcDistanceAvg() {
        double sum = 0;
        for (int i = distanceBound; i < bufferSize - distanceBound; i++) {
            sum += buffer[i];
        }
        return sum / (bufferSize - (2 * distanceBound));
    }
    static int partition(double[] array, int left, int pivot) {
        // Start with a sub-array that is empty
        int low = left - 1;
        // Compare all numbers before the pivot
        int high = pivot - 1;
        // Go over all elements in sub-array
        for (int j = left; j <= high; j++) {
            // Does this element go in the "<=" sub-array?
            if (array[j] <= array[pivot]) {
                // Move over marker of sub-array
                low++;
                // Swap the element into position
                double temp = array[low];
                array[low] = array[j];
                array[j] = temp;
            }
        }
        // Move the pivot into the correct position
        double temp = array[low + 1];
        array[low + 1] = array[pivot];
        array[pivot] = temp;
        // Return the index of the element in the correct place
        return low + 1;
    }
    static void quicksort(double[] array, int left, int pivot) {
        // Recursively called until only a single element left
        if (left < pivot) {
            // Partition the array into "<="/">" sub-arrays
            int new_pivot = partition(array, left, pivot);
            // Sort the sub-arrays via recursive calls
            // Sort the "<=" sub-array
            quicksort(array, left, new_pivot - 1);
            // Sort the ">" sub-array
            quicksort(array, new_pivot + 1, pivot);
        }
    }
    private static void sortBuffer() {
        quicksort(buffer, 0, bufferSize - 1);
    }
    private static void printBuffer() {
        System.out.println("==========");
        System.out.println(dataCnt + ". buffer");
        System.out.println("Actual dist: " + distance);
        for (int i = 0; i < bufferSize; i++) {
            System.out.print(buffer[i] + " ");
        }
        System.out.println("");
    }
    private static void sendDistance() {
        System.out.println("Distance: " + finalDistance);
    }
}