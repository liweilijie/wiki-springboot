# wiki-springboot

[start.spring.io](start.sping.io) 版本用`2.x` 的版本，`maven`来管理，依赖选择 `web` 和 `devtools`.
热部署两个依赖即可。

## 开发

**psvm** 快速生成代码:
```java
public static void main(String[] args) { }
```
**sout** 生成代码：`System.out.println();`

用 **idea** 自带的 `http` 测试功能来测试，不用移动窗口方便。

**gtr, gtrp, ptr, ptrp** 生成代码：
```http
GET http://127.0.0.1:8880/hello
Accept: application/json

###
```

自定义 **live template**:

**logf** 然后生成代码为：

将`CLASSNAME` 映射到 `classname()`即可。
```java
private static final Logger LOG = LoggerFactory.getLogger($CLASSNAME$.class); 
```

### log
将`logback-spring.xml` 放在 `src/main/resources/logback-spring.xml` 框架会自动识别。

### application.properties
配置 **springboot**  里面有 config/application.properties 也可以自动识别，或者 yaml 格式也可以的.

mybatis-generator:
配置在pom.xml 和 resources/generator/generator-config.xml 配置好之后,
使用generator -e 会自动解析并且生成代码文件:
- domain/Demo.java
- domain/DemoExample.java
- mapper/DemoMapper.java
- resources/mapper/DemoMapper.xml


## 前端 

安装好**node**, 然后安装`vue-cli`
```bash
# 设置registry 淘宝镜像
npm get registry
npm config set registry http://registry.npm.taobao.org
npm install -g @vue/cli
# 查看是否安装好
vue --version
# 创建一个vue 项目
vue create web
# 安装ant design vue 框架
npm -i ant-design-vue --save
# 安装axios
npm install axios --save
```
**vue create web**:
- 手动创建
- 选择Babel,typescript, router, vuex, linter/formatter
- 3.x
- 是否需要用class-style风格: 不需要
- 是否用Babel来配合typescript: 不需要
- history mode for router? 意思是路由地址栏包含#号,我们选择:需要
- ESLint的校验规则,选择error only 最简单的方式.
- lint on save 保存的时候才检查 代码.
- config Babel, ESLint是否单独用配置文件,选择: 单独的
- 保存成模板

```js
// 完整引入ant-design-vue 
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
// 引入 之后需要use
app.use(Antd);
```


## from
参考的一些文档知识:

- [https://github.com/yubinCloud/fairy-wiki](https://github.com/yubinCloud/fairy-wiki)