package com.xingy.data.stream.ts;

import com.xingy.data.stream.ts.filter.LessEqualZoreFilter;
import com.xingy.data.stream.ts.map.DoubleMapFunction;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author xinguiyuan
 * @className com.zhisheng.data.stream.map.flatmap.FilterAndMapMain
 * @date 2020/07/09 14:54
 * @description
 * TODO //
    Map DataStream -> DataStream 取一个元素并产生一个元素。一个映射函数
    Filter DataStream -> DataStream 为每个元素评估一个布尔函数，并保留该函数返回true的布尔函数。筛选出零值的筛选器：
 */
public class FilterAndMapMain {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<String> dataStream = env.socketTextStream("localhost", 9999);

        dataStream.print("dataStream source");

        // 1.map 取一个元素并产生一个元素。一个映射函数
        // 2.filter 为每个元素评估一个布尔函数，并保留该函数返回true的布尔函数。筛选出零值的筛选器：
        // 测试例子 0 10 20 30
        DataStream<Integer> ds = dataStream
                .map((MapFunction<String, Integer>) s -> Integer.valueOf(s) * 2)
                .filter((FilterFunction<Integer>) i -> i > 0);

        //使用自定义类
        DataStream<Integer> ds1 = dataStream
                .map(new DoubleMapFunction())
                .filter(new LessEqualZoreFilter());

        ds.print("ds filter & map");

        ds1.print("ds1 filter & map");
        env.execute("flink learning project template");
    }
}
