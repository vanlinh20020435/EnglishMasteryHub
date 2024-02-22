<template>
    <!-- backlog: permanent = true if width > 1024px -->
    <v-navigation-drawer :rail="true" :value="drawer" location="left" permanent>
        <v-list>
            <v-list-group>
                <v-list-group value="Actions">
                    <template v-slot:activator="{ props }">
                        <v-list-item v-bind="props" title="Actions"></v-list-item>
                    </template>

                </v-list-group>
            </v-list-group>
        </v-list>
    </v-navigation-drawer>
</template>

<script>
export default {
    props: {
        drawer: Boolean,
    },
    watch: {
        drawer(val) {
            console.log(val);
        }
    }
}
</script>