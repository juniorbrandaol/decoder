package com.ead.notfication.consumers;

import com.ead.notfication.dtos.NotificationCommandDto;
import com.ead.notfication.enuns.NotificationStatus;
import com.ead.notfication.models.NotificationModel;
import com.ead.notfication.services.NotificationService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

/*CONSUMER*/
@Component
public class NotificationConsumer {

    final NotificationService notificationService;

    public NotificationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${ead.broker.queue.notificationCommandQueue.name}",durable = "true"),
            exchange = @Exchange(value = "${ead.broker.exchange.notificationCommandExchange}",type = ExchangeTypes.TOPIC,ignoreDeclarationExceptions ="true"),
            key = "${ead.broker.key.notificationCommandkey}" )
    )
    public void listen(@Payload NotificationCommandDto notificationCommandDto) {
        NotificationModel notificationModel = new NotificationModel();
        BeanUtils.copyProperties(notificationCommandDto,notificationModel);
        notificationModel.setCriationDate(LocalDateTime.now(ZoneId.of("UTC")));
        notificationModel.setNotificationStatus(NotificationStatus.CREATED);
        notificationService.saveNotification(notificationModel);
    }

}
