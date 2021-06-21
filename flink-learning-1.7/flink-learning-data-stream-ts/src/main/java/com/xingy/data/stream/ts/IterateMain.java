package com.xingy.data.stream.ts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.IterativeStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author xinguiyuan
 * @className com.xingy.data.stream.ts.IterateMain
 * @date 2020/07/15 16:19
 * @description todo// Iterate DataStream → IterativeStream → DataStream
 */
public class IterateMain {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);
        // DataStream<String> dataStream = env.socketTextStream("localhost", 9999);

        List<Long> list = new ArrayList<>();
        list.add(10L);
        list.add(9L);
        list.add(8L);
        list.add(7L);

        DataStream<Long> initialStream = env.fromCollection(list);

        initialStream.print("dataStream source");

        IterativeStream<Long> iteration = initialStream.iterate();

        //iteration 需要设置Parallelism 与源的Parallelism一致

        // public void addFeedbackEdge(Transformation<T> transform) {
        //     if (transform.getParallelism() != this.getParallelism()) {
        //         throw new UnsupportedOperationException("Parallelism of the feedback stream must match the parallelism of the original stream. Parallelism of original stream: " + this.getParallelism() + "; parallelism of feedback stream: " + transform.getParallelism() + ". Parallelism can be modified using DataStream#setParallelism() method");
        //     } else {
        //         this.feedbackEdges.add(transform);
        //     }
        // }

        DataStream<Long> iterationBody = iteration.setParallelism(2).map(new MapFunction<Long, Long>() {
            @Override
            public Long map(Long aLong) throws Exception {
                return (aLong % new Random(2).nextLong() - 1);
            }
        });

        iterationBody.print("iterationBody ds");
        DataStream<Long> feedback = iterationBody.filter(new FilterFunction<Long>(){
            @Override
            public boolean filter(Long value) throws Exception {
                return value > 0;
            }
        });
        iteration.closeWith(feedback);
        DataStream<Long> output = iterationBody.filter(new FilterFunction<Long>(){
            @Override
            public boolean filter(Long value) throws Exception {
                return value <= 0;
            }
        });


        output.print("output");

        env.execute("flink learning project template");
    }
}
