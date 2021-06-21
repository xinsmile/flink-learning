package com.xingy.data.stream.ts.reduce;

import com.xingy.data.stream.ts.modle.WordCount;
import org.apache.flink.api.common.functions.ReduceFunction;

/**
 * @author xinguiyuan
 * @className com.xingy.data.stream.ts.reduce.WordCountReduce
 * @date 2020/07/14 16:49
 * @description
 */
public class WordCountReduce implements ReduceFunction<WordCount> {

    @Override
    public WordCount reduce(WordCount wordCount, WordCount t1) throws Exception {
        wordCount.setNum(wordCount.getNum() + t1.getNum());
        return wordCount;
    }
}
