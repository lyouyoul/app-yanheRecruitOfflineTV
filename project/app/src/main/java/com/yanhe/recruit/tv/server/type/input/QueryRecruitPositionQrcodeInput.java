package com.yanhe.recruit.tv.server.type.input;

import com.yanhe.recruit.tv.server.type.BaseInput;

/***
 * 根据id查找统一使用此类接收参数，可增加分页参数
 */
public class QueryRecruitPositionQrcodeInput extends BaseInput {
    private String recruitId;
    private String roomCode;
    private String positionCode;

    public QueryRecruitPositionQrcodeInput() {
    }

    public QueryRecruitPositionQrcodeInput(String recruitId, String roomCode, String positionCode) {
        this.recruitId = recruitId;
        this.roomCode = roomCode;
        this.positionCode = positionCode;
    }

    public String getRecruitId() {
        return recruitId;
    }

    public void setRecruitId(String recruitId) {
        this.recruitId = recruitId;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }
}
