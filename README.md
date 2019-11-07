# Threading pre-lab

## Background

The next lab will use threads to parallelize a complex process, and the threads
will need to communicate through a shared object. To prepare, in this lab
you'll get to make a threaded version of a simple linear search algorithm.

This repository contains a non-threaded search version of the search algorithm
along with stubs for the threaded verion that you'll need to complete.

If you want to do some reading on threads (in Java) you might find
[this tutorial](http://www.tutorialspoint.com/java/java_multithreading.htm)
to be helpful (be sure to go through the 4 additional tutorials at the bottom of that last tutorial before proceeding).  
You might also find [this writeup](http://www.javaworld.com/article/2077138/java-concurrency/introduction-to-java-threads.html) to be helpful.

Some of you may have already gone through these tutorials when reviewing the `threadsandprocesses` file on canvas.

## Communicating through shared state

Your threads will need to have some sort of _shared state_ that they can use to
keep track of whether _any_ thread has found the target value so far. This
solution deals with that by constructing a simple `Answer` object with a boolean
field that can be set and checked. If every `Runnable` is handed _one shared_
instance of that `Answer` object, then they can all write to it with
`setAnswer()`.

## Organizing the threads

This solution is structured assuming that if you have _K_ threads, you'll
split the list into _K_ equal sized pieces, and have each thread search a
different piece. If the length of the list is 100, for example, and you have
4 threads, then the threads will respectively search these sections of the
list:

* [0, 25), i.e., _0≤i<25_
* [25, 50)
* [50, 75)
* [75, 100)

Since these sections have no overlap, it's safe to have them searched
independently by different threads, each updating the `Answer`  by calling
`setTargetFound()` if it in fact finds the target in its section.

For this to work, we have a constructor for `ThreadedSearch` that takes the
`target`, the `list`, the `begin` and `end` values that define the segment
this searcher is supposed to check (e.g., 0 and 25 for the first thread in the
example above), and the shared `Answer` object. That constructor then stores
each of those values in fields so that they'll be available when that searchers
`run()` method is called by its containing thread.

## Waiting for all the threads to finish

One tool that you'll need is the `join()` method. (See, for example, [the discussion of join in the Java concurrency tutorial](http://docs.oracle.com/javase/tutorial/essential/concurrency/join.html)).
`join()` allows one thread to wait for another to finish, which is important
in searching because we need all the sub-threads to finish searching their
part of the array before we can look in their shared `Answer` object and
determine the final result.

A common pattern is something like the following:

```java
   // Create and start a bunch of threads
   Thread[] threads = new Thread[NUM_THREADS];
   for (int i=0; i<NUM_THREADS; ++i) {
      // Create and start thread i
      threads[i] = new Thread(...);
      threads[i].start();
   }

   // Wait for all the threads to finish
   for (int i=0; i<NUM_THREADS; ++i) {
      threads[i].join();
   }

   // Combine their results and wrap up as appropriate to the problem.
```

This assumes that the various threads have saved their partial results to
some shared state object that this main thread can access to extract and/or
assemble the final result. In our example, the shared `Answer` object serves
that purpose, and we'll just need to call `isFound()` to get the desired
answer.

It's important to realize that `join()` blocks, causing the main thread to
wait until the thread being joined finishes. This means that if we had
called `join()` in the first loop we would have lost all our parallelism
because we would have waited for the first thread to finish before starting
the second thread, etc.

## Exiting early if you find the target

If you implement this as described above, each of your threads will check their entire section of the list _even if one of the other threads has found the `target`_. If you want to really see the benefit of threading, you might want have your search loop also check that `answer.getAnswer()` is still `false`; if it returns `true` then you know someone else found the `target` and you can quit, even if you have buckets of other values to check.

## Does threading speed things up?

You'll need a pretty large list and a fair number of threads to see a meaningful speed-up. With a large list and 4 or more cores, however, you should be able to see a meaningful speed-up with threading.