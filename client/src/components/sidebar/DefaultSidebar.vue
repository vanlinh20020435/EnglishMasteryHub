<template>
    <!-- backlog: permanent = true if width > 1024px -->
    <v-navigation-drawer width="300" :model-value="drawer" :rail="isRail" location="left" permanent>
        <v-list v-model:opened="open">
            <SidebarItem v-for="(item, idx) in menu" :item="item" :active="activeItem === idx" :rail="isRail"
                :enableActive="() => activeItem = idx" />
        </v-list>
        <v-spacer />
        <div class="hihi" @click="isRail = !isRail">hihi</div>
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
        open: [],
        isRail: false,
        activeItem: 0
    }),
    mounted() {
        this.menu.forEach(item => {
            if (item.children) this.open.push(item.value)
        });
    }
};
</script>

<style scoped>
.hihi {
    width: 100%;
    height: 64px;
    position: absolute;
    bottom: 0;
    background-color: rgba(84, 84, 84, 0.65);
}
</style>
