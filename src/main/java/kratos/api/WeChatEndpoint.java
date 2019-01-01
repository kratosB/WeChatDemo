package kratos.api;

import kratos.util.JaxbUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import kratos.api.req.MessageReq;

/**
 * Created on 2018/7/20.
 *
 * @author zhiqiang bao
 */
@Slf4j
@RestController
public class WeChatEndpoint {

    @GetMapping("/weChat")
    public void weChat(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("获取到外部请求");
        String echostr = request.getParameter("echostr");
        response.getOutputStream().println(echostr);
    }

    @PostMapping("/weChat")
    public String postWeChat(@RequestParam(name = "signature", required = false) String signature,
                             @RequestParam(name = "timestamp", required = false) String timestamp,
                             @RequestParam(name = "nonce", required = false) String nonce, @RequestBody(required = false) String message) {
        log.info("获取到外部post请求, signature = {}, timestamp = {}, nonce = {}, message = {}", signature, timestamp, nonce, message);
        if (signature != null && validate(signature, timestamp, nonce)) {
            log.info("获取到外部post请求，验证通过");
        } else {
            log.info("获取到外部post请求，验证不通过");
            return null;
        }

        JaxbUtil jaxbUtil = new JaxbUtil(MessageReq.class);
        MessageReq messageReq = jaxbUtil.fromXml(message);
        String event = "event";
        String text = "text";
        if (StringUtils.equals(messageReq.getMsgType(), text)) {
            log.info("收到文本消息，fromUser = {},createTime = {},content = {}", messageReq.getFromUserName(), messageReq.getCreateTime(),
                    messageReq.getContent());
            String openId = messageReq.getFromUserName();
            String content = messageReq.getContent();
            // TODO: 2019/1/1
//            Memo memo = new Memo();
//            memo.setOpenId(openId);
//            memo.setContent(content);
//            memo.setCreatedTime(new Date());
//            memoDao.save(memo);
        } else if (StringUtils.equals(messageReq.getMsgType(), event)) {
            log.info("收到事件消息，fromUser = {},createTime = {},event = {},key = {}", messageReq.getFromUserName(),
                    messageReq.getCreateTime(), messageReq.getEvent(), messageReq.getEventKey());
            String result;
            switch (messageReq.getEventKey()) {
                case "WIFI":
                    break;
                case "WIFI_ACCOUNT":
                    result = "<xml> <ToUserName>" + messageReq.getFromUserName() + "</ToUserName> <FromUserName>"
                            + messageReq.getToUserName() + "</FromUserName> <CreateTime>" + messageReq.getCreateTime()
                            + "</CreateTime> <MsgType><![CDATA[text]]></MsgType> <Content><![CDATA[WIFI账号是ChinaNet-TmVx]]></Content> </xml>";
                    log.info(result);
                    return result;
                case "WIFI_PASSWORD":
                    result = "<xml> <ToUserName>" + messageReq.getFromUserName() + "</ToUserName> <FromUserName>"
                            + messageReq.getToUserName() + "</FromUserName> <CreateTime>" + messageReq.getCreateTime()
                            + "</CreateTime> <MsgType><![CDATA[text]]></MsgType> <Content><![CDATA[WIFI密码是uhhehvuc]]></Content> </xml>";
                    log.info(result);
                    return result;
                default:
                    result = "<xml> <ToUserName>" + messageReq.getFromUserName() + "</ToUserName> <FromUserName>"
                            + messageReq.getToUserName() + "</FromUserName> <CreateTime>" + messageReq.getCreateTime()
                            + "</CreateTime> <MsgType><![CDATA[text]]></MsgType> <Content><![CDATA[你好]]></Content> </xml>";
                    log.info(result);
                    return result;
            }
            return null;
        } else {
            log.info("收到其他消息，fromUser = {},createTime = {},messageType = {}", messageReq.getFromUserName(),
                    messageReq.getCreateTime(), messageReq.getMsgType());
        }
        String result = "<xml> <ToUserName>" + messageReq.getFromUserName() + "</ToUserName> <FromUserName>"
                + messageReq.getToUserName() + "</FromUserName> <CreateTime>" + messageReq.getCreateTime()
                + "</CreateTime> <MsgType><![CDATA[text]]></MsgType> <Content><![CDATA[你好]]></Content> </xml>";
        log.info(result);
        return result;
    }

    private boolean validate(String signature, String timestamp, String nonce) {
        log.info("开始验证请求是否是从微信来的，timestamp = {}，nonce = {}，signature = {}", timestamp, nonce, signature);
        String token = "bzq";
        String[] paramArray = new String[] {
                token, timestamp, nonce
        };
        Arrays.sort(paramArray);
        StringBuilder sb = new StringBuilder();
        for (String s : paramArray) {
            sb.append(s);
        }
        String generateCode = DigestUtils.sha1Hex(sb.toString());
        log.info("generateCode:" + generateCode);
        return generateCode.equals(signature);
    }


}
