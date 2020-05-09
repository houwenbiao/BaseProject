package com.qtimes.wonly;

import com.qtimes.npucore.ssd.utils.GPIOUtils;
import com.qtimes.utils.android.PluLog;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private long num = 0;
    private GPIOUtils gpioUtils;

    @Test
    public void addition_isCorrect() throws Exception {
        gpioUtils = new GPIOUtils();
        Observable.timer(5, TimeUnit.SECONDS).subscribeOn(Schedulers.computation()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                num++;
                if (num % 2 == 0) {
                    PluLog.i(gpioUtils.getDoorStatus());
                }
            }
        });
    }
}