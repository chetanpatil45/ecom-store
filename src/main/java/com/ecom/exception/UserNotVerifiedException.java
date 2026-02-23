package com.ecom.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserNotVerifiedException extends Exception{
    private boolean newEmailSent;
}
