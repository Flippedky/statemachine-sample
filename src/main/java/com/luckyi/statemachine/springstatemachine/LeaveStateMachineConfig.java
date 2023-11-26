package com.luckyi.statemachine.springstatemachine;

import com.luckyi.statemachine.colaStateMachine.LeaveContext;
import com.luckyi.statemachine.colaStateMachine.LeaveStatusEnum;
import com.luckyi.statemachine.colaStateMachine.Event;
import com.luckyi.statemachine.colaStateMachine.StateMachineRegist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageHeaders;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

import static com.luckyi.statemachine.colaStateMachine.Event.*;
import static com.luckyi.statemachine.colaStateMachine.LeaveStatusEnum.*;

/**
 * 请假流程状态机配置
 *
 * @author luckyi
 * @version 1.0
 * @date 2023-11-17 11:56
 */
@Component
public class LeaveStateMachineConfig extends StateMachineConfigurerAdapter<LeaveStatusEnum, Event> {

    private static final Logger log = LoggerFactory.getLogger(StateMachineRegist.class);

    /**
     * 状态机流程上下文信息标识
     */
    private final String STATE_MACHINE_CONTEXT = "leaveStateMachine";

    /**
     * 配置流程状态
     * @param states the {@link StateMachineStateConfigurer}
     * @throws Exception
     */
    @Override
    public void configure(StateMachineStateConfigurer<LeaveStatusEnum, Event> states) throws Exception {
        states.withStates().
                // 指定流程初始化状态
                initial(LEAVE_SUBMIT)
                //
                .states(EnumSet.allOf(LeaveStatusEnum.class));
    }

    /**
     * 配置流程转换条件
     * @param transitions the {@link StateMachineTransitionConfigurer}
     * @throws Exception
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<LeaveStatusEnum, Event> transitions) throws Exception {
        transitions
                // 内部转换 流程状态不变 只执行动作
                .withInternal().state(LEAVE_SUBMIT).event(EMPLOYEE_SUBMIT).action(doAction())
                .and()
                // 外部转换 指定事件触发流程状态变化 （触发事件->校验条件->执行动作->状态变化）
                .withExternal().source(LEAVE_SUBMIT).target(LEADER_AUDIT_PASS).event(DIRECT_LEADER_AUDIT).guard(checkIfPass()).action(doAction())
                .and()
                .withExternal().source(LEAVE_SUBMIT).target(LEADER_AUDIT_REFUSE).event(DIRECT_LEADER_AUDIT).guard(checkIfNotPass()).action(doAction())
                .and()
                .withExternal().source(LEADER_AUDIT_PASS).target(HR_PASS).event(HR_AUDIT).guard(checkIfPass()).action(doAction())
                .and()
                .withExternal().source(LEADER_AUDIT_PASS).target(HR_REFUSE).event(HR_AUDIT).guard(checkIfNotPass()).action(doAction())
                .and()
                .withExternal().source(HR_PASS).target(END).event(COMPLETE).action(doAction());

    }

    /**
     * 执行动作
     *
     * @return 状态转换执行动作
     */
    private Action<LeaveStatusEnum, Event> doAction() {
        return context -> {
            // 触发事件
            Event event = context.getEvent();
            // 获取流程上下文信息
            MessageHeaders headers = context.getMessageHeaders();
            LeaveContext ctx = headers.get(STATE_MACHINE_CONTEXT, LeaveContext.class);
            // 流程源状态
            State<LeaveStatusEnum, Event> source = context.getSource();
            // 流程目标状态
            State<LeaveStatusEnum, Event> target = context.getTarget();
            log.info("from:" + source.getId() + " to:" + target + " on:" + event + " condition:" + ctx);
        };
    }

    /**
     * 校验审批是否通过
     * @return Guard 门卫，条件判断返回true时再执行状态转移，可以做业务前置校验。
     */
    private Guard<LeaveStatusEnum, Event> checkIfPass() {
        return context -> {
            MessageHeaders headers = context.getMessageHeaders();
            LeaveContext ctx = headers.get(STATE_MACHINE_CONTEXT, LeaveContext.class);
            return ctx != null && ctx.getIdea().equals(0);
        };
    }

    /**
     * 校验审批是否拒绝
     * @return Guard 门卫，条件判断返回true时再执行状态转移，可以做业务前置校验。
     */
    private Guard<LeaveStatusEnum, Event> checkIfNotPass() {
        return context -> {
            MessageHeaders headers = context.getMessageHeaders();
            LeaveContext ctx = headers.get(STATE_MACHINE_CONTEXT, LeaveContext.class);
            return ctx != null && ctx.getIdea().equals(1);
        };
    }
}
