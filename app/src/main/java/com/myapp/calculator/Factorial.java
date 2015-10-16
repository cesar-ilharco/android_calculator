package com.myapp.calculator;

import android.support.annotation.VisibleForTesting;

import junit.framework.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Android Calculator App
 */

public class Factorial implements Function1<Integer, BigDecimal>{

    @Override
    public BigDecimal apply(Integer n) {
        return parallelFactorial(n);
    }

    @VisibleForTesting
    // Using directly BigDecimal for computation.
    static BigDecimal rawFactorial(int n){
        BigDecimal result = BigDecimal.ONE;
        for (int i=1; i<=n; ++i){
            result = result.multiply(new BigDecimal(i));
        }
        return result;
    }

    @VisibleForTesting
    static BigDecimal thunkFactorial(int n){
        return parallelFactorial(n, 1);
    }

    @VisibleForTesting
    static BigDecimal parallelFactorial(int n){
        int numCores = Runtime.getRuntime().availableProcessors();
        Assert.assertTrue(numCores >= 1);
        return parallelFactorial(n, numCores);
    }

    private static BigDecimal parallelFactorial(int n, int numThreads) {

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

    private static <T> boolean isComputed (List<Continuable<T> > continuables){
        for (Continuable<T> continuable : continuables){
            if (!continuable.isComputed()){
                return false;
            }
        }
        return true;
    }

    // Split numbers from 0 to n, inclusively, in min(n,p) + 1 partitions.
    // TODO: Split evenly according to computing time.
    @VisibleForTesting
    static List<Integer> getPartition (int n, int p){
        List<Integer> partition = new ArrayList<>();
        if (n < p){
            p = n;
        }
        for (int i=0; i<=p; ++i){
            partition.add((n*i)/p);
        }
        return partition;
    }

    private static Function2<BigDecimal,BigDecimal,BigDecimal> multiply = new Function2<BigDecimal, BigDecimal, BigDecimal>() {
        @Override
        public BigDecimal apply(BigDecimal a, BigDecimal b) {
            return a.multiply(b);
        }
    };
}
