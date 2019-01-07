package kratos.api;

import kratos.service.IWeChatService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 2019/1/7.
 *
 * @author zhiqiang bao
 */
@Slf4j
@RestController
public class WeChatMessageEndpoint {

    private IWeChatService weChatService;

    public WeChatMessageEndpoint(IWeChatService weChatService) {
        this.weChatService = weChatService;
    }

    @GetMapping("api/weChat/sendMsg")
    public void sendMsg(@RequestParam(value = "toUser") String toUser, @RequestParam(value = "message") String message) {
        log.info("开始发送微信消息，toUser = {}，message = {}", toUser, message);
        if (StringUtils.isBlank(toUser)) {
            toUser = "oYSUd1Nx3Ucq-1CHCDrMijbtX4x8";
        }
        weChatService.sendMessage(toUser, message);
        log.info("结束发送微信消息");
    }

    @GetMapping("api/api/weChat/sendTemplateMsg")
    public void sendTemplateMsg(@RequestParam(value = "toUser") String toUser,
                                @RequestParam(value = "templateCode") String templateCode, @RequestParam(value = "customerName") String customerName) {
        log.info("开始发送微信模板消息，toUser = {}，templateCode = {}，customerName = {}", toUser, templateCode, customerName);
        if (StringUtils.isBlank(toUser)) {
            toUser = "oYSUd1Nx3Ucq-1CHCDrMijbtX4x8";
        }
        weChatService.sendTemplateMessage(toUser, templateCode, customerName);
        log.info("结束发送微信模板消息");
    }

}
