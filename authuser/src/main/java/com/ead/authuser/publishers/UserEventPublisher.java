package com.ead.authuser.publishers;
/*CLASSE CRIADA PARA PUBLICAR MENSAGENS*/

import com.ead.authuser.dtos.UserEventDto;
import com.ead.authuser.enuns.ActionType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserEventPublisher {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value("${ead.brocker.exchange.userEvent}")
    private String exchangeUserEvent;

    public void publishUserevent(UserEventDto userEventDto, ActionType actionType){
        userEventDto.setActionType(actionType.toString());
        rabbitTemplate.convertAndSend(exchangeUserEvent,"",userEventDto);
    }
}
