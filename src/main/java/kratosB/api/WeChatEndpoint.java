package kratosB.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
}
