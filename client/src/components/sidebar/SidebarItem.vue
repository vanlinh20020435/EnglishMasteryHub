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
import { defineComponent, inject } from "vue";
import { authenticationRole } from "@/stores";
import { reactive } from "vue";

export default defineComponent({
  props: {
    item: Object,
    rail: Boolean,
    active: Boolean,
    enableActive: Function,
  },

  setup(props) {
    const authenticationStore = authenticationRole();
    const { authentication } = authenticationStore;

    const router = inject("router");
    // Create a reactive object to hold component state
    const state = reactive({
      activeItem: 0,
      isOpen: true,
    });

    const onClickItem = (idx = null) => {
      var path = `/${authentication.role}/${props.item.value}`;
      if (idx != null && props.item.children) {
        state.activeItem = idx;
        path += "/" + props.item.children[idx].value;
      }
      props.enableActive();
      console.log(path);
      router.replace(path);
    };

    return {
      activeItem: 0,
      isOpen: true,
      onClickItem,
    };
  },
});
</script>

<style scope>
.v-list-group__items .v-list-item.rail.sub-sidebar-item-custom {
  padding-inline-start: 16px !important;
}

.sub-sidebar-item-custom {
  transition: padding-inline-start 0.2s;
}
</style>
