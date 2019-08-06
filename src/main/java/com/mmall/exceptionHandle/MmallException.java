package com.mmall.exceptionHandle;

import com.mmall.common.ResponseCode;

public class MmallException extends RuntimeException {

    private int code;

    public MmallException(int code, String msg){
        super(msg);
        this.code=code;
    }

    public MmallException(ResponseCode resultEnum){
        super(resultEnum.getDesc());
        this.code=resultEnum.getCode();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
