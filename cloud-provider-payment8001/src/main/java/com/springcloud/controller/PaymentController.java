package com.springcloud.controller;

import com.springcloud.entities.CommonResult;
import com.springcloud.entities.Payment;
import com.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;


@RestController
@Slf4j
public class PaymentController {

    @Value("${server.port}")
    private  String serverPort;


    @Resource
    private DiscoveryClient discoveryClient;
    @Autowired
    private PaymentService paymentService;

    @PostMapping(value = "/payment/add")
    public CommonResult add(Payment payment) {
        int result = paymentService.add(payment);
        log.info("***********插入结果" + result);
        if (result > 0) {
            return new CommonResult(200, "插入结果成功,serverport:"+serverPort, result);
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

    @GetMapping(value = "/payment/discovery")
    public  Object discovery(){
        List<String> services = discoveryClient.getServices();
        for (String element : services) {
            log.info("****element: " +element);
        }
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance:instances) {
            log.info(instance.getServiceId()+"\t"+instance.getHost()+"\t"+instance.getPort()+"\t"+instance.getUri());
        }
        return this.discoveryClient;
    }

    @GetMapping(value = "payment/lb")
    public  String getPaymentLB(){
        return  serverPort;
    }

    @GetMapping(value = "/payment/feign/timeout")
    public String payMentFeignTimeOut(){
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  serverPort;
    }

    @GetMapping("/payment/zipkin")
    public String paymentZipkin(){
        return "hi,i'am paymentzipkin server fall back, welcome to";
    }

}
