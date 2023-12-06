package com.luckyi.statemachine.statedesignpattern;

/**
 * 开关实现类
 * @author luckyi
 */
public class TurnOnAction implements SwitchState{

    @Override
    public void handle() {
        System.out.println("开灯");
    }
}
