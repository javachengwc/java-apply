package com.rule.data.exception;

public class ArgColumnNotFoundException extends RengineException {

    public ArgColumnNotFoundException(String function, String colCal) {
        super(null, function + "参数列未找到, " + colCal);
    }
}
