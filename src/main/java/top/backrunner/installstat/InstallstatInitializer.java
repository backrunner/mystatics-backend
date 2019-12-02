package top.backrunner.installstat;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import top.backrunner.installstat.utils.misc.GeoIPUtils;

@Component
public class InstallstatInitializer implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 初始化纯真IP数据库
        System.out.println("Init cz88.net Geo IP database...");
        GeoIPUtils.o.init();
    }
}
