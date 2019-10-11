package com.len.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhuxiaomeng
 * @date 2017/12/30.
 * @email 154040976@qq.com
 */
@Getter
@Setter
@ToString
public class CurrentRole {

    private String id;

    private String roleName;

    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public CurrentRole(String id, String roleName, String remark) {
        this.id = id;
        this.roleName = roleName;
        this.remark = remark;
    }
}
