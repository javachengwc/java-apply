package com.rule.data.engine.excel;


import com.rule.data.model.vo.D2Data;

import java.util.Iterator;

public final class ExcelRange {

    private int x1, y1, x2, y2;

    private D2Data data;

    public ExcelRange(int x1, int y1, int x2, int y2, D2Data data) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.data = data;
    }

    public D2Data getData() {
        return data;
    }

    public Object getValue(int col, int row) {
        return data.getValue(col, row);
    }


    public int getX1() {
        return x1;
    }

    public int getX2() {
        return x2;
    }

    public int getY1() {
        return y1;
    }

    public int getY2() {
        return y2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExcelRange)) return false;

        ExcelRange that = (ExcelRange) o;

        if (x1 != that.x1) return false;
        if (x2 != that.x2) return false;
        if (y1 != that.y1) return false;
        if (y2 != that.y2) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x1;
        result = 31 * result + y1;
        result = 31 * result + x2;
        result = 31 * result + y2;
        return result;
    }

    public Iterator<Object> getIterator() {
        return new Iterator<Object>() {
            int point_x = x1 - 1;
            int point_y = y1;

            @Override
            public boolean hasNext() {
                if (point_x >= x1 && point_x <= x2 && point_y >= y1 && point_y <= y2) {
                    if (point_x == x2 && point_y == y2) {
                        return false;
                    }

                    return true;
                }

                if (point_x == x1 - 1 && point_y == y1) {
                    return true;
                }
                return false;
            }

            @Override
            public Object next() {
                point_x++;
                if (point_x > x2) {
                    point_x = x1;
                    point_y++;

                    if (point_y > y2) {
                        return null;
                    }
                }


                return data.getValue(point_x, point_y);
            }

            @Override
            public void remove() {
                //
            }
        };
    }
}
