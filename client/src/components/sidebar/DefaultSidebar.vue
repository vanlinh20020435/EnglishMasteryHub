<template>
    <!-- backlog: permanent = true if width > 1024px -->
    <v-navigation-drawer width="300" :model-value="drawer" location="left" permanent>
        <v-list v-model:opened="open">
            <SidebarItem v-for="item in menu" :item="item" />
        </v-list>
    </v-navigation-drawer>
</template>

<script>
import SidebarItem from './SidebarItem.vue';

export default {
    components: {
        SidebarItem
    },
    props: {
        drawer: Boolean,
        menu: Array
    },
    data: () => ({
        open: []
    }),
    mounted() {
        this.menu.forEach(item => {
            if (item.children) this.open.push(item.value)
        });
    }
};
</script>
