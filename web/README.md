# web

主要是**vue3**的一些知识。

**两种定义双向数据绑定**:

- ref(): 
  * 定义数据绑定: `const data = ref();` 
  * 给数据赋值: 要用 `data.value = value;`. 
  * 最后要在`setup()`里面 `return` 出去才能用。
- reactive(): 
  * 定义: const ebook1 = reactive({books: []}); 
  * 赋值: ebook1.books = resp.data.content;
  * 导出：ebooks2 = toRef(ebook1, "books");

```js
export default defineComponent({
  name: 'Home',
  setup() {
    // 定义数据绑定
    // 给数据赋值要用 data.value = value;
    const data = ref();

    // 定义
    const ebook1 = reactive({books: []});

    onMounted(() => {
      axios.get(url).then((resp)=>{
        data.value = resp.data.content;

        // 赋值
        ebook1.books = resp.data.content;
      })
    });

    return {
      data,
      ebooks2: toRef(ebook1, "books"), // 导出某个item,要加双引号
    }
  },
});
```