package kratos.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author QQQ
 */
@RestController
public class HealthCheckEndpoint {

    @ApiOperation("health check")
    @RequestMapping(value = "api/health", method = {
            RequestMethod.POST, RequestMethod.GET
    })
    public boolean healthCheck() {
        return true;
    }

}
