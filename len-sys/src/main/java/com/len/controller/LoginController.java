package com.len.controller;

import com.alibaba.fastjson.JSONArray;
import com.len.annotation.Log;
import com.len.base.CurrentUser;
import com.len.core.shiro.ShiroUtil;
import com.len.entity.SysMenu;
import com.len.entity.SysUser;
import com.len.service.MenuService;
import com.len.service.SysUserService;
import java.util.List;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.len.util.VerifyCodeUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author zhuxiaomeng
 * @date 2017/12/4.
 * @email 154040976@qq.com
 * 登录、退出页面
 */
@Controller
public class LoginController {

  @Autowired
  private SysUserService userService;

  @Autowired
  private MenuService menuService;

  /**
   * @GetMapping是一个组合注解 是@RequestMapping(method = RequestMethod.GET)的缩写
   * @return
   */
  @GetMapping(value = "")
  public String loginInit(){
    return loginCheck();
  }


  @GetMapping(value = "goLogin")
  public String goLogin(Model model,ServletRequest request){
    //创建实体
    Subject sub = SecurityUtils.getSubject();
    //主体认证
    if (sub.isAuthenticated()){
      return "/main/main";
    }else{
      return "/login";
    }
  }

  @GetMapping(value = "/login")
  public String loginCheck(){
      //每个请求创建一个Subject
      Subject sub = SecurityUtils.getSubject();
      //认证
      boolean flag = sub.isAuthenticated();
      //记住密码
      Boolean flag1 = sub.isRemembered()||flag;
      if (flag1){
        return "/main/main";
      }
      return "/login";
  }


  /**
   * 登录动作
   * @param user
   * @param model
   * @param rememberMe
   * @return
   */
  @ApiOperation(value = "/login", httpMethod = "POST", notes = "登录method")
  @PostMapping(value = "/login")
  public String login(SysUser user,Model model,String rememberMe,HttpServletRequest request){
      String codeMsg = (String) request.getAttribute("shiroLoginFailure");
      if ("error.code".equals(codeMsg)){
        model.addAttribute("message","验证码错误");
        return "/login";
      }
      UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername().trim(),user.getPassword());
      Subject sub = SecurityUtils.getSubject();
      String msg = null;
      try{
          sub.login(token);
          if (sub.isAuthenticated()){
            return "/main/main";
          }
      }catch (UnknownAccountException e) {
        msg = "用户名/密码错误";
      } catch (IncorrectCredentialsException e) {
        msg = "用户名/密码错误";
      } catch (ExcessiveAttemptsException e) {
        msg = "登录失败多次，账户锁定10分钟";
      }
    if(msg!=null){
      model.addAttribute("message",msg);
    }
    return "/login";
  }

  @Log(desc = "用户退出平台")
  @GetMapping(value = "/logout")
  public String logout(){
      Subject subject = SecurityUtils.getSubject();
      subject.logout();
      return "/login";
  }

  /**
   * 组装菜单json格式
   * update by 17/12/13
   * @return
   */
  public JSONArray getMenuJson(){
    List<SysMenu> mList=menuService.getMenuNotSuper();
    JSONArray jsonArr=new JSONArray();
    for(SysMenu sysMenu:mList){
      SysMenu menu=getChild(sysMenu.getId());
      jsonArr.add(menu);
    }
    return jsonArr;
  }

  public SysMenu getChild(String id){
    SysMenu sysMenu=menuService.selectByPrimaryKey(id);
    List<SysMenu> mList=menuService.getMenuChildren(id);
    for(SysMenu menu:mList){
      SysMenu m=getChild(menu.getId());
      sysMenu.addChild(m);
    }
    return sysMenu;
  }
  /*public JSONArray getMenu(){
    SysMenu root=new SysMenu("系统管理","url");
    SysMenu root1=new SysMenu("其他管理","url");
    SysMenu m1=new SysMenu("用户管理","user/ShowUser");
    SysMenu m2=new SysMenu("角色管理","user/ShowUser");
    SysMenu m3=new SysMenu("权限管理","user/ShowUser");
    SysMenu m4=new SysMenu("用户列表","user/ShowUser");
    SysMenu m5=new SysMenu("会员管理","user/ShowUser");
    SysMenu m6=new SysMenu("VIP管理","user/ShowUser");
    SysMenu m7=new SysMenu("VIP管理1","user/ShowUser");
    m6.addChild(m7);
    m3.addChild(m5);
    m3.addChild(m6);
    root.addChild(m1);
    root.addChild(m2);
    root.addChild(m3);
    root1.addChild(m4);
    JSONArray json=new JSONArray();
    json.add(root);
    json.add(root1);
    System.out.println(json);
    return json;
  }

  @GetMapping(value = "test")
  public String test(Model model){
    //model.addAttribute("menu",getMenu());
    return "ftl/main/tab";
  }*/




  public void jxJson(JSONArray ja,int x,int end){
    if(x==0){
      System.out.println("begin");
    }
    String str=" ";
    x++;
    for(int k=0;k<x;k++){
      str+=str;
    }
     for(int i=0;i<ja.size();i++){
      SysMenu menu= (SysMenu) ja.get(i);
      if(menu.getChildren().size()>0){
        System.out.println(str+menu.getName()+"-----------"+x+"级菜单");
        JSONArray js=new JSONArray();
          for(int j=0;j<menu.getChildren().size();j++){
            js.add(menu.getChildren().get(j));
          }
        jxJson(js,x,end);
      }else{
        System.out.println(str+x+"级子菜单-----------"+menu.getName());
      }
     }

  }

  @GetMapping(value="/getCode")
  public void getYzm(HttpServletResponse response, HttpServletRequest request){
    try {
      response.setHeader("Pragma", "No-cache");
      response.setHeader("Cache-Control", "no-cache");
      response.setDateHeader("Expires", 0);
      response.setContentType("image/jpg");

      //生成随机字串
      String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
      //存入会话session
      HttpSession session = request.getSession(true);
      session.setAttribute("_code", verifyCode.toLowerCase());
      //生成图片
      int w = 146, h = 33;
      VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
