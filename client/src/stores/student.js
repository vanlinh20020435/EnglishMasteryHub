import { ref, computed } from 'vue';
import { defineStore } from 'pinia';

export const studentStore = defineStore('student', () => {
  const student = ref({ class: {} });
  const cls = computed(() => student.value.class);
  async function updateStudent(newData) {
    console.log('store');
    student.value = {
      ...student.value,
      ...newData,
    };
  }

  function clearStudent() {
    student.value = { class: {} };
  }

  return { student, cls, updateStudent, clearStudent };
});
