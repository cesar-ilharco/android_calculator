package com.myapp.calculator;

import junit.framework.Assert;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class PerformanceTests {

    @Test
    public void testFactorial() throws Exception{

        final int n = 100000;

        long startNs = System.nanoTime();
        BigDecimal resultRaw = rawFactorial(n);
        System.out.println("Raw:      Time elapsed(ms) = " + (System.nanoTime() - startNs)/1000000L);

        startNs = System.nanoTime();
        BigDecimal resultThunk = thunkFactorial(n);
        System.out.println("Thunk:    Time elapsed(ms) = " + (System.nanoTime() - startNs)/1000000L);

        startNs = System.nanoTime();
        BigDecimal resultParallel = parallelFactorial(n);
        System.out.println("Parallel: Time elapsed(ms) = " + (System.nanoTime() - startNs)/1000000L);

        Assert.assertEquals(resultRaw, resultThunk);
        Assert.assertEquals(resultRaw, resultParallel);
    }

    @Test
    public void testPartition(){
        Assert.assertEquals(getPartition(40, 4), Arrays.asList(0, 10, 20, 30, 40));
        Assert.assertEquals(getPartition(4, 8), Arrays.asList(0, 1, 2, 3, 4));
    }


    // Using directly BigDecimal for computation.
    private BigDecimal rawFactorial(int n) throws Exception {
        BigDecimal result = BigDecimal.ONE;
        for (int i=1; i<=n; ++i){
            result = result.multiply(new BigDecimal(i));
        }
        return result;
    }

    private BigDecimal thunkFactorial(int n) throws Exception {
        return parallelFactorial(n, 1);
    }

    private BigDecimal parallelFactorial(int n) throws Exception {
        int numCores = Runtime.getRuntime().availableProcessors();
        return parallelFactorial(n, numCores);
    }

    private BigDecimal parallelFactorial(int n, int numThreads) throws Exception {

        List<Integer> partition = getPartition(n, numThreads);
        List<Node<BigDecimal>> lists = new ArrayList<>(partition.size()-1);
        for (int p=0; p<partition.size()-1; ++p){
            int start = partition.get(p)+1;
            int end = partition.get(p+1);
            lists.add(null);
            for(int i=start; i<=end; ++i) {
                lists.set(p, new Node(Number.create(i).getThunk(), lists.get(p)));
            }
        }

        final List<AccumulatedThunk<BigDecimal, BigDecimal> > thunks = new ArrayList<>(partition.size()-1);
        for (int p=0; p<partition.size()-1; ++p) {
            thunks.add(new AccumulatedThunk<>(multiply, Number.ONE.getThunk(), lists.get(p)));
        }

        List<Continuable<BigDecimal> > continuables = new ArrayList<>(partition.size()-1);
        for (int p=0; p<partition.size()-1; ++p) {
            final int i = p;
            continuables.add(new Continuable<>(new Thunk<BigDecimal>() {
                                    @Override
                                    protected BigDecimal compute() {
                                        return thunks.get(i).get();
                                    }
                            }));
        }

        int timeoutMs = 10;
        while(!isComputed(continuables)) {
            for (Continuable<BigDecimal> continuable : continuables){
                continuable.computeFor(timeoutMs);
            }
            timeoutMs *= 2;
        }

        BigDecimal result = BigDecimal.ONE;
        for (Continuable<BigDecimal> continuable : continuables){
            result = result.multiply(continuable.get());
        }

        return result;
    }

    private <T> boolean isComputed (List<Continuable<T> > continuables){
        for (Continuable<T> continuable : continuables){
            if (!continuable.isComputed()){
                return false;
            }
        }
        return true;
    }

    // Split numbers from 0 to n, inclusively, in p+1 partitions.
    // TODO: Split evenly according to computing time.
    private List<Integer> getPartition (int n, int p){
        List<Integer> partition = new ArrayList<>();
        if (n < p){
            p = n;
        }
        for (int i=0; i<=p; ++i){
            partition.add((n*i)/p);
        }
        return partition;
    }

    private Function2<BigDecimal,BigDecimal,BigDecimal> multiply = new Function2<BigDecimal, BigDecimal, BigDecimal>() {
        @Override
        public BigDecimal apply(BigDecimal a, BigDecimal b) {
            return a.multiply(b);
        }
    };

}