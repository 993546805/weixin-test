package com.tencent.wxcloudrun;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
@MapperScan(basePackages = {"com.tencent.wxcloudrun.dao"})
public class WxCloudRunApplication implements InitializingBean {

  private final Logger log = LoggerFactory.getLogger(WxCloudRunApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(WxCloudRunApplication.class, args);
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
    log.info("currentTime : {[]}",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
  }
}
