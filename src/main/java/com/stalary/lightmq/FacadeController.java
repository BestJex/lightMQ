/**
 * @(#)FacadeController.java, 2018-06-18.
 * <p>
 * Copyright 2018 Stalary.
 */
package com.stalary.lightmq;

import com.stalary.lightmq.data.Constant;
import com.stalary.lightmq.exception.ExceptionEnum;
import com.stalary.lightmq.exception.MyException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * FacadeController
 *
 * @author lirongqian
 * @since 2018/06/18
 */
@RestController
public class FacadeController {

    @Resource
    private OperatorService service;

    /**
     * 生产消息
     * @param topic 主题
     * @param key 键
     * @param value 值
     * @param order 是否顺序，默认无序，异步生产多组
     * @return 消息发送成功
     */
    @GetMapping("/produce")
    public JsonResponse produce(
            @RequestParam String topic,
            @RequestParam(required = false, defaultValue = "") String key,
            @RequestParam(required = false, defaultValue = "false") boolean order,
            @RequestParam(required = false, defaultValue = "true") boolean auto,
            @RequestParam String value) {
        if (StringUtils.isEmpty(value)) {
            return JsonResponse.exception(new MyException(ExceptionEnum.NULL_VALUE));
        }
        if (!order) {
            // 异步生产，不保证顺序
            service.produceAsync(topic, key, value, auto);
        } else {
            service.produceSync(topic, key, value, auto);
        }
        return JsonResponse.success("消息发送成功");
    }

    /**
     * 消费消息，提供阻塞和非阻塞模式
     * @param group 消费分组
     * @param topic 订阅主题
     * @return 消费到的数据
     */
    @GetMapping("/consume")
    public JsonResponse consume(
            @RequestParam(required = false, defaultValue = Constant.DEFAULT_GROUP) String group,
            @RequestParam(required = false, defaultValue = "true") boolean auto,
            @RequestParam String topic) {
        return JsonResponse.success(service.consume(group, topic, auto));
    }

    /**
     * 获得队列中所有数据
     * @return 所有数据
     */
    @GetMapping("/getAll")
    public JsonResponse getAll() {
        return JsonResponse.success(service.getAllMessage());
    }

    /**
     * 获取所有topic
     * @return 所有topic
     */
    @GetMapping("/getAllTopic")
    public JsonResponse getAllTopic() {
        return JsonResponse.success(service.getAllMessage().keySet());
    }

    /**
     * 注册group
     * @param group 消息组
     * @param topic 订阅主题
     * @return
     */
    @GetMapping("/registerGroup")
    public JsonResponse registerGroup(
            @RequestParam String group,
            @RequestParam String topic) {
        service.registerGroup(group, topic);
        return JsonResponse.success("注册group成功");
    }

    @GetMapping("/registerTopic")
    public JsonResponse registerTopic(
            @RequestParam String topic) {
        service.registerTopic(topic);
        return JsonResponse.success("注册topic成功");
    }
}