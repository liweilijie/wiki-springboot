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


## from
参考的一些文档知识:

- [https://github.com/yubinCloud/fairy-wiki](https://github.com/yubinCloud/fairy-wiki)