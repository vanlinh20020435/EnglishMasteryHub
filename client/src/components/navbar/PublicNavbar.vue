<template>
  <v-app-bar :order="0" color="#00bd7e" flat>
    <template v-slot:prepend>
      <div
        style="
          display: flex;
          align-items: center;
          height: 100%;
          cursor: pointer;
        "
        @click="$router.replace('/')">
        <img
          src="./../../assets/images/logoemhnavba.png"
          style="flex: 0; width: 30px; margin-right: 8px; margin-left: 20px" />
        <v-app-bar-title>English Master Hub</v-app-bar-title>
      </div>
    </template>
  </v-app-bar>
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
  }),
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
