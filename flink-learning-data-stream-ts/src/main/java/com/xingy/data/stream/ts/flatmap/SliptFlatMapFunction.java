package com.xingy.data.stream.ts.flatmap;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

/**
 * @author xinguiyuan
 * @className com.xingy.example.flatmap.SliptFlatMapFunction
 * @date 2020/07/10 16:19
 * @description
 */
public class SliptFlatMapFunction implements FlatMapFunction<String, String> {

    @Override
    public void flatMap(String s, Collector<String> collector) throws Exception {
        for (String s1 : s.split(",")) {
            collector.collect(s1);
        }
    }
}
