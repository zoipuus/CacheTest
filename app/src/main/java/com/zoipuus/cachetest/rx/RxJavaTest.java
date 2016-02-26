package com.zoipuus.cachetest.rx;


import rx.Observable;
import rx.Subscriber;

/**
 * Created by Administrator on 2016/2/26.
 * Test RxJava
 */
public class RxJavaTest {

    public static void main(String[] args) {
        System.out.println("main");
        onTestRxJava();
    }

    private static void onTestRxJava() {
        Observable.just("Hello,world!")
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println(s);
                    }
                });

    }
}
