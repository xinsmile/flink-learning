package com.xingy.window;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.xingy.window.trigger.MyContinuousProcessingTimeTrigger;
import com.xingy.window.windowing.MyWindow1;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.types.Row;
import org.apache.flink.util.Collector;

/**
 * @author xinguiyuan
 * @className com.xingy.window.ContinueProcessingTriggerDemo
 * @date 2020/09/16 17:18
 * @description
 */
public class ContinueProcessingTriggerDemo2 {
    public static void main(String[] args) throws Exception {

        String hostName="localhost";
        Integer port=Integer.parseInt("8801");

        final StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();

        //从指定socket获取输入数据
        DataStream<String> text=env.socketTextStream(hostName,port);


        text.flatMap(new LineSplitter())
                .keyBy(new KeySelector<Row, Tuple1<String>>() {
                    @Override
                    public Tuple1<String> getKey(Row row) throws Exception {
                        return new Tuple1<>(row.getField(0).toString());
                    }
                })
                .window(TumblingProcessingTimeWindows.of(Time.seconds(30)))
                .trigger(MyContinuousProcessingTimeTrigger.of(Time.seconds(10)))
                .apply(new MyWindow1(), TypeInformation.of(Row.class))
                .print("ContinuousProcessingTimeTrigger");

        env.execute("start demo!");

    }



    public static final class LineSplitter implements FlatMapFunction<String, Row> {

        @Override
        public void flatMap(String s, Collector<Row> out) throws Exception {
            DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("数据： time: " + format.format(new Date(System.currentTimeMillis()))
                    + ", data: " + s);
            String[] tokens=s.toLowerCase().split("\\W+");

            for (String token:tokens){
                if (token.length()>0){
                    Row row = new Row(2);
                    row.setField(0, token);
                    row.setField(1, 1);
                    out.collect(row);
                }
            }
        }
    }


    public static final class TimestampAdd implements MapFunction<Row, Tuple3<String,String,Integer>> {

        @Override
        public Tuple3<String, String, Integer> map(Row value) throws Exception {

            DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String s=format.format(new Date());

            return new Tuple3<>(value.getField(0).toString(),s,Integer.valueOf(value.getField(1).toString()));
        }
    }
}
