package br.com.f1rst.datas.user.queue;

import br.com.f1rst.datas.user.service.AddressService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class ReceiverQueue {

    private final AddressService addressService;

    @JmsListener(destination = "address-log", containerFactory = "myFactory")
    public void receiveMessage(Object[] objectDatas) {
        log.info("Received addressEntity {}", objectDatas);
        addressService.saveLog(objectDatas);
    }
}