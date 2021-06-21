package com.xingy.data.stream.ts;

import com.xingy.data.stream.ts.map.WordCountMapFunction;
import com.xingy.data.stream.ts.modle.WordCount;
import org.apache.flink.api.common.functions.FoldFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author xinguiyuan
 * @className com.xingy.example.Main
 * @date 2020/07/10 16:26
 * @description TODO// Fold KeyedStream→DataStream 带有初始值的键控数据流上的“滚动”折叠。将当前元素与上一个折叠值组合在一起并发出新值。
 */
public class FoldMain {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // test0,1
        // test0,2
        DataStream<String> dataStream = env.socketTextStream("localhost", 9999);

        dataStream.print("dataStream source");

        WordCount wc = new WordCount();
        wc.setWord("start");
        wc.setNum(11);
        DataStream<WordCount> ds =
                dataStream.map(new WordCountMapFunction())
                        .keyBy("word")
                        .fold(wc, new FoldFunction<WordCount, WordCount>() {
                            @Override
                            public WordCount fold(WordCount current, WordCount o) throws Exception {
                                current.setWord(current.getWord() + "-" + o.getNum());
                                return current;
                            }
                        });

        ds.print("ds fold");


        // 打印结果
        // dataStream source:8> test0,1
        // ds fold:6> WordCount{word='start-1', num=11}
        // dataStream source:1> test0,2
        // ds fold:6> WordCount{word='start-1-2', num=11}

        env.execute("flink learning project template");
    }
}
