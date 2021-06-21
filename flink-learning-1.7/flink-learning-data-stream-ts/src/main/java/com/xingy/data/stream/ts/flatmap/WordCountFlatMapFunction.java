package com.xingy.data.stream.ts.flatmap;

import com.xingy.data.stream.ts.function.WordCountFunction;
import com.xingy.data.stream.ts.modle.WordCount;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

/**
 * @author xinguiyuan
 * @className com.zhisheng.example.flatmap.WordCountFlatMapFunction
 * @date 2020/07/09 16:03
 * @description
 */
public class WordCountFlatMapFunction implements FlatMapFunction<String, WordCount> {


    @Override
    public void flatMap(String s, Collector<WordCount> collector) throws Exception {
        String[] str = s.split("@");
        for(String wc : str) {
            collector.collect(WordCountFunction.split(wc));
        }
    }
}
