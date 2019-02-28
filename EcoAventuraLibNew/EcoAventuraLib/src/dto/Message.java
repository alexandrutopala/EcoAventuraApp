/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;

/**
 *
 * @author Alexandru
 */
public enum Message implements Serializable{
    ASK_CONNECTION,
    CONFIRM_CONNECTION,
    NOTHING_TO_SEND, 
    NOTHING_TO_RECIEVE,
    PACKAGE_TO_SEND, 
    PACKAGE_TO_RECEIVE,
    IS_SOMETHING_TO_SEND,
    IS_SOMETHING_TO_RECIEVE,
    LOGIN_USER,
    CONNECT_USER,
    EXISTENT_USER,
    UNCREATED_USER,
    OK_USER,
    WRONG_PASSWORD,
    STOP_CONNECTION,
    KEEP_CONNECTION_ON,
    DISCONNECT_USER,
    CHECK_SERIE,
    REQUEST_SERIE,
    CHECK_CONNECTION,
    SENDING_SERIE,
    TEST_CONNECTION,
    ACTIVITIES_RECEIVED
}
