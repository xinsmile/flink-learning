package com.xingy.window;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.ContinuousEventTimeTrigger;
import org.apache.flink.util.Collector;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xinguiyuan
 * @className com.xingy.window.ContinueProcessingTriggerDemo
 * @date 2020/09/16 17:18
 * @description
 */
public class ContinueEventTriggerDemo {
    public static void main(String[] args) throws Exception {

        String hostName="localhost";
        Integer port=Integer.parseInt("8801");

        final StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();

        //从指定socket获取输入数据
        DataStream<String> text=env.socketTextStream(hostName,port);

        text.flatMap(new LineSplitter())
                .keyBy(0)
                .window(TumblingEventTimeWindows.of(Time.seconds(30)))
                .trigger(ContinuousEventTimeTrigger.of(Time.seconds(10)))
                .sum(1)
                .map(new TimestampAdd())
                .print();

        env.execute("start demo!");

    }



    public static final class LineSplitter implements FlatMapFunction<String, Tuple2<String,Integer>> {

        @Override
        public void flatMap(String s, Collector<Tuple2<String, Integer>> out) throws Exception {
            String[] tokens=s.toLowerCase().split("\\W+");

            for (String token:tokens){
                if (token.length()>0){
                    out.collect(new Tuple2<>(token,1));
                }
            }
        }
    }


    public static final class TimestampAdd implements MapFunction<Tuple2<String,Integer>, Tuple3<String,String,Integer>> {

        @Override
        public Tuple3<String, String, Integer> map(Tuple2<String, Integer> value) throws Exception {

            DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String s=format.format(new Date());

            return new Tuple3<>(value.f0,s,value.f1);
        }
    }
}
