package search;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    //I completed this with the shared wisdom of Melissa Kamrowski and Aaron Walter
    public static void main(String[] args) throws InterruptedException {
        System.out.println(Runtime.getRuntime().availableProcessors());
        // Update this to make things go faster or take longer for timing studies.
        final int ARRAY_SIZE = 10000;
        Random random = new Random();
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        for (int i = 0; i < ARRAY_SIZE; ++i) {
            numbers.add(random.nextInt(ARRAY_SIZE));
        }

        Instant startTime = Instant.now();
        System.out.println(searchArray(numbers.get(1), numbers));
        System.out.println(searchArray(numbers.get(5), numbers));

        System.out.println(searchArray(numbers.get(900), numbers));

        System.out.println(searchArray(numbers.get(3200), numbers));
        System.out.println(searchArray(numbers.get(7400), numbers));
        System.out.println(searchArray(numbers.get(9876), numbers));
        Instant endTime = Instant.now();
        Duration totalTime = Duration.between(startTime, endTime);
        System.out.println("Total time was " + (totalTime.getNano() / 1000000) + " milliseconds");

        startTime = Instant.now();
        System.out.println("--------");
        System.out.println(searchArray(100, numbers));
        System.out.println(searchArray(-45, numbers));
        System.out.println("---------");
        endTime = Instant.now();
        totalTime = Duration.between(startTime, endTime);
        System.out.println("Total time was " + (totalTime.getNano() / 1000000) + " milliseconds");
    }

    private static boolean searchArray(int target, ArrayList<Integer> list) throws InterruptedException {
        // You can replace ThreadedSearch with LinearSearch to see this work with
        // the given linear search code.
        //Searcher<Integer> searcher = new LinearSearch<>();

        // This specifies 4 threads for the tests. It would be a good idea to play
        // with this and see how that changes things. Keep in mind that your number
        // of threads *may* need to evenly divide the length of the list being
        // searched (ARRAY_SIZE in this case).
        Searcher<Integer> threadedSearcher = new ThreadedSearch<>(2);

        return threadedSearcher.search(target, list);
    }

    private static boolean lSearchArray(int target, ArrayList<Integer> list) throws InterruptedException {
        // You can replace ThreadedSearch with LinearSearch to see this work with
        // the given linear search code.
        //Searcher<Integer> searcher = new LinearSearch<>();

        // This specifies 4 threads for the tests. It would be a good idea to play
        // with this and see how that changes things. Keep in mind that your number
        // of threads *may* need to evenly divide the length of the list being
        // searched (ARRAY_SIZE in this case).
        Searcher<Integer> linearSearcher = new LinearSearch<>();

        return linearSearcher.search(target, list);
    }

}
