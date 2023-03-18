package com.ecsite.springbootoracle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/user")
    public String getUserData(Model model) {
        List<Map<String, Object>> userData = getUserData();

        int vip1Count = 0;
        int vip2Count = 0;
        int normalCount = 0;
        int standardCount = 0;


        for (Map<String, Object> user : userData) {
            String userType = (String) user.get("usertype");
            int count = ((Number) user.get("count")).intValue();

            if (userType.equals("vip1")) {
                vip1Count = count;
            }else if (userType.equals("vip2")){
                vip2Count =  count;
            }
            else if (userType.equals("normal")) {
                normalCount = count;
            }else if (userType.equals("standard")){
                standardCount = count;
            }
        }

        int totalCount = vip1Count + vip2Count + standardCount + normalCount ;
        double vip1Percentage = (double) vip1Count / totalCount * 100;
        double vip2Percentage = (double) vip2Count / totalCount * 100;
        double standardPercentage = (double) standardCount / totalCount * 100;
        double normalPercentage = (double) normalCount / totalCount * 100;

        model.addAttribute("vip1Percentage", vip1Percentage);
        model.addAttribute("vip2Percentage", vip2Percentage);
        model.addAttribute("standardPercentage", standardPercentage);
        model.addAttribute("normalPercentage", normalPercentage);
        return "user";
    }

    private List<Map<String, Object>> getUserData() {
        String query = "SELECT COUNT(*) AS count, usertype FROM admins GROUP BY usertype";
        return jdbcTemplate.queryForList(query);
    }

}

