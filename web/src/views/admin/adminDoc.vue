<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-row :gutter="24">
        <a-col :span="8">
          <a-form
              layout="inline"
              :model="param"
          >
            <a-form-item>
              <a-input v-model:value="param.name" placeholder="分类名称" @keyup.enter="handleQuery()" >
                <template #prefix><UserOutlined style="color: rgba(0, 0, 0, 0.25)" /></template>
              </a-input>
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="handleQuery()">
                查询
              </a-button>
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="add()">
                新增
              </a-button>
            </a-form-item>
          </a-form>
          <a-table
              v-if="level1 !== undefined && level1.length > 0"
              :columns="columns"
              :row-key="record => record.id"
              :data-source="level1"
              :loading="loading"
              :pagination="false"
              size="small"
              :defaultExpandAllRows="true"
          >
            <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'cover'">
                <img :src="record.cover" alt="avatar" height="20" width="20" />
              </template>
              <template v-else-if="column.key === 'action'">
                <a-space size="small">
                  <a-button type="primary" @click="edit(record)" size="small">
                    编辑
                  </a-button>
                  <a-popconfirm
                      title="删除后不可恢复，确认删除?"
                      ok-text="是"
                      cancel-text="否"
                      @confirm="handleDelete(record.id)"
                  >
                    <a-button type="danger" size="small">
                      删除
                    </a-button>
                  </a-popconfirm>
                </a-space>
              </template>
            </template>
          </a-table>
        </a-col>
        <a-col :span="16">
          <p>
            <a-form layout="inline" :model="param">
              <a-form-item>
                <a-button type="primary" @click="handleSave()" >
                  保存
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-form :model="doc" layout="vertical" >
            <a-form-item>
              <a-input v-model:value="doc.name" placeholder="名称" />
            </a-form-item>
            <a-form-item>
              <a-tree-select
                  v-model:value="doc.parent"
                  show-search
                  style="width: 100%"
                  :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                  placeholder="请选择父节点"
                  allow-clear
                  tree-default-expand-all
                  :tree-data="treeSelectData"
                  :fieldNames="{ label: 'name', value: 'id', key: 'id' }"
              >
              </a-tree-select>
            </a-form-item>
            <a-form-item>
              <a-input v-model:value="doc.sort" placeholder="顺序"/>
            </a-form-item>
            <a-form-item>
              <div id="content"></div>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>
    </a-layout-content>
  </a-layout>

<!--  <a-modal-->
<!--      v-model:visible="modalVisible"-->
<!--      title="分类表单"-->
<!--      :confirm-loading="modalLoading"-->
<!--      @ok="handleModalOk"-->
<!--  >-->
<!--  </a-modal>-->
</template>

<script lang="ts">
import {createVNode, defineComponent, onMounted, ref } from "vue";
import {message, Modal} from 'ant-design-vue';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import axios from 'axios';
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";
import E from "wangeditor"

export default defineComponent({
  name: 'AdminDoc',
  setup() {
    // 获取路由信息
    const route = useRoute();
    console.log("路由：", route);
    console.log("route.path：", route.path);
    console.log("route.query：", route.query);
    console.log("route.param：", route.params);
    console.log("route.fullPath：", route.fullPath);
    console.log("route.name：", route.name);
    console.log("route.meta：", route.meta);

    // param 给查询用的响应式参数
    const param = ref();
    param.value = {}; // 初始需要加空对象，不然会报错的。

    const docs = ref();
    const loading = ref(false);

    const columns = [
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '父分类',
        key: 'parent',
        dataIndex: 'parent'
      },
      {
        title: '排序',
        key: 'sort',
        dataIndex: 'sort'
      },
      {
        title: 'Action',
        key: 'action',
        dataIndex: 'action'
      }
    ];

    /**
     * 一级分类树, children属性就是二级分类
     * [{
     *   id: "",
     *   name: "",
     *   children: [{
     *     id: "",
     *     name: "",
     *   }]
     * }]
     */
    const level1 = ref(); // 一级分类树, children属性就是二级分类
    level1.value = [];

    // 因为树选择组件的属性状态，会随当前编辑的节点而变化，所以单独声明一个响应式变量
    const treeSelectData = ref();
    treeSelectData.value = [];


    /**
     * 数据查询
     */
    const handleQuery = () => {
      loading.value = true;
      level1.value = [];
      axios.get("/doc/all/"+route.query.ebookId).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          docs.value = data.content;
          console.log("原始数组:", docs.value);

          level1.value = [];
          level1.value = Tool.array2Tree(docs.value, 0);
          console.log("树形结构:", level1);

          // 父文档下拉框初始化，相当于点击新增
          treeSelectData.value = Tool.copy(level1.value) || [];
          // 为选择树添加一个"无"
          treeSelectData.value.unshift({id: 0, name: '无'});
        } else {
          message.error(data.message);
        }
      })
    };

    // ------------- 表单 ----------------
    const doc = ref();
    // 刚一开始就把ebookId 给 doc 进行保存
    doc.value = {
      ebookId: route.query.ebookId
    };

    const deleteIds: Array<string> = [];
    const deleteNames: Array<string> = [];
    let editor: E;


    const getDeleteIds = (treeSelectData: any, id: any) => {
      // 遍历数组，即遍历某一层节点。
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果当时节点就是目标节点
          console.log("delete:", node);
          // 将目标节点id 设置到 ids 进行删除
          deleteIds.push(id);
          deleteNames.push(node.name);

          // 遍历所有子节点， 将所有子节点都将删除
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              getDeleteIds(children, children[j].id)
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            getDeleteIds(children, id);
          }
        }
      }
    };

    const setDisable = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍历数组，即遍历某一层节点。
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果当时节点就是目标节点
          console.log("disabled", node);
          // 将目标节点设置为 disabled
          node.disabled = true;

          // 遍历所有子节点， 将所有子节点全部都加上 disabled
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDisable(children, children[j].id)
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisable(children, id);
          }
        }
      }
    };

    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleSave = () => {
      modalLoading.value = true;
      doc.value.content = editor.txt.html();

      axios.post("/doc/save", doc.value).then((response) => {
        modalLoading.value = false;
        const data = response.data;
        if (data.success) {
          message.success("保存成功！");
          // modalVisible.value = false;

          // 重新加载列表数据
          handleQuery();
        } else {
          message.error(data.message);
        }
      });
    };

    const handleQueryContent = () => {
      axios.get("/doc/find-content/"+doc.value.id).then((response) => {
        const data = response.data;
        if (data.success) {
          editor.txt.html(data.content);
        } else {
          message.error(data.message);
        }
      })
    };


    /**
     * 编辑
     */
    const edit = (record: any) => {
      // 清空富文本
      editor.txt.html("");
      modalVisible.value = true;
      // 在编辑的时候"复制对象"出来编辑，这样才不会影响响应式数据
      doc.value = Tool.copy(record);
      // 查询富文本内容
      handleQueryContent();

      // 不能选择当前节点及其所有子孙节点，作为父节点，会使树断开
      treeSelectData.value = Tool.copy(level1.value);
      setDisable(treeSelectData.value, record.id);

      // 为选择树添加一个"无"
      treeSelectData.value.unshift({id: 0, name: '无'});
    };

    /**
     *  新增
     */
    const add = () => {
      // 清空富文本
      editor.txt.html("");

      modalVisible.value = true;
      // 只有在新增时需要传入 ebookId, 在编辑时由数据库查询出来的有完事的 ebookId
      doc.value = {
        ebookId: route.query.ebookId
      };

      treeSelectData.value = Tool.copy(level1.value) || [];

      // 为选择树添加一个"无"
      treeSelectData.value.unshift({id: 0, name: '无'});
    };

    // 删除多个id: /doc/delete/1,2,3,4
    const handleDelete = (id: number) => {
      // 清空数组，否则多次删除时，数组会一直增加
      deleteIds.length = 0;
      deleteNames.length = 0;
      // 这里要用 level1.value, 因为 treeSelectData 里面多了一个无
      getDeleteIds(level1.value, id);
      console.log("delete ids:", deleteIds);
      console.log("delete names:", deleteNames);

      // 二次确认，将文档名称打印出来确认
      Modal.confirm({
        title: '重要提醒',
        icon: createVNode(ExclamationCircleOutlined),
        content: '将删除：【' + deleteNames.join("，") + "】删除后不可恢复，确认删除？",
        onOk() {
          // console.log(ids)
          axios.delete("/doc/delete/" + deleteIds.join(",")).then((response) => {
            const data = response.data; // data = commonResp
            if (data.success) {
              // 重新加载列表
              handleQuery();
            } else {
              message.error(data.message);
            }
          });
        }
      });
    };

    onMounted(() => {
      handleQuery();
      editor = new E('#content');
      editor.config.zIndex = 0;
      editor.create();
    });

    return {
      param,
      // docs,
      level1,
      columns,
      loading,

      doc,
      modalLoading,
      modalVisible,
      handleSave,
      handleQuery,

      edit,
      add,
      handleDelete,
      treeSelectData,
    }
  }
})
</script>

<style scoped>
img {
  width: 50px;
  height: 50px;
}
</style>
