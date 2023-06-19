# ChatGPT-Assistant

本次项目是基于ChatGPT的二次开发网站，旨在实现在线聊天的功能。 使用的技术有前端框架Vue3、TypeScript和ElementUI以及后端技术SpringBoot、MongoDB、Spring Data MongoDB和Spring WebSocket。
## 自我介绍
[我是起凡](https://space.bilibili.com/357290237)，一名全栈程序员，刚刚毕业于华侨大学。主要技术栈后端SpringBoot,JPA(Hibernate), MongoDB, Mybatis, SQL等。前端Vue3, Typescript, Taro小程序, Uni-App等。

## 主要功能
1. 登录注册
    用户sa-token管理用户的session。
2. 在线聊天
   用户在聊天框输入内容并发送给后端，后端将请求转发到Open AI的Chat GPT接口，返回数据后通过WebSocket推送给用户。
## 项目运行
### clone仓库
```shell
git clone https://github.com/qifan777/chatgpt-assistant.git
```
### 目录介绍
- bom 依赖管理
- chatgpt-assistant-client vue客户端
    - src
        - api 存放调用后端的接口
        - assets 静态资源图片等
        - components 通用的组件
        - router 路由
        - stores pinia状态管理
        - views 页面
            - home
                - components home页面使用的私有组件
                - HomeView.vue home页面
- chatgpt-assistant-server java核心代码
- infrastructure 基础设施
    - infrastructure-common 通用的类
    - infrastructure-generator 代码生成器
        - generator-core 注解定义
        - generator-processor 生成器逻辑
    - infrastructure-security 通用登录认证拦截器
### 环境安装
- jdk 17
- mongodb
    ```shell
    docker run -d \
      --name mongo \
      -e MONGO_INITDB_ROOT_USERNAME=root \
      -e MONGO_INITDB_ROOT_PASSWORD=123456 \
      -p 27017:27017 \
      mongo:6.0.5-jammy
    ```
- redis
    ```shell
    docker run --name redis -p 6379:6379 -d redis 
    ```
- node18
### 运行
#### 前端
1. vscode/webstorm导入chatgpt-assistant-client
2. 运行命令
    ```shell
    npm install
    npm run dev
    ```
#### 后端
1. idea 导入chatgpt-assistant整个文件夹
2. mvn install
3. 运行chatgpt-assistant-server下的Application。