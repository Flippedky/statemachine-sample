package com.luckyi.statemachine.domain;

/**
 * 请假状态枚举
 *
 * @author luckyi
 */
public enum LeaveStatusEnum {

    /**
     * 已申请
     */
    LEAVE_SUBMIT(1,"已申请"),
    /**
     * 直属领导审批通过
     */
    LEADER_AUDIT_PASS(2,"直属领导审批通过"),
    /**
     * 直属领导审批失败
     */
    LEADER_AUDIT_REFUSE(3,"直属领导审批失败"),
    /**
     * 审批通过
     */
    HR_PASS(4,"审批通过"),
    /**
     * HR拒绝
     */
    HR_REFUSE(5,"HR拒绝"),
    /**
     * 处理完成
     */
    END(6,"处理完成");

    /**
     * 流程标识
     */
    private Integer code;
    /**
     * 流程描述
     */
    private String desc;

    LeaveStatusEnum(Integer code, String desc){
        this.code=code;
        this.desc=desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
