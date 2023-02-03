# wiki-springboot

[start.spring.io](start.sping.io) 选择 web 和 devtools
热部署两个依赖即可。

## 开发

**psvm** 快速生成代码:
```java
public static void main(String[] args) { }
```
**sout** 生成代码：`System.out.println();`

自定义 **live template**:
**logf** 然后生成代码为：`private static final Logger LOG = LoggerFactory.getLogger($CLASSNAME$.class);` 将`CLASSNAME` 映射到 `classname()`即可。

### log
将`logback-spring.xml` 放在 `src/main/resources/logback-spring.xml` 框架会自动识别。