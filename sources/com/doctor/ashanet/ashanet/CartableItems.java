package com.doctor.ashanet.ashanet;

public class CartableItems {
    String descriptions;
    Integer id;
    Integer msg;
    Integer read;
    String titles;

    public CartableItems(String titles2, String descriptions2, Integer msg2, Integer id2, Integer read2) {
        this.titles = titles2;
        this.descriptions = descriptions2;
        this.msg = msg2;
        this.id = id2;
        this.read = read2;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id2) {
        this.id = id2;
    }

    public Integer getMsg() {
        return this.msg;
    }

    public void setMsg(Integer msg2) {
        this.msg = msg2;
    }

    public String getTitles() {
        return this.titles;
    }

    public void setTitles(String titles2) {
        this.titles = titles2;
    }

    public String getDescriptions() {
        return this.descriptions;
    }

    public void setDescriptions(String descriptions2) {
        this.descriptions = descriptions2;
    }

    public Integer getRead() {
        return this.read;
    }

    public void setRead(Integer read2) {
        this.read = read2;
    }
}
