<div align="center"><img width="100" src="https://mystats.pwp.app/img/logo_256.fda3c4a7.png"><h1>myStats</h1></div>

## 概述

该项目是基于Spring Boot + Vue打造的安装量统计平台，开箱即用，你可以在非常短的时间内部署属于你自己的私有安装量统计平台。

## 功能

- 完善的用户登录注册
- 基础的安装量统计功能，支持按平台/分支统计
- 重点数据图表化展示
- 自由的接口设计，简化接入的难度

## 使用方式

项目默认构建成jar，将jar复制到有Java环境的服务器上直接运行即可。

需要注意的是，你需要修改下面几处配置：

**src/main/resources/application.yml**

你需要根据application-dev.yml创建一份你自己的配置文件，并且更改application.yml中属于激活状态的配置文件。

在Web容器这边，你需要根据你的服务器配置设置相关的参数，你也可以更换我们默认使用的Undertow为其他的容器。

**src/main/resources/geoip/config.properties**

复制模板为上述文件名，填写纯真IP的路径

**src/main/resources/recaptcha/config.properties**

复制模板为上述文件名，填写Google验证码（reCaptcha）的API密钥。

**特别注意：上述两个properties我们未区分开发环境和部署环境，请自行改造项目或者在打包的时候替换内容。**

对于Shiro相关的配置，你可以自行修改我们默认使用的ehcache为redis等。

## 前端

前端是一个Vue + Element打造的面板，功能相对比较简单，你可以基于后端的接口打造一个全新的面板，只使用我们的后端程序。

该后端项目并不与前端项目捆绑。

[mystats-frontend](https://github.com/pwp-app/mystats-frontend)

## 许可证

Apache 2.0