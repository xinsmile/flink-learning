package com.xingy.window.trigger;

import com.xingy.window.model.WordEvent;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xinguiyuan
 * @className com.xingy.window.trigger.MyTrigger
 * @date 2020/07/16 10:46
 * @description
 *
 *  1、CONTINUE: 什么也不做
 *  2、FIRE: 触发计算
 *  3、PURGE: 清除窗口中的数据
 *  4、FIRE_AND_PURGE: 触发计算并清除窗口中的数据
 */
public class MyTrigger extends Trigger<WordEvent, TimeWindow> {


    private static final Logger log = LoggerFactory.getLogger(MyTrigger.class);

    public static MyTrigger create() {
        return new MyTrigger();
    }

    @Override
    public TriggerResult onElement(WordEvent wordEvent, long l, TimeWindow timeWindow, TriggerContext triggerContext) throws Exception {
        log.info("======onElement====window start = {}, window end = {}", timeWindow.getStart(), timeWindow.getEnd());

        return TriggerResult.CONTINUE;
    }

    @Override
    public TriggerResult onProcessingTime(long l, TimeWindow timeWindow, TriggerContext triggerContext) throws Exception {
        System.out.println("======onProcessingTime====");

        return TriggerResult.CONTINUE;
    }

    @Override
    public TriggerResult onEventTime(long l, TimeWindow timeWindow, TriggerContext triggerContext) throws Exception {
        System.out.println("======onEventTime====");

        return TriggerResult.CONTINUE;
    }

    @Override
    public void clear(TimeWindow timeWindow, TriggerContext triggerContext) throws Exception {

    }

    @Override
    public void onMerge(TimeWindow window, OnMergeContext ctx) throws Exception {
        super.onMerge(window, ctx);
    }
}
