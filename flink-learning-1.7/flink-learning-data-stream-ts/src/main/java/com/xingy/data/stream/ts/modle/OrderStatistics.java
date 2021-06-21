package com.xingy.data.stream.ts.modle;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author xinguiyuan
 * @className com.zhisheng.example.modle.OrderStatistics
 * @date 2020/07/10 11:29
 * @description 订单统计表
 */
public class OrderStatistics {
    private String id;
    private String name;
    private int count;
    private double sumPrice;
    private String statisTime;
    //时间维度
    private String dimension;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(double sumPrice) {
        this.sumPrice = sumPrice;
    }

    public String getStatisTime() {
        return statisTime;
    }

    public void setStatisTime(String statisTime) {
        this.statisTime = statisTime;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        OrderStatistics that = (OrderStatistics) o;

        return new EqualsBuilder()
                .append(count, that.count)
                .append(sumPrice, that.sumPrice)
                .append(id, that.id)
                .append(name, that.name)
                .append(statisTime, that.statisTime)
                .append(dimension, that.dimension)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(count)
                .append(sumPrice)
                .append(statisTime)
                .append(dimension)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "OrderStatistics{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", count=" + count +
                ", sumPrice=" + sumPrice +
                ", statisTime='" + statisTime + '\'' +
                ", dimension='" + dimension + '\'' +
                '}';
    }
}
