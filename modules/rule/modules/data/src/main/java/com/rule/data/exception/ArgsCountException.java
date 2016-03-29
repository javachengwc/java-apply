package com.rule.data.exception;

public class ArgsCountException extends RengineException {

    public ArgsCountException(String function) {
        super(null, function + "参数个数不匹配");
    }
}
