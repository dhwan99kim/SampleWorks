package com.sophism.sampleapp.data;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by D.H.KIM on 2016. 1. 14.
 */
@Root(name = "item", strict = false)
public class PostItem {
    @Element(name = "postcd")
    private String postcd;
    @Element(name = "address")
    private String address;

    public String getPostcd (){
        return postcd;
    }

    public void setPostcd (String postcd){
        this.postcd = postcd;
    }

    public String getAddress (){
        return address;
    }

    public void setAddress (String address){
        this.address = address;
    }

    @Override
    public String toString(){
        return "[postcd = "+postcd+", address = "+address+"]";
    }
}
