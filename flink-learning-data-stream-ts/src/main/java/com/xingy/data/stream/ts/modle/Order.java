package com.xingy.data.stream.ts.modle;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author xinguiyuan
 * @className com.zhisheng.example.modle.Order
 * @date 2020/07/10 11:25
 * @description 测试订单表
 */
public class Order {
    private String Id;
    private String name;
    private double price;
    private String createTime;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    @Override
    public String toString() {
        return "Order{" +
                "Id='" + Id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", createTime='" + createTime + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        return new EqualsBuilder()
                .append(price, order.price)
                .append(Id, order.Id)
                .append(name, order.name)
                .append(createTime, order.createTime)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(Id)
                .append(name)
                .append(price)
                .append(createTime)
                .toHashCode();
    }
}
