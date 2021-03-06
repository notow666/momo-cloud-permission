package com.momo.momopermissiongateway.feign;

import com.momo.momopermissiongateway.common.JSONResult;
import com.momo.momopermissiongateway.req.HasAclReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName: HasAclFeign
 * @Author: Jie Li
 * @Date 2019-10-15 18:00
 * @Description: TODO
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2019</p>
 **/
@FeignClient(name = "momo-permission-system-core", fallback = HasAclServiceFeign.HasAclServiceImplFeign.class)
public interface HasAclServiceFeign {

    @RequestMapping("/hasAcl/v1")
    JSONResult hasAcl(HasAclReq hasAclReq);

    @Component
    @Slf4j
    class HasAclServiceImplFeign implements HasAclServiceFeign {
        @Override
        public JSONResult hasAcl(HasAclReq hasAclReq) {
            log.error("hasAcl error");
            return JSONResult.errorMsg("服务调用权限校验异常");
        }
    }
}
