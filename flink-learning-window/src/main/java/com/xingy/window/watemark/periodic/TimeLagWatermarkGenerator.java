package com.xingy.window.watemark.periodic;

import java.text.SimpleDateFormat;
import java.util.Objects;
import javax.annotation.Nullable;
import com.xingy.window.model.WordEvent;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

/**
 * @author xinguiyuan
 * @className com.xingy.window.watemark.periodic.TimeLagWatermarkGenerator
 * @date 2020/07/17 15:53
 * @description
 */
public class TimeLagWatermarkGenerator implements AssignerWithPeriodicWatermarks<WordEvent> {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final long maxTimeLag = 5000; // 5 seconds

    @Nullable
    @Override
    public Watermark getCurrentWatermark() {
        // return the watermark as current time minus the maximum time lag
        return new Watermark(System.currentTimeMillis() - maxTimeLag);
    }

    @Override
    public long extractTimestamp(WordEvent wordEvent, long previousElementTimestamp) {
        long id = Thread.currentThread().getId();
        System.out.println("currentThreadId:" + id + ",key:" + wordEvent.getWord()
                + ",eventTime:[" + wordEvent.getCurrentTime()
                + "],watermark:[" + sdf.format(Objects.requireNonNull(getCurrentWatermark()).getTimestamp()) + "]");


        return wordEvent.getTimestamp();
    }
}
