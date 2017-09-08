package com.usamsl.global.ava.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Administrator on 2017/1/18.
 * 线程管理
 */
public class ThreadPoolManager {
    /**
     * 线程执行器
     */
    private static ExecutorService executorService = null;
    /**
     * 固定5个线程
     */
    private static int nThread = 5;
    /**
     * 单例
     */
    private static ThreadPoolManager taskExecutorPool = null;
    /**
     * 初始化线程
     */
    static {
        taskExecutorPool = new
                ThreadPoolManager(nThread * getNumCores());
    }
    /**
     * 构造函数
     */
    protected ThreadPoolManager(int threads){
    //   executorService = Executors.newFixedThreadPool(threads);
        executorService = Executors.newScheduledThreadPool(threads);
    }
    /**
     * 取得单例
     * @return
     */
    public static ThreadPoolManager getInstance(){
        return taskExecutorPool;
    }
    /**
     * 取得线程执行器
     * @return
     */
    public ScheduledExecutorService getSchedExecutorService(){
        return (ScheduledExecutorService)executorService;
    }
    public static int getNumCores(){
        int threadCount =
                Runtime.getRuntime().availableProcessors();
        return threadCount;
    }
}
