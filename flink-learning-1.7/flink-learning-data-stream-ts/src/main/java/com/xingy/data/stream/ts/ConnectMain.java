package com.xingy.data.stream.ts;

import java.util.ArrayList;
import java.util.List;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoFlatMapFunction;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;
import org.apache.flink.util.Collector;

/**
 * @author xinguiyuan
 * @className com.xingy.example.Main
 * @date 2020/07/10 16:26
 * @description
 * todo // Connect DataStream,DataStream → ConnectedStreams 连接”两个保留其类型的数据流。连接允许两个流之间共享状态。
        CoMap, CoFlatMap ConnectedStreams → DataStream  与连接的数据流上的map和flatMap相似
 */
public class ConnectMain {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        List<String> list = new ArrayList<>();
        list.add("test,test1");
        list.add("test2,test3");
        list.add("test4,test5");
        list.add("test6,test7");


        List<Integer> list2 = new ArrayList<>();
        list2.add(3);
        list2.add(4);
        list2.add(5);
        list2.add(7);

        DataStream<String> dataStream = env.fromCollection(list);
        DataStream<Integer> otherStream = env.fromCollection(list2);
        dataStream.print("dataStream source");

        ConnectedStreams<String, Integer> connectedStreams = dataStream.connect(otherStream);

        DataStream<Boolean> mapDs = connectedStreams.map(new CoMapFunction<String, Integer, Boolean>() {
            @Override
            public Boolean map1(String s) throws Exception {
                return true;
            }

            @Override
            public Boolean map2(Integer integer) throws Exception {
                return false;
            }
        });

        mapDs.print("mapDs");

        DataStream<String> flatMapDs = connectedStreams.flatMap(new CoFlatMapFunction<String, Integer, String>() {
            @Override
            public void flatMap1(String s, Collector<String> collector) throws Exception {
                for(String s1: s.split(",")) {
                    collector.collect(s1);
                }
            }

            @Override
            public void flatMap2(Integer integer, Collector<String> collector) throws Exception {
                collector.collect(String.valueOf(integer));
            }
        });
        flatMapDs.print("flatMapDs");



        env.execute("flink learning project template");
    }
}
