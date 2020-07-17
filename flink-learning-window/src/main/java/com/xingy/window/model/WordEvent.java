package com.xingy.window.model;

import java.util.Date;
import com.xkzhangsan.time.formatter.DateFormatPattern;
import com.xkzhangsan.time.formatter.DateTimeFormatterUtil;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class WordEvent {
    private String word;
    private int count;
    private long timestamp;
    private String currentTime;

    public WordEvent() {
    }

    public WordEvent(String word, int count, long timestamp) {
        this.word = word;
        this.count = count;
        this.timestamp = timestamp;
        this.currentTime = DateTimeFormatterUtil
                .format(new Date(timestamp), DateFormatPattern.YYYY_MM_DD_HH_MM_SS);
    }

    public WordEvent(String word, int count, String currentTime) {
        this.word = word;
        this.count = count;
        this.timestamp = DateTimeFormatterUtil
                .parseToDate(currentTime,
                        DateFormatPattern.YYYY_MM_DD_HH_MM_SS).getTime();
        this.currentTime = currentTime;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public String toString() {
        return "WordEvent{" +
                "word='" + word + '\'' +
                ", count=" + count +
                ", timestamp=" + timestamp +
                ", currentTime='" + currentTime + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        WordEvent wordEvent = (WordEvent) o;

        return new EqualsBuilder()
                .append(count, wordEvent.count)
                .append(timestamp, wordEvent.timestamp)
                .append(word, wordEvent.word)
                .append(currentTime, wordEvent.currentTime)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(word)
                .append(count)
                .append(timestamp)
                .append(currentTime)
                .toHashCode();
    }
}
