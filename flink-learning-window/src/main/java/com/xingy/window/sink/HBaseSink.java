package com.xingy.window.sink;

import java.util.ArrayList;
import java.util.List;
import com.xingy.window.utils.HBaseUtil;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;

/**
 * @author xinguiyuan
 * @className com.xingy.window.sink.HbaseSink
 * @date 2020/08/11 15:04
 * @description
 */
public class HBaseSink extends RichSinkFunction<Tuple2<String, Double>>{
    private transient Integer maxSize = 1000;
    private transient Long delayTime = 5000L;

    public HBaseSink() {
    }

    public HBaseSink(Integer maxSize, Long delayTime) {
        this.maxSize = maxSize;
        this.delayTime = delayTime;
    }

    private transient Connection connection;
    private transient Long lastInvokeTime;
    private transient List<Put> puts = new ArrayList<>(maxSize);

    // 创建连接
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);

        // 获取全局配置文件，并转为ParameterTool
        ParameterTool params =
                (ParameterTool) getRuntimeContext().getExecutionConfig().getGlobalJobParameters();

        //创建一个Hbase的连接
        connection = HBaseUtil.getConnection(
                params.getRequired("hbase.zookeeper.quorum"),
                params.getInt("hbase.zookeeper.property.clientPort", 2181)
        );

        // 获取系统当前时间
        lastInvokeTime = System.currentTimeMillis();
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }

    @Override
    public void invoke(Tuple2<String, Double> value, Context context) throws Exception {

        String rk = value.f0;
        //创建put对象，并赋rk值
        Put put = new Put(rk.getBytes());

        // 添加值：f1->列族, order->属性名 如age， 第三个->属性值 如25
        put.addColumn("f1".getBytes(), "order".getBytes(), value.f1.toString().getBytes());

        puts.add(put);// 添加put对象到list集合

        //使用ProcessingTime
        long currentTime = System.currentTimeMillis();

        //开始批次提交数据
        if (puts.size() == maxSize || currentTime - lastInvokeTime >= delayTime) {

            //获取一个Hbase表
            Table table = connection.getTable(TableName.valueOf("database:table"));
            table.put(puts);//批次提交

            puts.clear();

            lastInvokeTime = currentTime;
            table.close();
        }
    }
}
