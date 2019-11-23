package top.backrunner.installstat.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.backrunner.installstat.common.service.RecaptchaService;
import top.backrunner.installstat.config.RecaptchaConfig;
import top.backrunner.installstat.utils.network.HttpUtils;

import java.util.HashMap;

@Service
public class RecaptchaServiceImpl implements RecaptchaService {
    @Autowired
    private RecaptchaConfig recaptchaConfig;

    private final String VERIFY_URL = "https://www.recaptcha.net/recaptcha/api/siteverify";

    @Override
    public boolean verify(String token) {
        // 为空不处理
        if (token == null || "".equals(token)){
            return false;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("secret", recaptchaConfig.getSecret());
        params.put("response", token);
        String verifyRes = HttpUtils.postJson(VERIFY_URL, params);
        if (verifyRes == null){
            return false;
        }
        JSONObject res = JSON.parseObject(verifyRes);
        return (boolean)res.get("success");
    }
}
