package com.xingy.data.stream.ts.windows;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import com.xingy.data.stream.ts.modle.Order;
import com.xingy.data.stream.ts.modle.OrderStatistics;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.functions.windowing.RichWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * @author xinguiyuan
 * @className com.test.flink.windows.OrderWindowFunction
 * @date 2020/07/13 15:40
 * @description
 */
public class OrderWindowFunction extends RichWindowFunction<Order, OrderStatistics, Tuple, TimeWindow> {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Override
    public void apply(Tuple tuple, TimeWindow timeWindow, Iterable<Order> iterable, Collector<OrderStatistics> collector) throws Exception {
        Iterator<Order> iterator = iterable.iterator();
        // 总数
        int count = 0;
        // 总金额
        double sumPrice = 0;
        // 平均金额
        double avgPrice = 0;
        while (iterator.hasNext()) {
            //count操作
            count++;
            //sum操作
            sumPrice += iterator.next().getPrice();
        }
        if (count > 0) {
            avgPrice = sumPrice/count;
        }
        OrderStatistics ods = new OrderStatistics();
        ods.setCount(count);
        ods.setSumPrice(sumPrice);
        ods.setId(tuple.getField(0));
        ods.setName(tuple.getField(1));
        Date date = new Date(timeWindow.getStart());
        ods.setStatisTime(sdf.format(date));
        collector.collect(ods);
    }
}
