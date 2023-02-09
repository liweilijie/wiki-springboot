<template>
  <a-layout-footer style="text-align: center">
    不多电子书<span v-show="user.id">，欢迎：{{user.name}}</span>
  </a-layout-footer>
</template>

<script lang="ts">
import { defineComponent, computed, onMounted } from 'vue';
import store from "@/store";
import {Tool} from "@/util/tool";
import { notification } from 'ant-design-vue';


export default defineComponent({
  name: 'TheFooter',

  setup() {
    const user = computed(() => store.state.user);
    return {
      user
    }
  }
});
</script>
