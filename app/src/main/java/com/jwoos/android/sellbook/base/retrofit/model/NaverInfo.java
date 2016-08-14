package com.jwoos.android.sellbook.base.retrofit.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Jwoo on 2016-08-15.
 */
@Root(name = "item")
public class NaverInfo {
    @Element(name = "publisher")
    String publisher;
    @Element(name = "price")
    String price;
    @Element(name = "author")
    String author;
    @Element(name = "title")
    String title;

}
