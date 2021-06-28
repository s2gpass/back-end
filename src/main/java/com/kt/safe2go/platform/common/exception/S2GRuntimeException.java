package com.kt.safe2go.platform.common.exception;

public class S2GRuntimeException extends RuntimeException
{
    private int code = 9999;
    private String receiveMessage;

    public S2GRuntimeException()
    {
    }

    public S2GRuntimeException(int errorCode, String message)
    {
        super(message);
        this.code = errorCode;
    }

    public S2GRuntimeException(String message)
    {
        super(message);
    }

    public S2GRuntimeException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public S2GRuntimeException(Throwable cause)
    {
        super(cause);
    }

    public S2GRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    //
    public int getErrorCode()
    {
        return code;
    }

}
