# Project Monitor

对于运行中的过境平台项目，定时巡查短信发送情况，发现异常进行报警提醒。

## 功能实现
### 功能1 - 项目运行监控

#### 项目运行状态监控

1. web项目能否正常访问
2. 数据库能否正常连接
3. Linux服务器(如果是)能否SSH连接

#### 过境短信发送状态监控

##### 方式①：通过直连数据库进行监控  
提取目标项目配置表信息，短信发送表信息，号码推送表信息，并进行逻辑判断

##### 方式②：通过Web请求进行监控
目标项目提供了一些查询接口，可以通过web请求的方式判断当前短信发送状态

### 功能2 - 定时推送报警信息

集成钉钉机器人提醒

### 功能3 - 后台管理页面

提供后台管理页面，实现数据维护，项目运行调试，项目维护记录，导入导出等功能。  

独立的前端项目。

## 技术选型

SpringBoot  
MyBatis  
MySQL  
Quartz

## 未完成功能

1. 用户管理与权限控制相关
2. 项目维护记录，生命周期设计
3. 全局统计分析与数据可视化
4. 自定义定时任务配置与管理
5. 敏感数据加密处理，如密码等
6. 简易WEB-SSH控制台实现（封装常见命令，用于快速维护）
7. 简易WEB-SQL查询窗口实现（封装常见脚本，用于通用业务场景）

## 计划

离职之后，对项目进行改造，改造成更为通用的业务场景。




