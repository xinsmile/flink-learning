package com.xingy.data.stream.ts;

import com.xingy.data.stream.ts.map.WordCountMapFunction;
import com.xingy.data.stream.ts.modle.WordCount;
import com.xingy.data.stream.ts.reduce.WordCountReduce;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author xinguiyuan
 * @className com.xingy.data.stream.ts.ReduceMain
 * @date 2020/07/14 16:38
 * @description
 * TODO// reduce KeyedStream -> DataStream  对键控数据流进行“滚动”压缩。将当前元素与最后一个减少的值合并，并发出新值。
 */
public class ReduceMain {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<String> dataStream = env.socketTextStream("localhost", 9999);

        dataStream.print("dataStream source");

        // test0,0
        // test1,1
        // test2,2
        // test2,2
        // test3,3

        // DataStream<WordCount> ds =
        //         dataStream.map(new WordCountMapFunction())
        //                 .keyBy("word")
        //                 .reduce((ReduceFunction<WordCount>) (wordCount, t1) -> {
        //                     wordCount.setNum(wordCount.getNum() + t1.getNum());
        //                     return wordCount;
        //                 });

        DataStream<WordCount> ds =
                dataStream.map(new WordCountMapFunction())
                        .keyBy("word")
                        .reduce(new WordCountReduce());

        ds.print("ds keyBy reduce");

        env.execute("flink learning project template");
    }
}
