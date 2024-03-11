<template>
  <v-app-bar :order="0" color="#00bd7e" flat>
    <template v-slot:prepend>
      <v-btn variant="text" icon="mdi-menu" @click="drawwing"></v-btn>
      <div style="
          display: flex;
          align-items: center;
          height: 100%;
          cursor: pointer;
        " @click="$router.replace('/')">
        <img src="./../../assets/images/logoemhnavba.png"
          style="flex: 0; width: 30px; margin-right: 8px; margin-left: 20px" />
        <v-app-bar-title>English Master Hub</v-app-bar-title>
      </div>
    </template>
    <template v-slot:append>
      <v-row justify="center">
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
      </v-row>
    </template>
  </v-app-bar>
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
    ...mapState(authenticationRole, ['clearStore']),
  },
  data: () => ({
    isOpenLogout: false,
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
