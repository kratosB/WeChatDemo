package kratos.service;

/**
 * Created on 2019/1/7.
 *
 * @author zhiqiang bao
 */
public interface IWeChatService {

    /**
     * 获取access_token
     *
     * @return access_token
     */
    String getAccessToken();

    /**
     * 根据微信回调传来的code，获取authorization_code
     *
     * @param code
     *            code作为换取access_token的票据，每次用户授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期。
     * @return authorization_code
     */
    String getAuthToken(String code);
}
