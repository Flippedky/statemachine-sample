package com.luckyi.statemachine.springstatemachine;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageHeaders;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.kryo.MessageHeadersSerializer;
import org.springframework.statemachine.kryo.StateMachineContextSerializer;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * LeaveStateMachinePersist
 *
 * @author admin
 * @version 1.0
 * @date 2023-11-30 17:10
 */
@Component
public class LeaveStateMachinePersist<LeaveStatusEnum, Event> implements StateMachinePersist<LeaveStatusEnum, Event, String> {

    private static final Logger log = LoggerFactory.getLogger(LeaveStateMachinePersist.class);

    private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.addDefaultSerializer(StateMachineContext.class,new StateMachineContextSerializer<>());
        kryo.addDefaultSerializer(MessageHeaders.class,new MessageHeadersSerializer());
        return kryo;
    });

    private final String localFileName = "stateMachine.kryo";

    @Override
    public void write(StateMachineContext<LeaveStatusEnum, Event> context, String contextObj){
        try(Output output = new Output(new FileOutputStream(localFileName))){
            DefaultStateMachineContext<LeaveStatusEnum, Event> stateMachineContext = (DefaultStateMachineContext<LeaveStatusEnum, Event>) context;
            System.out.println("context-------------------" + stateMachineContext.toString());
            // Gson gson = new GsonBuilder().serializeNulls().registerTypeAdapter(StateMachineContext.class,new StateMachineContextAdapter()).create();
            // String json = gson.toJson(context);
            // log.debug("json-------------------{}",json);
            Kryo kryo = KRYO_THREAD_LOCAL.get();
            log.debug("kryo-------------------{}",kryo);
            kryo.writeObject(output,stateMachineContext);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public StateMachineContext<LeaveStatusEnum, Event> read(String contextObj) throws Exception {
        try (Input input = new Input(new FileInputStream(localFileName))) {
            Kryo kryo = KRYO_THREAD_LOCAL.get();
            return kryo.readObject(input, StateMachineContext.class);
        }
    }
}
