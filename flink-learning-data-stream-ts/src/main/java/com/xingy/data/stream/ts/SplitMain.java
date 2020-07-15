package com.xingy.data.stream.ts;

import java.util.ArrayList;
import java.util.List;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.collector.selector.OutputSelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SplitStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author xinguiyuan
 * @className com.xingy.example.Main
 * @date 2020/07/10 16:26
 * @description
 * todo //  Split DataStream → SplitStream
            Select SplitStream → DataStream （已过期，SideOutPut替换Split）
 */
public class SplitMain {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //测试数据
        //1
        //2
        //3
        DataStream<String> dataStream = env.socketTextStream("localhost", 9999);

        dataStream.print("dataStream source");

        DataStream<Integer> dataStream1 = dataStream.map(new MapFunction<String, Integer>() {
            @Override
            public Integer map(String s) throws Exception {
                return Integer.valueOf(s);
            }
        });

        SplitStream<Integer> split = dataStream1.split(new OutputSelector<Integer>() {
            @Override
            public Iterable<String> select(Integer value) {
                List<String> output = new ArrayList<String>();
                if (value % 2 == 0) {
                    output.add("even");
                }
                else {
                    output.add("odd");
                }
                return output;
            }
        });

        DataStream<Integer> even = split.select("even");
        DataStream<Integer> odd = split.select("odd");
        DataStream<Integer> all = split.select("even","odd");

        even.print("even");
        odd.print("odd");
        all.print("all");



        env.execute("flink learning project template");
    }
}
