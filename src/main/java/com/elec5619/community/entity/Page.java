package com.elec5619.community.entity;

/**
 * @author Zhengzuo Huo
 */
public class Page {

    // Current page number.
    private int current = 1;

    private int limit = 10;

    private int rows;

    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    /**
     * Get starting data row.
     */
    public int getOffset() {
        return (current - 1) * limit;
    }


    /**
     * Get total page number.
     */
    public int getTotal() {
        if (rows % limit == 0) {
            return rows / limit;
        } else {
            return rows / limit + 1;
        }
    }


    public int getFrom() {
        int from = current - 2;
        return Math.max(from, 1);
    }


    public int getTo() {
        int to = current + 2;
        int total = getTotal();
        return Math.min(to, total);
    }
}
