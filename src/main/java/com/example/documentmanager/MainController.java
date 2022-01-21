package com.example.documentmanager;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONObject;

@RestController
@CrossOrigin(origins = { "http://localhost:8100", "http://192.168.29.38:8100",
        "https://documentmanager.vercel.app" })
public class MainController {

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String index() {
        return "Greetings from Document manager backend on heroku..!";
    }

    @RequestMapping(path = "/api", method = RequestMethod.GET)
    public JSONObject apiTest() {
        JSONObject res = new JSONObject();
        res.put("data", "Greetings from Document manager backend on heroku..!");
        res.put("errCode", 0);
        res.put("success", true);
        return res;
    }

}
