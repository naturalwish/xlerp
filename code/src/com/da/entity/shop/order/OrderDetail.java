package com.da.entity.shop.order;

public class OrderDetail {
    private String order_detail_id;
    private String goods_id;
    private String goods_pic;
    private String goods_name;
    private double goods_price;
    private int goods_count;
    private double goods_total;
    private String attribute_detail_id;
    private String attribute_detail_name;
    private double pay_total;
    private String order_id;
    private int sort;
    private int status;

    public String getOrder_detail_id() {
        return order_detail_id;
    }

    public void setOrder_detail_id(String order_detail_id) {
        this.order_detail_id = order_detail_id;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_pic() {
        return goods_pic;
    }

    public void setGoods_pic(String goods_pic) {
        this.goods_pic = goods_pic;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public double getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(double goods_price) {
        this.goods_price = goods_price;
    }

    public int getGoods_count() {
        return goods_count;
    }

    public void setGoods_count(int goods_count) {
        this.goods_count = goods_count;
    }

    public double getGoods_total() {
        return goods_total;
    }

    public void setGoods_total(double goods_total) {
        this.goods_total = goods_total;
    }

    public String getAttribute_detail_id() {
        return attribute_detail_id;
    }

    public void setAttribute_detail_id(String attribute_detail_id) {
        this.attribute_detail_id = attribute_detail_id;
    }

    public String getAttribute_detail_name() {
        return attribute_detail_name;
    }

    public void setAttribute_detail_name(String attribute_detail_name) {
        this.attribute_detail_name = attribute_detail_name;
    }

    public double getPay_total() {
        return pay_total;
    }

    public void setPay_total(double pay_total) {
        this.pay_total = pay_total;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
