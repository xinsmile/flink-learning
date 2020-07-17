package com.xingy.window.watemark.periodic;

import java.text.SimpleDateFormat;
import java.util.Objects;
import javax.annotation.Nullable;
import com.xingy.window.model.WordEvent;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

/**
 * @author xinguiyuan
 * @className com.xingy.window.watemark.periodic.BoundedOutOfOrdernessGenerator
 * @date 2020/07/17 15:56
 * @description
 */
public class BoundedOutOfOrdernessGenerator implements AssignerWithPeriodicWatermarks<WordEvent> {

    private final long maxOutOfOrderness = 3000; // 3 seconds

    private long currentMaxTimestamp;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Nullable
    @Override
    public Watermark getCurrentWatermark() {
        // return the watermark as current highest timestamp minus the out-of-orderness bound
        return new Watermark(currentMaxTimestamp - maxOutOfOrderness);
    }

    @Override
    public long extractTimestamp(WordEvent wordEvent, long l) {
        long timestamp = wordEvent.getTimestamp();
        currentMaxTimestamp = Math.max(timestamp, currentMaxTimestamp);
        long id = Thread.currentThread().getId();
        System.out.println("currentThreadId:" + id + ",key:" + wordEvent.getWord()
                + ",eventTime:[" + wordEvent.getCurrentTime()
                + "],currentMaxTimestamp:[" + sdf.format(currentMaxTimestamp) + "],watermark:[" + sdf.format(Objects.requireNonNull(getCurrentWatermark()).getTimestamp()) + "]");

        return timestamp;
    }
}
