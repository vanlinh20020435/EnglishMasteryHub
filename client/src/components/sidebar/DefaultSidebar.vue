<template>
  <v-navigation-drawer
    :temporary="$vuetify.display.mdAndDown"
    :permanent="$vuetify.display.mdAndUp"
    width="300"
    :model-value="drawer"
    :rail="isRail"
    location="left">
    <v-list v-model:opened="open">
      <SidebarItem
        v-for="(item, idx) in menu"
        :item="item"
        :active="activeItem === idx"
        :rail="isRail"
        :enableActive="() => (activeItem = idx)" />
    </v-list>
    <v-spacer />
    <div class="railor" @click="isRail = !isRail">
      <v-icon v-if="isRail">mdi-arrow-expand-right</v-icon>
      <v-icon v-else>mdi-arrow-collapse-left</v-icon>
    </div>
  </v-navigation-drawer>
</template>

<script>
import SidebarItem from './SidebarItem.vue';

export default {
  components: {
    SidebarItem,
  },
  props: {
    drawer: Boolean,
    menu: Array,
  },
  data: () => ({
    open: [],
    isRail: false,
    activeItem: 0,
  }),
  mounted() {
    const path = this.$route.fullPath;
    const pathSplit1 = path.split('/')[2];
    this.menu.forEach((item, idx) => {
      if (item.value === pathSplit1) this.activeItem = idx;
      if (item.children) this.open.push(item.value);
    });
  },
};
</script>

<style scoped>
.railor {
  width: 100%;
  height: 64px;
  position: absolute;
  bottom: 0;
  background-color: #00bd7e;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}
</style>
