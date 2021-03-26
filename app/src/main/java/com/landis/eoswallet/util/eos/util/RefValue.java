package com.landis.eoswallet.util.eos.util;

public class RefValue<T> {
    public T data;

    public RefValue(){
        data = null;
    }

    public RefValue(T initialVal ){
        data = initialVal;
    }
}

