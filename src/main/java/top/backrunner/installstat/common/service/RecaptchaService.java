package top.backrunner.installstat.common.service;

public interface RecaptchaService {
    public boolean verify(String token);
}
