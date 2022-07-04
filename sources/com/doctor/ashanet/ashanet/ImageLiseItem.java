package com.doctor.ashanet.ashanet;

public class ImageLiseItem {
    String name;
    String url;

    public ImageLiseItem(String url2, String name2) {
        this.url = url2;
        this.name = name2;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url2) {
        this.url = url2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }
}
