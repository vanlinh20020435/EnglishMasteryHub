<template>
    <v-app class="login-app-wrapper">
        <v-container class="d-flex login-view">
            <v-card class="login-card">
                <v-form v-model="valid" @submit.prevent="submit">
                    <v-row>
                        <v-col md="12" class="d-flex justify-center align-center logo">
                            <img src="@/assets/images/logoemh.png" />
                        </v-col>
                        <v-col md="12">
                            <v-text-field v-model="username" @change="failed = false" :error="failed" :rules="usernameRules"
                                label="Username" hide-details="auto">
                            </v-text-field>
                        </v-col>
                        <v-col md="12">
                            <v-text-field v-model="password" @change="failed = false" :error="failed" :rules="passwordRules"
                                label="Password" hide-details="auto">
                            </v-text-field>
                        </v-col>
                        <v-col md="12"><v-btn color="#00bd7e" type="submit" block class="mt-2">Login</v-btn></v-col>
                        <v-snackbar color="error" v-model="failed" location="top">
                            Login failed!
                            <template v-slot:actions>
                                <v-btn color="pink" variant="text" @click="failed = false">
                                    Close
                                </v-btn>
                            </template>
                        </v-snackbar>
                        <v-snackbar color="success" v-model="success" location="top">
                            Login successfully!
                            <template v-slot:actions>
                                <v-btn color="white" @click="success = false">
                                    Close
                                </v-btn>
                            </template>
                        </v-snackbar>
                    </v-row>
                </v-form>
            </v-card>
        </v-container>
    </v-app>
</template>

<script>
import { login } from '@/services'
export default {
    data: () => ({
        failed: false,
        success: false,
        error: false,
        valid: true,
        username: null,
        password: null,
        usernameRules: [value => {
            if (value) return true
            return 'Name is required.'
        }],
        passwordRules: [value => {
            if (value) return true
            return 'Name is required.'
        }],
    }),
    methods: {
        async submit() {
            if (this.valid) {
                const res = await login(this.username, this.password)
                if (res.success) {
                    console.log(res.data.accessToken);
                    this.success = true
                    // navigation
                } else {
                    this.error = true
                    this.failed = true
                }
            }
        },
    }
}
</script>

<style>
.login-app-wrapper {
    background-image: url('@/assets/images/bg_login.png');
}

.v-col-md-12 {
    flex-basis: unset;
}

.login-view {
    width: 100%;
    min-height: 100vh;
    align-items: center;
    justify-content: center;
}

.login-card {
    padding: 16px;
    max-width: 500px;
}

.logo {
    max-height: 200px;
}

.logo img {
    scale: 0.5;
}
</style>