/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.charts;

import org.jfree.chart.urls.StandardXYURLGenerator;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author Administrator
 */
public class ProgenXYURLGenerator extends StandardXYURLGenerator {

    private String prefix = "javascript:submitform(";
    private String suffix = ")";
    private String url = "";
    /**
     * Series parameter name to go in each URL
     */
    private String seriesParameterName;
    /**
     * Item parameter name to go in each URL
     */
    private String itemParameterName;

    /**
     * Creates a new default generator. This constructor is equivalent to
     * calling
     * <code>StandardXYURLGenerator("index.html", "series", "item");
     * </code>.
     */
    public ProgenXYURLGenerator() {
        this(DEFAULT_PREFIX, DEFAULT_SERIES_PARAMETER, DEFAULT_ITEM_PARAMETER);
    }

    /**
     * Creates a new generator with the specified prefix. This constructor is
     * equivalent to calling
     * <code>ProgenXYURLGenerator(prefix, "series", "item");</code>.
     *
     * @param prefix the prefix to the URL (
     * <code>null</code> not permitted).
     */
    public ProgenXYURLGenerator(String prefix) {
        this(prefix, DEFAULT_SERIES_PARAMETER, DEFAULT_ITEM_PARAMETER);
    }

    /**
     * Constructor that overrides all the defaults
     *
     * @param prefix the prefix to the URL (
     * <code>null</code> not permitted).
     * @param seriesParameterName the name of the series parameter to go in each
     * URL (
     * <code>null</code> not permitted).
     * @param itemParameterName the name of the item parameter to go in each URL (
     * <code>null</code> not permitted).
     */
    public ProgenXYURLGenerator(String prefix,
            String seriesParameterName,
            String itemParameterName) {
        if (prefix == null) {
            throw new IllegalArgumentException("Null 'prefix' argument.");
        }
        if (seriesParameterName == null) {
            throw new IllegalArgumentException(
                    "Null 'seriesParameterName' argument.");
        }
        if (itemParameterName == null) {
            throw new IllegalArgumentException(
                    "Null 'itemParameterName' argument.");
        }
        this.prefix = prefix;
        this.seriesParameterName = seriesParameterName;
        this.itemParameterName = itemParameterName;
    }

    /**
     * Generates a URL for a particular item within a series.
     *
     * @param dataset the dataset.
     * @param series the series number (zero-based index).
     * @param item the item number (zero-based index).
     *
     * @return The generated URL.
     */
    public String generateURL(XYDataset dataset, int series, int item) {
        String url = this.prefix;
        boolean firstParameter = url.indexOf("?") == -1;
        url += firstParameter ? "?" : "&amp;";
        url += this.seriesParameterName + "=" + series + "&amp;" + this.itemParameterName + "=" + item;

        ////////////////////////////////////////////////////.println.println("url is "+url);
        return url;
    }
}
