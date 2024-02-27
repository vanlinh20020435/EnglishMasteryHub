<template>
  <v-list-item
    v-if="!item.children"
    :active="active"
    :prepend-icon="item.icon"
    :title="item.title"
    @click="onClickItem"
  >
  </v-list-item>
  <v-list-group v-else :value="item.value">
    <template v-slot:activator="{ props }">
      <v-list-item
        :active="active"
        :prepend-icon="item.icon"
        v-bind="props"
        :title="item.title"
      ></v-list-item>
    </template>
    <v-list-item
      v-for="({ title, icon, value }, idx) in item.children"
      :active="activeItem === idx && active"
      @click="() => onClickItem(idx)"
      :value="value"
      :title="title"
      :prepend-icon="icon"
      :class="(rail ? 'rail ' : '') + 'sub-sidebar-item-custom'"
    >
    </v-list-item>
  </v-list-group>
</template>

<script>
import { mapState } from "pinia";
import { authenticationRole } from "@/stores";

export default {
  props: {
    item: Object,
    rail: Boolean,
    active: Boolean,
    enableActive: Function,
  },
  data: () => ({
    activeItem: 0,
    isOpen: true,
  }),
  computed: {
    ...mapState(authenticationRole, ["authentication"]),
  },
  methods: {
    onClickItem(idx = null) {
      var path = `/${this.authentication.role}/${this.item.value}`;
      if (idx != null && this.item.children) {
        this.activeItem = idx;
        path += "/" + this.item.children[idx].value;
      }
      this.enableActive();
      this.$router.replace(path);
      console.log("authenticationRole ===", this.authentication);
    },
  },
};
</script>

<style scope>
.v-list-group__items .v-list-item.rail.sub-sidebar-item-custom {
  padding-inline-start: 16px !important;
}

.sub-sidebar-item-custom {
  transition: padding-inline-start 0.2s;
}
</style>
