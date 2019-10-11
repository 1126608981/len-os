package com.len.util;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhuxiaomeng
 * @date 2017/12/21.
 * @email 154040976@qq.com
 * 复选框类
 */
@Getter
@Setter
public class Checkbox {

  private String id;
  private String name;
  /**默认未选择*/
  private boolean check=false;

  @Override
  public String toString() {
    return "Checkbox{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", check=" + check +
            '}';
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isCheck() {
    return check;
  }

  public void setCheck(boolean check) {
    this.check = check;
  }
}
