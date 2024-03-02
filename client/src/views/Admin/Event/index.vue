<template>
    <v-container>
        <v-menu v-model="menu1" :close-on-content-click="false" max-width="290">
            <template v-slot:activator="{ on, attrs }">
                <v-text-field :value="date" clearable label="Formatted with Moment.js" readonly v-bind="attrs" v-on="on"
                    @click:clear="date = null"></v-text-field>
            </template>
            <v-date-picker v-model="date" @change="menu1 = false"></v-date-picker>
        </v-menu>
    </v-container>
</template>

<script>

export default {
    data: () => ({
        date: null,
        menu1: false,
    }),
}
</script>