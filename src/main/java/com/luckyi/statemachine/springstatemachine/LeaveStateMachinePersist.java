package com.luckyi.statemachine.springstatemachine;

import com.alibaba.fastjson2.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.luckyi.statemachine.domain.Event;
import com.luckyi.statemachine.domain.LeaveStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * LeaveStateMachinePersist
 *
 * @author admin
 * @version 1.0
 * @date 2023-11-30 17:10
 */
@Component
public class LeaveStateMachinePersist implements StateMachinePersist<LeaveStatusEnum, Event, String> {

    private static final Logger log = LoggerFactory.getLogger(LeaveStateMachinePersist.class);

    private String localFileName = "stateMachine.json";

    @Override
    public void write(StateMachineContext<LeaveStatusEnum, Event> context, String contextObj){
        try{
            File file = new File(localFileName);
            FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8);
            DefaultStateMachineContext<LeaveStatusEnum, Event> stateMachineContext = (DefaultStateMachineContext<LeaveStatusEnum, Event>) context;
            System.out.println("context-------------------" + stateMachineContext.toString());
            Gson gson = new GsonBuilder().serializeNulls().create();
            String json = gson.toJson(stateMachineContext);
            log.debug("json-------------------{}",json);
            writer.write(json);
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public StateMachineContext<LeaveStatusEnum, Event> read(String contextObj) throws Exception {
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(localFileName))) {
            //读入操作
            char[] cBuffer = new char[1024];
            int len;
            StringBuilder json = new StringBuilder();
            while ((len = reader.read(cBuffer)) != -1) {
                String str = new String(cBuffer, 0, len);
                json.append(str);
            }
            return JSONObject.parseObject(json.toString(), DefaultStateMachineContext.class);
        }
    }
}
