package com.ecom.api.dto;

public class DataChange<T> {
    private T data;
    private ChangeType changeType;

    public DataChange(T changeType, T address) {
    }

    public enum ChangeType{
        INSERT,
        UPDATE,
        DELETE
    }

    public DataChange(ChangeType changeType, T data) {
        this.changeType = changeType;
        this.data = data;
    }

    public DataChange() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ChangeType changeType) {
        this.changeType = changeType;
    }
}

