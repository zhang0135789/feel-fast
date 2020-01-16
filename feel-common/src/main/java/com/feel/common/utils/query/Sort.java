package com.feel.common.utils.query;

public class Sort extends Column {
    private String order = "asc";

    public String getOrder() {
        return this.order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Sort() {
    }

    public Sort(String name) {
        this.setName(name);
    }

    public void asc() {
        this.order = "asc";
    }

    public void desc() {
        this.order = "desc";
    }

    public int hashCode() {
        return String.valueOf(this.getName()).concat(this.order).hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else {
            return this.hashCode() == obj.hashCode();
        }
    }
}
