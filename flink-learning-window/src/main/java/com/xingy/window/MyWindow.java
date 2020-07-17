package com.xingy.window;

import java.util.Date;
import java.util.Iterator;
import com.xingy.window.model.WordCountEvent;
import com.xingy.window.model.WordEvent;
import com.xkzhangsan.time.formatter.DateFormatPattern;
import com.xkzhangsan.time.formatter.DateTimeFormatterUtil;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.RichWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * @author xinguiyuan
 * @className com.xingy.window.MyWindow
 * @date 2020/07/17 17:14
 * @description
 */
public class MyWindow extends RichWindowFunction<WordEvent, WordCountEvent, Tuple, TimeWindow> {

    private WordCountEvent wce = new WordCountEvent();

    public static MyWindow create() {
        return new MyWindow();
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
    }

    @Override
    public void close() throws Exception {
        super.close();
    }

    @Override
    public void apply(Tuple tuple, TimeWindow timeWindow, Iterable<WordEvent> iterable, Collector<WordCountEvent> collector) throws Exception {
        Iterator<WordEvent> iterator = iterable.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            WordEvent wc = iterator.next();
            count += wc.getCount();
        }
        wce.setWord(tuple.getField(0));
        wce.setTotalCount(count);
        wce.setStartTime(DateTimeFormatterUtil.format(
                new Date(timeWindow.getStart()), DateFormatPattern.YYYY_MM_DD_HH_MM_SS));
        wce.setEndTime(DateTimeFormatterUtil.format(
                new Date(timeWindow.getEnd()), DateFormatPattern.YYYY_MM_DD_HH_MM_SS));
        System.out.println(wce);
        collector.collect(wce);
    }
}
