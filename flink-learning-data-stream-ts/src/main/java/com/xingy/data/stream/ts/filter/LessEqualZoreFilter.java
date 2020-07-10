package com.xingy.data.stream.ts.filter;

import org.apache.flink.api.common.functions.FilterFunction;

/**
 * @author xinguiyuan
 * @className com.xingy.example.filter.GreaterThenZoreFilter
 * @date 2020/07/10 15:45
 * @description 过滤小于等于0
 */
public class LessEqualZoreFilter implements FilterFunction<Integer> {

    @Override
    public boolean filter(Integer i) throws Exception {
        return i > 0;
    }
}
