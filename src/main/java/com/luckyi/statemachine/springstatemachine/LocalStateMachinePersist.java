package com.luckyi.statemachine.springstatemachine;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageHeaders;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.kryo.MessageHeadersSerializer;
import org.springframework.statemachine.kryo.StateMachineContextSerializer;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * 状态机持久化配置
 * 可自定义持久化 例如：mysql、redis...(此处为kryo序列化本地存储)
 *
 * @author admin
 * @version 1.0
 * @date 2023-11-30 17:10
 */
@Component
public class LocalStateMachinePersist<S, E> implements StateMachinePersist<S, E, String> {

    private static final Logger log = LoggerFactory.getLogger(LocalStateMachinePersist.class);

    private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        // 指定类实例序列化器
        kryo.addDefaultSerializer(StateMachineContext.class,new StateMachineContextSerializer<>());
        kryo.addDefaultSerializer(MessageHeaders.class,new MessageHeadersSerializer());
        return kryo;
    });

    private final String localFileName = "stateMachine.kryo";

    @Override
    public void write(StateMachineContext<S, E> context, String contextObj) throws FileNotFoundException {
        try(Output output = new Output(new FileOutputStream(localFileName))){
            log.info("context-------------------" + context.toString());
            Gson gson = new GsonBuilder().serializeNulls().create();
            String json = gson.toJson(context);
            log.info("json-------------------{}",json);
            Kryo kryo = KRYO_THREAD_LOCAL.get();
            kryo.writeObject(output,context);
        }
    }

    @Override
    public StateMachineContext<S, E> read(String contextObj) throws Exception {
        try (Input input = new Input(new FileInputStream(localFileName))) {
            Kryo kryo = KRYO_THREAD_LOCAL.get();
            return kryo.readObject(input, StateMachineContext.class);
        }
    }
}
