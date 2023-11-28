package com.luckyi.statemachine.domain;

/**
 * 请假流程事件
 * @author luckyi
 */
public enum Event {

    /**
     * 员工提交
     */
    EMPLOYEE_SUBMIT,
    /**
     * 直属领导审批
     */
    DIRECT_LEADER_AUDIT,
    /**
     * HR审批
     */
    HR_AUDIT,
    /**
     * 完成
     */
    COMPLETE
}
