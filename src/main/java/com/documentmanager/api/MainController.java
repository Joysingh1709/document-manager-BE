package com.documentmanager.api;

import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = { "http://localhost:8100", "http://192.168.29.38:8100",
        "https://documentmanager.vercel.app" })
public class MainController {

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String index() {
        return "Greetings from Document manager backend on heroku..!";
    }

    @RequestMapping(path = "/api", method = RequestMethod.GET)
    public HashMap<String, Object> apiTest() {
        HashMap<String, Object> res = new HashMap<String, Object>();
        res.put("data", "Greetings from Document manager backend on heroku..!");
        res.put("errCode", "0");
        res.put("success", true);
        return res;
    }

}
