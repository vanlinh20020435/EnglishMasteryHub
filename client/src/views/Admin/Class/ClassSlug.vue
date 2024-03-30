<template>
  <v-card>
    <v-row>
      <v-col md="2">
        <img style="width: 100%; height: 100%" src="../../../assets/images/class.png" />
      </v-col>
      <v-col md="10">
        <div>
          <h2>{{ classInfo?.className }}</h2>
          <p><strong>Teacher: </strong>{{ classInfo?.teacher?.name }}</p>
          <p>
            <strong>Time: </strong>{{ classInfo?.startDate }} -
            {{ classInfo.endDate }}
          </p>
          <p><strong>Description: </strong>{{ classInfo?.description }}</p>
        </div>
      </v-col>
    </v-row>
  </v-card>
  <v-tabs v-model="tab" bg-color="#00bd7e" style="margin-top: 16px">
    <v-tab :value="0" :style="tab === 0 ? 'background: #039e6a' : ''">Danh sách học sinh</v-tab>
    <v-tab :value="1" :style="tab === 0 ? '' : 'background: #039e6a'">Danh sách bài kiểm tra</v-tab>
  </v-tabs>
  <ListStudents v-if="tab === 0" :classId="$route.params.id" />
  <ListTest v-else />
</template>

<script>
import { getClass } from '@/services';
import { authenticationRole, toastStore } from '@/stores';
import { mapState } from 'pinia';
import ListStudents from './ListStudents.vue';
import ListTest from './ListTest.vue';
export default {
  components: { ListStudents, ListTest },
  data: () => ({
    tab: 0,
    classInfo: {},
    isLoadingData: false,
  }),
  async mounted() {
    await this.fetchData();
  },
  computed: {
    ...mapState(authenticationRole, ['authentication']),
    ...mapState(toastStore, ['updateToast']),
  },
  methods: {
    async fetchData() {
      this.isLoadingData = true;
      const res = await getClass(
        this.authentication?.accessToken?.token,
        this.$route.params.id
      );
      if (res.success) {
        this.classInfo = res.data;
      }
      this.isLoadingData = false;
    },
  },
};
</script>
./ListTest.vue