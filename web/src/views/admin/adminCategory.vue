<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-form
          layout="inline"
          :model="param"
      >
        <a-form-item>
          <a-input v-model:value="param.name" placeholder="分类名称" @keyup.enter="handleQuery({page: 1, size: pagination.pageSize})" >
            <template #prefix><UserOutlined style="color: rgba(0, 0, 0, 0.25)" /></template>
          </a-input>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="handleQuery({page: 1, size: pagination.pageSize})">
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
        :columns="columns"
        :row-key="record => record.id"
        :data-source="categorys"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
        >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'cover'">
            <img :src="record.cover" alt="avatar" height="20" width="20" />
          </template>
          <template v-else-if="column.key === 'action'">
            <a-space size="small">
              <a-button type="primary" @click="edit(record)">
                编辑
              </a-button>
              <a-popconfirm
                  title="删除后不可恢复，确认删除?"
                  ok-text="是"
                  cancel-text="否"
                  @confirm="handleDelete(record.id)"
              >
                <a-button type="danger">
                  删除
                </a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>

    </a-layout-content>
  </a-layout>

  <a-modal
      v-model:visible="modalVisible"
      title="分类表单"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="category" :label-col="{span: 6}">
      <a-form-item label="名称">
        <a-input v-model:value="category.name" />
      </a-form-item>
      <a-form-item label="父分类">
        <a-input v-model:value="category.parent" />
      </a-form-item>
      <a-form-item label="排序">
        <a-input v-model:value="category.sort" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref } from "vue";
import {message} from 'ant-design-vue';
import axios from 'axios';
import {Tool} from "@/util/tool";

export default defineComponent({
  name: 'AdminCategory',
  setup() {
    // param 给查询用的响应式参数
    const param = ref();
    param.value = {}; // 初始需要加空对象，不然会报错的。

    const categorys = ref();
    const pagination = ref({
      current: 1,
      pageSize: 10,
      total: 0
    });
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
      }
    ];

    /**
     * 数据查询
     */
    const handleQuery = (params: any) => {
      loading.value = true;
      axios.get("/category/list", {
        params: {
          page: params.page,
          size: params.size,
          // 此param是响应式变量查询里面拿过来的，当param有值才会传递，没有值不会传递
          name: param.value.name
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          categorys.value = data.content.list;

          // 重置分布按钮
          pagination.value.current = params.page;
          pagination.value.total = data.content.total;
        } else {
          message.error(data.message);
        }
      })
    };

    /**
     * 表格点击页码时触发
     */
    const handleTableChange = (pagination: any) => {
      console.log("看看自带的分页参数都有啥:" + pagination);
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    };

    // ------------- 表单 ----------------
    const category = ref({});
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;
      axios.post("/category/save", category.value).then((response) => {
        modalLoading.value = false;
        const data = response.data;
        if (data.success) {
          modalVisible.value = false;

          // 重新加载列表数据
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        } else {
          message.error(data.message);
        }
      });
    };
    /**
     * 编辑
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      // 在编辑的时候"复制对象"出来编辑，这样才不会影响响应式数据
      category.value = Tool.copy(record);
    };

    /**
     *  新增
     */
    const add = () => {
      modalVisible.value = true;
      category.value = {};
    };

    const handleDelete = (id: number) => {
      axios.delete("/category/delete/"+id).then((response) => {
        const data = response.data;
        if (data.success) {
          // 重新加载列表数据
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        } else {
          message.error(data.message);
        }
      });
    };

    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      }); // 初始的时候查询一次内容
    });

    return {
      param,
      categorys,
      pagination,
      columns,
      loading,
      handleTableChange,

      category,
      modalLoading,
      modalVisible,
      handleModalOk,
      handleQuery,

      edit,
      add,
      handleDelete
    }
  }
})
</script>