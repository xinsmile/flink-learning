package com.xingy.window;

import com.xingy.window.model.WordEvent;
import com.xingy.window.watemark.periodic.BoundedOutOfOrdernessGenerator;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

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

        // DataStream<WordEvent> data = env.addSource(new CustomSource());

        DataStream<String> dataStream = env.socketTextStream("localhost", 9999);

        dataStream.print("dataStream source");
        DataStream<WordEvent> data = dataStream.map(new MapFunction<String, WordEvent>() {
            @Override
            public WordEvent map(String s) throws Exception {
                String[] strs = s.split(",");
                return new WordEvent(strs[0], Integer.parseInt(strs[1]), strs[2]);
            }
        });

        //设置水印
        data.assignTimestampsAndWatermarks(new BoundedOutOfOrdernessGenerator())
                .keyBy("word")
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                // .timeWindow(Time.seconds(3))
                .apply(MyWindow.create())
                .print("ds count");

        env.execute("flink learning project template");
    }
}
