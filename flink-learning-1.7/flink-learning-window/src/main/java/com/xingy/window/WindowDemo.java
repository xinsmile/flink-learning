package com.xingy.window;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import com.xingy.window.model.WordCountEvent;
import com.xingy.window.model.WordEvent;
import com.xingy.window.windowing.MyWindow;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.ContinuousEventTimeTrigger;

/**
 * @author xinguiyuan
 * @className com.xingy.window.WindowDemo
 * @date 2020/07/16 09:16
 * @description
 */
public class WindowDemo {
    public static void main(String[] args) throws Exception {
        WindowDemo.tumblingEventTimeTest();
    }

    /**
     * @Author xinguiyuan
     * @Description //TODO 滚动窗口 eventTime
     * @Date 2020/7/17 15:10 
     * @Param []
     * @Return void
     **/
    public static void tumblingEventTimeTest() throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //设置eventTime
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        //疑问 --- 并行度没有设置时 窗口没有触发 ？！
        env.setParallelism(1);

        // DataStream<WordEvent> data = env.addSource(new CustomSource());

        DataStream<String> dataStream = env.socketTextStream("localhost", 9999);

        // dataStream.print("dataStream source");
        DataStream<WordEvent> data = dataStream.map(new MapFunction<String, WordEvent>() {
            @Override
            public WordEvent map(String s) throws Exception {
                String[] strs = s.split(",");
                return new WordEvent(strs[0], Integer.parseInt(strs[1]), strs[2]);
            }
        });

        //设置水印
        DataStream<WordCountEvent> ds_second =
                // data.assignTimestampsAndWatermarks(new BoundedOutOfOrdernessGenerator())
        // data.assignTimestampsAndWatermarks(new TimeLagWatermarkGenerator())
        data.assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<WordEvent>(Time.seconds(1)) {
            private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            @Override
            public long extractTimestamp(WordEvent wordEvent) {
                long id = Thread.currentThread().getId();
                System.out.println("currentThreadId:" + id + ",key:" + wordEvent.getWord()
                        + ",eventCurrentTime:[" + wordEvent.getCurrentTime()
                        + "],eventTime:[" + sdf.format(new Date(wordEvent.getTimestamp()))
                        + "],watermark:[" + sdf.format(Objects.requireNonNull(getCurrentWatermark()).getTimestamp()) + "]");

                return wordEvent.getTimestamp();
            }
        }).keyBy("word")
                .window(TumblingEventTimeWindows.of(Time.seconds(30)))
                .trigger(ContinuousEventTimeTrigger.of(Time.seconds(10)))
                // .timeWindow(Time.seconds(10))
                // .timeWindow(Time.seconds(3))
                .apply(MyWindow.create());

        ds_second.print("ds_second");

        env.execute("flink learning project template");
    }
}
