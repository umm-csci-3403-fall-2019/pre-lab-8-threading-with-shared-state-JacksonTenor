package search;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ThreadedSearch<T> implements Searcher<T>, Runnable {

    private int numThreads;
    private T target;
    private List<T> list;
    private int begin;
    private int end;
    private Answer answer;

    public ThreadedSearch(int numThreads) {
        this.numThreads = numThreads;
    }

    private ThreadedSearch(T target, List<T> list, int begin, int end, Answer answer) {
        this.target = target;
        this.list = list;
        this.begin = begin;
        this.end = end;
        this.answer = answer;
    }

    /**
     * Searches `list` in parallel using `numThreads` threads.
     * <p>
     * You can assume that the list size is divisible by `numThreads`
     */
    public boolean search(T target, List<T> list) throws InterruptedException {
        Answer a = new Answer();
        Thread[] threads = new Thread[numThreads];
        int threadSize = list.size() / numThreads;
        //Make the threads
        for(int i=0; i<numThreads; i++){
            ThreadedSearch ts = new ThreadedSearch(target, list, i*threadSize, (i+1)*threadSize, a);
            threads[i] = new Thread(ts);
            threads[i].start();
        }
        /*
         * First construct an instance of the `Answer` inner class. This will
         * be how the threads you're about to create will "communicate". They
         * will all have access to this one shared instance of `Answer`, where
         * they can update the `answer` field inside that instance.
         *
         * Then construct `numThreads` instances of this class (`ThreadedSearch`)
         * using the 5 argument constructor for the class. You'll hand each of
         * them the same `target`, `list`, and `answer`. What will be different
         * about each instance is their `begin` and `end` values, which you'll
         * use to give each instance the "job" of searching a different segment
         * of the list. If, for example, the list has length 100 and you have
         * 4 threads, you would give the four threads the ranges [0, 25), [25, 50),
         * [50, 75), and [75, 100) as their sections to search.
         *
         * You then construct `numThreads`, each of which is given a different
         * instance of this class as its `Runnable`. Then start each of those
         * threads, wait for them to all terminate, and then return the answer
         * in the shared `Answer` instance.
         */
        //Wait for them to finish
        for (int i=0; i<numThreads; ++i) {
            threads[i].join();
        }
        return a.getAnswer();
    }

    public void run() {
        //Loop through the values until someone finds it or we get through all of our numbers.
        for(int i=begin; i<end; i++){
            if(list.get(i).equals(target)){
                answer.setAnswer(true);
                return;
            }
            if(answer.getAnswer()){
                return;
            }
        }
    }

    private class Answer {
        private boolean answer = false;

        // In a more general setting you would typically want to synchronize
        // this method as well. Because the answer is just a boolean that only
        // goes from initial initial value (`false`) to `true` (and not back
        // again), we can safely not synchronize this, and doing so substantially
        // speeds up the lookup if we add calls to `getAnswer()` to every step in
        // our threaded loops.
        public boolean getAnswer() {
            return answer;
        }

        // This has to be synchronized to ensure that no two threads modify
        // this at the same time, possibly causing race conditions.
        // Actually, that's not really true here, because we're just overwriting
        // the old value of answer with the new one, and no one will actually
        // call with any value other than `true`. In general, though, you do
        // need to synchronize update methods like this to avoid race conditions.
        public synchronized void setAnswer(boolean newAnswer) {
            answer = newAnswer;
        }
    }

}
