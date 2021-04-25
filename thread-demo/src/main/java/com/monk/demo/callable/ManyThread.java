package com.monk.demo.callable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ManyThread {

    private final static int maxThreads = 10;
    private final static int randomDataSize = 500000;
    private final static int batchSize = 3000;

    private static List<String> dataList = new ArrayList<String>();

    /**
     * 初始化数据
     */
    private static void initData() {
        int listSize = new Random().nextInt(randomDataSize);
        for (int i = 0; i < listSize; i++) {
            dataList.add(String.valueOf(i));
        }
        System.out.println("list size = " + listSize);
        System.out.println("===============================");
    }


    public static void main(String[] args) throws InterruptedException, ExecutionException {

        // 1. 初始化数据
        initData();

        long start = System.currentTimeMillis();

        // 2. 创集线程
        int threadNum = dataList.size() / batchSize + 1;
        threadNum = threadNum >= maxThreads ? maxThreads : threadNum;
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum, new ThreadFactory() {
            AtomicInteger atomic = new AtomicInteger();

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "Thread [" + atomic.getAndIncrement() + "]");
            }
        });

        // 3. 处理数据
        List<Callable<String>> runnableList = new ArrayList<>();
        int size = dataList.size();
        if (dataList.size() > batchSize) {
            int batch = size % batchSize == 0 ? size / batchSize : size / batchSize + 1;
            for (int j = 0; j < batch; j++) {
                int end = (j + 1) * batchSize;
                if (end > size) {
                    end = size;
                }
                List<String> workData = dataList.subList(j * batchSize, end);
                CheckTaskCallable callable = new CheckTaskCallable(workData);
                runnableList.add(callable);
            }
            List<Future<String>> results = executorService.invokeAll(runnableList);
            for (Future<String> str : results) {
                System.out.println(str.get());
            }
        }
        executorService.shutdown();

        long date = System.currentTimeMillis() - start;
        System.out.println("======" + date + "ms======");
    }
}
