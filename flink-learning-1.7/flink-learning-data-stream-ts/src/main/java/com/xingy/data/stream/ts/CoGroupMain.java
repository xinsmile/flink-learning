package com.xingy.data.stream.ts;

import java.util.Iterator;
import com.xingy.data.stream.ts.map.WordCountMapFunction;
import com.xingy.data.stream.ts.modle.WordCount;
import org.apache.flink.api.common.functions.CoGroupFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * @author xinguiyuan
 * @className com.xingy.example.Main
 * @date 2020/07/10 16:26
 * @description
 * todo // Window CoGroup DataStream，DataStream→DataStream  将两个数据流组合在给定键和一个公共窗口上。
    Connect DataStream,DataStream → ConnectedStreams 连接”两个保留其类型的数据流。连接允许两个流之间共享状态。
 */
public class CoGroupMain {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<String> dataStream = env.socketTextStream("localhost", 9999);
        DataStream<String> otherStream = env.socketTextStream("localhost", 9990);


        // List<String> list = new ArrayList<>();
        // list.add("test,1");
        // list.add("test,2");
        // list.add("test1,2");
        // list.add("test1,3");
        //
        //
        // List<String> list2 = new ArrayList<>();
        // list2.add("test,3");
        // list2.add("test,4");
        // list2.add("test1,3");
        // list2.add("test1,4");
        //
        // DataStream<String> dataStream = env.setParallelism(6).fromCollection(list);
        // DataStream<String> otherStream = env.setParallelism(6).fromCollection(list2);

        DataStream<WordCount> ds = dataStream.map(new WordCountMapFunction());

        DataStream<WordCount> ds1 = otherStream.map(new WordCountMapFunction());

        dataStream.print("dataStream source");

        DataStream<WordCount> coGroupds = ds.coGroup(ds1).where(new KeySelector<WordCount, String>() {
            @Override
            public String getKey(WordCount wordCount) throws Exception {
                return wordCount.getWord();
            }
        }).equalTo(new KeySelector<WordCount, String>() {
            @Override
            public String getKey(WordCount wordCount) throws Exception {
                return wordCount.getWord();
            }
        }).window(TumblingProcessingTimeWindows.of(Time.seconds(3)))
                .apply(new CoGroupFunction<WordCount, WordCount, WordCount>() {
                    @Override
                    public void coGroup(Iterable<WordCount> iterable, Iterable<WordCount> iterable1, Collector<WordCount> collector) throws Exception {
                        int num = 0;
                        String word = null;
                        System.out.println();
                        WordCount wc = new WordCount();
                        Iterator<WordCount> iterator = iterable.iterator();
                        Iterator<WordCount> iterator1 = iterable1.iterator();

                        while (iterator.hasNext()) {
                            WordCount wcTmp = iterator.next();
                            word = wcTmp.getWord();
                            System.out.println("iterable:" + word);
                            num += wcTmp.getNum();
                        }
                        while (iterator1.hasNext()) {
                            WordCount wcTmp = iterator1.next();
                            word = wcTmp.getWord();
                            System.out.println("iterator1:" + word);
                            num += wcTmp.getNum();
                        }
                        wc.setNum(num);
                        wc.setWord(word);
                        collector.collect(wc);
                    }
                });



        coGroupds.print("coGroupds");

        env.execute("flink learning project template");
    }
}
