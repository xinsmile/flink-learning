package com.xingy.data.stream.ts;

import com.xingy.data.stream.ts.modle.WordCount;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author xinguiyuan
 * @className com.zhisheng.data.stream.map.flatmap.Main1
 * @date 2020/07/09 14:54
 * @description
 */
public class Main1 {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //test0,0@test1,1@test2,2@test2,2@test3,3
        DataStream<String> dataStream = env.socketTextStream("localhost", 9999);


        //FlatMap
        DataStream<WordCount> ds = dataStream
                .flatMap((FlatMapFunction<String, WordCount>) (s, collector) -> {
            String[] strs = s.split("@");
            for (String wc : strs) {
                WordCount wordCount = new WordCount();
                String[] wordCounts = wc.split(",");
                if (wordCounts.length == 2) {
                    wordCount.setWord(wordCounts[0]);
                    wordCount.setNum(Integer.parseInt(wordCounts[1]));
                }
                collector.collect(wordCount);
            }
        }).returns(WordCount.class);


        ds.print("1");

        //Map
        DataStream<WordCount> ds2 = ds
                .map((MapFunction<WordCount, WordCount>) (owc) -> {
            owc.setNum(owc.getNum() * 2);
            return owc;
        });
        ds2.print("2");

        // filter
        DataStream<WordCount> ds3 = ds2
                .filter((FilterFunction<WordCount>) wordCount -> wordCount.getNum() > 0);
        ds3.print("3");

        //KeyBy
        KeyedStream ds4 = ds3.keyBy("word");
        DataStream<WordCount> ds5 = ds3
                .keyBy("word")
                .sum("num");

        ds4.print("4");

        ds5.print("5");

        env.execute("flink learning project template");
    }
}
