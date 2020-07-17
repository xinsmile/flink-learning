package com.xingy.window.model;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author xinguiyuan
 * @className com.xingy.window.model.WordCountEvent
 * @date 2020/07/17 17:18
 * @description
 */
public class WordCountEvent implements Serializable {
    private String word;
    private int totalCount;
    private String startTime;
    private String endTime;

    public WordCountEvent() {
    }

    public WordCountEvent(String word, int totalCount, String startTime, String endTime) {
        this.word = word;
        this.totalCount = totalCount;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        WordCountEvent that = (WordCountEvent) o;

        return new EqualsBuilder()
                .append(totalCount, that.totalCount)
                .append(word, that.word)
                .append(startTime, that.startTime)
                .append(endTime, that.endTime)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(word)
                .append(totalCount)
                .append(startTime)
                .append(endTime)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "WordCountEvent{" +
                "word='" + word + '\'' +
                ", totalCount=" + totalCount +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
