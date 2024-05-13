<template>
  <v-container fluid grid-list-sm>
    <v-row>
      <v-col v-for="(cls, idx) in data" :key="idx" :cols="cols">
        <v-card style="width: 100%; height: 100%; background: #efe841;" @click="() => handleClickItem(cls)">
          <img :src="cls.avatar || `/src/assets/images/class.png`" class="image" alt="lorem" width="100%"
            height="150" />
          <v-card-title>
            {{ cls.className }}
          </v-card-title>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
  <v-footer class="mt-5"></v-footer>
</template>

<script>
export default {
  props: {
    data: Array,
    loading: Boolean,
    reload: Function,
  },
  computed: {
    cols() {
      const { xl, lg, md, sm } = this.$vuetify.display;
      return (lg || xl) ? 2 : md ? 3 : sm ? 4 : 6;
    },
  },
  methods: {
    handleClickItem(item) {
      this.$router.push(`/admin/class/${item.classId}`);
    },
  }
};
</script>
