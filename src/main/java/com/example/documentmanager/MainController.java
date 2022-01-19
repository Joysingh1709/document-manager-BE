package com.example.documentmanager;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONObject;

@RestController
@CrossOrigin(origins = { "http://someserver:8100" })
public class MainController {

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String index() {
        return "Greetings from Document manager backend..!";
    }

    @RequestMapping(path = "/api", method = RequestMethod.GET)
    public JSONObject apiTest() {
        JSONObject res = new JSONObject();
        res.put("data", "Greetings from Document manager backend..!");
        res.put("errCode", 0);
        res.put("success", true);
        return res;
    }

}
