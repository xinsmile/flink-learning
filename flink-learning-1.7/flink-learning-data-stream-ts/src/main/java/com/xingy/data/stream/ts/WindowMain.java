package com.xingy.data.stream.ts;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author xinguiyuan
 * @className com.xingy.example.Main
 * @date 2020/07/10 16:26
 * @description todo// Window KeyedStream → WindowedStream
 */
public class WindowMain {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<String> dataStream = env.socketTextStream("localhost", 9999);

        dataStream.print("dataStream source");


        //滚动窗口 TumblingEventTimeWindows

        //滑动窗口 SlidingEventTimeWindows

        //会话窗口 EventTimeSessionWindows


        env.execute("flink learning project template");
    }
}
