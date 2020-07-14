package com.xingy.data.stream.ts;

import com.xingy.data.stream.ts.flatmap.SliptFlatMapFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author xinguiyuan
 * @className com.zhisheng.data.stream.map.flatmap.FlatMapMain
 * @date 2020/07/09 14:54
 * @description
 * TODO//
    FlatMap DataStream -> DataStream 取一个元素并产生零个，一个或多个元素。平面图功能可将句子拆分为单词：
 */
public class FlatMapMain {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<String> dataStream = env.socketTextStream("localhost", 9999);

        dataStream.print("dataStream source");

        // flatMap 取一个元素并产生零个，一个或多个元素。平面图功能可将句子拆分为单词：
        // test1,test2,test3
        DataStream<String> ds = dataStream.flatMap((FlatMapFunction<String, String>) (s, collector) -> {
            String[] str = s.split(",");
            for (String v : str) {
                collector.collect(v);
            }
        }).returns(String.class);

        //自定义
        DataStream<String> ds1 = dataStream.flatMap(new SliptFlatMapFunction());

        ds.print("ds flatMap");
        ds1.print("ds1 flatMap");

        env.execute("flink learning project template");
    }
}
