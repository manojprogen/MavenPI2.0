package com.progen.report.util.sort;

import com.google.common.base.Predicate;

/**
 *
 * @author arun
 */
/**
 * Each row's data. Data pertaining to sort columns are only brought here
 * rowSequence is the ordering of rows in view by data1 is the first column of
 * data to sort data2 is the second column of data to sort and so on
 *
 * @author arun
 */
public class RowData {

    Integer rowSequence;
    Object data1;
    Object data2;
    Object data3;
    Object data4;
    Object data5;
    Object data6;
    Object data7;
    Object data8;
    Object data9;
    Object data10;
    Object data11;
    Object data12;
    Object data13;
    Object data14;
    Object data15;
    Object data16;
    Object data17;
    Object data18;
    Object data19;
    Object data20;

    public Integer getRowSequence() {
        return rowSequence;
    }

    public Object getData1() {
        return data1;
    }

    public Object getData2() {
        return data2;
    }

    public Object getData3() {
        return data3;
    }

    public Object getData4() {
        return data4;
    }

    public Object getData5() {
        return data5;
    }

    public Object getData6() {
        return data6;
    }

    public Object getData7() {
        return data7;
    }

    public Object getData8() {
        return data8;
    }

    public Object getData9() {
        return data9;
    }

    public Object getData10() {
        return data10;
    }

    public Object getData11() {
        return data11;
    }

    public Object getData12() {
        return data12;
    }

    public Object getData13() {
        return data13;
    }

    public Object getData14() {
        return data14;
    }

    public Object getData15() {
        return data15;
    }

    public Object getData16() {
        return data16;
    }

    public Object getData17() {
        return data17;
    }

    public Object getData18() {
        return data18;
    }

    public Object getData19() {
        return data19;
    }

    public Object getData20() {
        return data20;
    }

    public void setRowSequence(Integer rowSequence) {
        this.rowSequence = rowSequence;
    }

    public void setData1(Object data1) {
        this.data1 = data1;
    }

    public void setData2(Object data2) {
        this.data2 = data2;
    }

    public void setData3(Object data3) {
        this.data3 = data3;
    }

    public void setData4(Object data4) {
        this.data4 = data4;
    }

    public void setData5(Object data5) {
        this.data5 = data5;
    }

    public void setData6(Object data6) {
        this.data6 = data6;
    }

    public void setData7(Object data7) {
        this.data7 = data7;
    }

    public void setData8(Object data8) {
        this.data8 = data8;
    }

    public void setData9(Object data9) {
        this.data9 = data9;
    }

    public void setData10(Object data10) {
        this.data10 = data10;
    }

    public void setData11(Object data11) {
        this.data11 = data11;
    }

    public void setData12(Object data12) {
        this.data12 = data12;
    }

    public void setData13(Object data13) {
        this.data13 = data13;
    }

    public void setData14(Object data14) {
        this.data14 = data14;
    }

    public void setData15(Object data15) {
        this.data15 = data15;
    }

    public void setData16(Object data16) {
        this.data16 = data16;
    }

    public void setData17(Object data17) {
        this.data17 = data17;
    }

    public void setData18(Object data18) {
        this.data18 = data18;
    }

    public void setData19(Object data19) {
        this.data19 = data19;
    }

    public void setData20(Object data20) {
        this.data20 = data20;
    }

    @Override
    public String toString() {
        return (this.getRowSequence() + " " + this.getData1() + " " + this.getData2());
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof RowData) {
            if (this.data1 != null) {
                if (((RowData) o).data1 != null) {
                    if (!this.data1.equals(((RowData) o).data1)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }

            if (this.data2 != null) {
                if (((RowData) o).data2 != null) {
                    if (!this.data2.equals(((RowData) o).data2)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }

            if (this.data3 != null) {
                if (((RowData) o).data3 != null) {
                    if (!this.data3.equals(((RowData) o).data3)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }

            if (this.data4 != null) {
                if (((RowData) o).data4 != null) {
                    if (!this.data4.equals(((RowData) o).data4)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }

            if (this.data5 != null) {
                if (((RowData) o).data5 != null) {
                    if (!this.data5.equals(((RowData) o).data5)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }

            if (this.data6 != null) {
                if (((RowData) o).data6 != null) {
                    if (!this.data6.equals(((RowData) o).data6)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }

            if (this.data7 != null) {
                if (((RowData) o).data7 != null) {
                    if (!this.data7.equals(((RowData) o).data7)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }

            if (this.data8 != null) {
                if (((RowData) o).data8 != null) {
                    if (!this.data8.equals(((RowData) o).data8)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }

            if (this.data9 != null) {
                if (((RowData) o).data9 != null) {
                    if (!this.data9.equals(((RowData) o).data9)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }

            if (this.data10 != null) {
                if (((RowData) o).data10 != null) {
                    if (!this.data10.equals(((RowData) o).data10)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }

            if (this.data11 != null) {
                if (((RowData) o).data11 != null) {
                    if (!this.data11.equals(((RowData) o).data11)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }

            if (this.data12 != null) {
                if (((RowData) o).data12 != null) {
                    if (!this.data12.equals(((RowData) o).data12)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }

            if (this.data13 != null) {
                if (((RowData) o).data13 != null) {
                    if (!this.data13.equals(((RowData) o).data13)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }

            if (this.data14 != null) {
                if (((RowData) o).data14 != null) {
                    if (!this.data14.equals(((RowData) o).data14)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            if (this.data15 != null) {
                if (((RowData) o).data15 != null) {
                    if (!this.data15.equals(((RowData) o).data15)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }

            if (this.data16 != null) {
                if (((RowData) o).data16 != null) {
                    if (!this.data16.equals(((RowData) o).data16)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            if (this.data17 != null) {
                if (((RowData) o).data17 != null) {
                    if (!this.data17.equals(((RowData) o).data17)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }

            if (this.data18 != null) {
                if (((RowData) o).data18 != null) {
                    if (!this.data18.equals(((RowData) o).data18)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            if (this.data19 != null) {
                if (((RowData) o).data19 != null) {
                    if (!this.data19.equals(((RowData) o).data19)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }

            if (this.data20 != null) {
                if (((RowData) o).data20 != null) {
                    if (!this.data20.equals(((RowData) o).data20)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }

        return true;
    }

    public static Predicate<RowData> getRowDataPredicate(final RowData rowData) {
        Predicate<RowData> predicate = new Predicate<RowData>() {

            public boolean apply(RowData input) {
                if (input.equals(rowData)) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.data1 != null ? this.data1.hashCode() : 0);
        hash = 67 * hash + (this.data2 != null ? this.data2.hashCode() : 0);
        hash = 67 * hash + (this.data3 != null ? this.data3.hashCode() : 0);
        hash = 67 * hash + (this.data4 != null ? this.data4.hashCode() : 0);
        hash = 67 * hash + (this.data5 != null ? this.data5.hashCode() : 0);
        hash = 67 * hash + (this.data6 != null ? this.data6.hashCode() : 0);
        hash = 67 * hash + (this.data7 != null ? this.data7.hashCode() : 0);
        hash = 67 * hash + (this.data8 != null ? this.data8.hashCode() : 0);
        hash = 67 * hash + (this.data9 != null ? this.data9.hashCode() : 0);
        hash = 67 * hash + (this.data10 != null ? this.data10.hashCode() : 0);
        hash = 67 * hash + (this.data11 != null ? this.data11.hashCode() : 0);
        hash = 67 * hash + (this.data12 != null ? this.data12.hashCode() : 0);
        hash = 67 * hash + (this.data13 != null ? this.data13.hashCode() : 0);
        hash = 67 * hash + (this.data14 != null ? this.data14.hashCode() : 0);
        hash = 67 * hash + (this.data15 != null ? this.data15.hashCode() : 0);
        hash = 67 * hash + (this.data16 != null ? this.data16.hashCode() : 0);
        hash = 67 * hash + (this.data17 != null ? this.data17.hashCode() : 0);
        hash = 67 * hash + (this.data18 != null ? this.data18.hashCode() : 0);
        hash = 67 * hash + (this.data19 != null ? this.data19.hashCode() : 0);
        hash = 67 * hash + (this.data20 != null ? this.data20.hashCode() : 0);

        return hash;
    }
}
