package com.xingy.data.stream.ts.modle;

import java.util.Objects;

/**
 * @author xinguiyuan
 * @className com.zhisheng.example.modle.WordCount
 * @date 2020/07/09 15:23
 * @description
 */
public class WordCount {

    private String word;
    private int num;

    public WordCount() {

    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordCount wordCount = (WordCount) o;
        return num == wordCount.num &&
                word.equals(wordCount.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, num);
    }

    @Override
    public String toString() {
        return "WordCount{" +
                "word='" + word + '\'' +
                ", num=" + num +
                '}';
    }
}
