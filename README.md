# Attacking_Shiro_with_CVE_2020_2555

前段时间项目上遇到了一个部署在 `Weblogic` 上的存在 `Shiro反序列化` 漏洞的 Web应用，于是参照 `Y4er` 
师傅的文章 [《使用WebLogic CVE-2020-2883配合Shiro rememberMe反序列化一键注入蚁剑shell》](https://xz.aliyun.com/t/8202) 的文章调了下 `payload`，
很遗憾当时并没有成功（可能是目标 `Weblogic` 打了补丁），但是 `payload` 本地测试是没问题的，也遇到很多曲折（后面打算写篇文章记录下，TBD），
在 `Y4er` 师傅的基础上新增了 `回显` 和 `植入冰蝎内存shell` 的功能，整理留作备忘。

p.s. 只适用于 `Weblogic 12g`
