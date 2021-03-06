package com.example.software.Controller;

import com.example.software.Entity.Response;
import com.example.software.Entity.User;
import com.example.software.Service.DiaryService;
import com.example.software.Service.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequestMapping("/user")
@Controller
public class UserController {

    @Autowired
    @Qualifier("userServiceImpl")
    UserService userService;

    @Autowired
    @Qualifier("diaryServiceImpl")
    DiaryService diaryService;

    @ResponseBody
    @RequestMapping("/login")
    public String login(HttpServletResponse httpServletResponse, @RequestBody User user){
        Boolean b = userService.loginValidation(user.getUsername(),user.getPassword());
        if(b)
        {
            Cookie cookie = new Cookie("username",user.getUsername());
            cookie.setPath("/");
            cookie.setMaxAge(3600);
            httpServletResponse.addCookie(cookie);
            String welcome = "Login Success! Welcome back "+ user.getUsername();
            return new Gson().toJson(new Response(true,welcome));
        }
        else
        {
            return new Gson().toJson(new Response(false,"Username or password is incorrect!"));
        }
    }

    @ResponseBody
    @RequestMapping("/register")
    public String register(@RequestBody @Valid User user, BindingResult result){
        if(result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                System.out.println(error.getDefaultMessage());
            }
            return new Gson().toJson(new Response(false,"Invalid Information"));
        }
        Boolean b = userService.createUser(user);
        if(b)
        {
            return new Gson().toJson(new Response(true,"Register Success!"));
        }
        else
        {
            return new Gson().toJson(new Response(false,"User already exist！"));
        }
    }

    @ResponseBody
    @RequestMapping("/logout")
    public String logout(HttpServletRequest httpServletRequest)
    {
        Cookie[] cookies = httpServletRequest.getCookies();
        {
            for(int i=0;i<cookies.length;i++)
            {
                // delete all the cookies
                cookies[i].setMaxAge(0);
            }
        }
        return new Gson().toJson(new Response(true,"Logout Success"));
    }

    @RequestMapping("/history")
    public String history(Model model, @CookieValue(value = "username",required = false) String username){
        //System.out.println(username);
        if(username==null)
        {
            System.out.println("没有cookies");
            // 应当返回需要登录页面并跳转到注册登录页
        }
        //添加该user的所有history
        //返回页面
        return "History";
    }


    @RequestMapping("/viewDiary")
    public String viewDiary(Model model, @CookieValue(value = "username",required = false) String username)
    {
        if(username==null)
        {
            //返回登录页面
        }
        model.addAttribute(new Gson().toJson(diaryService.getCovers()));
        model.addAttribute(new Gson().toJson(diaryService.getPaperColors()));
        model.addAttribute(new Gson().toJson(diaryService.getTypeOfPapers()));
        return "ViewDiary";
    }


}
