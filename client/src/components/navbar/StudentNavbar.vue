<template>
  <div style="background: #00bd7e;">
    <v-container>
      <div style="
                  display: flex;
                  align-items: center;
                  height: 100%;
                  cursor: pointer;
                " @click="$router.replace('/')">
        <img src="./../../assets/images/logoemhnavba.png" style="flex: 0; width: 30px; margin-right: 8px;" />
        <h2>English Master Hub</h2>
      </div>
      <v-spacer />
    </v-container>
  </div>
  <div style="background: #4d4d4d14;">
    <v-container class="d-flex" style="padding: 0;">
      <v-tabs v-model="tab" align-tabs="center" color="#00bd7e">
        <v-tab :value="1">Bảng tin</v-tab>
        <v-tab :value="2">Bài kiểm tra</v-tab>
        <v-tab :value="3">Giáo trình</v-tab>
      </v-tabs>
      <v-spacer />
    </v-container>
  </div>
</template>

<script>
import PopUpYesNo from '@/components/popup/PopUpYesNo.vue';
import { authenticationRole } from '@/stores';
import { mapState } from 'pinia';
export default {
  components: {
    PopUpYesNo,
  },
  props: {
    user: Object,
    drawwing: Function,
  },
  computed: {
    ...mapState(authenticationRole, ['clearStore']),
  },
  data: () => ({
    isOpenLogout: false,
    tab: 1
  }),
  methods: {
    logout() {
      this.isOpenLogout = false;
      localStorage.removeItem('accessToken');
      localStorage.removeItem('user');
      this.clearStore();
      this.$router.replace('/login');
    },
  },
};
</script>
