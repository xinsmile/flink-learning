package com.xingy.window.windowing;

import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.streaming.api.functions.windowing.RichWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.types.Row;
import org.apache.flink.util.Collector;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author xinguiyuan
 * @className com.xingy.window.windowing.MyWindow1
 * @date 2020/10/15 17:38
 * @description
 */
public class MyWindow1 extends RichWindowFunction<Row, Row, Tuple1<String>, TimeWindow> {

    @Override
    public void apply(Tuple1<String> tuple1, TimeWindow timeWindow, Iterable<Row> iterable, Collector<Row> collector) throws Exception {
        Iterator<Row> iterator = iterable.iterator();
        Set<Row> set = new HashSet<>();
        while (iterator.hasNext()){
            set.add(iterator.next());
        }
        if (set.size() > 0) {
            Row row = new Row(3);
            for (Row value : set) {
                row.setField(0, value.getField(0));
                row.setField(1, value.getField(1));
                row.setField(2, timeWindow.getStart());
            }
        }


    }
}
