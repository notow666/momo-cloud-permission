package com.momo.momopermissiongateway.req;

import com.momo.momopermissiongateway.common.RedisUser;
import lombok.*;

/**
 * @ClassName: HasAclReq
 * @Author: Jie Li
 * @Date 2019-10-15 17:55
 * @Description: TODO
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2019</p>
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
//@EqualsAndHashCode(of = {"id"})
public class HasAclReq extends RedisUser {
    private String url;
}
