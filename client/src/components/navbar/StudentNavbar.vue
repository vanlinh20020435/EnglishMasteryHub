<template>
  <div style="background: #00bd7e">
    <v-container class="d-flex" style="padding: 4px; align-items: center">
      <div style="
          display: flex;
          align-items: center;
          height: 100%;
          cursor: pointer;
        " @click="$router.replace('/')">
        <img src="./../../assets/images/logoemhnavba.png" style="flex: 0; width: 30px; margin-right: 8px" />
        <h2 style="color: #fff">English Master Hub</h2>
      </div>
      <v-spacer />
      <v-menu min-width="200px" rounded>
        <template v-slot:activator="{ props }">
          <v-btn icon v-bind="props" style="margin-right: 16px">
            <v-avatar size="large">
              <v-img alt="Avatar" :src="user.avatar ||
          'https://avatars0.githubusercontent.com/u/9064066?v=4&s=460'
          "></v-img>
            </v-avatar>
          </v-btn>
        </template>
        <v-card>
          <v-card-text>
            <div class="mx-auto text-center">
              <h3>{{ user.name }}</h3>
              <p class="text-caption mt-1">{{ user.email }}</p>
              <v-divider class="my-3"></v-divider>
              <v-btn rounded variant="text" prepend-icon="mdi-account" @click="$router.replace('/account')">
                Account
              </v-btn>
              <v-divider class="my-3"></v-divider>
              <v-btn rounded variant="text" prepend-icon="mdi-logout" @click="isOpenLogout = true">
                Log out
              </v-btn>
            </div>
          </v-card-text>
        </v-card>
      </v-menu>
    </v-container>
  </div>
  <div style="background: #4d4d4d14">
    <v-container class="d-flex" style="padding: 0">
      <v-tabs v-model="tab" align-tabs="center" color="#00bd7e">
        <v-tab :value="1" @click="$router.replace('/student/news')">Bảng tin</v-tab>
        <v-tab :value="2" @click="$router.replace('/student/test')">Bài kiểm tra</v-tab>
        <v-tab :value="3" @click="$router.replace('/student/document')">Giáo trình</v-tab>
      </v-tabs>
      <v-spacer />
    </v-container>
  </div>
  <PopUpYesNo msg="Bạn có chắc chắn muốn đăng xuất?" :visible="isOpenLogout" :handleClickYes="logout"
    :handleClickNo="() => (isOpenLogout = false)" />
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
    ...mapState(authenticationRole, ['clearAuth']),
  },
  data: () => ({
    isOpenLogout: false,
    tab: 1,
  }),
  created() {
    const path = this.$route.fullPath;
    const pathSplit2 = path.split('/')[2];
    const cases = {
      news: 1,
      test: 2,
      document: 3,
    };
    this.tab = cases[pathSplit2] || 1;
  },
  methods: {
    logout() {
      this.isOpenLogout = false;
      localStorage.removeItem('accessToken');
      localStorage.removeItem('user');
      this.clearAuth();
      this.$router.replace('/login');
    },
  },
};
</script>
