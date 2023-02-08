<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          v-model:openKeys="openKeys"
          v-model:selectedKeys="selectedKeys"
          :style="{ height: '100%', borderRight: 0 }"
          mode="inline"
          @click="handleClick"
      >
        <a-menu-item key="welcome">
            <MailOutlined />
            <span>欢迎</span>
        </a-menu-item>
        <a-sub-menu v-for="item in level1" :key="item.id">
          <template #title>
            <span><user-outlined /> {{item.name}}</span>
          </template>
          <a-menu-item v-for="child in item.children" :key="child.id">
            <MailOutlined /><span>{{child.name}}</span>
          </a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <div class="welcome" v-show="isShowWelcome">
        <h1>欢迎来到电子书馆。</h1>
      </div>
      <a-list v-show="!isShowWelcome" item-layout="vertical" size="large"  :grid="{ gutter: 20, column: 3 }" :data-source="ebooks">
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
          <span v-for="{ type, text } in actions" :key="type">
            <component :is="type" style="margin-right: 8px" />
            {{ text }}
          </span>
            </template>
            <a-list-item-meta :description="item.description">
              <template #title>
                <router-link :to="'/doc?ebookId=' + item.id">
                  {{ item.name }}
                </router-link>
              </template>
              <template #avatar><a-avatar :src="item.cover" /></template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref } from 'vue';
import {message} from 'ant-design-vue';
import axios from 'axios';
import {Tool} from "@/util/tool";
const ebooks = ref();

const actions: Record<string, string>[] = [
  { type: 'StarOutlined', text: '156' },
  { type: 'LikeOutlined', text: '156' },
  { type: 'MessageOutlined', text: '2' },
];

export default defineComponent({
  name: 'HomeView',
  setup() {
    const level1 = ref();
    let categorys: any; //内部用的普通变量
    /**
     * 数据查询
     */
    const handleQueryCategorys = () => {
      axios.get("/category/all").then((response) => {
        const data = response.data;
        if (data.success) {
          categorys = data.content;
          console.log("原始数组:", categorys);

          level1.value = [];
          level1.value = Tool.array2Tree(categorys, 0);
          console.log("树形结构:", level1);
        } else {
          message.error(data.message);
        }
      })
    };

    const isShowWelcome = ref(true);
    let category2Id = 0;

    const handleClick = (value: any) => {
      console.log('handleClick');
      if (value.key == 'welcome') {
        isShowWelcome.value = true;
      } else {
        isShowWelcome.value = false;

        // 将选中的分类值传给 category2Id
        category2Id = value.key;
        queryEbook(); // 重新再查询一次。
      }
    }

    const queryEbook = () => {
      // 这里应该用一个 /ebook/all 接口提供所有数据查询更妥当
      axios.get("/ebook/list", {
        params: {
          page: 1,
          size: 1000,
          category2Id: category2Id,
        }
      }).then((response) => {
        let data = response.data;
        if (data.success) {
          ebooks.value = response.data.content.list;
        } else {
          message.error(data.message);
        }
      });
    };

    onMounted(() => {
      handleQueryCategorys();
    });

    return {
      ebooks,
      actions,
      level1,
      handleClick,
      isShowWelcome
    }
  }
});
</script>

<style scoped>
.ant-avatar {
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}
</style>
