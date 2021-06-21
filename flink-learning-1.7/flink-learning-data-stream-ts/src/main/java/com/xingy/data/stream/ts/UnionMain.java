package com.xingy.data.stream.ts;

import java.util.ArrayList;
import java.util.List;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author xinguiyuan
 * @className com.xingy.example.Main
 * @date 2020/07/10 16:26
 * @description
 */
public class UnionMain {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<String> dataStream = env.socketTextStream("localhost", 9999);

        dataStream.print("dataStream source");

        DataStream ds = dataStream.union(dataStream);

        ds.print("union");

        List<String> list = new ArrayList<>();
        list.add("test");
        list.add("test2");
        list.add("test4");
        list.add("test6");

        List<String> list2 = new ArrayList<>();
        list2.add("test3");
        list2.add("test2");
        list2.add("test4");
        list2.add("test6");

        DataStream<String> sourceDs1 = env.fromCollection(list);
        DataStream<String> sourceDs2 = env.fromCollection(list2);

        DataStream ds1 = sourceDs1.union(sourceDs2);

        ds1.print("union");

        env.execute("flink learning project template");
    }
}
