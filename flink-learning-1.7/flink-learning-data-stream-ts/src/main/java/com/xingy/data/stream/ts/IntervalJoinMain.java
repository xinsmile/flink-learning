package com.xingy.data.stream.ts;

import java.text.SimpleDateFormat;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * @author xinguiyuan
 * @className com.xingy.example.Main
 * @date 2020/07/10 16:26
 * @description
 * todo// IntervalJoin KeyedStream→DataStream 仅支持eventTime 需要设置assignTimestampsAndWatermarks
    在给定的时间间隔内，使用公共密钥将两个键控流的两个元素e1和e2连接起来，
 */
public class IntervalJoinMain {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        DataStream<String> dataStream = env.socketTextStream("localhost", 9999);

        DataStream<String> dataStream1 = env.socketTextStream("localhost", 9990);

        // test,0,2020-07-10 10:20:10
        // test1,1,2020-07-10 10:20:10
        dataStream.print("dataStream source");
        KeyedStream<Tuple3<String, Double, Long>, Tuple> ds =getKeyedStream(dataStream);

        KeyedStream<Tuple3<String, Double, Long>, Tuple> ds1 = getKeyedStream(dataStream1);

        DataStream<Tuple3<String, Double, Long>> ds3 = ds.intervalJoin(ds1)
                .between(Time.seconds(-5), Time.seconds(5))
                .process(new ProcessJoinFunction<Tuple3<String, Double, Long>, Tuple3<String, Double, Long>, Tuple3<String, Double, Long>>() {
                    @Override
                    public void processElement(Tuple3<String, Double, Long> sdlT3, Tuple3<String, Double, Long> sdlT32, Context context, Collector<Tuple3<String, Double, Long>> collector) throws Exception {
                        collector.collect(sdlT3);
                        collector.collect(sdlT32);
                    }
                });

        ds3.print("DS3");

        env.execute("flink learning project template");
    }

    public static KeyedStream<Tuple3<String, Double, Long>, Tuple> getKeyedStream(DataStream ds) {
       return ds.flatMap(new FlatMapFunction<String, Tuple3<String, Double, Long>>() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            @Override
            public void flatMap(String s, Collector<Tuple3<String, Double, Long>> collector) throws Exception {
                String[] strs = s.split(",");
                Tuple3<String, Double, Long> tuple3 = new Tuple3<>();
                tuple3.f0 = strs[0];
                tuple3.f1 = Double.valueOf(strs[1]);
                tuple3.f2 = simpleDateFormat.parse(strs[2]).getTime();
                collector.collect(tuple3);
            }
        }).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<Tuple3<String, Double, Long>>(Time.seconds(5)) {
           @Override
           public long extractTimestamp(Tuple3<String, Double, Long> o) {
               return o.f2;
           }
       }).keyBy(0);
    }
}
