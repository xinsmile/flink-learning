package com.xingy.data.stream.ts;

import com.xingy.data.stream.ts.flatmap.WordCountFlatMapFunction;
import com.xingy.data.stream.ts.modle.WordCount;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author xinguiyuan
 * @className com.xingy.example.Main
 * @date 2020/07/10 16:26
 * @description TODO// KeyBy DataStream -> KeyedStream 从逻辑上将流划分为不相交的分区。具有相同键的所有记录都分配给同一分区。在内部，keyBy（）是通过哈希分区实现的。有多种指定密钥的方法
 */
public class KeyByMain {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<String> dataStream = env.socketTextStream("localhost", 9999);

        dataStream.print("dataStream source");

        //test0,0@test1,1@test2,2@test2,2@test3,3


        KeyedStream<WordCount, Tuple> kds =
                dataStream.flatMap(new WordCountFlatMapFunction()).keyBy("word");
        kds.print("kds flatMap & keyBy");


        // Aggregations
        DataStream<WordCount> ds =
                dataStream.flatMap(new WordCountFlatMapFunction())
                .keyBy("word")
                .sum("num");

        ds.print("ds sum flatMap & keyBy");


        DataStream<WordCount> ds1 =
                dataStream.flatMap(new WordCountFlatMapFunction())
                        .keyBy("word")
                        .max("num");

        ds1.print("ds1 max flatMap & keyBy");


        DataStream<WordCount> ds2 =
                dataStream.flatMap(new WordCountFlatMapFunction())
                        .keyBy("word")
                        .min("num");

        ds2.print("ds2 min flatMap & keyBy");

        env.execute("flink learning project template");
    }
}
