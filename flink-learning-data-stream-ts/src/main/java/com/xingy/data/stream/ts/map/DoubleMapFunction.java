package com.xingy.data.stream.ts.map;

import org.apache.flink.api.common.functions.MapFunction;

/**
 * @author xinguiyuan
 * @className com.xingy.example.map.DoubleMapFunction
 * @date 2020/07/10 15:40
 * @description
 */
public class DoubleMapFunction implements MapFunction<String, Integer> {

    @Override
    public Integer map(String s) throws Exception {
        return Integer.valueOf(s) * 2;

    }
}
