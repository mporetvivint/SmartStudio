package com.sunrun.leaguemediaassistant.Comm;

public interface OnEventListener<T> {
    public void onSuccess(T object);
    public void onFail();
}
