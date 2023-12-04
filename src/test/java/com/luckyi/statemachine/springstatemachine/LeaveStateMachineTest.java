package com.luckyi.statemachine.springstatemachine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.luckyi.statemachine.domain.Event;
import com.luckyi.statemachine.domain.LeaveContext;
import com.luckyi.statemachine.domain.LeaveStatusEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.luckyi.statemachine.domain.Event.DIRECT_LEADER_AUDIT;
import static com.luckyi.statemachine.domain.Event.EMPLOYEE_SUBMIT;
import static com.luckyi.statemachine.domain.LeaveStatusEnum.LEADER_AUDIT_PASS;
import static com.luckyi.statemachine.domain.LeaveStatusEnum.LEAVE_SUBMIT;
import static com.luckyi.statemachine.domain.StateMachineConstant.STATE_MACHINE_CONTEXT;

@SpringBootTest
public class LeaveStateMachineTest {

    @Autowired
    private StateMachine<LeaveStatusEnum, Event> leaveStateMachine;
    @Autowired
    private StateMachinePersister<LeaveStatusEnum, Event, String> leaveStateMachinePersister;

    @Test
    @DisplayName("员工提交")
    public void employeeSubmit() {
        // 启动状态机
        Mono<Void> started = leaveStateMachine.startReactively();
        // 状态机启动成功后 发送事件
        started.publishOn(Schedulers.boundedElastic()).doOnSuccess(s -> {
            // 构建事件消息
            Message<Event> message = MessageBuilder.withPayload(EMPLOYEE_SUBMIT).build();
            // 向状态机发送事件 获得一个结果流
            Flux<StateMachineEventResult<LeaveStatusEnum, Event>> resultFlux = leaveStateMachine.sendEvent(Mono.just(message));
            // 订阅结果流 获取状态机当前状态
            resultFlux.doOnComplete(() -> {
                try {
                    leaveStateMachinePersister.persist(leaveStateMachine, "leaveStateMachine");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                System.out.println("message has sent");
            }).subscribe(result -> {
                State<LeaveStatusEnum, Event> state = result.getRegion().getState();
                // 获取状态标识符
                LeaveStatusEnum status = state.getId();
                Assertions.assertEquals(LEAVE_SUBMIT, status);
            });
        }).subscribe();
    }

    @Test
    public void directLeaderAudit() throws Exception {
        StateMachine<LeaveStatusEnum, Event> stateMachine = leaveStateMachinePersister.restore(leaveStateMachine, "leaveStateMachine");
        LeaveContext leaveContext = new LeaveContext(0, "");
        Message<Event> message = MessageBuilder.withPayload(DIRECT_LEADER_AUDIT).setHeader(STATE_MACHINE_CONTEXT, leaveContext).build();
        Flux<StateMachineEventResult<LeaveStatusEnum, Event>> resultFlux = stateMachine.sendEvent(Mono.just(message));
        resultFlux.doOnComplete(() -> System.out.println("message has sent")).doOnNext(flux -> {
            State<LeaveStatusEnum, Event> state = flux.getRegion().getState();
            LeaveStatusEnum status = state.getId();
            Assertions.assertEquals(LEADER_AUDIT_PASS, status);
        }).subscribe();
    }

    @Test
    public void file() throws IOException {
        DefaultStateMachineContext<LeaveStatusEnum, Event> stateMachineContext = new DefaultStateMachineContext<>(LEAVE_SUBMIT, EMPLOYEE_SUBMIT, null, null);
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(stateMachineContext);
        File file = new File("file.txt");
        FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8);
        writer.write(json);
        writer.close();
    }

}
