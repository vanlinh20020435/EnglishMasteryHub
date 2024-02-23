<template>
    <v-list-item v-if="!item.children" :prepend-icon="item.icon" :title="item.title"> </v-list-item>
    <v-list-group v-else :value="item.value">
        <template v-slot:activator="{ props }">
            <v-list-item :prepend-icon="item.icon" v-bind="props" :title="item.title"></v-list-item>
        </template>
        <v-list-item v-for="{ title, icon, value } in item.children" :value="value" :title="title" :prepend-icon="icon">
        </v-list-item>
    </v-list-group>
</template>

<script>
export default {
    props: {
        item: Object
    },
};
</script>