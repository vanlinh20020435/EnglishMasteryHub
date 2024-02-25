<template>
    <v-card>
        <v-form v-model="valid" @submit.prevent="submit">
            <v-container>
                <v-row>
                    <v-col cols="12" md="4">
                        <v-text-field v-model="firstname" :rules="nameRules" label="First name"></v-text-field>
                    </v-col>

                    <v-col cols="12" md="4">
                        <v-text-field v-model="lastname" :rules="nameRules" label="Last name"></v-text-field>
                    </v-col>

                    <v-col cols="12" md="4">
                        <v-text-field v-model="email" :rules="emailRules" label="E-mail"></v-text-field>
                    </v-col>
                    <v-col cols="12" md="12"><v-btn type="submit" block class="mt-2">Submit</v-btn></v-col>
                    <v-snackbar v-model="snackbar" multi-line location="top">
                        {{ text }}
                        <template v-slot:actions>
                            <v-btn color="red" variant="text" @click="snackbar = false">
                                Close
                            </v-btn>
                        </template>
                    </v-snackbar>
                </v-row>
            </v-container>
        </v-form>
    </v-card>
    <div class="d-flex"><v-spacer />
        <v-btn>hihi</v-btn>
    </div>
    <v-card>
        <v-table>
            <thead>
                <tr>
                    <th class="text-left">
                        Name
                    </th>
                    <th class="text-left">
                        Calories
                    </th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="item in desserts" :key="item.name">
                    <td>{{ item.name }}</td>
                    <td>{{ item.calories }}</td>
                </tr>
            </tbody>
        </v-table>
    </v-card>
</template>

<script>
export default {
    data() {
        return {
            snackbar: false,
            text: `I am a multi-line snackbar.\nI can have more than one line. This is another line that is quite long.`,
            desserts: [
                {
                    name: 'Frozen Yogurt',
                    calories: 159,
                },
                {
                    name: 'Ice cream sandwich',
                    calories: 237,
                },
                {
                    name: 'Frozen Yogurt',
                    calories: 159,
                },
                {
                    name: 'Ice cream sandwich',
                    calories: 237,
                },
                {
                    name: 'Eclair',
                    calories: 262,
                },
                {
                    name: 'Cupcake',
                    calories: 305,
                },
                {
                    name: 'Gingerbread',
                    calories: 356,
                },
                {
                    name: 'Jelly bean',
                    calories: 375,
                },
                {
                    name: 'Lollipop',
                    calories: 392,
                },
                {
                    name: 'Honeycomb',
                    calories: 408,
                },
                {
                    name: 'Donut',
                    calories: 452,
                },
                {
                    name: 'KitKat',
                    calories: 518,
                },
            ],
            valid: false,
            firstname: '',
            lastname: '',
            nameRules: [
                value => {
                    if (value) return true

                    return 'Name is required.'
                },
                value => {
                    if (value?.length <= 10) return true

                    return 'Name must be less than 10 characters.'
                },
            ],
            email: '',
            emailRules: [
                value => {
                    if (value) return true

                    return 'E-mail is requred.'
                },
                value => {
                    if (/.+@.+\..+/.test(value)) return true

                    return 'E-mail must be valid.'
                },
            ],
        }
    },
    methods: {
        submit() {
            this.snackbar = true
            console.log(this.valid);
        }
    }
}
</script>