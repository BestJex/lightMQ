package com.stalary.lightmq.exception;

/**
 * @author Stalary
 * @description
 * @date 2018/4/28
 */
public enum ExceptionEnum {

    NO_TOPIC(1001, "不存在该topic"),

    NO_GROUP(1002, "不存该group"),

    REPEAT_TOPIC(1003, "topic重复"),

    REPEAT_GROUP(1004, "group重复");

    Integer code;

    String msg;

    ExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
