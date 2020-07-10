package com.xingy.data.stream.ts.map;

import com.xingy.data.stream.ts.modle.WordCount;
import com.xingy.data.stream.ts.function.WordCountFunction;
import org.apache.flink.api.common.functions.MapFunction;

/**
 * @author xinguiyuan
 * @className com.zhisheng.example.flatmap.WordCountMapFunction
 * @date 2020/07/09 15:28
 * @description
 */
public class WordCountMapFunction implements MapFunction<String, WordCount> {

    @Override
    public WordCount map(String value) throws Exception {
        return WordCountFunction.split(value);
    }
}
