import { ref } from 'vue';
import { defineStore } from 'pinia';

export const testStore = defineStore('test', () => {
  const test = ref({});
  async function updateTest(newData) {
    test.value = {
      ...newData,
    };
  }

  function clearTest() {
    test.value = {};
  }

  return { test, updateTest, clearTest };
});
