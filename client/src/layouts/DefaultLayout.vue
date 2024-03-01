<template>
  <v-layout>
    <v-app-bar :order="0" color="#00bd7e" flat title="Application bar">
      <template v-slot:prepend>
        <v-btn variant="text" icon="mdi-menu" @click="drawer = !drawer"></v-btn>
      </template>
      <template v-slot:append>
        <v-row justify="center">
          <v-menu min-width="200px" rounded>
            <template v-slot:activator="{ props }">
              <v-btn icon v-bind="props" style="margin-right: 16px;">
                <v-avatar color="brown" size="large">
                  <v-img alt="Avatar"
                    :src="authentication.user.avatar || 'https://avatars0.githubusercontent.com/u/9064066?v=4&s=460'"></v-img>
                </v-avatar>
              </v-btn>
            </template>
            <v-card>
              <v-card-text>
                <div class="mx-auto text-center">
                  <h3>{{ authentication.user.name }}</h3>
                  <p class="text-caption mt-1">{{ authentication.user.email }}</p>
                  <v-divider class="my-3"></v-divider>
                  <v-btn rounded variant="text" prepend-icon="mdi-account">
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
    <DefaultSidebar :drawer="drawer" :menu="menu" />
    <v-main class="d-flex justify-center" style="width: 100vw; height: 100vh">
      <v-container class="v-container__full" style="padding: 0; background-color: #fff">
        <slot></slot>
      </v-container>
    </v-main>
    <PopUpYesNo msg="Bạn có chắc chắn muốn đăng xuất?" :visible="isOpenLogout" :handleClickYes="logout"
      :handleClickNo="() => isOpenLogout = false" />
  </v-layout>
</template>

<script>
import DefaultSidebar from "@/components/sidebar/DefaultSidebar.vue";
import PopUpYesNo from "@/components/popup/PopUpYesNo.vue";
import { authenticationRole } from "@/stores";
import { mapState } from "pinia";
export default {
  components: {
    DefaultSidebar,
    PopUpYesNo
  },
  props: {
    menu: Array,
  },
  computed: {
    ...mapState(authenticationRole, ["authentication", "clearStore"]),
  },
  data: () => ({
    drawer: true,
    isOpenLogout: false
  }),
  methods: {
    logout() {
      this.isOpenLogout = false
      localStorage.removeItem("accessToken");
      localStorage.removeItem("user");
      this.clearStore()
      this.$router.replace('/login')
    }
  }
};
</script>
