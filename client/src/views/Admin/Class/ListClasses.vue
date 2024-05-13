<template>
  <v-data-table hover @click:row="(a, b) => handleClickItem(b.item)" :loading="loading" :headers="headers" :items="data"
    :sort-by="[{ key: 'classId', order: 'asc' }]">
    <template v-slot:item.avatar="{ item }">
      <v-avatar >
        <img alt="Avatar" :src="item.avatar || 'https://img.freepik.com/premium-vector/empty-school-class-room-blank-classroom-scene-with-empty-chalkboard-doodle-vector-illustration_135595-7227.jpg'"></img>
      </v-avatar>
    </template>
    <template v-slot:item.teacher="{ item }">
      {{ item.teacher?.username }}
    </template>
    <template v-slot:item.actions="{ item }">
      <v-icon class="me-2" color="primary" size="small" @click.stop.prevent="() => emitEdit(item)">
        mdi-pencil
      </v-icon>
      <v-icon size="small" color="error" @click.stop.prevent="() => emitDelete(item)">
        mdi-delete
      </v-icon>
    </template>
  </v-data-table>
</template>

<script>
import imageClass from '@/assets/images/class.png';
export default {
  props: {
    data: Array,
    loading: Boolean,
    reload: Function,
    emitEdit: Function,
    emitDelete: Function
  },
  data() {
    return {
      headers: [
        { title: 'Class ID', align: 'center', key: 'classId', sortable: false },
        { title: 'Class name', key: 'className', sortable: false },
        { title: 'Avatar', key: 'avatar', sortable: false },
        { title: 'Start', key: 'startDate' },
        { title: 'End', key: 'endDate' },
        { title: 'Description', key: 'description', sortable: false },
        { title: 'Teacher', key: 'teacher', sortable: false },
        { title: 'Actions', key: 'actions', sortable: false },
      ],
    };
  },
  methods: {
    handleClickItem(item) {
      this.$router.push(`/admin/class/${item.classId}`);
    },
    getAvtDefaultClass() {
      return imageClass;
    }
  },
};
</script>
