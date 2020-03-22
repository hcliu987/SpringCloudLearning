package com.springcloud.controller;


import com.springcloud.service.PaymentService;
import com.springcloud.entities.CommonResult;
import com.springcloud.entities.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PaymentController {

    @Value("${server.port}")
    private  String serverPort;
    @Autowired
    private PaymentService paymentService;

    @PostMapping(value = "/payment/add")
    public CommonResult add(Payment payment) {
        int result = paymentService.add(payment);
        log.info("***********插入结果" + result);
        if (result > 0) {
            return new CommonResult(200, "插入结果成功,serverPort:"+serverPort, result);
        } else {
            return new CommonResult(404, "插入结果失败", null);
        }

    }
    @GetMapping(value = "/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable ("id") Long id) {
        Payment payment = paymentService.getPaymentById(id);
        log.info("**********查询结果" + payment);
        if (payment !=null) {
            return new CommonResult(200, "查询结果成功,serverport:"+serverPort, payment);
        } else {
            return new CommonResult(404, "查询结果失败，没有对应id"+id, null);
        }
    }
}
