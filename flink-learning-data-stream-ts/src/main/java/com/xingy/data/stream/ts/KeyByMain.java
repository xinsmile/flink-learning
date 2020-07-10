package com.xingy.data.stream.ts;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author xinguiyuan
 * @className com.xingy.example.Main
 * @date 2020/07/10 16:26
 * @description
 */
public class KeyByMain {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<String> dataStream = env.socketTextStream("localhost", 9999);

        dataStream.print("dataStream source");



        env.execute("flink learning project template");
    }
}
