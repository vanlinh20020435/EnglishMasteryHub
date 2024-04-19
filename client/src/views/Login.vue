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
                            <v-text-field v-model="username" @change="onChange" :error="loginFailed"
                                :rules="usernameRules" label="Username" hide-details="auto">
                            </v-text-field>
                        </v-col>
                        <v-col md="12">
                            <v-text-field type="password" v-model="password" @change="onChange" :error="loginFailed"
                                :rules="passwordRules" label="Password" hide-details="auto">
                            </v-text-field>
                        </v-col>
                        <v-col md="12">
                            <v-btn color="#00bd7e" type="submit" block class="mt-2">Login</v-btn>
                        </v-col>
                    </v-row>
                </v-form>
            </v-card>
        </v-container>
    </v-app>
</template>

<script>
import { login, getUserInfo } from '@/services'
import { authenticationRole, toastStore } from "@/stores";
import { mapState } from "pinia";

import avtStudentMale from "@/assets/images/avatar_student_male.jpg";
import avtStudentFemale from "@/assets/images/avatar_student_female.jpg";
import avtTeacherFemale from "@/assets/images/avatar_teacher_female.jpg";
import avtTeacherMale from "@/assets/images/avatar_teacher_male.jpg";

export default {
    data: () => ({
        loginFailed: false,
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
    computed: {
        ...mapState(authenticationRole, ["updateAuth", "authentication"]),
        ...mapState(toastStore, ["updateToast"]),
    },
    methods: {
        async submit() {
            this.loginFailed = false
            if (this.valid) {
                const res = await login(this.username, this.password)
                if (res.success) {
                    this.updateAuth({ accessToken: res.data.accessToken })
                    const userInfoRes = await getUserInfo(res.data.accessToken.token)
                    if (userInfoRes.success) {
                        userInfoRes.data.role = userInfoRes.data.role.toLowerCase();
                        userInfoRes.data.avatar = userInfoRes.data.avatar ? userInfoRes.data.avatar : this.getAvtUser(userInfoRes.data);
                        this.updateAuth({ user: userInfoRes.data })
                        this.updateToast('success', "Login success!")
                        this.$router.replace(`/${this.authentication.user.role}`)
                    } else {
                        this.error = true
                        this.loginFailed = true
                        this.updateToast('error', "User not found!")
                    }
                } else {
                    this.error = true
                    this.updateToast('error', 'Login failed!')
                    this.loginFailed = true
                }
            }
        },
        onChange() {
            this.loginFailed = false
            this.userNotFound = false
        },
        getAvtUser(userInfo) {
            let avatar = '';
            switch (userInfo.role.toLowerCase()) {
                case 'student':
                    avatar = userInfo.gender ? avtStudentMale : avtStudentFemale;
                    break;
                case 'teacher':
                case 'admin':
                    avatar = avtTeacherMale; // Assuming male avatar for teacher and admin
                    if (!userInfo.gender) {
                        avatar = avtTeacherFemale; // Change to female avatar if gender is female
                    }
                    break;
                default:
                    // Handle other roles if needed
                    break;
            }

            return avatar;
        }
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