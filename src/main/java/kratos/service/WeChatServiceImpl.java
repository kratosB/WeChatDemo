package kratos.service;

import com.fasterxml.jackson.databind.JavaType;
import kratos.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2019/1/7.
 *
 * @author zhiqiang bao
 */
@Slf4j
@Service
public class WeChatServiceImpl implements IWeChatService {

    private final String weChatApiAddress = "https://api.weixin.qq.com";

    private String appId ;

    private String appKey ;

    @Autowired
    public WeChatServiceImpl(RedisTemplate<String, String> redisTemplate) {
        ValueOperations<String, String> stringObjectValueOperations = redisTemplate.opsForValue();
        appId = stringObjectValueOperations.get("appId");
        appKey = stringObjectValueOperations.get("appKey");
        log.info("appId = {}, appKey = {}", appId, appKey);
    }

    @Override
    public String getAccessToken() {
        String grantType = "&grant_type=client_credential";
        String url = weChatApiAddress + "/cgi-bin/token?appid=" + appId + "&secret=" + appKey + grantType;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        JavaType mapType = JsonMapper.nonEmptyMapper().contructMapType(HashMap.class, String.class, String.class);
        Map<String, String> resultMap = JsonMapper.nonEmptyMapper().fromJson(result, mapType);
        String accessToken = resultMap.get("access_token");
        log.info("accessToken = {}", accessToken);
        return accessToken;
    }

    @Override
    public String getAuthToken(String code) {
        String grantType = "&grant_type=authorization_code";
        String url = weChatApiAddress + "/sns/oauth2/access_token?appid=" + appId + "&secret=" + appKey + "&code=" + code
                + grantType;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        log.info("authorization_code = {}", result);
        JavaType mapType = JsonMapper.nonEmptyMapper().contructMapType(HashMap.class, String.class, String.class);
        Map<String, String> resultMap = JsonMapper.nonEmptyMapper().fromJson(result, mapType);
        String accessToken = resultMap.get("access_token");
        log.info("accessToken = {}", accessToken);
        return accessToken;
    }

    @Override
    public void sendMessage(String toUser, String message) {
        String url = weChatApiAddress + "/cgi-bin/message/custom/send?access_token=" + getAccessToken();
        Map<String, String> textMap = new HashMap<>(2);
        textMap.put("content", message);
        Map<String, Object> dataMap = new HashMap<>(2);
        dataMap.put("text", textMap);
        dataMap.put("touser", toUser);
        dataMap.put("msgtype", "text");
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<>(JsonMapper.nonEmptyMapper().toJson(dataMap), headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, formEntity, String.class);
            log.info(responseEntity.getBody());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void sendTemplateMessage(String toUser, String templateCode, String customerName) {
        String url = weChatApiAddress + "/cgi-bin/message/template/send?access_token=" + getAccessToken();
        Map<String, String> customerNameMap = new HashMap<>(2);
        customerNameMap.put("value", customerName);
        customerNameMap.put("color", "#173177");

        Map<String, Object> data = new HashMap<>(1);
        data.put("customerName", customerNameMap);

        Map<String, Object> dataMap = new HashMap<>(3);
        dataMap.put("touser", toUser);
        dataMap.put("template_id", templateCode);
        dataMap.put("data", data);
        dataMap.put("url", "http://www.qq.com");
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<>(JsonMapper.nonEmptyMapper().toJson(dataMap), headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, formEntity, String.class);
            log.info(responseEntity.getBody());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
