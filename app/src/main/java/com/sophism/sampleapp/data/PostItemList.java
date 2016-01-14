package com.sophism.sampleapp.data;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by D.H.KIM on 2016. 1. 14.
 */
@Root(name = "post", strict = false)
public class PostItemList {
    @ElementList(name = "itemlist")
    private List<PostItem> itemlist;

    public List getItemlist ()
    {
        return itemlist;
    }

    public void setItemlist (List<PostItem> itemlist){
        this.itemlist = itemlist;
    }

    @Override
    public String toString(){
        return "ClassPojo [itemlist = "+itemlist+"]";
    }
}
