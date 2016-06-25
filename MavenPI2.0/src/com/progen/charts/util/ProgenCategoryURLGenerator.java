package com.progen.charts.util;

import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.data.category.CategoryDataset;

public class ProgenCategoryURLGenerator extends StandardCategoryURLGenerator {

    private String prefix = "javascript:submitform(";
    private String suffix = ")";
    private String url = "";
    private String seriesParameterName = "series";
    private String categoryParameterName = "category";

    public ProgenCategoryURLGenerator() {
        super();
    }

    public ProgenCategoryURLGenerator(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("Null 'prefix' argument.");
        }
        this.prefix = prefix;
    }

    public ProgenCategoryURLGenerator(String prefix, String suffix) {
        if (prefix == null) {
            throw new IllegalArgumentException("Null 'prefix' argument.");
        }
        if (suffix == null) {
            throw new IllegalArgumentException("Null 'prefix' argument.");
        }
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public ProgenCategoryURLGenerator(String prefix, String suffix, String url) {
        if (prefix == null) {
            throw new IllegalArgumentException("Null 'prefix' argument.");
        }
        if (suffix == null) {
            throw new IllegalArgumentException("Null 'prefix' argument.");
        }
        this.prefix = prefix;
        this.suffix = suffix;
        this.url = url;

    }

    public String generateURL(CategoryDataset dataset, int series,
            int category) {
        String url = this.prefix;
        Comparable seriesKey = dataset.getRowKey(series);
        Comparable categoryKey = dataset.getColumnKey(category);
        boolean firstParameter = url.indexOf("?") == -1;
        //        try {
        if (this.url == null || this.url.equals("")) {
            url += "'" + categoryKey.toString() + "'";
        } else {
            url += "'" + this.url + categoryKey.toString() + "'";
        }

        url += this.suffix;
        //+ URLEncoder.encode(categoryKey.toString(), "UTF-8");
        // not supported in JDK 1.2.2
        //        }
        //        catch (UnsupportedEncodingException uee) {
        //            url += "&" + this.categoryParameterName + "="
        // + categoryKey.toString();
        //        }
        return url;
    }
}
