<template>
    <v-layout class="rounded rounded-md">
        <v-app-bar :order="0" color="grey-lighten-2" flat title="Application bar">
            <template v-slot:prepend>
                <v-switch v-model="drawer" hide-details inset :true-value="true" :false-value="false"></v-switch>
            </template>
            <template v-slot:append>
                <v-btn variant="tonal" style="margin-right: 5px;">click</v-btn>
                <v-btn variant="tonal">click</v-btn>
            </template>
        </v-app-bar>

        <DefaultSidebar :drawer="drawer" />
        <!-- <v-navigation-drawer v-model="drawer" location="left" permanent>
            <v-list :items="items"></v-list>
        </v-navigation-drawer> -->

        <v-main class="d-flex align-center justify-center" style="min-height: 300px;">
            <router-view />
        </v-main>
    </v-layout>
</template>

<script>
import DefaultSidebar from '@/components/sidebar/DefaultSidebar.vue'
export default {
    components: {
        DefaultSidebar
    },
    data: () => ({
        items: [
            {
                title: 'Foo',
                value: 'foo',
            },
            {
                title: 'Bar',
                value: 'bar',
            },
            {
                title: 'Fizz',
                value: 'fizz',
            },
            {
                title: 'Buzz',
                value: 'buzz',
            },
        ],
        drawer: false
    }),
}
</script>