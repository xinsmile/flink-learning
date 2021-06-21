package com.xingy.window.trigger;

/**
 * @author xinguiyuan
 * @className com.xingy.window.trigger.MyContinuousEventTimeTrigger
 * @date 2020/10/14 15:09
 * @description
 */

import org.apache.flink.annotation.PublicEvolving;
import org.apache.flink.annotation.VisibleForTesting;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.state.ReducingState;
import org.apache.flink.api.common.state.ReducingStateDescriptor;
import org.apache.flink.api.common.typeutils.base.LongSerializer;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.Window;

@PublicEvolving
public class MyContinuousEventTimeTrigger<W extends Window> extends Trigger<Object, W> {
    private static final long serialVersionUID = 1L;
    private final long interval;
    private final ReducingStateDescriptor<Long> stateDesc;

    private MyContinuousEventTimeTrigger(long interval) {
        this.stateDesc = new ReducingStateDescriptor("fire-time", new Min(), LongSerializer.INSTANCE);
        this.interval = interval;
    }

    @Override
    public TriggerResult onElement(Object element, long timestamp, W window, TriggerContext ctx) throws Exception {
        if (window.maxTimestamp() <= ctx.getCurrentWatermark()) {
            return TriggerResult.FIRE;
        } else {
            ctx.registerEventTimeTimer(window.maxTimestamp());
            ReducingState<Long> fireTimestamp = (ReducingState)ctx.getPartitionedState(this.stateDesc);
            if (fireTimestamp.get() == null) {
                long start = timestamp - timestamp % this.interval;
                long nextFireTimestamp = start + this.interval;
                ctx.registerEventTimeTimer(nextFireTimestamp);
                fireTimestamp.add(nextFireTimestamp);
            }

            return TriggerResult.CONTINUE;
        }
    }

    @Override
    public TriggerResult onEventTime(long time, W window, TriggerContext ctx) throws Exception {
        if (time == window.maxTimestamp()) {
            return TriggerResult.FIRE;
        } else {
            ReducingState<Long> fireTimestampState = (ReducingState)ctx.getPartitionedState(this.stateDesc);
            Long fireTimestamp = (Long)fireTimestampState.get();
            if (fireTimestamp != null && fireTimestamp == time) {
                fireTimestampState.clear();
                fireTimestampState.add(time + this.interval);
                ctx.registerEventTimeTimer(time + this.interval);
                return TriggerResult.FIRE;
            } else {
                return TriggerResult.CONTINUE;
            }
        }
    }

    @Override
    public TriggerResult onProcessingTime(long time, W window, TriggerContext ctx) throws Exception {
        return TriggerResult.CONTINUE;
    }

    @Override
    public void clear(W window, TriggerContext ctx) throws Exception {
        ReducingState<Long> fireTimestamp = (ReducingState)ctx.getPartitionedState(this.stateDesc);
        Long timestamp = (Long)fireTimestamp.get();
        if (timestamp != null) {
            ctx.deleteEventTimeTimer(timestamp);
            fireTimestamp.clear();
        }

    }

    @Override
    public boolean canMerge() {
        return true;
    }

    @Override
    public void onMerge(W window, OnMergeContext ctx) throws Exception {
        ctx.mergePartitionedState(this.stateDesc);
        Long nextFireTimestamp = (Long)((ReducingState)ctx.getPartitionedState(this.stateDesc)).get();
        if (nextFireTimestamp != null) {
            ctx.registerEventTimeTimer(nextFireTimestamp);
        }

    }

    @Override
    public String toString() {
        return "MyContinuousEventTimeTrigger(" + this.interval + ")";
    }

    @VisibleForTesting
    public long getInterval() {
        return this.interval;
    }

    public static <W extends Window> MyContinuousEventTimeTrigger<W> of(Time interval) {
        return new MyContinuousEventTimeTrigger(interval.toMilliseconds());
    }

    private static class Min implements ReduceFunction<Long> {
        private static final long serialVersionUID = 1L;

        private Min() {
        }

        @Override
        public Long reduce(Long value1, Long value2) throws Exception {
            return Math.min(value1, value2);
        }
    }
}
