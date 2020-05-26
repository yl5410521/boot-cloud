package com.alien.base.common.exception;

import lombok.Data;

/**
 * @author Exrickx
 */
@Data
public class AlienException extends RuntimeException {

    private String msg;

    public AlienException(String msg){
        super(msg);
        this.msg = msg;
    }
}
