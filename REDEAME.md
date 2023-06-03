# ChatGPT-Assistant

本次项目是基于ChatGPT的二次开发网站，旨在实现在线聊天的功能。 使用的技术有前端框架Vue3、TypeScript和ElementUI以及后端技术SpringBoot、MongoDB、Spring Data MongoDB和Spring WebSocket。
## 主要功能
1. 登录注册
    用户sa-token管理用户的session。
2. 在线聊天
   用户在聊天框输入内容并发送给后端，后端将请求转发到Open AI的Chat GPT接口，返回数据后通过WebSocket推送给用户。
## 项目运行
```shell
git clone https://github.com/qifan777/chatgpt-assistant.git
```
### 目录介绍
```shell

```
### 环境安装
- java 17
- mongodb
    ```shell
    docker run -d \
      --name mongo \
      -e MONGO_INITDB_ROOT_USERNAME=root \
      -e MONGO_INITDB_ROOT_PASSWORD=123456 \
      -p 27017:27017 \
      mongo:6.0.5-jammy
    ```
### 
