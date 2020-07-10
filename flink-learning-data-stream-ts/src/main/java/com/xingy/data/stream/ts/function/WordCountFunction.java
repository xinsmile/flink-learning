package com.xingy.data.stream.ts.function;

import com.xingy.data.stream.ts.modle.WordCount;

/**
 * @author xinguiyuan
 * @className com.zhisheng.example.function.WordCountFunction
 * @date 2020/07/09 16:00
 * @description
 */
public class WordCountFunction {
    public static WordCount split(String value) {
        String[] wordCounts = value.split(",");
        WordCount wordCount = new WordCount();
        if (wordCounts.length == 2) {
            wordCount.setWord(wordCounts[0]);
            wordCount.setNum(Integer.parseInt(wordCounts[1]));
        }
        return wordCount;
    }
}
