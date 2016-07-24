package com.sam_chordas.android.stockhawk.widget;

/**
 * Created by cse on 20-07-2016.
 */
public class DatabaseInfo {

    String symbol;
    String change;
    String is_up;
    String bid_price;

    public void setPercent_change(String percent_change) {
        this.percent_change = percent_change;
    }

    public String getPercent_change() {
    
        return percent_change;
    }

    String percent_change;

    public String getSymbol() {
        return symbol;
    }

    public String getChange() {
        return change;
    }

    public String getIs_up() {
        return is_up;
    }

    public String getBid_price() {
        return bid_price;
    }

    public void setSymbol(String symbol) {

        this.symbol = symbol;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public void setIs_up(String is_up) {
        this.is_up = is_up;
    }

    public void setBid_price(String bid_price) {
        this.bid_price = bid_price;
    }
}
