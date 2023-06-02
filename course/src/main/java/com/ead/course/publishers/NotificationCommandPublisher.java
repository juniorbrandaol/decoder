package com.ead.course.publishers;

import com.ead.course.dtos.NotificationCommandDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NotificationCommandPublisher {

     @Autowired
     RabbitTemplate rabbitTemplate;

     @Value("${ead.broker.exchange.notificationCommandExchange}")
     private String notificationCommandExchange;
     @Value("${ead.broker.key.notificationCommandkey}")
     private String notificationCommandKey;

     public void publishiNotificationCommand(NotificationCommandDto notificationCommandDto){
         rabbitTemplate.convertAndSend(notificationCommandExchange,notificationCommandKey,notificationCommandDto);
     }

}