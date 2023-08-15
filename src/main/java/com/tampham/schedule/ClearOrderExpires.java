package com.tampham.schedule;

import com.tampham.enums.OrderStatus;
import com.tampham.models.Order;
import com.tampham.repository.OrderRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Component
public class ClearOrderExpires {
    private static final Logger LOGGER = LogManager.getLogger(ClearOrderExpires.class);

    @Autowired
    private OrderRepository orderRepository;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy");

    @Scheduled(cron = "0 0 10-20 * * *")
    public void removeOrderExpires(){
        Date currentDate = new Date();
        List<Order> orders = orderRepository.findAll();

        for (Order order : orders){
            if (order.getStatus().equals(OrderStatus.PENDING)){
                int result = (int) ((currentDate.getTime() - order.getCreatedDate().getTime()) / (1000 * 60 * 60 * 24));

                if (result > 1){
                    order.setStatus(OrderStatus.CANClED);
                    orderRepository.save(order);
                }
            }
        }
    }
}
