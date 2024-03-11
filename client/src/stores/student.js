import { ref, computed } from 'vue';
import { defineStore } from 'pinia';

export const useCounterStore = defineStore('counter', () => {
  const student = ref({ class: {} });
  const cls = computed(() => student.value.class);
  function updateStudent(newData) {
    authentication.value = {
      ...authentication.value,
      ...newData,
    };
  }

  function clearStudent() {
    student.value = { class: {} };
  }

  return { student, cls, updateStudent, clearStudent };
});
