package com.xingy.data.stream.ts.stream;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import com.xingy.data.stream.ts.modle.Order;
import com.xingy.data.stream.ts.modle.OrderStatistics;
import com.xingy.data.stream.ts.windows.OrderWindowFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * @author xinguiyuan
 * @className com.test.flink.CountEventTimeFlink
 * @date 2020/07/13 14:44
 * @description 使用 eventTime
 */
public class CountEventTimeFlink {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        System.out.println("environment.getParallelism >>>> " + env.getParallelism());

        //id,name,price,createTime


        List<String> list = new ArrayList<>();
        list.add("1,test1,10.02,2020-07-10 10:00:01");
        list.add("1,test1,11.02,2020-07-10 10:00:01");
        list.add("1,test1,12.02,2020-07-10 10:00:01");

        list.add("2,test2,10.02,2020-07-10 10:01:02");
        list.add("2,test2,11.02,2020-07-10 10:01:03");
        list.add("2,test2,12.02,2020-07-10 10:01:04");

        list.add("3,test3,10.02,2020-07-10 11:01:02");
        list.add("3,test3,11.02,2020-07-10 12:01:03");
        list.add("3,test3,12.02,2020-07-10 13:01:04");


        DataStream<String> dataStream = env.socketTextStream("localhost", 9999);
        // DataStream<String> dataStream = env.setParallelism(6).fromCollection(list);
        KeyedStream<Order, Tuple> ks = dataStream.flatMap(new FlatMapFunction<String, Order>() {
            @Override
            public void flatMap(String s, Collector<Order> collector) throws Exception {
               String[] strs = s.split(",");
               if (strs.length == 4) {
                   collector.collect(new Order(strs[0], strs[1], Double.valueOf(strs[2]), strs[3]));
               }
            }
        }).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<Order>(Time.seconds(5)) {
            private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            @Override
            public long extractTimestamp(Order order) {
                try {
                    return sdf.parse(order.getCreateTime()).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return System.currentTimeMillis();
            }
        }).keyBy("id","name");

        // ks.print("ks");

        DataStream<OrderStatistics> ds_seconds = ks.timeWindow(Time.seconds(1)).apply(new OrderWindowFunction());

        DataStream<OrderStatistics> ds_minutes = ks.timeWindow(Time.minutes(1)).apply(new OrderWindowFunction());

        ds_seconds.print("ds_seconds==========>");

        ds_minutes.print("ds_minutes==========>");

        env.execute("CountEventTimeFlink");
    }
}
