package com.momo.service.service.aclmanager.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.momo.mapper.dataobject.RoleDO;
import com.momo.mapper.dataobject.UserAccountPwdDO;
import com.momo.mapper.dataobject.manual.SysUserListDO;
import com.momo.mapper.mapper.manual.UserMapper;
import com.momo.mapper.req.aclmanager.SysUserListReq;
import com.momo.mapper.req.sysmain.RedisUser;
import com.momo.mapper.res.aclmanager.SysUserListRes;
import com.momo.service.service.BaseService;
import com.momo.service.service.aclmanager.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: momo-cloud-permission
 * @description: 用户管理
 * @author: Jie Li
 * @create: 2019-08-02 17:20
 **/
@Service
@Slf4j
public class SysUserServiceImpl extends BaseService implements SysUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public PageInfo<SysUserListRes> sysUserList(SysUserListReq sysUserListReq) {
        RedisUser redisUser = this.redisUser();
        PageHelper.startPage(sysUserListReq.getPageNum(), sysUserListReq.getPageSize(), "id desc");
        List<SysUserListDO> pageSysUserList = userMapper.pageSysUserList(redisUser.getGroupId(), sysUserListReq.getSysUserName(), sysUserListReq.getFlag());
        PageInfo<SysUserListDO> pageInfo = new PageInfo<>(pageSysUserList);
        List<SysUserListRes> resList = Lists.newArrayList();
        List<SysUserListDO> doList = pageInfo.getList();

        PageInfo<SysUserListRes> pageInfoRes = new PageInfo<>();
        pageInfoRes.setPageNum(pageInfo.getPageNum());
        pageInfoRes.setPageSize(pageInfo.getPageSize());
        pageInfoRes.setTotal(pageInfo.getTotal());
        if (CollectionUtils.isNotEmpty(doList)) {
            doList.forEach(sysUserListDO -> {
                SysUserListRes sysUserListRes = new SysUserListRes();
                BeanUtils.copyProperties(sysUserListDO, sysUserListRes);
                //管理员按钮是否显示
                List<RoleDO> roles = sysUserListDO.getRoles();
                Set<Integer> rolesSet = roles.stream().map(roleDO -> roleDO.getSysRoleType()).collect(Collectors.toSet());
                //角色的类型，0：管理员(老板)，1：管理员(员工) 2其他
                if (rolesSet.contains(0) || rolesSet.contains(1)) {
                    sysUserListRes.setEditButton(false);
                    sysUserListRes.setPwdButton(false);
                }
                //用户是自己登陆，则显示
                if (sysUserListDO.getId().equals(redisUser.getBaseId())) {
                    sysUserListRes.setEditButton(true);
                    sysUserListRes.setPwdButton(true);
                }
                //账号已经绑定的类型
                List<SysUserListRes> bindingsTypeList = Lists.newArrayList();
                //账号未经绑定的类型
                List<SysUserListRes> unBindingsTypeList = Lists.newArrayList();
                UserAccountPwdDO userAccountPwdDO = sysUserListDO.getUserAccountPwdDO();
                //密码绑定
                if (null != userAccountPwdDO) {
                    SysUserListRes sysUser = new SysUserListRes();
                    sysUser.setBindingFlag(userAccountPwdDO.getFlag());
                    sysUser.setBindingName("账号密码");
                    bindingsTypeList.add(sysUser);
                } else {
                    SysUserListRes sysUser = new SysUserListRes();
                    sysUser.setBindingName("账号密码");
                    unBindingsTypeList.add(sysUser);
                }
                sysUserListRes.setBindingsTypeList(bindingsTypeList);
                sysUserListRes.setUnBindingsTypeList(unBindingsTypeList);
                resList.add(sysUserListRes);
            });
            pageInfoRes.setList(resList);

            return pageInfoRes;
        }
        return pageInfoRes;
    }
}